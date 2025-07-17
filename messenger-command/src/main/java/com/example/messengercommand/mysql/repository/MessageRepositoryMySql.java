package com.example.messengercommand.mysql.repository;

import com.example.messengercommand.model.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepositoryMySql extends JpaRepository<Message, UUID> {

    @Transactional(rollbackOn = Exception.class)
    @Query("SELECT m FROM Message m WHERE m.sessionId = :sessionId")
    Optional<Message> findBySessionId(@Param("sessionId") final UUID sessionId);
}
