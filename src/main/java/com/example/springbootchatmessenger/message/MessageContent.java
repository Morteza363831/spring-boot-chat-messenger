package com.example.springbootchatmessenger.message;

import lombok.Data;

@Data
public class MessageContent {

    private String sender;
    private String receiver;
    private String content;
}
