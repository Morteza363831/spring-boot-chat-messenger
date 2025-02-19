package com.example.springbootchatmessenger.message;


import com.example.springbootchatmessenger.session.SessionEntityDto;
import com.example.springbootchatmessenger.session.SessionMapper;
import com.example.springbootchatmessenger.utility.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private final ObjectMapper mapper;

    public MessageServiceImpl(MessageRepository messageRepository,
                              ObjectMapper mapper) {
        this.messageRepository = messageRepository;
        this.mapper = mapper;
    }



    @Transactional(rollbackOn = Exception.class)
    @Override
    public void saveMessage(UUID sessionId, MessageContent messageContent) {
        // create new message entity if there is no tuple in database
        addMessageContentAndUpdateMessageEntity(sessionId, messageContent);
    }

    @Transactional(rollbackOn = Exception.class)
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
        Optional<MessageEntity> messageEntityOptional = Optional
                .ofNullable(messageRepository
                        .findBySessionEntity(SessionMapper.INSTANCE.sessionDtoToSessionEntity(sessionEntityDto)));

        MessageEntity messageEntity = messageEntityOptional.orElse(null);
        // if message entity is null create one
        if (messageEntityOptional.isEmpty()) {
            messageEntity = saveMessageEntity(sessionEntityDto);
        }
        // then read message contents
        MessageContentList messageContentList = new MessageContentList();
        try {
            messageContentList = mapper.readValue(messageEntity.getContent(), MessageContentList.class);
        } catch (JsonProcessingException e) {
            log.error("Couldn't parse message", e);
        }
        return messageContentList.getMessageContentList();
    }





    private MessageEntity createMessageEntity(SessionEntityDto sessionEntityDto) {

        // find message entity by session
        Optional<MessageEntity> messageEntityOptional = Optional
                .ofNullable(messageRepository
                        .findBySessionEntity(SessionMapper.INSTANCE.sessionDtoToSessionEntity(sessionEntityDto)));
        // if message entity is null create one
        if (messageEntityOptional.isPresent()) {
            // persist message entity
            return messageRepository.save(messageEntityOptional.get());
        }
        // persist message entity
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSessionEntity(SessionMapper.INSTANCE.sessionDtoToSessionEntity(sessionEntityDto));
        try {
            messageEntity.setContent(mapper.writeValueAsString(new MessageContentList()));
        } catch (JsonProcessingException e) {
            log.error("Couldn't parse messages");
        }
        return messageRepository.save(messageEntity);
    }

    private void addMessageContentAndUpdateMessageEntity(UUID sessionId, MessageContent messageContent) {
        try {
            MessageEntity messageEntity = messageRepository.findBySessionEntityId(sessionId);
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
