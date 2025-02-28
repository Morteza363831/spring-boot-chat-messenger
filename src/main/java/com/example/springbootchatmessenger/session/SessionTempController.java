package com.example.springbootchatmessenger.session;

import com.example.springbootchatmessenger.structure.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Session Management", description = "Operations related to managing chat sessions")
@RestController
@RequestMapping("/api/v1/sessions")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class SessionTempController {

    private final SessionService sessionService;

    public SessionTempController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(summary = "Create a chat session", description = "Creates a new session between two users")
    @ApiResponse(responseCode = "201", description = "Session created successfully")
    @ApiResponse(responseCode = "409", description = "Session already exists")
    @PostMapping("/create")
    public ResponseEntity<?> createSession(@RequestBody SessionCreateDto sessionCreateDto) {
        final Optional<SessionDto> sessionEntityDto = Optional.ofNullable(sessionService.save(sessionCreateDto));

        if (sessionEntityDto.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseResult<>(
                            "success",
                            HttpStatus.CREATED.value(),
                            "Session created successfully",
                            sessionEntityDto.get(),
                            "/api/v1/sessions/create"
                    ));
        }
        log.error("Session creation failed");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseResult<>(
                        "failure",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Session creation failed",
                        new Object(),
                        "/api/v1/sessions/create"
                ));
    }

    @Operation(summary = "Get session by users", description = "Retrieves the chat session for two users")
    @ApiResponse(responseCode = "200", description = "Session retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Session not found")
    @GetMapping
    public ResponseEntity<?> getSession(@RequestParam String user1 , @RequestParam String user2) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseResult<>(
                        "success",
                        HttpStatus.OK.value(),
                        "Session founded successfully",
                        sessionService.findByUserIds(user1, user2),
                        "/api/v1/sessions?user1" + user1 + "&user2" + user2
                ));
    }

    @Operation(summary = "Delete a chat session", description = "Deletes an existing chat session")
    @ApiResponse(responseCode = "200", description = "Session deleted successfully")
    @ApiResponse(responseCode = "404", description = "Session not found")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteSession(@RequestBody SessionDeleteDto sessionDeleteDto) {
        sessionService.deleteSession(sessionDeleteDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseResult<>(
                        "success",
                        HttpStatus.OK.value(),
                        "Session deleted successfully",
                        new Object(),
                        "/api/v1/sessions/delete"
                ));
    }
}
