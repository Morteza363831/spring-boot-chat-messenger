package com.example.springbootchatmessenger.message;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * store messages in database using this repository
 */

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {


    /*
     * this query will find all messages between to users and display to them when they open the chat page !
     */
    @Query(value = "SELECT * FROM messages WHERE (sender = :sender AND receiver = :receiver) " +
            "OR (sender = :receiver AND receiver = :sender)", nativeQuery = true)
    List<MessageEntity> findMessagesByUsernames(@Param("sender") String sender, @Param("receiver") String receiver);
}
