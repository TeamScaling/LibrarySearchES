package com.scaling.libraryservice.search.util.elasticsearch;

import com.scaling.libraryservice.search.entity.Book;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;
import org.elasticsearch.xcontent.XContentType;


public class BookDataLoaderBulk {

	private final String url;
	private final String username;
	private final String password;

	public BookDataLoaderBulk(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public void loadDataWithPaging(int pageSize) throws IOException, SQLException {
		// MySQL 데이터베이스 연결
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
			// 총 데이터 개수 조회
			String countSql = "SELECT COUNT(*) FROM books";
			try (PreparedStatement countStmt = conn.prepareStatement(countSql)) {
				try (ResultSet countRs = countStmt.executeQuery()) {
					if (countRs.next()) {
						int totalCount = countRs.getInt(1);

						// 페이지 단위로 데이터 조회
						ExecutorService executor = Executors.newFixedThreadPool(3); // 쓰레드 풀 생성

						List<CompletableFuture<Void>> futures = new ArrayList<>(); // Future 객체를 저장할 리스트 생성

						for (int i = 0; i < totalCount; i += pageSize) {
//							String sql = "SELECT * FROM books LIMIT ?, ?";
							String sql = " SELECT * FROM books WHERE id_no > 3254114 LIMIT ?, ?";

							try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
								pstmt.setInt(1, i);
								pstmt.setInt(2, pageSize);
								try (ResultSet rs = pstmt.executeQuery()) {
									// Elasticsearch에 데이터 색인
									RestHighLevelClient client = new RestHighLevelClient(
										RestClient.builder(HttpHost.create("http://localhost:9200")));

									BulkRequest bulkRequest = new BulkRequest();

									while (rs.next()) {
										Book book = new Book();
										book.setId(rs.getLong("id_no"));
										book.setTitle(rs.getString("title_nm"));
										book.setContent(rs.getString("book_intrcn_cn"));
										book.setAuthor(rs.getString("authr_nm"));
										book.setIsbn(rs.getString("isbn_thirteen_no"));
										book.setBookImg(rs.getString("image_url"));
										book.setLoanCnt(rs.getInt("loan_cnt"));

										// IndexRequest 객체 생성 시에 고유 ID를 지정합니다.
										IndexRequest indexRequest = new IndexRequest("maps")
											.id(String.valueOf(book.getId())); // id_no 컬럼을 고유 ID로 사용
										indexRequest.source(getJsonString(book), XContentType.JSON);
										bulkRequest.add(indexRequest);
									}

									CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
										try {
											BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
											if (bulk.hasFailures()) {
												// handle failures here
											}
										} catch (IOException e) {
											// handle exception here
										}
									});
									futures.add(future);
								}
							}
						}

						// Wait for all futures to complete
						CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
					}
				}
			}
		} catch (Exception e) {
			// handle exception here
		}
	}

	private static String getJsonString(Book book) throws IOException {
		XContentBuilder builder = XContentFactory.jsonBuilder();
		builder.startObject();
		builder.field("id_no", book.getId());
		builder.field("title_nm", book.getTitle());
		builder.field("book_intrcn_cn", book.getContent());
		builder.field("authr_nm", book.getAuthor());
		builder.field("isbn_thirteen_no", book.getIsbn());
		builder.field("image_url", book.getBookImg());
		builder.field("loan_cnt", book.getLoanCnt());
		builder.endObject();
		return Strings.toString(builder);
	}

}

