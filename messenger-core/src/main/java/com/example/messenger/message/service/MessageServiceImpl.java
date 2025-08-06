package com.example.messenger.message;


import com.example.messenger.exceptions.*;
import com.example.messenger.kafka.CommandProducer;
import com.example.messenger.message.model.*;
import com.example.messenger.message.query.MessageQueryClient;
import com.example.messenger.utility.AESGCMUtil;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengerutilities.utility.TopicNames;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * This class will process Message requests like create Message entity , send message , get messages , delete messages and etc
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    // inject default beans
    private final Validator validator;
    private final ObjectMapper mapper;

    // inject others
    private final MessageRepository messageRepository;
    private final CommandProducer commandProducer;
    private final MessageQueryClient messageQueryClient;

    // public methods

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void saveMessage(final UUID sessionId,final MessageContent messageContent) {

        validate(messageContent);
        // create new message entity if there is no tuple in database
        addMessageContentAndUpdateMessageEntity(sessionId, messageContent);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void saveMessageEntity(final UUID sessionId) {
        createMessageEntity(sessionId);
    }

    // For now this functionality is not accessible
    @Override
    public MessageContent findById(Long id) {
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public List<MessageContent> findAll(final UUID sessionId) {

        Optional<MessageEntity> messageEntityOptional = messageQueryClient.getMessageBySessionId(sessionId);

        MessageDto messageDto = null;
        if (messageEntityOptional.isPresent()) {
            messageDto = MessageMapper.INSTANCE.toDto(messageEntityOptional.get());
        }

        if (messageEntityOptional.isEmpty()) {
            saveMessageEntity(sessionId);
        }


        // Deprecated
        /*// find message entity
        Optional<MessageEntity> messageEntityOptional = messageRepository.findBySessionId(sessionId);

        MessageDto messageDto = null;
        if (messageEntityOptional.isPresent()) {
            messageDto = MessageMapper.INSTANCE.toDto(messageEntityOptional.get());
        }
        // if message entity is null create one
        if (messageEntityOptional.isEmpty()) {
            messageDto = saveMessageEntity(sessionId);
        }*/

        // then read message contents
        MessageContentList messageContentList;
        try {
            messageContentList = mapper.readValue(decryption(messageDto.getContent(), decode(messageDto.getEncryptedAesKey())), MessageContentList.class);
        } catch (JsonProcessingException e) {
            throw new JsonProcessingCustomException();
        }
        return messageContentList.getMessageContentList();
    }

    @Override
    public MessageEntity makeMessageEntityObject(UUID sessionId) {
        final MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSessionId(sessionId);
        try {
            final SecretKey secretKey = AESGCMUtil.generateAESKey();
            messageEntity.setContent(encryption(mapper.writeValueAsString(new MessageContentList()), secretKey));
            messageEntity.setEncryptedAesKey(encode(secretKey));
            return messageEntity;
        } catch (JsonProcessingException e) {
            throw new JsonProcessingCustomException();
        } catch (Exception e) {
            throw new KeyGenerationFailureException();
        }
    }

    // private methods


    private void createMessageEntity(final UUID sessionId) {

        messageQueryClient.getMessageBySessionId(sessionId)
                .ifPresentOrElse(foundedMessageEntity -> {
                    throw new EntityAlreadyExistException(foundedMessageEntity.getId().toString());
                }, () -> {
                    MessageEntity messageEntity = makeMessageEntityObject(sessionId);
                    commandProducer.sendWriteEvent(TopicNames.MESSAGE_WRITE_TOPIC, RequestTypes.SAVE, DataTypes.MESSAGE, messageEntity);
                });

        // Deprecated
        /*// find message entity by session
        Optional<MessageEntity> messageEntityOptional = messageRepository.findBySessionId(sessionId);
        // if message entity is null create one
        if (messageEntityOptional.isPresent()) {
            // persist message entity
            throw new EntityAlreadyExistException(messageEntityOptional.get().getId().toString());
        }

        // persist message entity
        final MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSessionId(sessionId);
        try {
            final SecretKey secretKey = AESGCMUtil.generateAESKey();
            messageEntity.setContent(encryption(mapper.writeValueAsString(new MessageContentList()), secretKey));
            messageEntity.setEncryptedAESKey(encode(secretKey));
        } catch (JsonProcessingException e) {
            throw new JsonProcessingCustomException();
        } catch (Exception e) {
            throw new KeyGenerationFailureException();
        }*/
/*
        return messageQueryClient.getMessageBySessionId(sessionId)
                .map(MessageMapper.INSTANCE::toDto)
                .orElseThrow(() -> new CustomEntityNotFoundException("Message not found"));*/
    }

    private void addMessageContentAndUpdateMessageEntity(UUID sessionId, MessageContent messageContent) {

        messageQueryClient.getMessageBySessionId(sessionId)
                .ifPresentOrElse(foundedMessageEntity -> {
                    try {
                        // Decrypt the existing message content
                        final SecretKey secretKey = decode(foundedMessageEntity.getEncryptedAesKey());
                        final String decryptedContent = decryption(foundedMessageEntity.getContent(), secretKey);

                        // Parse JSON
                        final MessageContentList messageContentList = checkNull(mapper.readValue(decryptedContent, MessageContentList.class));
                        messageContentList.getMessageContentList().add(messageContent);

                        // Encrypt updated content
                        final String encryptedContent = encryption(mapper.writeValueAsString(messageContentList), secretKey);
                        foundedMessageEntity.setContent(encryptedContent);
                        commandProducer.sendWriteEvent(TopicNames.MESSAGE_WRITE_TOPIC, RequestTypes.UPDATE, DataTypes.MESSAGE, foundedMessageEntity);
                    } catch (JsonProcessingException e) {
                        throw new JsonProcessingCustomException();
                    }

                }, () -> {
                    throw new CustomEntityNotFoundException("Message not found for session : " + sessionId);
                });

        // Deprecated
        /*Optional<MessageEntity> messageEntityOptional = messageRepository.findBySessionId(sessionId);
        if (messageEntityOptional.isEmpty()) {
            throw new CustomEntityNotFoundException("Message entity not found for session: " + sessionId);
        }

        final MessageEntity messageEntity = messageEntityOptional.get();
        try {
            // Decrypt the existing message content
            final SecretKey secretKey = decode(messageEntity.getEncryptedAESKey());
            final String decryptedContent = decryption(messageEntity.getContent(), secretKey);

            // Parse JSON
            final MessageContentList messageContentList = checkNull(mapper.readValue(decryptedContent, MessageContentList.class));
            messageContentList.getMessageContentList().add(messageContent);

            // Encrypt updated content
            final String encryptedContent = encryption(mapper.writeValueAsString(messageContentList), secretKey);
            messageEntity.setContent(encryptedContent);

            messageRepository.save(messageEntity);
        } catch (JsonProcessingException e) {
            throw new JsonProcessingCustomException();
        }*/
    }

    private MessageContentList checkNull(final MessageContentList messageContentList) {
        // if the list is empty create one
        if (messageContentList.getMessageContentList() == null) {
            messageContentList.setMessageContentList(new ArrayList<>());
            return messageContentList;
        }
        return messageContentList;
    }

    private void validate(final Object dto) {
        final List<String> violations = new ArrayList<>();
        validator.validate(dto).forEach(field -> violations.add(field.getMessage()));
        if (!violations.isEmpty()) {
            throw new CustomValidationException(violations);
        }
    }


    private String encryption(final String content, final SecretKey secretKey) {
        try {
            return AESGCMUtil.encrypt(content, secretKey, AESGCMUtil.generateIV());
        } catch (Exception e) {
            throw new EncryptionFailureException(content);
        }
    }

    private String decryption(String content, SecretKey key) {
        try {
            return AESGCMUtil.decrypt(content, key);
        } catch (Exception e) {
            throw new DecryptionFailureException(content);
        }
    }

    private String encode(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    private SecretKey decode(String encodedKey) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedBytes, "AES");
    }

}
