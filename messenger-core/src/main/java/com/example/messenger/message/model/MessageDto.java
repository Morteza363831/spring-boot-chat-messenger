package com.example.messenger.message.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link MessageEntity}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MessageDto(

        UUID id,

        String content,

        UUID sessionId,

        String encryptedAesKey) { }