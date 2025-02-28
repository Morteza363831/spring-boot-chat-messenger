package com.example.springbootchatmessenger.user;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Value;

import java.util.UUID;

/**
 * DTO for {@link UserEntity}
 */

@Value
public class UserDto {

    UUID id;
    String username;
    boolean enabled;
    String email;
    String firstName;
    String lastName;
    String authorities;
}