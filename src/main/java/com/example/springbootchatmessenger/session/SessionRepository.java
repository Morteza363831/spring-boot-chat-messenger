package com.example.springbootchatmessenger.session;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    @Query("SELECT s FROM SessionEntity s JOIN FETCH s.userEntities WHERE s.id IN " +
            "(SELECT s1.id FROM SessionEntity s1 JOIN s1.userEntities u WHERE u.id IN (:userIds) " +
            "GROUP BY s1 HAVING COUNT(u.id) = :size)")
    @Transactional(value = Transactional.TxType.REQUIRES_NEW,rollbackOn = Exception.class)
    List<SessionEntity> findSessionEntityByUserEntities(@Param("userIds") List<UUID> userIds, @Param("size") Long size);
}
