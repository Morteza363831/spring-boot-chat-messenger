package com.example.springbootchatmessenger.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/*
 * this class will config web socket with name chat .
 * clients can connect to the socket with /chat in stompClient
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    /*
     * register for each client will be handling here
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").setAllowedOrigins("*"); // endpoint to connect to websocket
    }


    /*
     * config simple message broker for socket .
     * you can use rabbitmq or kafka instead of it !
     * this is just a simple message broke from web socket library
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/topic", "/queue"); // simple in memory broker for broadcasting messages
        registry.setApplicationDestinationPrefixes("/app"); // prefix for messages sent from clients
    }
}
