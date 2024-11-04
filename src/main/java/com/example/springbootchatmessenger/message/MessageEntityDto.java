package com.example.springbootchatmessenger.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link MessageEntity}
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntityDto implements Serializable {
    //private long id;
    private String content;
    private String sender;
    private String receiver;
}