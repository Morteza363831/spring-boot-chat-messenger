package com.example.messenger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JsonProcessingException extends BaseException {
    public JsonProcessingException() {
        super("JSON processing error", HttpStatus.BAD_REQUEST);
    }
}
