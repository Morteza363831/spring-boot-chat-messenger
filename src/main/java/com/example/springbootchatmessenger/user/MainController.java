package com.example.springbootchatmessenger.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/*
 * open main page and find all users
 */

@RestController
@RequestMapping("/main")
@Slf4j
public class MainController {

    private final ResourceLoader resourceLoader;
    private final UserService userService;
    private final List<String> users = new ArrayList<>();

    public MainController(final ResourceLoader resourceLoader,
                          final UserService userService) {
        this.resourceLoader = resourceLoader;
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<Resource> mainPage(@PathVariable String username) {
        Resource resource = resourceLoader.getResource("classpath:templates/main.html");
        if (resource.exists()) {
            if (!users.contains(username)) {
                users.add(username);
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html")
                    .body(resource);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/")
    public ResponseEntity<?> getUsers() {
        final List<UserEntityDto> userEntityDtos = userService.getAllUsers();
        final List<String> userList = new ArrayList<>();
        userEntityDtos.forEach(userEntityDto -> userList.add(userEntityDto.getUsername()));
        if (userEntityDtos != null) {
            return ResponseEntity.ok(userList);
        }
        log.info("No users found");
        return ResponseEntity.notFound().build();
    }
}