package com.example.messenger.session.model;

import com.example.messenger.user.model.UserEntity;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SessionEntity {

    private UUID id;
    private UserEntity user1;
    private UserEntity user2;

    public static SessionEntity createSession(UserEntity user1, UserEntity user2) {
        if (user1.getId().compareTo(user2.getId()) > 0) {
            return new SessionEntity(UUID.randomUUID(), user2, user1); // Always store in consistent order
        }
        return new SessionEntity(UUID.randomUUID(), user1, user2);
    }
}
