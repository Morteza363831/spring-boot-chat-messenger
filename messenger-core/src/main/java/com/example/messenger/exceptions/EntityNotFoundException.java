package com.example.messenger.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends  BaseException {
    String entityId;
    public EntityNotFoundException(String entityId) {
        super("Entity : " + entityId + " not found.", HttpStatus.NOT_FOUND);
        this.entityId = entityId;
    }
}
