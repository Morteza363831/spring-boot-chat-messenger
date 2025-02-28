package com.example.springbootchatmessenger.session;

import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link SessionEntity}
 */
@Value
public class SessionDto implements Serializable {

    UUID id;
    String user1;
    String user2;
}