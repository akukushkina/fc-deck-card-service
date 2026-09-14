package com.platform.deck.card.service.service;

import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.request.DeckUpdateRequest;
import com.platform.deck.card.service.model.response.DeckResponse;
import com.platform.deck.card.service.model.response.PagedResponse;

public interface DeckService {
    DeckResponse createDeck(DeckRequest deckRequest, String userId);

    PagedResponse<DeckResponse> getDecks(String userId, int page, int pageSize);

    DeckResponse showDeck(String userId, String deckId);

    DeckResponse updateDeck(String userId, String deckId, DeckUpdateRequest request);

    void deleteDeck(String userId, String deckId);

}
