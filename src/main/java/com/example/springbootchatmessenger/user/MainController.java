package com.example.springbootchatmessenger.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    List<String> users = new ArrayList<>();


    public MainController(ResourceLoader resourceLoader,
                          UserService userService) {
        this.resourceLoader = resourceLoader;
        this.userService = userService;
    }

    /*
     * this endpoint will open the main page for the user which have {username}
     */
    @GetMapping("/{username}") // Adjusted to include username as a path variable
    public ResponseEntity<Resource> mainPage(@PathVariable String username, Authentication authentication) {
        OidcUser user = (OidcUser) authentication.getPrincipal();
        String authenticatedUsername = user.getPreferredUsername();

        if (!authenticatedUsername.equals(username)) {
            log.error("Unauthorized access attempt by user {} to page of user {}", authenticatedUsername, username);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String RESOURCE = "classpath:templates/main.html";
        Resource resource = resourceLoader.getResource(RESOURCE);

        // Check if the resource exists
        if (resource.exists()) {
            if(!users.contains(username)) {
                users.add(username);
            }
            // Return modified HTML content with appropriate headers
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "text/html")
                    .body(resource);
        }

        return ResponseEntity.notFound().build();
    }


    /*
     * will find all users
     */
    @GetMapping("/")
    public ResponseEntity<List<String>> getUsers() {

        List<UserEntityDto> userEntityDtos = userService.getAllUsers();
        if (!userEntityDtos.isEmpty()) {
            for (UserEntityDto userEntityDto : userEntityDtos) {
                if (!users.contains(userEntityDto.getUsername())) {
                    users.add(userEntityDto.getUsername());
                }
            }
            log.info("Users found: {}", users);
            return ResponseEntity.ok(users);
        }
        log.info("No users found");
        return ResponseEntity.notFound().build();
    }
}