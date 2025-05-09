package com.example.messenger.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerException extends BaseException {
    private String message;
    public InternalServerException(String message) {
        super("Internal server error : " + message, HttpStatus.INTERNAL_SERVER_ERROR);
        this.message = message;
    }
}
