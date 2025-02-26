package com.example.springbootchatmessenger.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String type;
    private int status;
    private String message;
    private String path;
    private Date timestamp;
    private String description;
}
