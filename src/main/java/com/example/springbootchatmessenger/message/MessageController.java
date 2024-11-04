package com.example.springbootchatmessenger.message;

import com.example.springbootchatmessenger.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public MessageController(SimpMessagingTemplate simpMessagingTemplate,
                             MessageService messageService,
                             SessionService sessionService) {
        this.simpMessagingTemplate = simpMessagingTemplate;// constructor injection
        this.messageService = messageService;
        this.sessionService = sessionService;
    }

    /*
     * this endpoint will handle messages which are sent between to user !
     * MessageEntityDto store content of message , sender and receiver usernames
     */
    @MessageMapping("/send") // endpoint for sending messages
    public void sendMessage(MessageEntityDto chatMessage) {
        messageService.save(chatMessage); // store message in db
        Long chatId = sessionService.findByUsernames(chatMessage.getSender(), chatMessage.getReceiver()).getId(); // find chat id for sender and receiver
        simpMessagingTemplate.convertAndSend("/topic/" + chatId, chatMessage);
        log.info("chat id for sender , receiver is : {} , {} , {}", chatMessage.getSender(), chatMessage.getReceiver(), chatId);
    }

    /*
     * this endpoint will get all messages between to users with their chat id (sessionId)
     */
    @GetMapping("/messages")
    public ResponseEntity<List<MessageEntityDto>> getAllMessages(@RequestParam String sender,
                                                                 @RequestParam String receiver) {
        List<MessageEntityDto> messages = messageService.findAll(sender, receiver);
        return ResponseEntity.ok(messages);
    }
}