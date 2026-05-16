package com.BloodBank.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	 @ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
	        Map<String, String> error = new HashMap<>();
	        error.put("message", ex.getMessage());

	        return ResponseEntity.badRequest().body(error); // 400 instead of 500
	    }

	    /* 🔴 HANDLE VALIDATION ERRORS */
	    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
	    public ResponseEntity<?> handleValidation(
	            org.springframework.web.bind.MethodArgumentNotValidException ex) {

	        Map<String, String> errors = new HashMap<>();

	        ex.getBindingResult().getFieldErrors().forEach(err -> {
	            errors.put(err.getField(), err.getDefaultMessage());
	        });

	        return ResponseEntity.badRequest().body(errors);
	    }
	    
	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<?> handle(Exception ex) {
	        return ResponseEntity.status(500)
	                .body(Map.of("message", "Server error"));
	    }
	}

