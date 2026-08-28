package com.platform.deck.card.service.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CardRequest(@NotBlank(message = "Front content is required")
                          @Size(max = 1000, message = "Front content must be 1000 characters or less")
                          String front,

                          @NotBlank(message = "Back content is required")
                          @Size(max = 2000, message = "Back content must be 2000 characters or less")
                          String back)
{}
