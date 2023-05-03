package com.scaling.libraryservice.commons.apiConnection;

import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import org.springframework.web.util.UriComponentsBuilder;

public class LoanItemConn implements ApiObserver, ConfigureUriBuilder {

    private static final String API_URL = "http://data4library.kr/api/loanItemSrch";

    private static final String DEFAULT_AUTH_KEY = "0f6d5c95011bddd3da9a0cc6975868d8293f79f0ed1c66e9cd84e54a43d4bb72";
    public static final ApiStatus apiStatus = new ApiStatus(API_URL,10);

    @Override
    public UriComponentsBuilder configUriBuilder(String pageSize) {
        UriComponentsBuilder uriBuilder
            = UriComponentsBuilder.fromHttpUrl(API_URL)
            .queryParam("authKey",DEFAULT_AUTH_KEY)
            .queryParam("pageSize", pageSize)
            .queryParam("format","json");

        return uriBuilder;
    }

    @Override
    public ApiStatus getApiStatus() {
        return apiStatus;
    }

}
