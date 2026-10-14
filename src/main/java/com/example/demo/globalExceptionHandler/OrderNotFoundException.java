package com.example.demo.globalExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;


public class OrderNotFoundException extends RuntimeException {
	
	 public OrderNotFoundException(Long id) {
	        super("Order not found with id: " + id);
	    }

}
