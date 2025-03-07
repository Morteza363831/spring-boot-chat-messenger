package com.example.springbootchatmessenger.user;

import com.example.springbootchatmessenger.roles.EncryptionUtil;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Objects;
import java.util.UUID;

/*
 * user entity class will store details about user in database
 */

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;
    @Column(nullable = false, unique = true, columnDefinition = "varchar(25)")
    private String username;
    @Column(nullable = false)
    private boolean enabled;
    @Column(nullable = false, unique = true, columnDefinition = "varchar(100)")
    private String email;
    @Column(columnDefinition = "varchar(50)")
    private String firstName;
    @Column(columnDefinition = "varchar(50)")
    private String lastName;
    @Column(nullable = false, columnDefinition = "varchar(max)")
    private String authorities;
    @Column(nullable = false, columnDefinition = "varchar(255)")
    private String password;

    @PrePersist
    public void prePersist() {
        if (authorities == null || authorities.isBlank() || !EncryptionUtil.isEncrypted(authorities)) {
             authorities = "ROLE_USER";
             authorities= EncryptionUtil.encrypt(authorities);
        }
    }

    @PreUpdate
    private void preUpdate() {
        if (!authorities.isBlank() && !EncryptionUtil.isEncrypted(authorities)) {
            authorities = EncryptionUtil.encrypt(authorities);
        }
    }

    @PostPersist
    public void postPersist() {
        if (email == null || email.isEmpty()) {
            throw new DataIntegrityViolationException("Email is required");
        }
    }



    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserEntity that = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
