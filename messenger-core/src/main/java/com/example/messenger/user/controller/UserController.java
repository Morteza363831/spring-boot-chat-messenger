package com.example.messenger.user.controller;

import com.example.messenger.exceptions.*;
import com.example.messenger.structure.ResponseResult;
import com.example.messenger.user.model.UserCreateDto;
import com.example.messenger.user.model.UserDeleteDto;
import com.example.messenger.user.model.UserDto;
import com.example.messenger.user.model.UserUpdateDto;
import com.example.messenger.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * User controller will handle user requests .
 * Create user endpoint does not need any authorize .
 * Other endpoints need authorize before starting a process (authorize with username or ROLE_ADMIN)
 */

@Tag(name = "User Management", description = "Operations related to user management")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    // services
    private final UserService userService;


    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @ApiResponse(responseCode = "201", description = "User successfully created")
    @ApiResponse(responseCode = "409", description = "Duplicate user violation")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @SecurityRequirements() // Excludes security for this endpoint
    @PostMapping("/register")
    public ResponseEntity<ResponseResult<UserDto>> registerUser(@RequestBody UserCreateDto user) {

        final Optional<UserDto> userEntityDtoOptional = Optional.ofNullable(userService.save(user));
        if (userEntityDtoOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseResult<>(
                            "success",
                            HttpStatus.CREATED.value(),
                            "User registered successfully",
                            userEntityDtoOptional.get(),
                            "/api/v1/users/register"
                    ));
        } else {
            throw new InternalServerException("User could not be created.");
        }
    }

    @Operation(summary = "Update user details", description = "Updates the details of an existing user")
    @ApiResponse(responseCode = "204", description = "Successful")
    @ApiResponse(responseCode = "400", description = "Validation failed or Entity not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("isMatch(#username) || hasAccess('ROLE_ADMIN')")
    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(
            @PathVariable @Pattern(regexp = "^[a-zA-Z0-9_-]{3,16}$") String username,
            @RequestBody UserUpdateDto user) {
        userService.update(username, user);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseResult<>(
                        "success",
                        HttpStatus.NO_CONTENT.value(),
                        "User updated successfully",
                        Map.of(),
                        "/api/v1/users/update"
                ));
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @ApiResponse(responseCode = "200", description = "Successful")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseResult<>(
                        "Success",
                        HttpStatus.OK.value(),
                        "User founded successfully",
                        userService.getUsers(),
                        "/api/v1/users/get"
                ));
    }

    @Operation(summary = "Get user by username", description = "Finds a user by their username")
    @ApiResponse(responseCode = "200", description = "Successful")
    @ApiResponse(responseCode = "400", description = "Validation failed or Entity not found")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("isMatch(#username)|| hasAccess('ROLE_ADMIN')")
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseResult<>(
                        "Success",
                        HttpStatus.OK.value(),
                        "User founded successfully",
                        userService.getUser(username),
                        "/api/v1/users/get"
                ));
    }

    @Operation(summary = "Delete a user", description = "Deletes a user from the system")
    @ApiResponse(responseCode = "200", description = "Successful")
    @ApiResponse(responseCode = "400", description = "Validation failed or Entity not found")
    @ApiResponse(responseCode = "500", description = "User creation failed")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("isMatch(#userDeleteDto.getUsername()) || hasAccess('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserDeleteDto userDeleteDto) {
        userService.delete(userDeleteDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseResult<>(
                        "Success",
                        HttpStatus.OK.value(),
                        "User founded successfully",
                        Map.of(),
                        "/api/v1/users/get"
                ));
    }
}
