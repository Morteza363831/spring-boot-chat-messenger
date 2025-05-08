package com.example.messenger.session;


import com.example.messenger.exceptions.CustomValidationException;
import com.example.messenger.exceptions.EntityAlreadyExistException;
import com.example.messenger.message.MessageMapper;
import com.example.messenger.message.MessageService;
import com.example.messenger.user.UserDto;
import com.example.messenger.user.UserMapper;
import com.example.messenger.user.UserService;
import com.example.messenger.user.UserUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This class will process for session requests like create, get, delete and etc
 */

@Slf4j
@Service
public class SessionServiceImpl implements SessionService {

    // inject default beans
    private final Validator validator;


    // inject other
    private final SessionRepository sessionRepository;
    private final MessageService messageService;
    private final UserService userService;

    public SessionServiceImpl(final SessionRepository sessionRepository,
                              final UserService userService,
                              final MessageService messageService,
                              final Validator validator) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
        this.messageService = messageService;
        this.validator = validator;
    }



    @Transactional(rollbackOn = Exception.class)
    @Override
    public SessionDto save(final SessionCreateDto sessionCreateDto) {

        validate(sessionCreateDto);

        final UserDto firstUser = userService.getUserByUsername(sessionCreateDto.getUser1());
        final UserDto secondUser = userService.getUserByUsername(sessionCreateDto.getUser2());

        // Check if session already exists
        sessionRepository.findExistingSession(firstUser.getId(), secondUser.getId())
                .ifPresent(existing -> {
                    throw new EntityAlreadyExistException(existing.getId().toString());
                });

        // Create and save session
        SessionEntity sessionEntity = SessionEntity.createSession(
                UserMapper.INSTANCE.toEntity(firstUser),
                UserMapper.INSTANCE.toEntity(secondUser)
        );

        sessionEntity = sessionRepository.save(sessionEntity);

        // Create an empty message entity
        sessionEntity.setMessageEntity(MessageMapper.INSTANCE.toEntity(
                messageService.saveMessageEntity(sessionEntity.getId()))
        );

        sessionEntity = sessionRepository.save(sessionEntity);

        // 🔹 Securely update user authorities inside the same transaction
        updateUserAuthorities(firstUser, sessionEntity.getId());
        updateUserAuthorities(secondUser, sessionEntity.getId());

        return SessionMapper.INSTANCE.sessionEntityToSessionDto(sessionEntity);
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public SessionDto findByUserIds(final String user1 , final String user2) {


        // find users by their usernames
        final UserDto firstUser = userService.getUserByUsername(user1);
        final UserDto secondUser = userService.getUserByUsername(user2);

        return sessionRepository.findExistingSession(firstUser.getId(), secondUser.getId())
                .map(SessionMapper.INSTANCE::sessionEntityToSessionDto)
                .orElseGet(() -> save(new SessionCreateDto(user1, user2)));
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
        sessionRepository
                .findById(sessionDeleteDto.getId())
                .ifPresentOrElse(sessionRepository::delete, () -> {
                    throw new EntityNotFoundException(sessionDeleteDto.getId().toString());
                });

    }

    private void updateUserAuthorities(final UserDto user, final UUID sessionId) {
        final UserUpdateDto userUpdateDto = UserMapper.INSTANCE.userDtoToUpdateDto(user);

        // Encrypt and append new session ID
        final String updatedAuthorities = user.getAuthorities() + "," + sessionId;

        userUpdateDto.setAuthorities(updatedAuthorities);
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
