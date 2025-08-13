package com.example.messenger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SessionNotFoundException extends BaseException {

    public SessionNotFoundException(String user1, String user2) {
        super("Session for users : " + user1 + " and " + user2 + " not found.", HttpStatus.NOT_FOUND);
    }
}
