package com.example.springbootchatmessenger.message;


import com.example.springbootchatmessenger.session.SessionEntityDto;
import com.example.springbootchatmessenger.session.SessionMapper;
import com.example.springbootchatmessenger.session.SessionService;
import com.example.springbootchatmessenger.utility.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
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
    private final SessionService sessionService;

    public MessageServiceImpl(MessageRepository messageRepository,
                              SessionService sessionService) {
        this.messageRepository = messageRepository;
        this.sessionService = sessionService;
    }



    @Override
    public void saveMessage(MessageContent messageContent) {
        // create new message entity if there is no tuple in database
        addMessageContentAndUpdateMessageEntity(messageContent);
    }

    @Override
    public MessageEntity saveMessageEntity(SessionEntityDto sessionEntityDto) {
        return createMessageEntity(sessionEntityDto);
    }

    @Override
    public MessageContent findById(Long id) {
        return null;
    }

    @Override
    public List<MessageContent> findAll(SessionEntityDto sessionEntityDto) {
        // find message entity
        MessageEntity messageEntity = messageRepository.findBySessionEntity(SessionMapper.INSTANCE.sessionDtoToSessionEntity(sessionEntityDto));
        // if message entity is null create one
        if (messageEntity == null) {
            messageEntity = saveMessageEntity(sessionEntityDto);
        }
        // then read message contents
        try {
            MessageContentList messageContentList = checkNull(JsonMapper.MAPPER.readValue(messageEntity.getContent(),MessageContentList.class));
            return messageContentList.getMessageContentList();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }





    private MessageEntity createMessageEntity(SessionEntityDto sessionEntityDto) {

        // find message entity by session
        MessageEntity messageEntity = messageRepository.findBySessionEntity(SessionMapper.INSTANCE.sessionDtoToSessionEntity(sessionEntityDto));
        // if message entity is null create one
        if (messageEntity == null) {
            messageEntity = new MessageEntity();
            messageEntity.setSessionEntity(SessionMapper.INSTANCE.sessionDtoToSessionEntity(sessionEntityDto));
            MessageContentList messageContentList = new MessageContentList();
            try {
                messageEntity.setContent(JsonMapper.MAPPER.writeValueAsString(messageContentList));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        // persist message entity
        return messageRepository.save(messageEntity);
    }

    private void addMessageContentAndUpdateMessageEntity(MessageContent messageContent) {
        try {
            // find session (we need session to detect messages between two users)
            SessionEntityDto sessionEntityDto = sessionService.findByUsernames(messageContent.getSender(), messageContent.getReceiver());
            MessageEntity messageEntity = messageRepository.findBySessionEntity(SessionMapper.INSTANCE.sessionDtoToSessionEntity(sessionEntityDto));
            // get message content (sender , receiver and message) then map it from json to an object
            MessageContentList messageContentList = checkNull(JsonMapper.MAPPER.readValue(messageEntity.getContent(), MessageContentList.class));
            // add message
            messageContentList.getMessageContentList().add(messageContent);
            messageEntity.setContent(JsonMapper.MAPPER.writeValueAsString(messageContentList));
            // persist data
            messageRepository.save(messageEntity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private MessageContentList checkNull(MessageContentList messageContentList) {
        // if the list is empty create one
        if (messageContentList.getMessageContentList() == null) {
            messageContentList.setMessageContentList(new ArrayList<>());
            return messageContentList;
        }
        return messageContentList;
    }

}
