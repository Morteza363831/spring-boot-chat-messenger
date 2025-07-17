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
@Table(name = "messages", uniqueConstraints = {
        @UniqueConstraint(name = "session_id", columnNames = {"session_id"})
})
public class Message {
    @Id
    @Size(max = 16)
    @Column(name = "id", nullable = false, length = 16)
    private String id;

    @NotNull
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Size(max = 255)
    @NotNull
    @Column(name = "encrypted_aes_key", nullable = false)
    private String encryptedAesKey;

}