package com.example.springbootchatmessenger.user;

import com.example.springbootchatmessenger.jwt.CustomAuthenticationManager;
import com.example.springbootchatmessenger.jwt.CustomUserDetailsService;
import com.example.springbootchatmessenger.jwt.JwtTokenUtil;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/*
 * open login page
 */

@RestController
@RequestMapping("/login")
public class LoginController {

    private final CustomAuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public LoginController(CustomAuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping
    public String login() {
        return "Welcome to Spring Boot Chat Messenger!";
    }

    @PostMapping("/token")
    public String token(@RequestParam String username, @RequestParam String password) {
        authenticate(username, password);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtTokenUtil.generateToken(userDetails);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
