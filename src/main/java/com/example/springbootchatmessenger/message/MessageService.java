package com.example.springbootchatmessenger.message;

import com.example.springbootchatmessenger.session.SessionEntityDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    void saveMessage(final UUID sessionId,final MessageContent messageContent);

    MessageEntity saveMessageEntity(final SessionEntityDto sessionEntityDto);

    MessageContent findById(Long id);

    List<MessageContent> findAll(final SessionEntityDto sessionEntityDto);
}
