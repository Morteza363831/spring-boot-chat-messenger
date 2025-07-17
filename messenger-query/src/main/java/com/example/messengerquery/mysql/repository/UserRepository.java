package com.example.messengerquery.mysql.repository;

import com.example.messengerquery.model.User;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Transactional(rollbackOn = Exception.class)
    @Lazy
    Optional<User> findByUsername(final String username);

    @Transactional(rollbackOn = Exception.class)
    boolean existsByUsername(final String username);
}