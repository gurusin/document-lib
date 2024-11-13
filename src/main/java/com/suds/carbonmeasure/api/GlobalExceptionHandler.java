package com.suds.carbonmeasure.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle all uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        // Create a generic error response message
        String errorMessage = ex.getMessage();

        // Log the exception details (optional)
        // Logger can be used here if preferred
        ex.printStackTrace();

        // Return a generic error response with HTTP 500 status
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
