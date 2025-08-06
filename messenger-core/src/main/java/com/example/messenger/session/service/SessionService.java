package com.example.messenger.session;

import com.example.messenger.session.model.SessionCreateDto;
import com.example.messenger.session.model.SessionDeleteDto;
import com.example.messenger.session.model.SessionDto;

import java.util.UUID;

public interface SessionService {

    SessionDto save(final SessionCreateDto sessionCreateDto);

    SessionDto findByUserIds(final String user1, final String user2);

    SessionDto findBySessionId(final UUID sessionId);

    void deleteSession(final SessionDeleteDto sessionDeleteDto);
}
