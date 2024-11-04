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
    private final ModelMapper modelMapper;

    public SessionServiceImpl(SessionRepository sessionRepository,
                              ModelMapper modelMapper) {
        this.sessionRepository = sessionRepository;
        this.modelMapper = modelMapper;
    }



    @Override
    public SessionEntityDto save(String firstusername, String secondusername) {
        SessionEntityDto sessionEntityDto = new SessionEntityDto();
        sessionEntityDto.setUsername1(firstusername);
        sessionEntityDto.setUsername2(secondusername);
        SessionEntity sessionEntity = modelMapper.map(sessionEntityDto, SessionEntity.class);
        return modelMapper.map(sessionRepository.save(sessionEntity), SessionEntityDto.class);
    }

    @Override
    public SessionEntityDto findByUsernames(String firstUsername, String secondUsername) {
        List<SessionEntity> sessions = sessionRepository.findByUsernames(firstUsername, secondUsername);

        if (sessions.isEmpty()) {
            log.error("session not found for usernames : {} , {}", firstUsername, secondUsername);
            // Create a new session if none exists
            SessionEntityDto sessionEntityDto = save(firstUsername, secondUsername);
            log.info("session created with chatId : {}", sessionEntityDto.getId());
            return sessionEntityDto;
        } else {
            SessionEntityDto sessionEntityDto = modelMapper.map(sessions.get(0), SessionEntityDto.class);
            log.info("session founded");
            return sessionEntityDto;
        }
    }
}
