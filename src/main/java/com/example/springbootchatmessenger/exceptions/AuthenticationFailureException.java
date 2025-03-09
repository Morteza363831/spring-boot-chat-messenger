package com.example.springbootchatmessenger.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationFailureException extends BaseException {
    private String message;
    public AuthenticationFailureException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
        this.message = message;
    }
}
