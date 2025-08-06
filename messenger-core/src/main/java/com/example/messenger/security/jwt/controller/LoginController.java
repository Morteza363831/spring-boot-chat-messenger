package com.example.messenger.security.jwt.controller;

import com.example.messenger.exceptions.AuthenticationFailureException;
import com.example.messenger.exceptions.CustomEntityNotFoundException;
import com.example.messenger.exceptions.CustomValidationException;
import com.example.messenger.security.jwt.CustomAuthenticationManager;
import com.example.messenger.security.jwt.CustomUserDetailsService;
import com.example.messenger.security.jwt.JwtTokenUtil;
import com.example.messenger.security.jwt.model.AuthenticationDto;
import com.example.messenger.structure.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Login controller class will handle requests for token and login
 * /token endpoint is public and anyone can access to it
 */

@Slf4j
@Tag(name = "Authentication", description = "Handles user authentication and JWT token generation")
@SecurityRequirements() // No authentication required
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {

    // services
    private final CustomAuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    // public methods

    @Operation(summary = "Generate JWT token", description = "Authenticates user and returns JWT token")
    @ApiResponse(responseCode = "200", description = "Token generated successfully")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody AuthenticationDto authenticationDto) {
        try {
            authenticate(authenticationDto.getUsername(), authenticationDto.getPassword());
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseResult<>(
                            "success",
                            HttpStatus.OK.value(),
                            "Login successful",
                            token,
                            "/api/v1/auth/token"
                    ));
        } catch (CustomEntityNotFoundException e) {
            log.error("User not found: {}", authenticationDto.getUsername());
            throw new CustomEntityNotFoundException(authenticationDto.getUsername());
        } catch (AuthenticationFailureException failure) {
            log.error("Authentication failure: {}", failure.getMessage());
            throw new AuthenticationFailureException();
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", authenticationDto.getUsername());
            throw new CustomValidationException(List.of(e.getMessage()));
        }
    }

    // utils

    private void authenticate(final String username, final String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
