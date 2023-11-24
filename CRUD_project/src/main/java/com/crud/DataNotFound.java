package com.crud;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFound extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DataNotFound(String message) {
		super(message);
	}
	
}
