package com.example.springbootchatmessenger.user;

import com.example.springbootchatmessenger.keycloak.KeycloakService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/*
 * this class will open signup page and will add a user to database
 */

@Slf4j
@RestController
@RequestMapping("/signup")
public class SignupController {

    private final ResourceLoader resourceLoader;
    private final KeycloakService keycloakService;
    private final UserService userService;
    public SignupController(ResourceLoader resourceLoader,
                            KeycloakService keycloakService,
                            UserService userService) {
        this.resourceLoader = resourceLoader;
        this.keycloakService = keycloakService;
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<Resource> signUp() {

        String RESOURCE = "classpath:templates/signUp.html";
        Resource resource = resourceLoader.getResource(RESOURCE);
        //  check the resource is correct or no
        if (resource.exists()) {
            // return response object
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE,"text/html")
                    .body(resource);
        }
        return ResponseEntity.notFound().build();
    }


    /*
     * this endpoint will create new user
     */
    @PostMapping()
    public ResponseEntity<String> createUser(@RequestBody UserEntityDto user, HttpServletResponse response) throws IOException {
        keycloakService.registerUser(user);
        response.sendRedirect("/login");
        if (response.isCommitted()) {
            userService.save(user);
            return ResponseEntity.status(302).build();
        }
        log.error("sign up failed");
        return ResponseEntity.status(409).build();
    }
}
