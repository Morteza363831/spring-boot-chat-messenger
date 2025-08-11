package com.example.messengercommand.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Setter
@Getter
@ToString
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnknownException extends BaseException {

    public UnknownException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public UnknownException(Throwable cause) {
        super(cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
