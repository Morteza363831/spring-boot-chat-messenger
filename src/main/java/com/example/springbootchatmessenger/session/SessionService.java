package com.example.springbootchatmessenger.session;

import java.util.UUID;

public interface SessionService {

    SessionDto save(final SessionCreateDto sessionCreateDto);

    SessionDto findByUserIds(final String user1, final String user2);

    SessionDto findBySessionId(final UUID sessionId);

    void deleteSession(final SessionDeleteDto sessionDeleteDto);
}
