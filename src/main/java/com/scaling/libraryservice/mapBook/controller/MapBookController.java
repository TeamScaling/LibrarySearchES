package com.scaling.libraryservice.mapBook.controller;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.service.MapBookService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MapBookController {

    private final LibraryFindService libraryFindService;

    private final MapBookService mapBookService;

    @Timer
    @PostMapping("/books/mapBook/search")
    @CircuitBreaker(name="getMapBooks", fallbackMethod = "getHasBookMarkers")
    public String getMapBooks(ModelMap model, @RequestBody ReqMapBookDto reqMapBookDto) {

        log.info("지역코드" + reqMapBookDto.getAreaCd() + ", 위도"+ reqMapBookDto.getLat() + ", 경도"+ reqMapBookDto.getLon());

        List<LibraryDto> nearbyLibraries = libraryFindService.getNearByLibraries(reqMapBookDto);

        List<RespMapBookDto> mapBooks = mapBookService.matchMapBooks(nearbyLibraries, reqMapBookDto);

        String message;

        if(mapBooks.get(0).getLoanAvailable().equals("Y")){
            message =  "대출 가능한 도서관을 보여줍니다.";
        }  else {
            message = "대출 가능한 도서가 없으므로 도서를 소장한 도서관 정보를 보여줍니다.";}

        model.put("mapBooks", mapBooks);
        model.put("message", message);

        return "mapBook/mapBookMarker";
    }

    // getMapBook 메서드에서 이용하는 OpenAPI 장애시 circuitBreaker fallBackMethod로 호출
    public String getHasBookMarkers(ModelMap model, ReqMapBookDto reqMapBookDto, Throwable t) {

        log.info("[Error]" + t);

        List<LibraryDto> nearbyLibraries = libraryFindService.getNearByLibraries(reqMapBookDto);

        List<RespMapBookDto> hasBookLibs = nearbyLibraries.stream().map(l -> new RespMapBookDto(reqMapBookDto, l,"N")).toList();

        model.put("hasBookLibs", hasBookLibs);

        return "mapBook/hasLibMarker";
    }


}
