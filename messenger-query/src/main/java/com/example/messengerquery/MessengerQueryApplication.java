package com.example.messengerquery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class MessengerQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessengerQueryApplication.class, args);
    }

}
