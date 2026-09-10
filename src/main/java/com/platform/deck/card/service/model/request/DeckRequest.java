package com.platform.deck.card.service.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeckRequest {
    @NotNull
    @Size(max = 100, message = "Deck name must be 100 characters or less")
    private String name;

    @Size(max = 500, message = "Description must be 500 characters or less")
    private String description;
}
