package com.example.springbootchatmessenger.session;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * session entity will store sessions for each user in the database
 * so for each user we have a chat page with chatId equal to id (session id)
 */

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
public class SessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username1;
    private String username2;
}
