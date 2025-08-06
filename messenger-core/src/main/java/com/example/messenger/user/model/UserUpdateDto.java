package com.example.messenger.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
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
public class UserUpdateDto implements Serializable {

    private String username;
    @Email(message = "Email is not valid", regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private String email;
    @Size(message = "First name size must be less than 50", max = 50)
    private String firstName;
    @Size(message = "Last name size must be less than 50", max = 50)
    private String lastName;
    @Pattern(message = "Incorrect authorities ", regexp = "^$|^[A-Za-z0-9_]+(?:,[A-Za-z0-9_-]+)*$")
    private String authorities;
}