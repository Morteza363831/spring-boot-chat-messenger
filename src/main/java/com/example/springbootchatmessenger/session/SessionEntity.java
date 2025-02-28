package com.example.springbootchatmessenger.session;

import com.example.springbootchatmessenger.exceptions.SessionUserSizeException;
import com.example.springbootchatmessenger.message.MessageEntity;
import com.example.springbootchatmessenger.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.HibernateProxy;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/*
 * session entity will store sessions for each user in the database
 * so for each user we have a chat page with chatId equal to id (session id)
 */
@Entity
@Table(name = "sessions", uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"}))
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false, referencedColumnName = "id")
    private UserEntity user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false, referencedColumnName = "id")
    private UserEntity user2;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private MessageEntity messageEntity;

    @PrePersist
    @PreUpdate
    public void validateUsers() {
        if (user1.equals(user2)) {
            throw new SessionUserSizeException("A session must have two different users.");
        }
    }

    public static SessionEntity createSession(UserEntity user1, UserEntity user2) {
        if (user1.getId().compareTo(user2.getId()) > 0) {
            return new SessionEntity(null, user2, user1, null); // Always store in consistent order
        }
        return new SessionEntity(null, user1, user2, null);
    }
}
