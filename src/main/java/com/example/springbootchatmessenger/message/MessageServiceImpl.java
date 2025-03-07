package com.example.springbootchatmessenger.message;


import com.example.springbootchatmessenger.exceptions.CustomEntityNotFoundException;
import com.example.springbootchatmessenger.exceptions.CustomValidationException;
import com.example.springbootchatmessenger.exceptions.EntityAlreadyExistException;
import com.example.springbootchatmessenger.utility.AESGCMUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

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
            messageContentList = mapper.readValue(decryption(messageDto.getContent(), decode(messageDto.getEncryptedAESKey())), MessageContentList.class);
        } catch (JsonProcessingException e) {
            log.error("Couldn't parse message", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return messageContentList.getMessageContentList();
    }

    private MessageDto createMessageEntity(final UUID sessionId) {

        // find message entity by session
        Optional<MessageEntity> messageEntityOptional = messageRepository.findBySessionId(sessionId);
        // if message entity is null create one
        if (messageEntityOptional.isPresent()) {
            // persist message entity
            throw new EntityAlreadyExistException(messageEntityOptional.get().getId().toString());
        }
        // persist message entity
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSessionId(sessionId);
        try {
            final SecretKey secretKey = AESGCMUtil.generateAESKey();
            messageEntity.setContent(encryption(mapper.writeValueAsString(new MessageContentList()), secretKey));
            messageEntity.setEncryptedAESKey(encode(secretKey));
        } catch (JsonProcessingException e) {
            log.error("Couldn't parse messages");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return MessageMapper.INSTANCE.toDto(messageRepository.save(messageEntity));
    }

    private void addMessageContentAndUpdateMessageEntity(UUID sessionId, MessageContent messageContent) {
        Optional<MessageEntity> messageEntityOptional = messageRepository.findBySessionId(sessionId);
        if (messageEntityOptional.isEmpty()) {
            throw new CustomEntityNotFoundException("Message entity not found for session: " + sessionId);
        }

        MessageEntity messageEntity = messageEntityOptional.get();
        try {
            // Decrypt the existing message content
            final SecretKey secretKey = decode(messageEntity.getEncryptedAESKey());
            final String decryptedContent = decryption(messageEntity.getContent(), secretKey);

            // Parse JSON
            MessageContentList messageContentList = checkNull(mapper.readValue(decryptedContent, MessageContentList.class));
            messageContentList.getMessageContentList().add(messageContent);

            // Encrypt updated content
            String encryptedContent = encryption(mapper.writeValueAsString(messageContentList), secretKey);
            messageEntity.setContent(encryptedContent);

            messageRepository.save(messageEntity);
        } catch (Exception e) {
            throw new RuntimeException("Error updating message entity", e);
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


    private String encryption(String content, SecretKey secretKey) {
        try {
            return AESGCMUtil.encrypt(content, secretKey, AESGCMUtil.generateIV());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String decryption(String content, SecretKey key) throws Exception {
        return AESGCMUtil.decrypt(content, key);
    }

    private String encode(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    private SecretKey decode(String encodedKey) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedBytes, "AES");
    }

}
