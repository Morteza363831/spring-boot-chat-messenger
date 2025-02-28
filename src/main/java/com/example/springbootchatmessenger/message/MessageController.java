package com.example.springbootchatmessenger.message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "Message Management", description = "Operations for sending and retrieving messages")
@RestController
@RequestMapping("/api/v1/message")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;

    public MessageController(final SimpMessagingTemplate simpMessagingTemplate,
                             final MessageService messageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
    }

    @Operation(summary = "Send a message", description = "Sends a message between users within a session")
    @ApiResponse(responseCode = "200", description = "Message sent successfully")
    @MessageMapping("/send")
    public void sendMessage(final UUID sessionId,
                            final MessageContent chatMessage) {
        messageService.saveMessage(sessionId, chatMessage);
        simpMessagingTemplate.convertAndSend("/topic/" + sessionId, chatMessage);
        log.info("Message sent in session: {} | Sender: {} | Receiver: {}", sessionId, chatMessage.getSender(), chatMessage.getReceiver());
    }

    @Operation(summary = "Get all messages", description = "Retrieves all messages for a given session")
    @ApiResponse(responseCode = "200", description = "Messages retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Session not found")
    @GetMapping("/{sessionId}")
    public ResponseEntity<List<MessageContent>> getAllMessages(@PathVariable UUID sessionId) {
        final List<MessageContent> messages = messageService.findAll(sessionId);
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Store a message", description = "Stores a message in the database for a session")
    @ApiResponse(responseCode = "201", description = "Message stored successfully")
    @PostMapping("/{sessionId}")
    public void saveMessage(@PathVariable final UUID sessionId,
                            @RequestBody final MessageContent messageContent) {
        messageService.saveMessage(sessionId, messageContent);
    }
}
