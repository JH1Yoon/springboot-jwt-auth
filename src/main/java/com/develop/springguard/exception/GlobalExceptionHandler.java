package com.develop.springguard.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code", errorCode.getCode());
        error.put("message", errorCode.getMessage());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("error", error);

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
}