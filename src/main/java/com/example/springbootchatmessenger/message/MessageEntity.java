package com.example.springbootchatmessenger.message;


import com.example.springbootchatmessenger.session.SessionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/*
 * message entity to store messages in database .
 * id for each message
 * content to store the message
 * sender to store sender username
 * receiver to store receiver username
 * --> this structure helps us find messages between to users
 */

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Lob
    private String content;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "session_entity_id", referencedColumnName = "id")
    private SessionEntity sessionEntity;

}
