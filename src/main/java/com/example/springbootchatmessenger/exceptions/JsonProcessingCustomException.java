package com.example.springbootchatmessenger.exceptions;

import org.springframework.http.HttpStatus;

public class JsonProcessingCustomException extends BaseException {
    private String message;
    public JsonProcessingCustomException(String message) {
        super("JSON processing error : " + message, HttpStatus.BAD_REQUEST);
        this.message = message;
    }
}
