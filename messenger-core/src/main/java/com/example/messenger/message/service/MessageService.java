package com.example.messenger.message;
import com.example.messenger.message.model.MessageContent;
import com.example.messenger.message.model.MessageEntity;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    void saveMessage(final UUID sessionId,final MessageContent messageContent);

    void saveMessageEntity(final UUID sessionId);

    MessageContent findById(Long id);

    List<MessageContent> findAll(final UUID sessionId);

    MessageEntity makeMessageEntityObject(UUID sessionId);
}
