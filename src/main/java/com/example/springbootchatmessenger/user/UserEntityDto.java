package com.example.springbootchatmessenger.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link UserEntity}
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntityDto {

    private String username;
    private Boolean enabled;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String authorities;
}