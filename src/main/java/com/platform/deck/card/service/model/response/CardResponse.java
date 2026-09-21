package com.platform.deck.card.service.model.response;

import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record CardResponse(String id,
                           String deckId,
                           String ownerId,
                           String front,
                           String back,
                           Float easeFactor,
                           Integer interval,
                           LocalDate nextReviewDate,
                           Instant createdAt,
                           Instant updatedAt) {}
