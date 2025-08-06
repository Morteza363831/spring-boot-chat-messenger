package com.example.messenger.session.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for {@link SessionEntity}
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionDeleteDto {
    @NotNull(message = "User1 name cannot be null")
    @NotEmpty(message = "User1 name cannot be empty")
    private String user1;
    @NotNull(message = "User2 name cannot be null")
    @NotEmpty(message = "User2 name cannot be empty")
    private String user2;
}