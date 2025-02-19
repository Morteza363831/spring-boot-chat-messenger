package com.example.springbootchatmessenger.session;

public interface SessionService {

    SessionEntityDto save(final SessionEntityDto sessionEntityDto);

    SessionEntityDto findByUserIds(final String firstUsername,final String secondUsername);
}
