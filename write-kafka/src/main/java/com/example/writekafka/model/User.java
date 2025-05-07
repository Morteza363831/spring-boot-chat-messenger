package com.example.writekafka.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "UQ__users__F3DBC572F7A0BFA8", columnNames = {"username"}),
        @UniqueConstraint(name = "UQ__users__AB6E6164A43CA5BA", columnNames = {"email"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 25)
    @NotNull
    @Column(name = "username", nullable = false, length = 25)
    private String username;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @NotNull
    @Lob
    @Column(name = "authorities", nullable = false)
    private String authorities;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

}