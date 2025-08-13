package com.example.messenger.message.service;

import com.example.messenger.message.model.MessageContent;
import com.example.messenger.message.model.MessageEntity;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    void saveMessage(UUID sessionId, MessageContent messageContent);

    void saveEntity(UUID sessionId);

    MessageContent getEntity(Long id);

    List<MessageContent> getMessages(UUID sessionId);
}
