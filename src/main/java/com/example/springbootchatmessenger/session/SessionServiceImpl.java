package com.example.springbootchatmessenger.session;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public SessionEntityDto findByUsernames(String username1, String username2) {

        SessionEntity sessionEntity = sessionRepository.findByUsernames(username1, username2);
        SessionEntityDto sessionEntityDto;

        if (checkNull(sessionEntity)) {
            log.error("session not found for usernames : {} , {}", username1, username2);
            // Create a new session if none exists
            sessionEntityDto = addNewSession(username1, username2);
            log.info("session created with chatId : {}", sessionEntityDto.getId());
            return sessionEntityDto;
        } else {
            sessionEntityDto = SessionMapper.INSTANCE.sessionEntityToSessionDto(sessionEntity);
            log.info("session founded");
            return sessionEntityDto;
        }
    }

    private boolean checkNull(SessionEntity sessionEntity) {
        return sessionEntity == null;
    }

    private SessionEntityDto addNewSession(String username1, String username2) {

        return save(new SessionEntityDto(null, username1, username2));
    }
}
