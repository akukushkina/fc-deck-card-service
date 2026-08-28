package com.platform.deck.card.service.service;

import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.response.DeckResponse;

public interface DeckService {
    DeckResponse createDeck(DeckRequest deckRequest, String userId);
}
