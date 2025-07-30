package com.example.messenger.user;

import com.example.messenger.exceptions.*;
import com.example.messenger.structure.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * User controller will handle users management endpoints
 * Create user endpoint does not need any authorize .
 * Other endpoints needs authorize before starting of process (authorize with username or ROLE_ADMIN)
 */

@Tag(name = "User Management", description = "Operations related to user management")
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user", description = "Registers a new user in the system")
    @ApiResponse(responseCode = "201", description = "User successfully created")
    @ApiResponse(responseCode = "409", description = "Duplicate user violation")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @ApiResponse(responseCode = "500", description = "Internal error")
    @SecurityRequirements() // Excludes security for this endpoint
    @PostMapping("/register")
    public ResponseEntity<ResponseResult<UserDto>> registerUser(@RequestBody final UserCreateDto user) {

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
        userService.updateUser(username, user);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseResult<>(
                        "success",
                        HttpStatus.NO_CONTENT.value(),
                        "User updated successfully",
                        new Object(),
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
                        userService.getAllUsers(),
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
    public ResponseEntity<?> getUser(@PathVariable final String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseResult<>(
                        "Success",
                        HttpStatus.OK.value(),
                        "User founded successfully",
                        userService.getUserByUsername(username),
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
    public ResponseEntity<?> deleteUser(@RequestBody final UserDeleteDto userDeleteDto) {
        userService.deleteUserById(userDeleteDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseResult<>(
                        "Success",
                        HttpStatus.OK.value(),
                        "User founded successfully",
                        new Object(),
                        "/api/v1/users/get"
                ));
    }
}
