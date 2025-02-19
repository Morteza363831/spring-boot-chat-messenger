package com.example.springbootchatmessenger.user;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/*
 * this class will open signup page and will add a user to database
 */

@RestController
@RequestMapping("/signup")
@Slf4j
public class SignupController {

    private final ResourceLoader resourceLoader;
    private final UserService userService;

    public SignupController(final ResourceLoader resourceLoader,
                            final UserService userService) {
        this.resourceLoader = resourceLoader;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Resource> signupPage() {
        Resource resource = resourceLoader.getResource("classpath:templates/signup.html");

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html")
                    .body(resource);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody final UserEntityDto user) {
        try {
            Optional<UserEntityDto> userEntityDto = Optional.ofNullable(userService.save(user));

            if (userEntityDto.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(userEntityDto.get());
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User could not be created.");
            }
        } catch (Exception e) {
            log.error("Error while creating user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
}
