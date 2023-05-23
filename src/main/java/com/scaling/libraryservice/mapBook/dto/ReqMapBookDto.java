package com.scaling.libraryservice.mapBook.dto;

import com.scaling.libraryservice.commons.caching.CacheKey;
import com.scaling.libraryservice.mapBook.service.LibraryFindService;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.lang.Nullable;

@Getter @Setter
@ToString
public class ReqMapBookDto implements CacheKey {

    private String isbn;
    private Double lat;
    private Double lon;

    private int areaCd;
    private boolean isSupportedArea;

    public ReqMapBookDto() {}

    public ReqMapBookDto(String isbn, Double lat, Double lon) {
        this.isbn = isbn;
        this.lat = lat;
        this.lon = lon;
    }

    public ReqMapBookDto(String isbn, int areaCd) {
        this.isbn = isbn;
        this.areaCd = areaCd;
    }

    public ReqMapBookDto(JSONObject jsonObject){
        this.isbn = jsonObject.getString("isbn");
        this.lat = Double.valueOf(jsonObject.getString("lat"));
        this.lon = Double.valueOf(jsonObject.getString("lon"));
    }

    public boolean isValidCoordinate(){

        return this.lat > 33.11 & this.lat < 38.61
            & this.lon > 124.60 & this.lon <131.87;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReqMapBookDto that = (ReqMapBookDto) o;
        return Objects.equals(isbn, that.isbn) && Objects.equals(areaCd, that.areaCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, areaCd);
    }

    public void updateAreaCd(Integer areaCd){
        this.areaCd = areaCd;
    }
}
