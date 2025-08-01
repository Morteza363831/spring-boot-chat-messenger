package com.example.messenger.message;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.UUID;

@FeignClient(
        name = "messageClient",
        url = "http://localhost:8051/api/v1/query/message")
public interface MessageQueryClient {

    @GetMapping("/{id}")
    Optional<MessageEntity> getMessageBySessionId(@PathVariable("id") UUID sessionId);
}
