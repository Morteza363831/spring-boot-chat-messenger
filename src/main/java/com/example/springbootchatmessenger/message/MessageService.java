package com.example.springbootchatmessenger.message;

import com.example.springbootchatmessenger.session.SessionEntityDto;

import java.util.List;

public interface MessageService {

    void saveMessage(MessageContent messageContent);

    MessageEntity saveMessageEntity(SessionEntityDto sessionEntityDto);

    MessageContent findById(Long id);

    List<MessageContent> findAll(SessionEntityDto sessionEntityDto);
}
