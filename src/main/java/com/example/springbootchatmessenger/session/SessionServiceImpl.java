package com.example.springbootchatmessenger.session;


import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
        List<SessionEntity> sessions = sessionRepository.findByUsernames(username1, username2);

        if (sessions.isEmpty()) {
            log.error("session not found for usernames : {} , {}", username1, username2);
            // Create a new session if none exists
            SessionEntityDto sessionEntityDto = new SessionEntityDto();
            sessionEntityDto.setUsername1(username1);
            sessionEntityDto.setUsername2(username2);
            sessionEntityDto = save(sessionEntityDto);
            log.info("session created with chatId : {}", sessionEntityDto.getId());
            return sessionEntityDto;
        } else {
            SessionEntityDto sessionEntityDto = SessionMapper.INSTANCE.sessionEntityToSessionDto(sessions.get(0));
            log.info("session founded");
            return sessionEntityDto;
        }
    }
}
