package com.platform.deck.card.service.model.response;

import java.time.Instant;

public record DeckResponse(String id,
                           String ownerId,
                           String name,
                           String description,
                           Instant createdAt,
                           Instant updatedAt) {
}
