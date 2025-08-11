package com.example.messengercommand.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Setter
@Getter
@ToString
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ConvertObjectException extends BaseException {
    public ConvertObjectException(String entityName) {
        super("Cannot convert object data " + " to : " + entityName, HttpStatus.BAD_REQUEST);
    }
}
