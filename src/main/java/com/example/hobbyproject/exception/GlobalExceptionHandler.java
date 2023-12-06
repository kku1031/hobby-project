package com.example.hobbyproject.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostException.class)
    public ResponseEntity<String> handlePostException(PostException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
    }

}
