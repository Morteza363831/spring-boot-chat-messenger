package com.example.springbootchatmessenger.session;

import com.example.springbootchatmessenger.user.UserEntity;
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
    private UUID id;
    private Set<UserEntity> userEntities;
}