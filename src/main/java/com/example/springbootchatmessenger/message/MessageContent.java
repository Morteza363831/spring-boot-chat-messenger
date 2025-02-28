package com.example.springbootchatmessenger.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageContent {

    @NotBlank(message = "Sender username cannot be null")
    private String sender;
    @NotBlank(message = "Receiver username cannot be null")
    private String receiver;
    @NotNull(message = "Content cannot be null")
    private String content;
}
