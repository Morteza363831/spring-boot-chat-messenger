package com.example.messengerquery.model;

import com.example.messengerquery.util.EncryptionUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "username", columnNames = {"username"}),
        @UniqueConstraint(name = "email", columnNames = {"email"})
})
public class User {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "username", nullable = false, length = 25)
    private String username;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Lob
    @Column(name = "authorities", nullable = false)
    private String authorities;

    @Column(name = "password", nullable = false)
    private String password;

    @PostLoad
    private void postLoad() {
        authorities = EncryptionUtil.decrypt(authorities);
    }

}