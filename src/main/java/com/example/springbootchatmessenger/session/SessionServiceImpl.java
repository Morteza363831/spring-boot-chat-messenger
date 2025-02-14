package com.example.springbootchatmessenger.session;


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

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }



    @Override
    public SessionEntityDto save(SessionEntityDto sessionEntityDto) {
        SessionEntity sessionEntity = SessionMapper.INSTANCE.sessionDtoToSessionEntity(sessionEntityDto);
        return SessionMapper.INSTANCE.sessionEntityToSessionDto(sessionRepository.save(sessionEntity));
    }

    @Override
    public SessionEntityDto findByUserIds(Long firstUserId, Long secondUserId) {
        return SessionMapper.INSTANCE.sessionEntityToSessionDto(sessionRepository.findSessionEntityByUserEntities(List.of(1L,2L), 2L).get(0));
    }

    private boolean checkNull(SessionEntity sessionEntity) {
        return sessionEntity == null;
    }
}
