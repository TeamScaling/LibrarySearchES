package com.scaling.libraryservice.recommend.controller;

import com.scaling.libraryservice.recommend.dto.ReqRecommendDto;
import com.scaling.libraryservice.recommend.dto.TestingBookDto;
import com.scaling.libraryservice.recommend.service.RecommendService;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchRestController {

    private final BookSearchService bookSearchService;

    private final RecommendService recommendService;

    //     도서 검색
    @GetMapping("/books/search/test")
    public ResponseEntity<RespBooksDto> restSearchBook(@RequestBody TestingBookDto testingBookDto){

        var result = bookSearchService.getBooksByTarget(testingBookDto.getBookName(),1,10,"title");

        if(result.getDocuments().isEmpty()){
            log.info("[Not Found]This book is Not Found");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }


    @PostMapping("/books/recommend")
    public ResponseEntity<List<String>> getRecommends(@RequestBody ReqRecommendDto recommendDto){

        System.out.println("query.............."+recommendDto.getQuery());

        return ResponseEntity.ok(recommendService.getRecommendBook(recommendDto.getQuery()));
    }

}
