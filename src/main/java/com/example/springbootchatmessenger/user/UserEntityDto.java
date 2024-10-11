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
public class UserEntityDto implements Serializable {
    String username;
    Boolean enabled;
    String email;
    Boolean emailVerified;
    String firstName;
    String lastName;
    String password;

}