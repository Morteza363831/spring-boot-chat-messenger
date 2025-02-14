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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ResourceLoader resourceLoader;
    private final SessionService sessionService;

    public ChatController(ResourceLoader resourceLoader, SessionService sessionService) {
        this.resourceLoader = resourceLoader;
        this.sessionService = sessionService;
    }

    @GetMapping
    public ResponseEntity<String> openChat(@RequestParam Long senderUserId,
                                           @RequestParam Long receiverUserId) {
        String RESOURCE_PATH = "classpath:templates/chat.html"; // Path to chat HTML file
        Resource resource = resourceLoader.getResource(RESOURCE_PATH);

        log.info("Attempting to open chat between sender: '{}' and receiver: '{}'", senderUserId, receiverUserId);

        try {
            UUID chatId = sessionService.findByUserIds(senderUserId, receiverUserId).getId();
            log.info("Chat id for sender '{}' and receiver '{}' is: {}", senderUserId, receiverUserId, chatId.toString());

            // Check if the resource exists and read it as a string
            if (resource.exists()) {
                String htmlContent = new String(Files.readAllBytes(Paths.get(resource.getURI())));

                // Insert the chat ID into the HTML by replacing a placeholder
                htmlContent = htmlContent.replace("{{chatId}}", String.valueOf(chatId.toString()));

                log.info("Chat page has been successfully opened with chat id: {}", chatId.toString());
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "text/html")
                        .body(htmlContent);
            } else {
                log.error("Chat HTML file does not exist at path: {}", RESOURCE_PATH);
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            log.error("An error occurred while reading the chat HTML file: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        } catch (Exception e) {
            log.error("An unexpected error occurred while opening the chat: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
}
