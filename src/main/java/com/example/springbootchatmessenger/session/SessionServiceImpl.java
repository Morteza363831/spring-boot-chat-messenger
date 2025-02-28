package com.example.springbootchatmessenger.session;


import com.example.springbootchatmessenger.message.MessageService;
import com.example.springbootchatmessenger.user.UserEntity;
import com.example.springbootchatmessenger.user.UserEntityDto;
import com.example.springbootchatmessenger.user.UserMapper;
import com.example.springbootchatmessenger.user.UserService;
import jakarta.transaction.Transactional;
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

    public SessionServiceImpl(final SessionRepository sessionRepository,
                              final UserService userService,
                              final MessageService messageService) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
        this.messageService = messageService;
    }



    @Transactional(rollbackOn = Exception.class)
    @Override
    public SessionEntityDto save(final SessionEntityDto sessionEntityDto) {
        SessionEntity sessionEntity = SessionMapper.INSTANCE.sessionDtoToSessionEntity(sessionEntityDto);
        messageService.saveMessageEntity(SessionMapper.INSTANCE.sessionEntityToSessionDto(sessionEntity));
        return SessionMapper.INSTANCE.sessionEntityToSessionDto(sessionRepository.save(sessionEntity));
    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public SessionDto findByUserIds(final SessionFindDto sessionFindDto) {

        validate(sessionFindDto);

        // find users by their usernames
        final UserDto firstUser = userService.getUserByUsername(sessionFindDto.getUser1());
        final UserDto secondUser = userService.getUserByUsername(sessionFindDto.getUser2());

        return sessionRepository.findExistingSession(firstUser.getId(), secondUser.getId())
                .map(SessionMapper.INSTANCE::sessionEntityToSessionDto)
                .orElseGet(() -> save(SessionMapper.INSTANCE.findDtoToCreateDto(sessionFindDto)));
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
