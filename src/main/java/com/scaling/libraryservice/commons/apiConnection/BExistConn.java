package com.scaling.libraryservice.commons.apiConnection;

import com.scaling.libraryservice.mapBook.domain.ApiObserver;
import com.scaling.libraryservice.mapBook.domain.ConfigureUriBuilder;
import com.scaling.libraryservice.mapBook.dto.ApiStatus;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 도서관 책 보유 정보를 확인하는 API와 연결하는 클래스입니다.
 */
public class BExistConn implements ConfigureUriBuilder, ApiObserver {

    private final String libCode;
//    private final Integer libNo;
    private static final String API_URL = "http://data4library.kr/api/bookExist";
    private static final String DEFAULT_AUTH_KEY = "55db267f8f05b0bf8e23e8d3f65bb67d206a6b5ce24f5e0ee4625bcf36e4e2bb";
//    private static final String DEFAULT_AUTH_KEY = "0f6d5c95011bddd3da9a0cc6975868d8293f79f0ed1c66e9cd84e54a43d4bb72";
    public static final ApiStatus apiStatus = new ApiStatus(API_URL, 10);
//    http://data4library.kr/api/bookExist?authKey=0f6d5c95011bddd3da9a0cc6975868d8293f79f0ed1c66e9cd84e54a43d4bb72&isbn13=9788965700036&libCode=127058
    private final Integer DEFAULT_LIB_CHECKING_NO = 141258;

    private final String DEFAULT_ISBN_CHECKING = "9788089365210";

    public BExistConn(String libCode) {
        this.libCode = libCode;
    }

    /**
     * 주어진 대상 도서에 대한 도서관 책 보유 정보 API URI를 구성합니다.
     *
     * @param target 확인하려는 도서의 ISBN
     * @return 구성된 URI 정보를 담고 있는 {@link UriComponentsBuilder} 객체
     */
    @Override
    public UriComponentsBuilder configUriBuilder(String target) {

        if (target == null) {
            target = DEFAULT_ISBN_CHECKING;
        }

        return UriComponentsBuilder.fromHttpUrl(API_URL)
            .queryParam("authKey", DEFAULT_AUTH_KEY)
            .queryParam("isbn13", target)
            .queryParam("libCode", libCode)
            .queryParam("format", "json");
    }

    /**
     * API의 상태를 반환합니다.
     *
     * @return API 상태 객체
     */
    @Override
    public ApiStatus getApiStatus() {
        return apiStatus;
    }
}