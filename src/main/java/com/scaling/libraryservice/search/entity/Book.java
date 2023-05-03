package com.scaling.libraryservice.search.entity;

import javax.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;


@ToString
@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "books")
public class Book {

	@Id
	@Field(name = "id_no")
	private Long id;

	@Field(name = "title_nm")
	private String title;

	@Field(name = "book_intrcn_cn")
	private String content;

	@Field(name = "authr_nm")
	private String author;

	@Field(name = "isbn_thirteen_no")
	private String isbn;

	@Field(name = "image_url")
	private String bookImg;

	@Column(name = "loan_cnt")
	private Integer loanCnt;

	public Integer getLoanCnt() {
		return loanCnt == null ? 0 : this.loanCnt;
	}

	public Book(Long id, String title, String content, String author, String isbn,
		String image_url) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.author = author;
		this.isbn = isbn;
		this.bookImg = image_url;
	}

	public Book(Long id, String title, String author) {
		this.id = id;
		this.title = title;
		this.author = author;
	}
}
