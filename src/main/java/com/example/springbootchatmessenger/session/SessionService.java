package com.example.springbootchatmessenger.session;

import java.util.UUID;

public interface SessionService {

    SessionEntityDto save(final SessionEntityDto sessionEntityDto);

    SessionDto findBySessionId(final UUID sessionId);

    void deleteSession(final SessionDeleteDto sessionDeleteDto);
}
