package com.example.springbootchatmessenger.message;

import java.util.List;

public interface MessageService {

    MessageEntityDto save(MessageEntityDto messageEntityDto);

    MessageEntityDto findById(Long id);

    List<MessageEntityDto> findAll(String sender, String receiver);
}
