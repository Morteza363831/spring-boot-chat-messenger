package com.example.springbootchatmessenger.message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    void saveMessage(final UUID sessionId,final MessageContent messageContent);

    MessageDto saveMessageEntity(final UUID sessionId);

    MessageContent findById(Long id);

    List<MessageContent> findAll(final UUID sessionId);
}
