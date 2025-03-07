package com.example.springbootchatmessenger.message;


import com.example.springbootchatmessenger.exceptions.CustomValidationException;
import com.example.springbootchatmessenger.utility.JsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
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
    private final Validator validator;
    private final ObjectMapper mapper;

    public MessageServiceImpl(final MessageRepository messageRepository,
                              final Validator validator,
                              ObjectMapper mapper) {
        this.messageRepository = messageRepository;
        this.validator = validator;
        this.mapper = mapper;
    }



    @Transactional(rollbackOn = Exception.class)
    @Override
    public void saveMessage(final UUID sessionId,final MessageContent messageContent) {

        validate(messageContent);
        // create new message entity if there is no tuple in database
        addMessageContentAndUpdateMessageEntity(sessionId, messageContent);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public MessageDto saveMessageEntity(final UUID sessionId) {

        return createMessageEntity(sessionId);
    }

    @Override
    public MessageContent findById(Long id) {
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public List<MessageContent> findAll(final UUID sessionId) {

        // find message entity
        Optional<MessageEntity> messageEntityOptional = messageRepository.findBySessionId(sessionId);

        MessageDto messageDto = null;
        if (messageEntityOptional.isPresent()) {
            messageDto = MessageMapper.INSTANCE.toDto(messageEntityOptional.get());
        }
        // if message entity is null create one
        if (messageEntityOptional.isEmpty()) {
            messageDto = saveMessageEntity(sessionId);
        }
        // then read message contents
        MessageContentList messageContentList = new MessageContentList();
        try {
            messageContentList = mapper.readValue(messageDto.getContent(), MessageContentList.class);
        } catch (JsonProcessingException e) {
            log.error("Couldn't parse message", e);
        }
        return messageContentList.getMessageContentList();
    }





    private MessageDto createMessageEntity(final UUID sessionId) {

        // find message entity by session
        Optional<MessageEntity> messageEntityOptional = messageRepository.findBySessionId(sessionId);
        // if message entity is null create one
        if (messageEntityOptional.isPresent()) {
            // persist message entity
            return MessageMapper.INSTANCE.toDto(messageRepository.save(messageEntityOptional.get()));
        }
        // persist message entity
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSessionId(sessionId);
        try {
            messageEntity.setContent(mapper.writeValueAsString(new MessageContentList()));
        } catch (JsonProcessingException e) {
            log.error("Couldn't parse messages");
        }
        return MessageMapper.INSTANCE.toDto(messageRepository.save(messageEntity));
    }

    private void addMessageContentAndUpdateMessageEntity(UUID sessionId, MessageContent messageContent) {
        try {
            Optional<MessageEntity> messageEntityOptional = messageRepository.findBySessionId(sessionId);
            // get message content (sender , receiver and message) then map it from json to an object
            MessageContentList messageContentList = checkNull(JsonMapper.MAPPER.readValue(messageEntityOptional.get().getContent(), MessageContentList.class));
            // add message
            messageContentList.getMessageContentList().add(messageContent);
            messageEntityOptional.get().setContent(JsonMapper.MAPPER.writeValueAsString(messageContentList));
            // persist data
            messageRepository.save(messageEntityOptional.get());
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

    private void validate(Object dto) {
        List<String> violations = new ArrayList<>();
        validator.validate(dto).forEach(field -> violations.add(field.getMessage()));
        if (!violations.isEmpty()) {
            throw new CustomValidationException(violations);
        }
    }
}
