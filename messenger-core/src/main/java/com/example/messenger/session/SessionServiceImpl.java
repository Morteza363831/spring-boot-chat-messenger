package com.example.messenger.session;


import com.example.messenger.exceptions.CustomEntityNotFoundException;
import com.example.messenger.exceptions.CustomValidationException;
import com.example.messenger.exceptions.EntityAlreadyExistException;
import com.example.messenger.kafka.CommandProducer;
import com.example.messenger.message.MessageEntity;
import com.example.messenger.message.MessageMapper;
import com.example.messenger.message.MessageService;
import com.example.messenger.user.UserDto;
import com.example.messenger.user.UserMapper;
import com.example.messenger.user.UserService;
import com.example.messenger.user.UserUpdateDto;
import com.example.messengerutilities.utility.DataTypes;
import com.example.messengerutilities.utility.RequestTypes;
import com.example.messengerutilities.utility.TopicNames;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This class will process for session requests like create, get, delete and etc
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    // inject default beans
    private final Validator validator;


    // inject other
    private final CommandProducer commandProducer;
    private final SessionRepository sessionRepository;
    private final SessionQueryClient sessionQueryClient;
    private final MessageService messageService;
    private final UserService userService;


    @Transactional(rollbackOn = Exception.class)
    @Override
    public SessionDto save(final SessionCreateDto sessionCreateDto) {

        validate(sessionCreateDto);

        final UserDto firstUser = userService.getUserByUsername(sessionCreateDto.getUser1());
        final UserDto secondUser = userService.getUserByUsername(sessionCreateDto.getUser2());

        sessionQueryClient.getSession(firstUser.getId(), secondUser.getId())
                .ifPresentOrElse(foundedSession -> {
                    throw new EntityAlreadyExistException("session exists for users : " + sessionCreateDto.getUser1() + " and " + sessionCreateDto.getUser2());
                }, () -> {
                    // Create and save session
                    SessionEntity sessionEntity = SessionEntity.createSession(
                            UserMapper.INSTANCE.toEntity(firstUser),
                            UserMapper.INSTANCE.toEntity(secondUser)
                    );
                    commandProducer.sendWriteEvent(TopicNames.SESSION_WRITE_TOPIC, RequestTypes.SAVE, DataTypes.SESSION, sessionEntity);
                    messageService.saveMessageEntity(sessionEntity.getId());
                    // produce event on topic (command request)
                    //commandProducer.sendWriteEvent(TopicNames.SESSION_WRITE_TOPIC, RequestTypes.UPDATE, DataTypes.SESSION, sessionEntity);
                    // 🔹 Securely update user authorities inside the same transaction
                    updateUserAuthorities(firstUser, sessionEntity.getId());
                    updateUserAuthorities(secondUser, sessionEntity.getId());
                });

        // Deprecated
        /*// Check if session already exists
        sessionRepository.findExistingSession(firstUser.getId(), secondUser.getId())
                .ifPresent(existing -> {
                    throw new EntityAlreadyExistException(existing.getId().toString());
                });

        // Create and save session
        SessionEntity sessionEntity = SessionEntity.createSession(
                UserMapper.INSTANCE.toEntity(firstUser),
                UserMapper.INSTANCE.toEntity(secondUser)
        );

        // produce event on topic (command request)
        commandProducer.sendWriteEvent(TopicNames.SESSION_WRITE_TOPIC, RequestTypes.SAVE, DataTypes.SESSION, sessionEntity);
        sessionEntity = sessionRepository.save(sessionEntity); // Deprecated


        // Create an empty message entity
        sessionEntity.setMessageEntity(MessageMapper.INSTANCE.toEntity(
                messageService.saveMessageEntity(sessionEntity.getId()))
        );

        // produce event on topic (command request)
        commandProducer.sendWriteEvent(TopicNames.SESSION_WRITE_TOPIC, RequestTypes.SAVE, DataTypes.SESSION, sessionEntity);
        sessionEntity = sessionRepository.save(sessionEntity); // Deprecated

        // 🔹 Securely update user authorities inside the same transaction
        updateUserAuthorities(firstUser, sessionEntity.getId());
        updateUserAuthorities(secondUser, sessionEntity.getId());*/

        return sessionQueryClient.getSession(firstUser.getId(), secondUser.getId())
                .map(SessionMapper.INSTANCE::sessionEntityToSessionDto)
                .orElseThrow(() -> new EntityNotFoundException("session not found for users : " + firstUser.getId() + " and " + secondUser.getId()));
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public SessionDto findByUserIds(final String user1 , final String user2) {

        // find users by their usernames
        final UserDto firstUser = userService.getUserByUsername(user1);
        final UserDto secondUser = userService.getUserByUsername(user2);

        return sessionQueryClient.getSession(firstUser.getId(), secondUser.getId())
                .map(SessionMapper.INSTANCE::sessionEntityToSessionDto)
                .orElseGet(() -> save(new SessionCreateDto(user1, user2)));
        // Deprecated
        /*return sessionRepository.findExistingSession(firstUser.getId(), secondUser.getId())
                .map(SessionMapper.INSTANCE::sessionEntityToSessionDto)
                .orElseGet(() -> save(new SessionCreateDto(user1, user2)));*/
    }

    // For now this functionality is not accessible
    @Override
    public SessionDto findBySessionId(final UUID id) {
        /*return sessionRepository
                .findById(id)
                .map(SessionMapper.INSTANCE::sessionEntityToSessionDto)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));*/
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void deleteSession(final SessionDeleteDto sessionDeleteDto) {

        validate(sessionDeleteDto);

        final UserDto firstUser = userService.getUserByUsername(sessionDeleteDto.getUser1());
        final UserDto secondUser = userService.getUserByUsername(sessionDeleteDto.getUser2());

        sessionQueryClient.getSession(firstUser.getId(), secondUser.getId())
                        .ifPresentOrElse(foundedSession -> {
                            // produce event on topic (command request)
                            commandProducer.sendWriteEvent(TopicNames.SESSION_WRITE_TOPIC, RequestTypes.DELETE, DataTypes.SESSION, foundedSession);
                        }, () -> {
                            throw new CustomEntityNotFoundException("Session for user : " + sessionDeleteDto.getUser1() + " not found");
                        });

        // Deprecated
        /*sessionRepository
                .findById(sessionDeleteDto.getId())
                .ifPresentOrElse(
                        session -> {
                            // produce event on topic (command request)
                            commandProducer.sendWriteEvent(TopicNames.SESSION_WRITE_TOPIC, RequestTypes.DELETE, DataTypes.SESSION, session);
                            sessionRepository.delete(session); // Deprecated
                        }, () -> {
                            throw new EntityNotFoundException(sessionDeleteDto.getId().toString());
                        });*/
    }

    private void updateUserAuthorities(final UserDto user, final UUID sessionId) {
        final UserUpdateDto userUpdateDto = UserMapper.INSTANCE.userDtoToUpdateDto(user);

        // Encrypt and append new session ID
        final String updatedAuthorities = user.getAuthorities() + "," + sessionId;

        userUpdateDto.setAuthorities(updatedAuthorities);
        // produce event on topic (command request)
        userService.updateUser(user.getUsername(), userUpdateDto);
    }


    private void validate(final Object dto) {
        final List<String> violations = new ArrayList<>();
        validator.validate(dto).forEach(field -> violations.add(field.getMessage()));
        if (!violations.isEmpty()) {
            throw new CustomValidationException(violations);
        }
    }
}
