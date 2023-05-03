package com.scaling.libraryservice.search.repository;

import com.scaling.libraryservice.commons.timer.Timer;
import com.scaling.libraryservice.search.entity.Book;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookEsQueryRepository {

	private final ElasticsearchOperations operations;

	@Timer
	public Page<Book> findBooksByTitle(String query, Pageable pageable) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
			.must(QueryBuilders.matchQuery("title_nm", query))  // ngram 분석 결과가 일치하는 정도에 따라 검색
			.should(QueryBuilders.termQuery("title_nm.keyword", query));  // 결과가 정확히 일치하는 제목에 점수 추가


		NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
			.withQuery(boolQueryBuilder)
			.withPageable(pageable)
			.build();

		SearchHits<Book> searchHits = operations.search(nativeSearchQuery, Book.class);
		System.out.println(searchHits);

		List<Book> books = searchHits.getSearchHits().stream()
			.map(SearchHit::getContent)
			.collect(Collectors.toList());
		return new PageImpl<>(books, pageable, searchHits.getTotalHits());

		//		NativeSearchQuery nativeSearchQuery = makeNativeQuery(boolQueryBuilder, pageable);

//		return pageBook(nativeSearchQuery, pageable, operations);
	}

	public Page<Book> findBooksByAuthor(String author, Pageable pageable) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
			.must(QueryBuilders.matchQuery("authr_nm", author))
			.should(QueryBuilders.termQuery("authr_nm.keyword", author));

		NativeSearchQuery nativeSearchQuery = makeNativeQuery(boolQueryBuilder, pageable);

		return pageBook(nativeSearchQuery, pageable, operations);
	}

	public Page<Book> findBooksByIsbn(String isbn, Pageable pageable) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
			.must(QueryBuilders.matchQuery("isbn_thirteen_no", isbn));

		NativeSearchQuery nativeSearchQuery = makeNativeQuery(boolQueryBuilder, pageable);

		return pageBook(nativeSearchQuery, pageable, operations);
	}

	private NativeSearchQuery makeNativeQuery (BoolQueryBuilder boolQueryBuilder, Pageable pageable){
		return new NativeSearchQueryBuilder()
			.withQuery(boolQueryBuilder)
			.withPageable(pageable)
			.build();
	}

	private Page<Book> pageBook(NativeSearchQuery nativeSearchQuery, Pageable pageable, ElasticsearchOperations operations){
		SearchHits<Book> searchHits = operations.search(nativeSearchQuery, Book.class);
		System.out.println("=========================================================");
		System.out.println(searchHits);
		System.out.println("=========================================================");

		List<Book> books = searchHits.getSearchHits().stream()
			.map(SearchHit::getContent)
			.collect(Collectors.toList());
		return new PageImpl<>(books, pageable, searchHits.getTotalHits());
	}
}

//		SearchHits<Book> search = operations.search(nativeSearchQuery, Book.class);
//		List<SearchHit<Book>> searchHitList = search.getSearchHits();
//		List<Book> list = new ArrayList<>();
//		for (SearchHit<Book> libraryEsSearchHit : searchHitList) {
//			list.add(libraryEsSearchHit.getContent());
//		}
//		return list;