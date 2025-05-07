package com.example.messenger.session;

import com.example.messenger.exceptions.SessionUserSizeException;
import com.example.messenger.message.MessageEntity;
import com.example.messenger.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

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
        //TODO
    }

    public static SessionEntity createSession(UserEntity user1, UserEntity user2) {
        if (user1.getId().compareTo(user2.getId()) > 0) {
            return new SessionEntity(null, user2, user1, null); // Always store in consistent order
        }
        return new SessionEntity(null, user1, user2, null);
    }
}
