package com.platform.deck.card.service.service;

import com.platform.deck.card.service.model.request.CardRequest;
import com.platform.deck.card.service.model.response.CardResponse;
import jakarta.validation.Valid;

public interface CardService {
    void deleteCardsFromDeck(String deckId, String userId);

    CardResponse addCard(String userId, String deckId, @Valid CardRequest request);
}
