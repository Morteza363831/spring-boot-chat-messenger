package com.example.messenger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SessionUserSizeException extends BaseException {

    public SessionUserSizeException(String sessionId) {
        super(sessionId + " users size is maximum.", HttpStatus.BAD_REQUEST);
    }
}
