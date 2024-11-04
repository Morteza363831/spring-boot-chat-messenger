package com.example.springbootchatmessenger.session;

public interface SessionService {

    SessionEntityDto save(String firstusername, String secondusername);

    SessionEntityDto findByUsernames(String firstUsername, String secondUsername);
}
