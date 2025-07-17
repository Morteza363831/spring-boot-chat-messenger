package com.example.messengerquery.controller;

import com.example.messengerquery.Service.UserQueryService;
import com.example.messengerquery.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/query/user")
@RequiredArgsConstructor
public class UserController {

    private final UserQueryService userService;


    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.findByUsername(username);
    }
}
