package com.example.messengerquery.mysql.repository;

import com.example.messengerquery.model.Session;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {

    @Transactional(rollbackOn = Exception.class)
    @EntityGraph(attributePaths = {"user1", "user2"})
    @Query("""
        SELECT s FROM Session s
        WHERE (s.user1.id = :user1Id AND s.user2.id = :user2Id)
           OR (s.user1.id = :user2Id AND s.user2.id = :user1Id)
    """)
    Optional<Session> findExistingSession(@Param("user1Id") String user1Id, @Param("user2Id") String user2Id);

    @EntityGraph(attributePaths = {"user1", "user2"})
    @Query(value = "select s from Session s")
    Page<Session> findAllEntityGraph(Pageable pageable);
}