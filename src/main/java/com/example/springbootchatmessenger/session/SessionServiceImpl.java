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

    @Override
    public SessionEntityDto findByUserIds(final String firstUsername,final String secondUsername) {
        final UserEntityDto firstUser = userService.getUserByUsername(firstUsername);
        final UserEntityDto secondUser = userService.getUserByUsername(secondUsername);
        final List<SessionEntity> sessionEntityList = sessionRepository.findSessionEntityByUserEntities(List.of(firstUser.getId(),secondUser.getId()), 2L);
        final List<SessionEntityDto> sessionEntityDtoList = new ArrayList<>();

        if (sessionEntityList.size() > 0) {
            sessionEntityList.forEach(sessionEntity -> {
                sessionEntityDtoList.add(SessionMapper.INSTANCE.sessionEntityToSessionDto(sessionEntity));
            });
        }
        else {
            final SessionEntityDto sessionEntityDto = getSessionEntityDto(firstUser, secondUser);
            sessionEntityDtoList.add(save(sessionEntityDto));
        }
        return sessionEntityDtoList.get(0);
    }

    private static SessionEntityDto getSessionEntityDto(final UserEntityDto firstUser, final UserEntityDto secondUser) {
        final SessionEntityDto sessionEntityDto = new SessionEntityDto();
        final Set<UserEntity> userEntities = new HashSet<>();
        userEntities.addAll(List.of(UserMapper.INSTANCE.userDtoToUserEntity(firstUser), UserMapper.INSTANCE.userDtoToUserEntity(secondUser)));
        sessionEntityDto.setUserEntities(userEntities);
        return sessionEntityDto;
    }
}
