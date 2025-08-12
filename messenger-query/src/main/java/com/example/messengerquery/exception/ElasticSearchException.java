package com.example.messengerquery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ElasticSearchException extends BaseException {

    public ElasticSearchException() {
        super("Could not connect to Elasticsearch.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
