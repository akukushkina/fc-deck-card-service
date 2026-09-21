package com.platform.deck.card.service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
public class DeckCardServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeckCardServiceApplication.class, args);
    }
}
