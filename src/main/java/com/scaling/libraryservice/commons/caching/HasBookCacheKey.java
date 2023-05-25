package com.scaling.libraryservice.commons.caching;

import java.util.Objects;

public class HasBookCacheKey implements CacheKey {

    private String isbn;

    private Integer areaCd;

    public HasBookCacheKey(String isbn, Integer areaCd) {
        this.isbn = isbn;
        this.areaCd = areaCd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HasBookCacheKey that = (HasBookCacheKey) o;
        return Objects.equals(isbn, that.isbn) && Objects.equals(areaCd,
            that.areaCd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, areaCd);
    }
}
