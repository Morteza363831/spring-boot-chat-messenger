package com.example.messengercommand.model;

import com.example.messengercommand.utility.EncryptionUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @Size(max = 36)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Size(max = 25)
    @Column(name = "username", nullable = false, length = 25)
    private String username;

    @Size(max = 100)
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Lob
    @Column(name = "authorities", nullable = false)
    private String authorities;

    @Size(max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @PrePersist
    public void prePersist() {
        if (authorities == null || authorities.isBlank() || EncryptionUtil.isEncrypted(authorities)) {
            authorities = "ROLE_USER";
            authorities= EncryptionUtil.encrypt(authorities);
        }
        if (!enabled) {
            enabled = true;
        }
    }

    @PreUpdate
    private void preUpdate() {
        if (!authorities.isBlank() && EncryptionUtil.isEncrypted(authorities)) {
            authorities = EncryptionUtil.encrypt(authorities);
        }
    }

}