package com.example.messenger.session;

import com.example.messenger.exceptions.SessionUserSizeException;
import com.example.messenger.message.MessageEntity;
import com.example.messenger.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SessionEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false, referencedColumnName = "id")
    private UserEntity user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false, referencedColumnName = "id")
    private UserEntity user2;

    @PrePersist
    @PreUpdate
    public void validateUsers() {
        //TODO
    }

    public static SessionEntity createSession(UserEntity user1, UserEntity user2) {
        if (user1.getId().compareTo(user2.getId()) > 0) {
            return new SessionEntity(UUID.randomUUID(), user2, user1); // Always store in consistent order
        }
        return new SessionEntity(UUID.randomUUID(), user1, user2);
    }
}
