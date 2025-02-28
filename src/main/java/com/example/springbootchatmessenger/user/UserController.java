package com.example.springbootchatmessenger.user;


import com.example.springbootchatmessenger.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/*
 * this class will open signup page and will add a user to database
 */

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private final ResourceLoader resourceLoader;
    private final UserService userService;

    public UserController(final ResourceLoader resourceLoader,
                          final UserService userService) {
        this.resourceLoader = resourceLoader;
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<Resource> signupPage() {
        Resource resource = resourceLoader.getResource("classpath:templates/signup.html");

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html")
                    .body(resource);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    // User management

    @PostMapping("/")
    public ResponseEntity<?> createUser(@RequestBody final UserCreateDto user) {

        Optional<UserDto> userEntityDtoOptional = Optional.ofNullable(userService.save(user));
        if (userEntityDtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userEntityDtoOptional.get());
        } else {
            throw new InternalServerException("User could not be created.");
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody final UserUpdateDto user) {
        userService.updateUser(username, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {

        final List<UserDto> userDtos = userService.getAllUsers();
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable final String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteUser(@RequestBody final UserDeleteDto userDeleteDto) {
        userService.deleteUserById(userDeleteDto);
        return ResponseEntity.ok().build();
    }

}
