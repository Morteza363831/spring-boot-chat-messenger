package com.example.messenger.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class BaseException extends RuntimeException {
    private final HttpStatus status;
    private final Date timestamp;

    public BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.timestamp = new Date();
    }
}
