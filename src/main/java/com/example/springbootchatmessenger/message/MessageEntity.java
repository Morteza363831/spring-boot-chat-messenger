package com.example.springbootchatmessenger.message;


import com.example.springbootchatmessenger.session.SessionEntity;
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
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    private SessionEntity sessionEntity;

    @Lob
    private String content;



}
