package com.example.couponcore.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ErrorResponse> handleCoreException(CoreException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResponse(e.getErrorType().getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(500)
                .body(new ErrorResponse("500", e.getMessage()));
    }
}
