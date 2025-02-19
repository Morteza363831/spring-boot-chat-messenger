package com.example.springbootchatmessenger.user;

import com.example.springbootchatmessenger.jwt.CustomAuthenticationManager;
import com.example.springbootchatmessenger.jwt.CustomUserDetailsService;
import com.example.springbootchatmessenger.jwt.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/*
 * open login page
 */

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    private final CustomAuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ResourceLoader resourceLoader;

    public LoginController(CustomAuthenticationManager authenticationManager,
                           CustomUserDetailsService userDetailsService,
                           JwtTokenUtil jwtTokenUtil,
                           ResourceLoader resourceLoader) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping
    public ResponseEntity<Resource> loginPage() {
        Resource resource = resourceLoader.getResource("classpath:templates/login.html");
        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html")
                    .body(resource);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestParam final String username, @RequestParam final String password) {
        try {
            authenticate(username, password);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(token);
        }
        catch (Exception e) {
            log.error("Unexpected error occurred");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private void authenticate(final String username,final String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
