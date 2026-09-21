package com.platform.deck.card.service.config;

import com.mongodb.client.MongoClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@ConditionalOnClass(MongoClient.class)
@EnableMongoAuditing
public class MongoConfig {
}
