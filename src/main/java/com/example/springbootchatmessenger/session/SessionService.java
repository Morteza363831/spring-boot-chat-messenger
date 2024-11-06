package com.example.springbootchatmessenger.session;

public interface SessionService {

    SessionEntityDto save(SessionEntityDto sessionEntityDto);

    SessionEntityDto findByUsernames(String firstUsername, String secondUsername);
}
