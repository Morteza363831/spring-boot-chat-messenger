package com.example.messengercommand.mysql.repository;

import com.example.messengercommand.model.Session;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepositoryMySql extends JpaRepository<Session, UUID> {

    @Transactional(rollbackOn = Exception.class)
    @Query("""
        SELECT s FROM Session s
        WHERE (s.user1.id = :user1Id AND s.user2.id = :user2Id)
           OR (s.user1.id = :user2Id AND s.user2.id = :user1Id)
    """)
    Optional<Session> findExistingSession(@Param("user1Id") UUID user1Id, @Param("user2Id") UUID user2Id);
}
