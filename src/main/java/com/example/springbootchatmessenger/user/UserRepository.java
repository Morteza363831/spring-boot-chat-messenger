package com.example.springbootchatmessenger.user;


import jakarta.persistence.Table;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/*
 * this class will process between backend and database
 */

@Repository
@Table(name = "users")
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Lazy
    Optional<UserEntity> findByUsername(final String username);

    boolean existsByUsername(final String username);
}

