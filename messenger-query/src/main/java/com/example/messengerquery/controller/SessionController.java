package com.example.messengerquery.controller;

import com.example.messengerquery.Service.SessionQueryService;
import com.example.messengerquery.model.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/query/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionQueryService sessionService;


    @GetMapping
    public Session getSession(@RequestParam UUID user1, @RequestParam UUID user2) {
        return sessionService.findByUser1AndUser2(user1, user2);
    }


}
