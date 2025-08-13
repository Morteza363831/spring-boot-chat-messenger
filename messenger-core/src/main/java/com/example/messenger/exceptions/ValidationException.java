package com.example.messenger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends BaseException {

    public Object errors;
    public ValidationException(List<String> errors) {
        super("Validation failed", HttpStatus.BAD_REQUEST);
        this.errors = errors;
    }
}
