package com.example.messenger.exceptions;

import org.springframework.http.HttpStatus;

public class EntityAlreadyExistException extends BaseException {

    public EntityAlreadyExistException(String entityName) {
        super(entityName + " already exists", HttpStatus.CONFLICT);
    }
}
