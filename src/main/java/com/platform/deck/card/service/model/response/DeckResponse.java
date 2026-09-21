package com.platform.deck.card.service.model.response;

import lombok.Builder;

import java.time.Instant;

@Builder
public record DeckResponse(String id,
                           String ownerId,
                           String name,
                           String description,
                           Instant createdAt,
                           Instant updatedAt) {
}
