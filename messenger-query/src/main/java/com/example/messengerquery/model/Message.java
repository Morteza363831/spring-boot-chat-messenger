package com.example.messengerquery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.UUID;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "messages")
public class Message {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "encrypted_aes_key", nullable = false)
    private String encryptedAesKey;

}