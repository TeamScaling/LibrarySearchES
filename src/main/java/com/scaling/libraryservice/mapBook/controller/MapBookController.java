package com.scaling.libraryservice.mapBook.controller;

import com.scaling.libraryservice.commons.apiConnection.BExistConn;
import com.scaling.libraryservice.commons.resilience4j.Substitute;
import com.scaling.libraryservice.mapBook.dto.LibraryDto;
import com.scaling.libraryservice.mapBook.dto.ReqMapBookDto;
import com.scaling.libraryservice.mapBook.dto.RespMapBookDto;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import com.scaling.libraryservice.mapBook.service.MapBookService;
import com.scaling.libraryservice.mapBook.util.MapBookApiHandler;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MapBookController {

    private final LibraryFindService libraryFindService;

    private final MapBookService mapBookService;

//    private final MapBookApiHandler mapBookApiHandler;
//
//    @PostConstruct
//    public void init() {
//        mapBookApiHandler.checkOpenApi();
//    }

    @PostMapping("/books/mapBook/search")
    @Substitute(name= BExistConn.class, fallbackMethod = "getHasBookMarkers")
    public String getMapBooks(ModelMap model, @RequestBody ReqMapBookDto reqMapBookDto) {

        log.info("지역코드" + reqMapBookDto.getAreaCd() + "위도"+ reqMapBookDto.getLat() + "경도"+ reqMapBookDto.getLon());

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

    // getMapBook가 이용하는 OpenAPI 장애시 circuitBreakerAspect에 의해 호출 됨
    public String getHasBookMarkers(ModelMap model, @ModelAttribute ReqMapBookDto reqMapBookDto) {

        log.info("요청을 실패했습니다.");

        List<LibraryDto> nearbyLibraries = libraryFindService.getNearByLibraries(reqMapBookDto);

        List<RespMapBookDto> hasBookLibs = nearbyLibraries.stream().map(l -> new RespMapBookDto(reqMapBookDto, l,"N")).toList();

        model.put("hasBookLibs", hasBookLibs);

        return "mapBook/hasLibMarker";
    }


}
