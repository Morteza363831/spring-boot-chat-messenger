package com.example.springbootchatmessenger.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageContentList {

    private List<MessageContent> messageContentList;
}
