package com.example.messenger.session.service;

import com.example.messenger.session.model.SessionCreateDto;
import com.example.messenger.session.model.SessionDeleteDto;
import com.example.messenger.session.model.SessionDto;

import java.util.UUID;

public interface SessionService {

    SessionDto getSession(String user1, String user2);
    SessionDto getSession(UUID sessionId);

    SessionDto save(SessionCreateDto sessionCreateDto);
    void delete(SessionDeleteDto sessionDeleteDto);
}
