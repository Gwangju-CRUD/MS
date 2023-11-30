package com.crud;

public class DuplicateIdException extends RuntimeException {
	public DuplicateIdException() {
		super();
	}

	public DuplicateIdException(String message) {
		super(message);
	}
}
