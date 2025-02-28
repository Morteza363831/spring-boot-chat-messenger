package com.example.springbootchatmessenger.user;

import com.example.springbootchatmessenger.exceptions.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "User Management", description = "Operations related to user management")
@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private final ResourceLoader resourceLoader;
    private final UserService userService;

    public UserController(final ResourceLoader resourceLoader, final UserService userService) {
        this.resourceLoader = resourceLoader;
        this.userService = userService;
    }

    @Operation(summary = "Get Signup Page", description = "Returns the signup page HTML")
    @SecurityRequirement(name = "bearerToken")
    @ApiResponse(responseCode = "200", description = "Signup page returned successfully")
    @ApiResponse(responseCode = "404", description = "Signup page not found")
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

    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @SecurityRequirements() // Excludes security for this endpoint
    @ApiResponse(responseCode = "201", description = "User successfully created")
    @ApiResponse(responseCode = "409", description = "Duplicate user violation")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PostMapping("/")
    public ResponseEntity<?> createUser(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User creation data",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "User Example",
                                    value = "{\"username\": \"john_doe\", \"password\": \"securePassword\"}"
                            )
                    )
            )
            final UserCreateDto user) {

        Optional<UserDto> userEntityDtoOptional = Optional.ofNullable(userService.save(user));
        if (userEntityDtoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userEntityDtoOptional.get());
        } else {
            throw new InternalServerException("User could not be created.");
        }
    }

    @Operation(summary = "Update user details", description = "Updates the details of an existing user")
    @SecurityRequirement(name = "bearerToken")
    @ApiResponse(responseCode = "200", description = "Successful")
    @ApiResponse(responseCode = "400", description = "Validation failed or Entity not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(
            @PathVariable String username,
            @RequestBody UserUpdateDto user) {
        userService.updateUser(username, user);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @SecurityRequirement(name = "bearerToken")
    @ApiResponse(responseCode = "200", description = "Successful")
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by username", description = "Finds a user by their username")
    @SecurityRequirement(name = "bearerToken")
    @ApiResponse(responseCode = "200", description = "Successful")
    @ApiResponse(responseCode = "400", description = "Validation failed or Entity not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable final String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @Operation(summary = "Delete a user", description = "Deletes a user from the system")
    @SecurityRequirement(name = "bearerToken")
    @ApiResponse(responseCode = "200", description = "Successful")
    @ApiResponse(responseCode = "400", description = "Validation failed or Entity not found")
    @ApiResponse(responseCode = "500", description = "User creation failed")
    @DeleteMapping("/")
    public ResponseEntity<?> deleteUser(@RequestBody final UserDeleteDto userDeleteDto) {
        userService.deleteUserById(userDeleteDto);
        return ResponseEntity.ok().build();
    }
}
