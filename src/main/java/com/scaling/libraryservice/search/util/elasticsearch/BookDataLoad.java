package com.scaling.libraryservice.search.util.elasticsearch;

import java.io.IOException;
import java.sql.SQLException;

public class BookDataLoad {
	public static void main(String[] args) {

		String url = "jdbc:mysql://localhost:3306/library_service";
		String username = "root";
		String password = "wnsdud12!@";

		BookDataLoader loader = new BookDataLoader(url, username, password);
//		BookDataLoaderBulk loader = new BookDataLoaderBulk(url, username, password);

		try {
			loader.loadDataWithPaging(2000);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
}

