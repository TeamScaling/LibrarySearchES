package com.scaling.libraryservice.search.controller;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.service.BookSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SearchViewController {

	private final BookSearchService bookSearchService;

	@GetMapping("/")
	public String main() {
		return "home";
	}

	@Timer
	@GetMapping("/books/search")
	public String searchBooks(@RequestParam(value="query") String query,
		@RequestParam(value = "page", defaultValue = "1") int page,
		@RequestParam(value = "size", defaultValue = "10") int size,
		@RequestParam(value = "target", defaultValue = "title") String target,
		ModelMap model)
	{
		RespBooksDto searchResult = bookSearchService.getBooksByTarget(query, page, size, target);

		if(!query.isEmpty()) {
			model.put("searchResult", searchResult);
			model.put("totalPages", searchResult.getMeta().getTotalPages());
			model.put("size", searchResult.getMeta().getTotalElements());
		}

		return "searchResult";

	}
}