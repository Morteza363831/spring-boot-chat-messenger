package com.example.messengerquery.mysql.repository;

import com.example.messengerquery.model.Session;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    @Transactional(rollbackOn = Exception.class)
    @Query("""
        SELECT s FROM Session s
        WHERE (s.user1Id = :user1Id AND s.user2Id = :user2Id)
           OR (s.user1Id = :user2Id AND s.user2Id = :user1Id)
    """)
    Optional<Session> findExistingSession(@Param("user1Id") UUID user1Id, @Param("user2Id") UUID user2Id);
}