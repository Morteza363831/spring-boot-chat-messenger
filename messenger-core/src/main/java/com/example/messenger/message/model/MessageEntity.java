package com.example.messenger.message.model;


import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MessageEntity {

    private UUID id;
    private String content;
    private UUID sessionId; // Store only session ID
    private String encryptedAesKey;

    public MessageEntity() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
