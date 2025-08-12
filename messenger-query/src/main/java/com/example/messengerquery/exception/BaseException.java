package com.example.messengerquery.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@ToString
public class BaseException extends RuntimeException {

    private HttpStatus status;
    private String message;
    private Object details;
    private Throwable ex;

    public BaseException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    public BaseException(Throwable ex, HttpStatus status) {
        super(ex);
        this.ex = ex;
        this.status = status;

    }

    public BaseException(String message, Throwable ex, HttpStatus status) {
        super(message, ex);
        this.message = message;
        this.ex = ex;
        this.status = status;
    }

    public BaseException(String message, Object details, HttpStatus status) {
        super(message);
        this.details = details;
        this.status = status;
        this.message = message;
    }
}
