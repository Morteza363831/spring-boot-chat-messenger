package com.example.springbootchatmessenger.user;


import jakarta.persistence.Table;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * this class will process between backend and database
 */

@Repository
@Table(name = "users")
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Lazy
    UserEntity findByUsername(String username);
}

