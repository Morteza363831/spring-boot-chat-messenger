package com.example.springbootchatmessenger.session;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * this class store sessions in database
 */

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {


    /*
     * this query will get session for two users
     * we can use this session to open the chat page (using chat id)
     */
    @Query(value = "SELECT * FROM sessions WHERE (username1 = :firstUsername " +
            "AND username2 = :secondUsername) " +
            "OR (username1 = :secondUsername AND username2 = :firstUsername)", nativeQuery = true)
    SessionEntity findByUsernames(@Param("firstUsername") String firstUsername, @Param("secondUsername") String secondUsername);

}
