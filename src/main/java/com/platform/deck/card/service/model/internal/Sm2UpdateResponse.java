package com.platform.deck.card.service.model.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sm2UpdateResponse {
    private float easeFactor;
    private Integer interval;
    private LocalDate nextReviewDate;
}
