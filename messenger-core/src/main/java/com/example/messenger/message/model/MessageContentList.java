package com.example.messenger.message.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageContentList {

    private List<MessageContent> messageContentList;
}
