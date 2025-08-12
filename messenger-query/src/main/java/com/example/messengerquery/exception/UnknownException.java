package com.example.messengerquery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnknownException extends BaseException {

    public UnknownException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public UnknownException(Throwable cause) {
        super(cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
