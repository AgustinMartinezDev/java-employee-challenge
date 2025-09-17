package com.reliaquest.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class EmployeeControllerAdvice {

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<String> handleNotFound(HttpClientErrorException.NotFound ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Resource was not found");
    }

    @ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
    public ResponseEntity<String> handleTooManyRequests(HttpClientErrorException.TooManyRequests ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Rate limit exceeded");
    }
}
