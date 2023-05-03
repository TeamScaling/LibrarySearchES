package com.scaling.libraryservice.search.util.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ElasticsearchIndexDeleter {
	private static RestHighLevelClient client;

	public ElasticsearchIndexDeleter(RestHighLevelClient client) {
		ElasticsearchIndexDeleter.client = client;
	}

	public static void deleteIndex(String indexName) throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest(indexName);
		AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
		if (response.isAcknowledged()) {
			System.out.println("Index deleted successfully.");
		} else {
			System.out.println("Failed to delete index.");
		}
	}

	public static void main(String[] args) throws IOException {
		RestHighLevelClient client = new RestHighLevelClient(
			RestClient.builder(new HttpHost("localhost", 9200, "http")));

		// 인덱스 삭제
		client.indices().delete(new DeleteIndexRequest("books"), RequestOptions.DEFAULT);
		client.close();

	}
}
