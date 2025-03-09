package com.example.springbootchatmessenger.user;

import lombok.Value;

import java.util.UUID;

/**
 * DTO for {@link UserEntity}
 */

@Value
public class UserDto {

    UUID id;
    String username;
    String email;
    String firstName;
    String lastName;
    String authorities;
}