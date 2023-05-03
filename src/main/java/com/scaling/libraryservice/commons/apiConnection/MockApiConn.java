package com.scaling.libraryservice.commons.apiConnection;

import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import org.springframework.web.util.UriComponentsBuilder;

public class MockApiConn implements ConfigureUriBuilder, ApiObserver {

    private static String apiUrl = "http://mockServer.kr/api/bookExist?format=json";

    private static final ApiStatus apiStatus = new ApiStatus(apiUrl,5);

    public static void setApiUrl(String apiUrl) {
        MockApiConn.apiUrl = apiUrl;
    }

    @Override
    public UriComponentsBuilder configUriBuilder(String target) {
        return UriComponentsBuilder.fromHttpUrl(apiUrl);
    }


    @Override
    public ApiStatus getApiStatus() {
        return apiStatus;
    }
}
