package com.example.messengerquery.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Setter
@Getter
@ToString
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class IndexingException extends BaseException {

    public IndexingException(String entityName) {
        super("Something went wrong during indexing : " + entityName, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public IndexingException(Throwable cause) {
        super(cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
