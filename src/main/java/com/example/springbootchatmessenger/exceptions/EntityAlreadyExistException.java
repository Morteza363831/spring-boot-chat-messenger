package com.example.springbootchatmessenger.exceptions;

import org.springframework.http.HttpStatus;

public class EntityAlreadyExistException extends BaseException {
    private String entityName;
    public EntityAlreadyExistException(String entityName) {
        super(entityName + " already exists", HttpStatus.CONFLICT);
        this.entityName = entityName;
    }
}
