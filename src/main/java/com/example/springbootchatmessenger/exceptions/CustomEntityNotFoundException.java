package com.example.springbootchatmessenger.exceptions;

import org.springframework.http.HttpStatus;

public class CustomEntityNotFoundException extends  BaseException {
    String entityId;
    public CustomEntityNotFoundException(String entityId) {
        super("Entity with id : " + entityId + " not found.", HttpStatus.NOT_FOUND);
        this.entityId = entityId;
    }
}
