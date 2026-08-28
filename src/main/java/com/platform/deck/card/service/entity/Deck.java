package com.platform.deck.card.service.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "decks")
@CompoundIndex(name = "owner_created_idx", def = "{'ownerId': 1, 'createdAt': -1}")
public class Deck {

    @Id
    private String id;

    private String ownerId;

    private String name;

    private String description;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
