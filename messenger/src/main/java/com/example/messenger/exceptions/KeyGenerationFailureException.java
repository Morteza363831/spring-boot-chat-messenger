package com.example.messenger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class KeyGenerationFailureException extends BaseException {
    public KeyGenerationFailureException() {
        super("Failed to generate key", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
