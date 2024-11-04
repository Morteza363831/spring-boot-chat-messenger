package com.example.springbootchatmessenger.message;


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

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;

    public MessageServiceImpl(MessageRepository messageRepository,
                              ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.modelMapper = modelMapper;
    }



    @Override
    public MessageEntityDto save(MessageEntityDto messageEntityDto) {
        MessageEntity messageEntity = modelMapper.map(messageEntityDto, MessageEntity.class);
        return modelMapper.map(messageRepository.save(messageEntity), MessageEntityDto.class);
    }

    @Override
    public MessageEntityDto findById(Long id) {
        MessageEntity messageEntity = messageRepository.findById(id).orElse(null);
        return modelMapper.map(messageEntity, MessageEntityDto.class);
    }

    @Override
    public List<MessageEntityDto> findAll(String sender, String receiver) {
        List<MessageEntity> messageEntities = messageRepository.findMessagesByUsernames(sender, receiver);
        List<MessageEntityDto> messageEntityDtos = new ArrayList<>();
        for (MessageEntity messageEntity : messageEntities) {
            messageEntityDtos.add(modelMapper.map(messageEntity, MessageEntityDto.class));
        }
        return messageEntityDtos;
    }
}
