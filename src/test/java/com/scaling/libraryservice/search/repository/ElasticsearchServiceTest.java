//package com.scaling.libraryservice.search.repository;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import com.scaling.libraryservice.search.entity.Book;
//import java.io.IOException;
//import java.util.List;
//
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.SearchHits;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//
//@SpringBootTest
//public class ElasticsearchServiceTest {
//
//	@Mock
//	private static ElasticsearchOperations elasticsearchOperations;
//
//	@InjectMocks
//	private BookEsQueryRepository repository;
//
//	@BeforeAll
//	public static void setup() {
//		RestHighLevelClient client = mock(RestHighLevelClient.class);
//		elasticsearchOperations = new ElasticsearchRestTemplate(client);
//		SearchResponse searchResponse = mock(SearchResponse.class);
//		SearchHits searchHits = mock(SearchHits.class);
//		SearchHit[] searchHitArray = new SearchHit[2];
//		SearchHit searchHit1 = mock(SearchHit.class);
//		SearchHit searchHit2 = mock(SearchHit.class);
//		searchHitArray[0] = searchHit1;
//		searchHitArray[1] = searchHit2;
//
//		// 첫 번째 검색 결과
//		when(searchHit1.getSourceAsString())
//			.thenReturn("{\"id\":\"1\",\"title\":\"Java Programming\"}");
//
//		// 두 번째 검색 결과
//		when(searchHit2.getSourceAsString())
//			.thenReturn("{\"id\":\"2\",\"title\":\"Spring Boot in Action\"}");
//
//		// SearchHits 설정
//		when(searchHits.getHits())
//			.thenReturn(searchHitArray);
//
//		// SearchResponse 설정
//		when(searchResponse.getHits())
//			.thenReturn(searchHits);
//
//		// client.search() 호출 시 SearchResponse 리턴
//		try {
//			when(client.search(ArgumentMatchers.any(SearchRequest.class), ArgumentMatchers.any()))
//				.thenReturn(searchResponse);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void testSearch() {
//		// given
//		String index = "books";
//		String keyword = "java";
//		Pageable pageable = PageRequest.of(-1, 10);
//
//		// when
//		Page<Book> result = repository.findBooksByTitle(index, pageable);
//
//		// then
//		assertEquals(2, result.getTotalElements());
////		assertEquals("Java Programming", result.get(0).getTitle());
////		assertEquals("Spring Boot in Action", result.get(1).getTitle());
//	}
//}
//
