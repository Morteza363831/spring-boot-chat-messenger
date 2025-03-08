package com.example.springbootchatmessenger.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuthenticationDto {

    @NotBlank(message = "invalid username")
    private String username;
    @NotBlank(message = "invalid password")
    private String password;
}
