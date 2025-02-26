package com.example.springbootchatmessenger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomIllegalArgumentException extends BaseException {
    String type;
    public CustomIllegalArgumentException(String type) {
        super("Invalid argument", HttpStatus.BAD_REQUEST);
        this.type = type;
    }
}
