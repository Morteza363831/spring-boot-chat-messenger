package com.example.messenger.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Setter
@Getter
@Builder
public class ResponseResult {
    @JsonProperty("status")
    private String status;      // success, error, fail
    @JsonProperty("status_code")
    private Integer statusCode;     // HTTP status code
    @JsonProperty("message")
    private String message;     // Custom message
    @JsonProperty("timestamp")
    @Builder.Default
    private Date timestamp= new Date();     // Response time
    @JsonProperty("data")
    private Object data;             // Generic response data
    @JsonProperty("path")
    private String path;        // Endpoint path

    // Getters and Setters
}
