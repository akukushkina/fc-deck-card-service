package com.platform.deck.card.service.model.request;

import jakarta.validation.constraints.Size;

public record CardUpdateRequest(
                          @Size(max = 1000, message = "Front content must be 1000 characters or less")
                          String front,

                          @Size(max = 2000, message = "Back content must be 2000 characters or less")
                          String back)
{}
