package com.example.messenger.user;


import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Table(name = "users")
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Transactional(rollbackOn = Exception.class)
    @Lazy
    Optional<UserEntity> findByUsername(final String username);

    @Transactional(rollbackOn = Exception.class)
    boolean existsByUsername(final String username);
}

