package com.example.springbootchatmessenger.exceptions.handler;

import com.example.springbootchatmessenger.exceptions.BaseException;
import com.example.springbootchatmessenger.exceptions.CustomValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(final BaseException e,final HttpServletRequest request) {
        return buildErrorResponse(e, e.getStatus(), request);
    }

    @ExceptionHandler(value = CustomValidationException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(final CustomValidationException e,final HttpServletRequest request) {
        log.error("{}, {}", e.getMessage(), e.errors.toString());
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getClass().getSimpleName(), e.getStatus().value(), e.getMessage(), request.getRequestURI(), e.getTimestamp(), e.errors.toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(final Exception e,final HttpServletRequest request) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR , request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(e.getClass().getSimpleName(), HttpStatus.CONFLICT.value(), "Database constraint", request.getRequestURI(),  new Date(), extractConstraintError(e)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParseException(HttpMessageNotReadableException ex) {
        log.error("JSON Parsing Error: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON input.");
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, HttpStatus status, HttpServletRequest request) {
        log.error("Error occurred: {}", e.getMessage());
        return ResponseEntity.status(status).body(
                new ErrorResponse(
                        e.getClass().getSimpleName(),
                        status.value(),
                        extractConstraintError(e),
                        request.getRequestURI(),
                        new Date(),
                        ""
                )
        );
    }

    private String extractConstraintError(Exception e) {
        if (e instanceof DataIntegrityViolationException ex) {
            ex.getMostSpecificCause();
            return ex.getMostSpecificCause().getMessage(); // Get the root cause message
        }
        return e.getMessage(); // Default message if no specific cause is found
    }
}
