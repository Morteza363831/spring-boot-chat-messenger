package com.example.messengercommand.model;

import com.example.messengercommand.utility.EncryptionUtil;
import jakarta.persistence.*;
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