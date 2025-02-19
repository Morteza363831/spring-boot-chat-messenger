package com.example.springbootchatmessenger.session;

import com.example.springbootchatmessenger.user.UserEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link SessionEntity}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntityDto implements Serializable {

    @NotNull(message = "Session Id cannot be null")
    private UUID id;
    @Size(min = 1)
    @NotNull(message = "User entity list cannot be null")
    private Set<UserEntity> userEntities;
}