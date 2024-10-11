package com.example.springbootchatmessenger.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * user entity class will store details about user in database
 */

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private Boolean enabled;

    private String email;

    private boolean emailVerified;

    private String firstName;

    private String lastName;

    private String password;

}
