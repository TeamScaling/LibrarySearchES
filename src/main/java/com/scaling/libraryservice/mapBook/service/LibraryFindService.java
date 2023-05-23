package com.scaling.libraryservice.mapBook.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.commons.caching.CustomCacheManager;
import com.scaling.libraryservice.commons.caching.CustomCacheable;
import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.entity.Library;
import com.scaling.libraryservice.mapBook.exception.LocationException;
import com.scaling.libraryservice.mapBook.repository.HasBookAreaRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryHasBookRepository;
import com.scaling.libraryservice.mapBook.repository.LibraryRepository;
import com.scaling.libraryservice.mapBook.util.HaversineCalculater;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Setter
@Getter
@Service
public class LibraryFindService {

    private static List<Library> libraries;
    private final LibraryRepository libraryRepo;
    private final LibraryHasBookRepository hasBookRepo;
    private final HasBookAreaRepository hasBookAreaRepo;
    private final CustomCacheManager<List<LibraryDto>> cacheManager;

    @PostConstruct
    private void init() {
        // DB에 담겨진 lib_info (도서관 정보)를 빈이 생성될 때, 함께 List로 가져온다.
        libraries = libraryRepo.findAll();

        Cache<CacheKey, List<LibraryDto>> libraryCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .maximumSize(1000)
            .build();

        cacheManager.registerCaching(libraryCache, this.getClass());
    }

    public List<LibraryDto> getNearByLibraries(ReqMapBookDto reqMapBooDto) throws LocationException {

        Objects.requireNonNull(reqMapBooDto);

        outPutAreaCd(reqMapBooDto);

        return isSupportedArea(reqMapBooDto) ? getNearByHasBookLibraries(reqMapBooDto) : getNearByLibrariesAreaCd(reqMapBooDto.getAreaCd());
    }

    public List<LibraryDto> getNearByLibrariesAreaCd(Integer areaCd) {

        return libraryRepo.findAllByAreaCd(areaCd).stream()
            .map(LibraryDto::new)
            .toList();
    }

    @CustomCacheable
    List<LibraryDto> getNearByHasBookLibraries(ReqMapBookDto reqMapBookDto) {

        log.info("This is supported Area");

        String isbn13 = reqMapBookDto.getIsbn();
        Integer areaCd = reqMapBookDto.getAreaCd();

        List<Library> libraries = hasBookRepo.findHasBookLibraries(isbn13, areaCd);

        List<LibraryDto> result
            = hasBookRepo.findHasBookLibraries(isbn13, areaCd)
            .stream()
            .map(l -> new LibraryDto(l,"Y", true))
            .toList();


        if (result.isEmpty()) {
            log.info(areaCd + " 이 지역의 도서관 중 도서를 소장 하는 도서관 없음");

            return getNearByLibrariesAreaCd(areaCd).stream().toList();
        }

        return result;
    }

    private void outPutAreaCd(ReqMapBookDto reqMapBookDto) {

        if (!reqMapBookDto.isValidCoordinate()) { throw new LocationException("잘못된 위치 정보입니다.");}

        Integer areaCd = findNearestLibrary(reqMapBookDto).getAreaCd();

        reqMapBookDto.updateAreaCd(areaCd);

    }

    // 사용자의 위치 정보와 가장 거리가 가까운 도서관을 찾는다.
    private LibraryDto findNearestLibrary(ReqMapBookDto userLocation) throws LocationException {

        LibraryDto result = libraryRepo.findAll().stream().min((l1, l2) -> {

            double d1 = HaversineCalculater.calculateDistance(
                userLocation.getLat(), userLocation.getLon(), l1.getLibLat(), l1.getLibLon());

            double d2 = HaversineCalculater.calculateDistance(
                userLocation.getLat(), userLocation.getLon(), l2.getLibLat(), l2.getLibLon());

            return Double.compare(d1, d2);
        }).map(LibraryDto::new).orElseThrow(() -> new LocationException("최단 거리 도서관 찾기 실패"));
        log.info("가장 가까운 도서관은 " + result.getLibNm() + "가장 가까운 도서관의 지역 번호는 " + result.getAreaCd());
        return result;
    }

    private boolean isSupportedArea(ReqMapBookDto reqMapBookDto) {

        Objects.requireNonNull(reqMapBookDto);

        boolean isSupported = hasBookAreaRepo.findById(reqMapBookDto.getAreaCd()).isPresent();

        if(isSupported) reqMapBookDto.setSupportedArea(true);

        return isSupported;
    }

}