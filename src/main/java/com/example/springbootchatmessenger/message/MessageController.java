package com.example.springbootchatmessenger.message;

import com.example.springbootchatmessenger.structure.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Tag(name = "Message Management", description = "Operations for sending and retrieving messages")
@RestController
@RequestMapping("/api/v1/messages")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private final ObjectMapper mapper;

    public MessageController(final SimpMessagingTemplate simpMessagingTemplate,
                             final MessageService messageService,
                             final ObjectMapper mapper) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
        this.mapper = mapper;
    }

    @Operation(summary = "Send a message", description = "Sends a message between users within a session")
    @ApiResponse(responseCode = "200", description = "Message sent successfully")
    @MessageMapping("/send")
    public MessageContent sendMessage(@Payload String body) {
        JsonNode messageNode = null;
        try {
            messageNode = mapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        final UUID sessionId = UUID.fromString(messageNode.get("sessionId").asText());
        final String sender = messageNode.get("sender").asText();
        final String receiver = messageNode.get("receiver").asText();
        final String content = messageNode.get("content").asText();
        MessageContent messageContent = new MessageContent();
        messageContent.setSender(sender);
        messageContent.setReceiver(receiver);
        messageContent.setContent(content);

        messageService.saveMessage(sessionId, messageContent);
        simpMessagingTemplate.convertAndSend("/topic/" + sessionId, messageContent);
        log.info("Message sent in session: {} | Sender: {} | Receiver: {}", sessionId, messageContent.getSender(), messageContent.getReceiver());
        return messageContent;
    }

    @Operation(summary = "Get all messages", description = "Retrieves all messages for a given session")
    @ApiResponse(responseCode = "200", description = "Messages retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Session not found")
    @PreAuthorize("hasAccess(#sessionId)")
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getAllMessages(@PathVariable UUID sessionId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseResult<>(
                        "success",
                        HttpStatus.OK.value(),
                        "Messages founded successfully",
                        messageService.findAll(sessionId),
                        "/api/v1/messages/" + sessionId
                ));
    }

    @Operation(summary = "Store a message", description = "Stores a message in the database for a session")
    @ApiResponse(responseCode = "201", description = "Message stored successfully")
    @PreAuthorize("isMatch(#messageContent.getSender()) && hasAccess(#sessionId)")
    @PostMapping("/{sessionId}")
    public void saveMessage(@PathVariable final UUID sessionId,
                            @RequestBody final MessageContent messageContent) {
        messageService.saveMessage(sessionId, messageContent);
    }
}
