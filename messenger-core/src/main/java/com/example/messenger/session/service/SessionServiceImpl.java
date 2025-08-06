package com.example.messenger.session.service;


import com.example.messenger.exceptions.CustomEntityNotFoundException;
import com.example.messenger.exceptions.CustomValidationException;
import com.example.messenger.exceptions.EntityAlreadyExistException;
import com.example.messenger.kafka.CommandProducer;
import com.example.messenger.message.service.MessageService;
import com.example.messenger.session.model.*;
import com.example.messenger.session.query.SessionQueryClient;
import com.example.messenger.user.model.UserEntity;
import com.example.messenger.user.service.UserService;
import com.example.messenger.utility.AuthorityUpdateType;
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
 * SessionServiceImpl will manage session logics like (save , update , delete)
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    // tools
    private final Validator validator;
    private final SessionMapper sessionMapper;

    // services
    private final SessionQueryClient sessionQueryClient;
    private final CommandProducer commandProducer;

    private final UserService userService;
    private final MessageService messageService;


    @Transactional(rollbackOn = Exception.class)
    @Override
    public SessionDto getSession(String user1 , String user2) {

        // find users by their usernames
        final UserEntity firstUser = userService.getUserEntity(user1);
        final UserEntity secondUser = userService.getUserEntity(user2);

        return sessionQueryClient.getSession(firstUser.getId(), secondUser.getId())
                .map(sessionMapper::sessionEntityToSessionDto)
                .orElseGet(() -> save(new SessionCreateDto(user1, user2)));
    }

    // Not implemented
    @Override
    public SessionDto getSession(UUID id) {
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public SessionDto save(SessionCreateDto sessionCreateDto) {

        validate(sessionCreateDto);

        // find users
        final UserEntity firstUser = userService.getUserEntity(sessionCreateDto.getUser1());
        final UserEntity secondUser = userService.getUserEntity(sessionCreateDto.getUser2());

        sessionQueryClient.getSession(firstUser.getId(), secondUser.getId())
                .ifPresentOrElse(foundedSession -> {
                    throw new EntityAlreadyExistException(sessionCreateDto.getUser1() + " and " + sessionCreateDto.getUser2());
                }, () -> {
                    // Create and save session
                    SessionEntity sessionEntity = SessionEntity.createSession(firstUser, secondUser);

                    commandProducer.sendWriteEvent(TopicNames.SESSION_WRITE_TOPIC, RequestTypes.SAVE, DataTypes.SESSION, sessionEntity);
                    messageService.saveEntity(sessionEntity.getId());
                    // produce event on topic (command request)
                    // Securely update user authorities inside the same transaction
                    userService.updateUserAuthorities(firstUser, sessionEntity.getId().toString(), AuthorityUpdateType.UPDATE);
                    userService.updateUserAuthorities(secondUser, sessionEntity.getId().toString(), AuthorityUpdateType.UPDATE);
                });

        return sessionQueryClient.getSession(firstUser.getId(), secondUser.getId())
                .map(sessionMapper::sessionEntityToSessionDto)
                .orElseThrow(() -> new EntityNotFoundException(firstUser.getId() + " and " + secondUser.getId()));
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void delete(final SessionDeleteDto sessionDeleteDto) {

        validate(sessionDeleteDto);

        final UserEntity firstUser = userService.getUserEntity(sessionDeleteDto.getUser1());
        final UserEntity secondUser = userService.getUserEntity(sessionDeleteDto.getUser2());

        sessionQueryClient.getSession(firstUser.getId(), secondUser.getId())
                        .ifPresentOrElse(foundedSession -> {
                            // produce event on topic (command request)
                            commandProducer.sendWriteEvent(TopicNames.SESSION_WRITE_TOPIC, RequestTypes.DELETE, DataTypes.SESSION, foundedSession);
                            // update user authorities (remove session)
                            userService.updateUserAuthorities(firstUser, foundedSession.getId().toString(), AuthorityUpdateType.DELETE);
                            userService.updateUserAuthorities(secondUser, foundedSession.getId().toString(), AuthorityUpdateType.DELETE);
                        }, () -> {
                            throw new CustomEntityNotFoundException("Session for user : " + sessionDeleteDto.getUser1() + " not found");
                        });

    }


    // utils

    private void validate(Object dto) {
        final List<String> violations = new ArrayList<>();
        validator.validate(dto).forEach(field -> violations.add(field.getMessage()));
        if (!violations.isEmpty()) {
            throw new CustomValidationException(violations);
        }
    }
}
