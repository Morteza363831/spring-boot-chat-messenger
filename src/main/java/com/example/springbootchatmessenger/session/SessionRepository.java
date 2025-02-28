package com.example.springbootchatmessenger.session;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/*
 * this class store sessions in database
 */

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, UUID> {


    /*
     * this query will get session for two users
     * we can use this session to open the chat page (using chat id)
     */

    @Transactional(rollbackOn = Exception.class)
    @Query("""
        SELECT s FROM SessionEntity s
        WHERE (s.user1.id = :user1Id AND s.user2.id = :user2Id)
           OR (s.user1.id = :user2Id AND s.user2.id = :user1Id)
    """)
    Optional<SessionEntity> findExistingSession(@Param("user1Id") UUID user1Id, @Param("user2Id") UUID user2Id);
}
