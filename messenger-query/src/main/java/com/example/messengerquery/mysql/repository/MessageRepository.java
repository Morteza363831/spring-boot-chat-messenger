package com.example.messengerquery.mysql.repository;

import com.example.messengerquery.model.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    @Transactional(rollbackOn = Exception.class)
    @Query("SELECT m FROM Message m WHERE m.sessionId = :sessionId")
    Optional<Message> findBySessionId(@Param("sessionId") final String sessionId);
}