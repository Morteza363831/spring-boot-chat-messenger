package com.example.springbootchatmessenger.message;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    @Transactional(rollbackOn = Exception.class)
    @Query("SELECT m FROM MessageEntity m WHERE m.sessionId = :sessionId")
    Optional<MessageEntity> findBySessionId(@Param("sessionId") final UUID sessionId);
}
