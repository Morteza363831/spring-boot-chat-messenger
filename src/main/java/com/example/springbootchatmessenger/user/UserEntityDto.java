package com.example.springbootchatmessenger.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link UserEntity}
 */

@Data
public class UserEntityDto {

    private String username;
    private Boolean enabled;
    private String email;
    private Boolean emailVerified;
    private String firstName;
    private String lastName;
    private String authorities;
    private String password;

}