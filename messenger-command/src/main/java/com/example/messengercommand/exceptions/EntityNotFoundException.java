package com.example.messengercommand.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Setter
@Getter
@ToString
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException(String entityName) {
        super("Entity with name : " + entityName + " not found.", HttpStatus.NOT_FOUND);
    }
}
