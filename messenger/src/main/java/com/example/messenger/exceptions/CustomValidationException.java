package com.example.messenger.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomValidationException extends BaseException {
    public List<String> errors;
    public CustomValidationException(List<String> errors) {
        super("Validation Error", HttpStatus.BAD_REQUEST);
        this.errors = errors;
    }
}
