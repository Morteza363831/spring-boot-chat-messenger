package com.example.springbootchatmessenger.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/session")
@Slf4j
public class SessionTempController {

    private final SessionService sessionService;

    public SessionTempController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody SessionEntityDto session) {
        final Optional<SessionEntityDto> sessionEntityDto = Optional.ofNullable(sessionService.save(session));

        if (sessionEntityDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(sessionEntityDto.get());
        }
        log.error("Session creation failed");
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping
    public ResponseEntity<SessionEntityDto> getSession(@RequestParam Long firstUserId, @RequestParam Long secondUserId) {
        return ResponseEntity.ok(sessionService.findByUserIds(firstUserId, secondUserId));
    }
}
