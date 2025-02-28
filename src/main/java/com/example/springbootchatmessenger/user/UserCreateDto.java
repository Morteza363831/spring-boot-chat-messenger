package com.example.springbootchatmessenger.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link UserEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreateDto implements Serializable {
    @Size(min = 2, max = 25, message = "Username size must be between 2 and 25")
    @NotBlank(message = "Username cannot be null")
    private String username;
    @Email(message = "Invalid email type")
    @NotBlank(message = "Email cannot be null")
    private String email;
    private String firstName;
    private String lastName;
    @NotBlank(message = "Password cannot be null")
    private String password;
}