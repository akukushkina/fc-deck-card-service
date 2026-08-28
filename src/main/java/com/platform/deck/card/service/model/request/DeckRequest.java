package com.platform.deck.card.service.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeckRequest(@NotBlank(message = "Deck name is required")
                          @Size(max = 100, message = "Deck name must be 100 characters or less")
                          String name,

                          @Size(max = 500, message = "Description must be 500 characters or less")
                          String description) {
}
