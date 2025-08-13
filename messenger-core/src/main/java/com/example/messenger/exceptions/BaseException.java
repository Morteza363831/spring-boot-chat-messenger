package com.example.messenger.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Setter
@Getter
@ToString
public class BaseException extends RuntimeException {

    private HttpStatus status;
    private String message;
    private Object details;
    private Throwable ex;
    private Date timestamp;

    public BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.timestamp = new Date();
    }

    public BaseException(String message, Throwable ex, HttpStatus status) {
        super(message, ex);
        this.status = status;
        this.timestamp = new Date();
    }

    public BaseException(String message, Object details, HttpStatus status) {
        super(message);
        this.details = details;
        this.timestamp = new Date();
        this.status = status;
    }
}
