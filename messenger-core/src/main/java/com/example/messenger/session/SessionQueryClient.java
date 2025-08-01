package com.example.messenger.session;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@FeignClient(
        name = "SessionClient",
        url = "http://localhost:8051/api/v1/query/session")
public interface SessionQueryClient {
    @GetMapping
    Optional<SessionEntity> getSession(@RequestParam UUID user1, @RequestParam UUID user2);
}
