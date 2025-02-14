package com.example.springbootchatmessenger.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageContent {

    private Long senderUserId;
    private Long receiverUserId;
    private String content;
}
