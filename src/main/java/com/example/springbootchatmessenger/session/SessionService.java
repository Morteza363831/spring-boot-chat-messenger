package com.example.springbootchatmessenger.session;

import java.util.List;

public interface SessionService {

    SessionEntityDto save(SessionEntityDto sessionEntityDto);

    SessionEntityDto findByUserIds(Long firstUserId, Long secondUserId);
}
