package com.example.messenger.session.controller;

import com.example.messenger.session.model.SessionCreateDto;
import com.example.messenger.session.model.SessionDeleteDto;
import com.example.messenger.session.model.SessionDto;
import com.example.messenger.session.service.SessionService;
import com.example.messenger.structure.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Tag(name = "Session Management", description = "Operations related to managing chat sessions")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;


    @Operation(summary = "Create a chat session", description = "Creates a new session between two users")
    @ApiResponse(responseCode = "201", description = "Session created successfully")
    @ApiResponse(responseCode = "409", description = "Session already exists")
    @PreAuthorize("isMatch(#sessionCreateDto.getUser1()) || hasAccess('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createSession(@RequestBody SessionCreateDto sessionCreateDto, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseResult.builder()
                        .status("success")
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Session created successfully")
                        .data(sessionService.save(sessionCreateDto))
                        .path(request.getRequestURI())
                        .build()
                );
    }

    @Operation(summary = "Get session by users", description = "Retrieves the chat session for two users")
    @ApiResponse(responseCode = "200", description = "Session retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Session not found")
    @PreAuthorize("isMatch(#user1) || hasAccess('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<?> getSession(@RequestParam String user1 , @RequestParam String user2, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseResult.builder()
                        .status("success")
                        .statusCode(HttpStatus.OK.value())
                        .message("Session retrieved successfully")
                        .data(sessionService.getSession(user1, user2))
                        .path(request.getRequestURI())
                        .build()
                );
    }

    @Operation(summary = "Delete a chat session", description = "Deletes an existing chat session")
    @ApiResponse(responseCode = "200", description = "Session deleted successfully")
    @ApiResponse(responseCode = "404", description = "Session not found")
    @PreAuthorize("isMatch(#sessionDeleteDto.getUser1())|| hasAccess('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSession(@RequestBody SessionDeleteDto sessionDeleteDto, HttpServletRequest request) {
        sessionService.delete(sessionDeleteDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseResult.builder()
                        .status("success")
                        .statusCode(HttpStatus.OK.value())
                        .message("Session deleted successfully")
                        .data(Map.of())
                        .path(request.getRequestURI())
                        .build()
                );
    }


    @GetMapping("/{sessionId}")
    @PreAuthorize("hasAccess(#sessionId)")
    public ResponseEntity<?> getSession(@PathVariable String sessionId) {
        return ResponseEntity.ok().build();
    }
}
