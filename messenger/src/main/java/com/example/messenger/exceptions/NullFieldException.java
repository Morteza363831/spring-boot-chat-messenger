package com.example.messenger.exceptions;

import org.springframework.http.HttpStatus;

public class NullFieldException extends BaseException {
    private String message;
    public NullFieldException(String message) {
        super(" There is a null field for : " + message, HttpStatus.BAD_REQUEST);
        this.message = message;
    }
}
