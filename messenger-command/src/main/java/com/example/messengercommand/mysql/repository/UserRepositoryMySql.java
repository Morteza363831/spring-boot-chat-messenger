package com.example.messengercommand.mysql.repository;

import com.example.messengercommand.model.User;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepositoryMySql extends JpaRepository<User, String> {

    @Transactional(rollbackOn = Exception.class)
    @Lazy
    Optional<User> findByUsername(final String username);

    @Transactional(rollbackOn = Exception.class)
    boolean existsByUsername(final String username);
}
