package com.epam.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

public class ControllerExceptionHandler {
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public Map<String, String> handleTaskOverlapException(RuntimeException ex, WebRequest req) {
		Map<String, String> error = new HashMap<>();
		error.put("timestamp", new Date().toString());
		error.put("status", HttpStatus.BAD_REQUEST.name());
		error.put("error", ex.getMessage());
		error.put("path", req.getDescription(false));
		
		return error;
	}

}
