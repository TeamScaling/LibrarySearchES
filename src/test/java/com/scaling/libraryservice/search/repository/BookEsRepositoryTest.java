//package com.scaling.libraryservice.search.repository;
//
//
//import com.scaling.libraryservice.search.entity.Book;
//import java.util.List;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//
//
//@SpringBootTest
//public class BookEsRepositoryTest {
//
//	@Mock
//	private ElasticsearchOperations elasticsearchOperations;
//
//	@InjectMocks
//	private BookEsQueryRepository bookEsQueryRepository;
//
//	@BeforeAll
//	public static void setup(){
//
//	}
//
//	@Test
//	@DisplayName("BookEsRepository에서 책을 잘 반환하는지 확인")
//	public void test_find_books(){
//		// given
//		String query = "토비의 스프링";
//		int page = 1;
//		Book book1 = new Book(1L, "제목1", "작가1");
//		Book book2 = new Book(2L, "제목2", "작가2");
//		Book book3 = new Book(3L, "제목3", "작가3");
//		List<Book> bookList = List.of(book1, book2, book3);
//
//		Pageable pageable = PageRequest.of(0, 10);
//
//		// ES Operations에 내용 저장
//		elasticsearchOperations.save(bookList);
//
//		// when
//		Page<Book> book_page = bookEsQueryRepository.findBooksByTitle(query, pageable);
//
//		//
//		System.out.println(book_page);
//		// when
//
//
//		// then
//
//	}
//}
