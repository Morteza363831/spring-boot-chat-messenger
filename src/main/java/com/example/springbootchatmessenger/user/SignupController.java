package com.example.springbootchatmessenger.user;

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

@Slf4j
@RestController
@RequestMapping("/signup")
public class SignupController {

    private final ResourceLoader resourceLoader;
    private final UserService userService;
    public SignupController(ResourceLoader resourceLoader,
                            UserService userService) {
        this.resourceLoader = resourceLoader;
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
    public ResponseEntity<?> createUser(@RequestBody UserEntityDto user) {
        Optional<UserEntityDto> userEntityDtoOptional = Optional.ofNullable(userService.save(user));
        if (userEntityDtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userEntityDtoOptional.get());
        }
        log.info("User creation failed");
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
