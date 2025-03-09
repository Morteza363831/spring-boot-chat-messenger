package com.example.springbootchatmessenger.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DecryptionFailureException extends BaseException {
    private String message;
    public DecryptionFailureException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
        this.message = message;
    }
}
