package com.example.springbootchatmessenger.session;

import com.example.springbootchatmessenger.message.MessageEntity;
import com.example.springbootchatmessenger.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.proxy.HibernateProxy;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/*
 * session entity will store sessions for each user in the database
 * so for each user we have a chat page with chatId equal to id (session id)
 */

@Entity
@Table(name = "sessions")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /*private String username1;
    private String username2;*/

    @ManyToMany
    @JoinTable(name = "sessions_userEntities",
            joinColumns = @JoinColumn(name = "sessionEntity_id"),
            inverseJoinColumns = @JoinColumn(name = "userEntities_id"))
    @ToString.Exclude
    private Set<UserEntity> userEntities = new LinkedHashSet<>();

    @OneToOne(mappedBy = "sessionEntity", orphanRemoval = true)
    private MessageEntity messageEntity;

    public void addUser(UserEntity userEntity) {

        if (userEntities.size() >= 2) {
            log.error("A session can only have two users");
            return;
        }
        userEntities.add(userEntity);
    }

    public void removeUser(UserEntity userEntity) {
        userEntities.remove(userEntity);
    }

    @PrePersist
    @PreUpdate
    public void validateUsers() {
        if (userEntities.size() > 2) {
            throw new IllegalStateException("A session must have exactly two users.");
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        SessionEntity that = (SessionEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
