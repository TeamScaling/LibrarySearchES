package com.scaling.libraryservice.mapBook.util;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
public class ApiQuerySender {

    private final RestTemplate restTemplate;

    /**
     * 대상 Api에 요청을 보내 원하는 응답 데이터를 받는다.
     *
     * @param configUriBuilder Api에 대한 요청 param 값들을 담고 있는 객체
     * @param target           Api에 요청 하고자 하는 데이터 식별 값
     * @return Api 응답 데이터를 담는 ResponseEntity
     * @throws OpenApiException API와의 연결에 문제가 있을 경우.
     */
    @Timer
    public CompletableFuture<ResponseEntity<String>> sendSingleQueryAsync(ConfigureUriBuilder configUriBuilder,
        String target) {
        Objects.requireNonNull(configUriBuilder);

        return CompletableFuture.supplyAsync(() -> {
            UriComponentsBuilder uriBuilder = configUriBuilder.configUriBuilder(target);
            try {
                return restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    String.class);
            } catch (Exception e) {
                throw new OpenApiException("api 문제");
            }
        });
    }

    /**
     * Api에 대한 요청을 병렬 처리 한다.
     *
     * @param uriBuilders Api에 대한 요청 param 값들을 담고 있는 객체
     * @param target      Api에 요청 하고자 하는 데이터 식별 값
     * @param nThreads    병렬 처리를 수행할 쓰레드 갯수
     * @return Api 응답 데이터 ResponseEntity들을 담은 List
     * @throws OpenApiException API와의 연결에 문제가 있을 경우.
     */
    @Timer
    public List<ResponseEntity<String>> sendMultiQuery(List<? extends ConfigureUriBuilder> uriBuilders,
        String target, int nThreads) throws OpenApiException {
        Objects.requireNonNull(uriBuilders);

        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        List<CompletableFuture<ResponseEntity<String>>> futures = uriBuilders.stream()
            .map(builder -> sendSingleQueryAsync(builder, target))
            .toList();

        List<ResponseEntity<String>> result;

        try {
            result = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApplyAsync(v -> futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()))
                .get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.toString());
            throw new OpenApiException("api 문제 발생");
        } finally {
            executorService.shutdown();
        }

        return result;

    }


}