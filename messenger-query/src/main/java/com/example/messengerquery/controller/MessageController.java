package com.example.messengerquery.controller;

import com.example.messengerquery.Service.MessageQueryService;
import com.example.messengerquery.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/query/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageQueryService messageService;


    @GetMapping("/{id}")
    public Message getMessageBySessionId(@PathVariable("id") UUID id) {
        return messageService.findBySessionId(id);
    }
}
