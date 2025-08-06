package com.example.messenger.user.model;

import java.util.UUID;

/**
 * DTO for {@link UserEntity}
 */


public record UserDto(

        UUID id,

        String username,

        String email,

        String firstName,

        String lastName,

        String authorities) {}