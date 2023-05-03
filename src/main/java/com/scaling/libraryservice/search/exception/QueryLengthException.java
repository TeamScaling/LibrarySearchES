package com.scaling.libraryservice.search.exception;

public class QueryLengthException extends RuntimeException{

	public QueryLengthException() {
	}

	public QueryLengthException(String message) {
		super(message);
	}

}
