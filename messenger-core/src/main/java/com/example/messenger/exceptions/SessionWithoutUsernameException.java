package com.example.messenger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SessionWithoutUsernameException extends BaseException {
    String message;
    public SessionWithoutUsernameException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
        this.message = message;
    }
}
