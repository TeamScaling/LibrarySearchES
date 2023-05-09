package com.scaling.libraryservice.search.service;

import com.scaling.libraryservice.search.dto.BookDto;
import com.scaling.libraryservice.search.dto.MetaDto;
import com.scaling.libraryservice.search.dto.RespBooksDto;
import com.scaling.libraryservice.search.entity.Book;
import com.scaling.libraryservice.search.repository.BookEsQueryRepository;
//import com.scaling.libraryservice.search.repository.BookRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookSearchService {

//	private final BookRepository bookRepository;
	private final BookEsQueryRepository bookEsQueryRepository;

	public RespBooksDto getBooksByTarget(String target, String query, int page, int size) {

		Pageable pageable = PageRequest.of(page - 1, 10);

		Page<Book> books;

		if(target.equals("author")){
			books = bookEsQueryRepository.findBooksByAuthor(query, pageable);
		}
		if(target.equals("isbn")){
			books = bookEsQueryRepository.findBooksByIsbn(query, pageable);
		}
		else {
			books = bookEsQueryRepository.findBooksByTitle(query, pageable);
		}
		return makeBooksDto(books, page, size);
	}

	// RespBooksDto로 반환
	private RespBooksDto makeBooksDto(Page<Book>books, int page, int size){
		Objects.requireNonNull(books);

		List<BookDto> document = books.getContent().stream().map(BookDto::new).toList();

		MetaDto meta
			= new MetaDto(books.getTotalPages(), books.getTotalElements(), page, size);

		return new RespBooksDto(meta, document);
	}

}
