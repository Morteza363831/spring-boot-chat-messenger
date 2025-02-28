package com.example.springbootchatmessenger.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link UserEntity}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateDto implements Serializable {
    @NotBlank(message = "Username cannot be null")
    private String username;
    @Email(message = "Invalid email type", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    @NotBlank(message = "Email cannot be null")
    private String email;
    @Size(message = "First name size must be less than 50", max = 50)
    private String firstName;
    @Size(message = "Last name size must be less than 50", max = 50)
    private String lastName;
    @Pattern(message = "Incorrect authorities ", regexp = "^$|^[A-Za-z0-9_]+(?:,[A-Za-z0-9_]+)*$")
    private String authorities;
}