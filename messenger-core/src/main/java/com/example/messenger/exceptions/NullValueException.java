package com.example.messenger.exceptions;

import org.springframework.http.HttpStatus;

public class NullValueException extends BaseException {
    public NullValueException(String value) {
        super(value + " cannot be null.", HttpStatus.BAD_REQUEST);
    }
}
