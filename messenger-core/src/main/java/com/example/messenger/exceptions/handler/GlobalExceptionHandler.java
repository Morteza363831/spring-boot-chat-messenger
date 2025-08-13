package com.example.messenger.exceptions.handler;

import com.example.messenger.exceptions.BaseException;
import com.example.messenger.exceptions.ValidationException;
import com.example.messenger.structure.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> handleCustomException(BaseException e, HttpServletRequest request) {
        return buildErrorResponse(e, e.getStatus(), request);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<?> handleCustomException(ValidationException e, HttpServletRequest request) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ResponseResult<>(
                        "failure",
                        e.getStatus().value(),
                        e.getMessage(),
                        e.errors,
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(value = JsonProcessingException.class)
    public ResponseEntity<?> handleJsonProcessingException(JsonProcessingException e, HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(value = HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<?> handleUnauthorizedException(HttpClientErrorException.Unauthorized e, HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception e, HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR , request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseResult<>(
                        "failure",
                        HttpStatus.BAD_REQUEST.value(),
                        "Error occurred",
                        e.getMessage(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON input.");
    }

    private ResponseEntity<?> buildErrorResponse(Exception e, HttpStatus status, HttpServletRequest request) {
        return ResponseEntity
                .status(status)
                .body(new ResponseResult<>(
                        "failure",
                        status.value(),
                        "Error occurred",
                        e.getMessage(),
                        request.getRequestURI()
                ));
    }
}
