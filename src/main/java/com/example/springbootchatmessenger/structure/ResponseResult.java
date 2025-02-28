package com.example.springbootchatmessenger.structure;

import lombok.Value;

import java.util.Date;

@Value
public class ResponseResult<T> {
    String status;      // success, error, fail
    int statusCode;     // HTTP status code
    String message;     // Custom message
    Date timestamp;     // Response time
    T data;             // Generic response data
    String path;        // Endpoint path

    public ResponseResult(String status, int statusCode, String message, T data, String path) {
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = new Date();
        this.data = data;
        this.path = path;
    }

    // Getters and Setters
}
