package com.example.writekafka.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "messages", uniqueConstraints = {
        @UniqueConstraint(name = "UQ__messages__69B13FDD5866B2CE", columnNames = {"session_id"})
})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

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