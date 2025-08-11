package com.example.messengercommand.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@ToString
public class BaseException extends RuntimeException {
    private HttpStatus code;
    private String message;
    private Object details;
    private Throwable ex;

    public BaseException(String message, HttpStatus code) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(String message, HttpStatus code, Object details) {
        super(message);
        this.code = code;
        this.message = message;
        this.details = details;
    }

    public BaseException(Throwable ex, HttpStatus code  ) {
        super(ex);
    }
}
