package com.scaling.libraryservice.recommend.repository;

import com.scaling.libraryservice.search.entity.Book;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RecommendEsRepository extends ElasticsearchRepository<Book, Long>{

	List<Book> findBooksByTitle(String query, Pageable pageable);;

}
