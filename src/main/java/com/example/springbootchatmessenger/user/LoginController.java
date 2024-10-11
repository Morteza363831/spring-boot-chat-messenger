package com.example.springbootchatmessenger.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * open login page
 */

@RestController
@RequestMapping("/login")
public class LoginController {


    @GetMapping
    public String login() {
        return "redirect:http://localhost:8080/realms/Messenger/protocol/openid-connect/auth?response_type=code&client_id=chat-messenger&scope=openid%20profile%20roles&redirect_uri=http://localhost:8020/login/oauth2/code/keycloak";
    }

}
