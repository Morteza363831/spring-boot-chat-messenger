package com.example.springbootchatmessenger.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationFailureException extends BaseException {
    public AuthenticationFailureException() {
        super("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }
}
