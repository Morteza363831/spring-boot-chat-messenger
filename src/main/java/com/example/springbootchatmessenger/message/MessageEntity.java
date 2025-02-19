package com.example.springbootchatmessenger.message;


import com.example.springbootchatmessenger.session.SessionEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Objects;
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

    @NotNull(message = "Message Id cannot be null")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;

    @Lob
    @Column(columnDefinition = "BLOB")
    @JdbcTypeCode(SqlTypes.BLOB)
    private byte[] content;

    @NotNull(message = "Session Id cannot be null for Message")
    @OneToOne(orphanRemoval = true)
    @JoinColumn
    private SessionEntity sessionEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEntity that = (MessageEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(content, that.content) && Objects.equals(sessionEntity, that.sessionEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, sessionEntity);
    }

    public String getContent() {
        return new String(content);
    }

    public void setContent(String content) {
        this.content = content.getBytes();
    }
}
