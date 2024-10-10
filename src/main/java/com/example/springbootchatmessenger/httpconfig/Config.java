package com.example.springbootchatmessenger.httpconfig;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/*
 * the config class will make bean objects for important classes like :
 * RestTemplate to send requests between services
 * ModelMapper to map entities and dtos
 */

@Configuration
@ComponentScan(basePackages = "com.example")
public class Config {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }



}
