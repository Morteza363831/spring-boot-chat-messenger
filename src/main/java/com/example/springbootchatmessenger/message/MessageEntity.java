package com.example.springbootchatmessenger.message;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

/*
 * message entity to store messages in database .
 * id for each message
 * content to store the message
 * sender to store sender username
 * receiver to store receiver username
 * --> this structure helps us find messages between to users
 */
@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;

    @Lob
    @Column(columnDefinition = "BLOB")
    @JdbcTypeCode(SqlTypes.BLOB)
    private byte[] content;

    @NotNull
    @Column(name = "session_id", unique = true, nullable = false)
    private UUID sessionId; // Store only session ID


    public String getContent() {
        return content != null ? new String(content) : "";
    }

    public void setContent(String content) {
        this.content = content.getBytes();
    }
}
