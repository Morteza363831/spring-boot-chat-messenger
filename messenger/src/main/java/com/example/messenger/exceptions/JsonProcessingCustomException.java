package com.example.messenger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JsonProcessingCustomException extends BaseException {
    public JsonProcessingCustomException() {
        super("JSON processing error", HttpStatus.BAD_REQUEST);
    }
}
