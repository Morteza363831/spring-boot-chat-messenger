package com.example.springbootchatmessenger.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/session")
@Slf4j
public class SessionTempController {

    private final SessionService sessionService;

    public SessionTempController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/")
    public ResponseEntity<?> createSession(@RequestBody SessionCreateDto sessionCreateDto) {
        final Optional<SessionDto> sessionEntityDto = Optional.ofNullable(sessionService.save(sessionCreateDto));

        if (sessionEntityDto.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(sessionEntityDto.get());
        }
        log.error("Session creation failed");
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/")
    public ResponseEntity<SessionDto> getSession(@RequestBody SessionFindDto sessionFindDto) {
        return ResponseEntity.ok(sessionService.findByUserIds(sessionFindDto));
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteSession(@RequestBody SessionDeleteDto sessionDeleteDto) {
        sessionService.deleteSession(sessionDeleteDto);

        return ResponseEntity.ok().build();
    }

}
