package com.example.springbootchatmessenger.session;


import com.example.springbootchatmessenger.exceptions.CustomValidationException;
import com.example.springbootchatmessenger.exceptions.EntityAlreadyExistException;
import com.example.springbootchatmessenger.message.MessageMapper;
import com.example.springbootchatmessenger.message.MessageService;
import com.example.springbootchatmessenger.user.UserDto;
import com.example.springbootchatmessenger.user.UserMapper;
import com.example.springbootchatmessenger.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 * this class will handle sessions
 * --> save will save session in the database for two users
 * --> findByUsernames will find a session fore these two users (sender and receiver or reverse)
 */

@Slf4j
@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final MessageService messageService;
    private final UserService userService;
    private final Validator validator;

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

        // Check is session existence
        sessionRepository.findExistingSession(firstUser.getId(), secondUser.getId())
                .ifPresent(existing -> {
                    throw new EntityAlreadyExistException(existing.getId().toString());
                });

        // Create and save session
        SessionEntity sessionEntity = SessionEntity.createSession(UserMapper.INSTANCE.toEntity(firstUser), UserMapper.INSTANCE.toEntity(secondUser));

        sessionEntity = sessionRepository.save(sessionEntity);

        sessionEntity.setMessageEntity(MessageMapper.INSTANCE.toEntity(messageService.saveMessageEntity(sessionEntity.getId())));

        return SessionMapper.INSTANCE.sessionEntityToSessionDto(sessionRepository.save(sessionEntity));
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public SessionDto findByUserIds(final String user1 , String user2) {


        // find users by their usernames
        final UserDto firstUser = userService.getUserByUsername(user1);
        final UserDto secondUser = userService.getUserByUsername(user2);

        return sessionRepository.findExistingSession(firstUser.getId(), secondUser.getId())
                .map(SessionMapper.INSTANCE::sessionEntityToSessionDto)
                .orElseGet(() -> save(new SessionCreateDto(user1, user2)));
    }

    @Override
    public SessionDto findBySessionId(final UUID id) {
        return sessionRepository
                .findById(id)
                .map(SessionMapper.INSTANCE::sessionEntityToSessionDto)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
    }

    @Override
    public void deleteSession(SessionDeleteDto sessionDeleteDto) {

        validate(sessionDeleteDto);

        final Optional<SessionEntity> sessionEntityOptional = sessionRepository.findById(sessionDeleteDto.getId());

        if (sessionEntityOptional.isEmpty()) {
            throw new EntityNotFoundException(sessionDeleteDto.getId().toString());
        }

        sessionRepository.delete(sessionEntityOptional.get());
    }

    private void validate(Object dto) {
        List<String> violations = new ArrayList<>();
        validator.validate(dto).forEach(field -> violations.add(field.getMessage()));
        if (!violations.isEmpty()) {
            throw new CustomValidationException(violations);
        }
    }
}
