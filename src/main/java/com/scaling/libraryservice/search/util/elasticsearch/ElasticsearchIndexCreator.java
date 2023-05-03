package com.scaling.libraryservice.search.util.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;

public class ElasticsearchIndexCreator {

	public static void main(String[] args) throws IOException {

		RestHighLevelClient client = new RestHighLevelClient(
			RestClient.builder(new HttpHost("localhost", 9200, "http")));

		// 인덱스 생성
		CreateIndexRequest request = new CreateIndexRequest("books");
		// 샤드, 레플리카 수 조정
		request.settings(Settings.builder()
			.put("index.number_of_shards", 6)
			.put("index.number_of_replicas", 1)
		);

		XContentBuilder settingsBuilder = XContentFactory.jsonBuilder()
			.startObject()
				.startObject("analysis")
					.startObject("analyzer")
						.startObject("ngram_analyzer")
							.field("tokenizer", "ngram_tokenizer")
						.endObject()
					.endObject()
					.startObject("tokenizer")
						.startObject("ngram_tokenizer")
							.field("type", "ngram")
							.field("min_gram", 1)
							.field("max_gram", 1)
							.startArray("token_chars")
								.value("letter")
								.value("digit")
						.endArray()
					.endObject()
				.endObject()
			.endObject()
		.endObject();

		request.settings(settingsBuilder);


		XContentBuilder mappingBuilder = XContentFactory.jsonBuilder()
			.startObject()
				.startObject("properties")
					.startObject("id_no")
						.field("type", "long")
					.endObject()
					.startObject("title_nm")
						.field("type", "text")
						.field("analyzer", "ngram_analyzer")
						.startObject("fields")
							.startObject("keyword")
								.field("type", "keyword")
								.field("ignore_above", 256)
							.endObject()
							.startObject("ngram_analyzer")
								.field("type", "text")
								.field("analyzer", "ngram_analyzer")
							.endObject()
						.endObject()
					.endObject()
					.startObject("book_intrcn_cn")
						.field("type", "text")
					.endObject().
					startObject("authr_nm")
						.field("type", "text")
					.endObject()
					.startObject("isbn_thirteen_no")
						.field("type", "keyword")
					.endObject()
					.startObject("image_url")
						.field("type", "keyword")
					.endObject()
					.startObject("loan_cnt")
						.field("type", "integer")
					.endObject()
				.endObject()
			.endObject();

		request.mapping(mappingBuilder.toString());

		client.indices().create(request, RequestOptions.DEFAULT);

		client.close();
	}
}