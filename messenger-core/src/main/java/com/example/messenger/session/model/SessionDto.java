package com.example.messenger.session.model;

import java.util.UUID;

/**
 * DTO for {@link SessionEntity}
 */
public record SessionDto(

        UUID id,

        String user1,

        String user2) {}