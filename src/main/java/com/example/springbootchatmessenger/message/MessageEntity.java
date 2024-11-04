package com.example.springbootchatmessenger.message;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String content;

    private String sender;

    private String receiver;
}
