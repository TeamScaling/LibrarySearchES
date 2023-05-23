package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.mapBook.entity.Library;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

// Library entity를 담는 dto
@Getter
@Setter
@ToString
@Slf4j
public class LibraryDto {

    private String libNm;
    private String libCode;
    private Integer libNo;
    private Double libLon;
    private Double libLat;
    private String libArea;
    private String libUrl;
    private Integer areaCd;
    private String hasBook = "N";
    private boolean isSupportedArea = false;

    public LibraryDto(Library library) {
        this.libNm = library.getLibNm();
        this.libCode = library.getLibNo().toString();
        this.libNo = library.getLibNo();
        this.libLon = library.getLibLon();
        this.libLat = library.getLibLat();
        this.libArea = library.getLibArea();
        this.libUrl = library.getLibUrl();
        this.areaCd = library.getAreaCd();
    }

    public LibraryDto(Library library,String hasBook) {
        this.libNm = library.getLibNm();
        this.libNo = library.getLibNo();
        this.libLon = library.getLibLon();
        this.libLat = library.getLibLat();
        this.libArea = library.getLibArea();
        this.libUrl = library.getLibUrl();
        this.areaCd = library.getAreaCd();
        this.hasBook = hasBook;
    }

    public LibraryDto(Library library,String hasBook,boolean isSupportedArea) {
        this.libNm = library.getLibNm();
        this.libNo = library.getLibNo();
        this.libLon = library.getLibLon();
        this.libLat = library.getLibLat();
        this.libArea = library.getLibArea();
        this.libUrl = library.getLibUrl();
        this.areaCd = library.getAreaCd();
        this.hasBook = hasBook;
        this.isSupportedArea = isSupportedArea;
    }

}
