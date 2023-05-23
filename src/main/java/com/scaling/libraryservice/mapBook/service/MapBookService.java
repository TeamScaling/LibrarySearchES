package com.scaling.libraryservice.mapBook.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.commons.caching.CustomCacheable;
import com.scaling.libraryservice.commons.resilience4j.Substitute;
import com.scaling.libraryservice.mapBook.dto.ApiBookExistDto;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.exception.OpenApiException;
import com.scaling.libraryservice.mapBook.util.ApiQueryBinder;
import com.scaling.libraryservice.mapBook.util.ApiQuerySender;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapBookService {

    private final ApiQuerySender apiQuerySender;
    private final ApiQueryBinder apiQueryBinder;
    private final CustomCacheManager<List<RespMapBookDto>> customCacheManager;
    private final RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        Cache<CacheKey, List<RespMapBookDto>> mapBookCache = Caffeine.newBuilder()
            .expireAfterWrite(6, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

        customCacheManager.registerCaching(mapBookCache, this.getClass());
    }
    /**
     * 사용자가 원하는 도서와 사용자 주변의 도서관을 조합하여 대출 가능한 도서관들의 정보를 반환 한다.
     *
     * @param nearByLibraries 사용자 주변의 도서관 정보 Dto List
     * @param reqMapBookDto   사용자가 요청한 도서 정보와 위치 정보를 담는 Dto
     * @return 대출 가능한 도서관 정보를 담은 응답 Dto를 List 형태로 반환 한다.
     */

//    @Substitute(name= BExistConn.class, fallbackMethod = "hasBookMarkers")
//    @CustomCacheable
    public List<RespMapBookDto> matchMapBooks(List<LibraryDto> nearByLibraries, ReqMapBookDto reqMapBookDto) throws OpenApiException {

        Objects.requireNonNull(nearByLibraries);
        Objects.requireNonNull(reqMapBookDto);

        List<BExistConn> bExistConns;

        if (reqMapBookDto.isSupportedArea()) {
//            bExistConns = nearByLibraries.stream().filter(l -> l.getHasBook().equals("Y")).map(n -> new BExistConn(n.getLibCode())).toList();
            bExistConns = nearByLibraries.stream().map(n -> new BExistConn(n.getLibCode())).toList();
            if (bExistConns.isEmpty()) {
                return nearByLibraries.stream().map(l -> new RespMapBookDto(reqMapBookDto, l, "N")).toList();
            }
        } else {
            bExistConns = nearByLibraries.stream().map(n -> new BExistConn(n.getLibCode())).toList();
        }

        List<ResponseEntity<String>> responseEntities =
            apiQuerySender.sendMultiQuery(bExistConns, reqMapBookDto.getIsbn(), nearByLibraries.size());

        Map<Integer, ApiBookExistDto> bookExistMap = apiQueryBinder.bindBookExistMap(responseEntities);

        if (mappingLoanableLib(nearByLibraries, bookExistMap).stream().filter(l -> l.getLoanAvailable().equals("Y")).toList().isEmpty()){

            return nearByLibraries.stream().map(l -> new RespMapBookDto(reqMapBookDto, l, "N")).toList();

        } else {

            return mappingLoanableLib(nearByLibraries, bookExistMap).stream().filter(l -> l.getLoanAvailable().equals("Y")).toList();}
    }
    /**
     * 대출 가능 Api 응답을 주변 도서관 정보와 연결 하여 대출 가능한 주변 도서관 정보를 담은 List를 반환 한다.
     *
     * @param nearByLibraries 사용자 주변 도서관 정보를 담은 Dto
     * @param bookExistMap 도서관 코드를 key, 대출 가능 Api 응답 Dto를 value로 가지는 Map
     * @return 대출 가능한 주변 도서관 정보에 대한 Dto List
     */
    private List<RespMapBookDto> mappingLoanableLib(List<LibraryDto> nearByLibraries, Map<Integer, ApiBookExistDto> bookExistMap) {
        List<RespMapBookDto> result = new ArrayList<>();

        for (LibraryDto l : nearByLibraries) {

            ApiBookExistDto apiBookExistDto = bookExistMap.get(l.getLibNo());

            if (apiBookExistDto != null) {
                result.add(new RespMapBookDto(apiBookExistDto, l));
            }
        }
        return result;
    }

}