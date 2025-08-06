package com.example.messenger.session.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link SessionEntity}
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionCreateDto {
    @NotNull(message = "User1 name cannot be null")
    @NotEmpty(message = "User1 name cannot be empty")
    private String user1;
    @NotNull(message = "User2 name cannot be null")
    @NotEmpty(message = "User2 name cannot be empty")
    private String user2;
}