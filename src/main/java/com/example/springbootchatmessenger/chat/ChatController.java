package com.example.springbootchatmessenger.chat;

import com.example.springbootchatmessenger.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * This class handles requests for the chat page,
 * establishing a connection between sender and receiver.
 */

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ResourceLoader resourceLoader;
    private final SessionService sessionService;

    public ChatController(ResourceLoader resourceLoader,
                          SessionService sessionService) {
        this.resourceLoader = resourceLoader;
        this.sessionService = sessionService;
    }

    /*
     * This method returns the chat page for sender and receiver usernames
     * with a special chatId (session id between two users).
     */
    @GetMapping
    public ResponseEntity<Resource> openChat(@RequestParam String sender,
                                             @RequestParam String receiver) {
        String RESOURCE = "classpath:templates/chat.html"; // Path to chat HTML file
        Resource resource = resourceLoader.getResource(RESOURCE);

        log.info("Attempting to open chat between sender: '{}' and receiver: '{}'", sender, receiver);

        try {
            Long chatId = sessionService.findByUsernames(sender, receiver).getId();
            log.info("Chat id for sender '{}' and receiver '{}' is: {}", sender, receiver, chatId);

            // Check if the resource exists
            if (resource.exists()) {
                log.info("Chat page has been successfully opened with chat id: {}", chatId);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "text/html")
                        .header("Chat-ID", String.valueOf(chatId)) // Add chat ID to headers
                        .body(resource);
            } else {
                log.error("Chat HTML file does not exist at path: {}", RESOURCE);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred while opening the chat: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
}
