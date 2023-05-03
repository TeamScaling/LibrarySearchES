package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.search.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookRepository extends ElasticsearchRepository<Book, Integer> {

	Page<Book> findBooksByTitle(String query, Pageable pageable);;

}
