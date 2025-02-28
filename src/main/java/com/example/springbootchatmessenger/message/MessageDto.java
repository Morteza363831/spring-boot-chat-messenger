package com.example.springbootchatmessenger.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link MessageEntity}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDto implements Serializable {

    UUID id;
    String content;
}