package com.platform.deck.card.service.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Document(collection = "cards")
@CompoundIndexes({
        @CompoundIndex(name = "deck_created_idx", def = "{'deckId': 1, 'createdAt': 1}"),
        @CompoundIndex(name = "deck_next_review_idx", def = "{'deckId': 1, 'nextReviewDate': 1}")
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @Id
    private String id;

    private String deckId;

    private String ownerId;

    private String front;

    private String back;

    @Builder.Default
    private Float easeFactor = 2.5f;

    @Builder.Default
    private Integer interval = 1;

    @Builder.Default
    private LocalDate nextReviewDate = LocalDate.now();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
