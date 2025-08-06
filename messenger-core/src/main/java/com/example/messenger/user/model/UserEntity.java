package com.example.messenger.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * This is a entity-dto projection to store data .
 * I will send command requests (Insert, Update and Delete) using this **template**
 * */

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserEntity {

    @Id
    private UUID id;
    private String username;
    private boolean enabled;
    private String email;
    private String firstName;
    private String lastName;
    private String authorities;
    private String password;

    public UserEntity() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
