package com.example.springbootchatmessenger.exceptions;

import org.springframework.http.HttpStatus;

public class DataIntegrityViolationCustomException extends BaseException {
    private String message;
    public DataIntegrityViolationCustomException(String message) {
        super("Database integrity exception : " + message, HttpStatus.BAD_REQUEST);
        this.message = message;
    }
}
