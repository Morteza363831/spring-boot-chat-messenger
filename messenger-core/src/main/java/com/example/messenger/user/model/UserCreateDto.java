package com.example.messenger.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link UserEntity}
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCreateDto implements Serializable {
    @Size(min = 2, max = 25, message = "Username size must be between 2 and 25")
    @NotBlank(message = "Username cannot be null")
    private String username;
    @Email(message = "Email is not valid", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    @NotBlank(message = "Email cannot be null")
    private String email;
    private String firstName;
    private String lastName;
    @NotBlank(message = "Password cannot be null")
    private String password;
}