package com.platform.deck.card.service.model.internal;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record Sm2UpdateRequest(@NotNull(message = "Ease factor is required")
                               Float easeFactor,

                               @NotNull(message = "Interval is required")
                               Integer interval,

                               @NotNull(message = "Next review date is required")
                               LocalDate nextReviewDate) {
}
