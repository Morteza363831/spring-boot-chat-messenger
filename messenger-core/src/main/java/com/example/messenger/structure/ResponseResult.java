package com.example.messenger.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Date;

@Value
public class ResponseResult<T> {
    @JsonProperty("status")
    String status;      // success, error, fail
    @JsonProperty("status_code")
    int statusCode;     // HTTP status code
    @JsonProperty("message")
    String message;     // Custom message
    @JsonProperty("timestamp")
    Date timestamp;     // Response time
    @JsonProperty("data")
    T data;             // Generic response data
    @JsonProperty("path")
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
