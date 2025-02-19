package com.example.springbootchatmessenger.message;

import com.example.springbootchatmessenger.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class MessageController {

    /*
     * constructor injection objects .
     * simpleMessagingTemplate
     * messageService
     * sessionService
     */
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private final SessionService sessionService;

    // chatController constructor to inject objects
    public MessageController(final SimpMessagingTemplate simpMessagingTemplate,
                             final MessageService messageService,
                             final SessionService sessionService) {
        this.simpMessagingTemplate = simpMessagingTemplate;// constructor injection
        this.messageService = messageService;
        this.sessionService = sessionService;
    }

    /*
     * this endpoint will handle messages which are sent between to user !
     * MessageEntityDto store content of message , sender and receiver usernames
     */
    @MessageMapping("/send") // endpoint for sending messages
    public void sendMessage(final UUID sessionId,
                            final MessageContent chatMessage) {
        messageService.saveMessage(sessionId, chatMessage); // store message in db
        simpMessagingTemplate.convertAndSend("/topic/" + sessionId, chatMessage);
        log.info("chat id for sender , receiver is : {} , {} , {}", chatMessage.getSenderUserId(), chatMessage.getReceiverUserId(), sessionId);
    }

    /*
     * this endpoint will get all messages between to users with their chat id (sessionId)
     */
    @GetMapping("/messages")
    public ResponseEntity<List<MessageContent>> getAllMessages(@RequestParam final String sender,
                                                                 @RequestParam final String receiver) {
        final List<MessageContent> messages = messageService.findAll(sessionService.findByUserIds(sender, receiver));
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/message/{sessionId}")
    public void saveMessage(@PathVariable final UUID sessionId ,
                            @RequestBody final MessageContent chatMessage) {
        messageService.saveMessage(sessionId, chatMessage);
    }
}