package com.scaling.libraryservice.recommend.util;

import com.scaling.libraryservice.recommend.dto.RespRecommend;
import com.scaling.libraryservice.search.dto.RespBooksDto;

public interface RecommendRule {

    RespRecommend recommendBooks(RespBooksDto searchResult);

}
