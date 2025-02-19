package com.example.springbootchatmessenger.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link UserEntity}
 */

@Data
public class UserEntityDto {

    private UUID id;
    @NotEmpty(message = "Username cannot be empty")
    @NotBlank(message = "Username cannot be blank")
    @Size(message = "Username size must be between 1 and 25", min = 1, max = 25)
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "Enabled cannot be null")
    private Boolean enabled;
    @Email(message = "Incorrect email", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    @Size(min = 6, max = 100)
    @NotBlank(message = "Email cannot be blank")
    @NotEmpty(message = "Email cannot be empty")
    @NotNull(message = "Email cannot be null")
    private String email;
    @Size(message = "First name size must be less than 50", max = 50)
    private String firstName;
    @Size(message = "First name size must be less than 50", max = 50)
    private String lastName;
    @Pattern(message = "Incorrect authorities ", regexp = "^$|^[A-Za-z0-9_]+(?:,[A-Za-z0-9_]+)*$")
    private String authorities;
    @Size(message = "Password size must be between 1 and 50", min = 1, max = 255)
    @NotBlank(message = "Password cannot be blank")
    @NotEmpty(message = "Password cannot be empty")
    @NotNull(message = "Password cannot be null")
    private String password;
}