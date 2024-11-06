package com.example.springbootchatmessenger.message;


import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
 * this class will handle requests and responses for messages
 * --> save messages in database
 * --> get all messages from database
 * --> get special message (unnecessary)
 */

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }



    @Override
    public MessageEntityDto save(MessageEntityDto messageEntityDto) {
        MessageEntity messageEntity = MessageMapper.INSTANCE.messageEntityDtoToMessageEntity(messageEntityDto);
        return MessageMapper.INSTANCE.messageEntityToMessageEntityDto(messageRepository.save(messageEntity));
    }

    @Override
    public MessageEntityDto findById(Long id) {
        MessageEntity messageEntity = messageRepository.findById(id).orElse(null);

        if (messageEntity == null) {
            log.error("Message with id {} not found", id);
            return null;
        }
        return MessageMapper.INSTANCE.messageEntityToMessageEntityDto(messageEntity);
    }

    @Override
    public List<MessageEntityDto> findAll(String sender, String receiver) {
        List<MessageEntity> messageEntities = messageRepository.findMessagesByUsernames(sender, receiver);
        return MessageMapper.INSTANCE.messageEntityListToMessageEntityDtoList(messageEntities);
    }
}
