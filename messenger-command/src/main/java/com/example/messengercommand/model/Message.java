package com.example.messengercommand.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Entity
@Table(name = "messages", uniqueConstraints = {
        @UniqueConstraint(name = "UQ__messages__69B13FDD5866B2CE", columnNames = {"session_id"})
})
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