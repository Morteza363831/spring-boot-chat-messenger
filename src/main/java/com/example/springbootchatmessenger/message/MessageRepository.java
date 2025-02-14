package com.example.springbootchatmessenger.message;


import com.example.springbootchatmessenger.session.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/*
 * store messages in database using this repository
 */

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {



    /*
     * Will find a message entity using session entity .
     * The message entity contains all messages and information
     * about sender and receiver and session id too.
     */
    MessageEntity findBySessionEntity(SessionEntity sessionEntity);

    MessageEntity findBySessionEntityId(UUID sessionEntityId);
}
