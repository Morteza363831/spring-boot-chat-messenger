package com.example.springbootchatmessenger.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageContent {

    private UUID senderUserId;
    private UUID receiverUserId;
    private String content;
}
