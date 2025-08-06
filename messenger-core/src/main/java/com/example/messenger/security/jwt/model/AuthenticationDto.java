package com.example.messenger.security.jwt.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthenticationDto class contains username and password to getting token
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDto {

    @NotBlank(message = "invalid username")
    private String username;
    @NotBlank(message = "invalid password")
    private String password;
}
