package com.example.messenger.message;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
@Setter
@AllArgsConstructor
public class MessageEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Lob
    @Column(nullable = false)
    private String content;

    @NotNull
    @Column(name = "session_id", unique = true, nullable = false, updatable = false)
    private UUID sessionId; // Store only session ID

    @Column(nullable = false, updatable = false, unique = true)
    private String encryptedAesKey;

    public MessageEntity() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
