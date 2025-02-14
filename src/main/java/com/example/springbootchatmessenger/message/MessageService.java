package com.example.springbootchatmessenger.message;

import com.example.springbootchatmessenger.session.SessionEntityDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    void saveMessage(UUID sessionId, MessageContent messageContent);

    MessageEntity saveMessageEntity(SessionEntityDto sessionEntityDto);

    MessageContent findById(Long id);

    List<MessageContent> findAll(SessionEntityDto sessionEntityDto);
}
