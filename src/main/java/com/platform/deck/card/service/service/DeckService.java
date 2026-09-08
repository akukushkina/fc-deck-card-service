package com.platform.deck.card.service.service;

import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.response.DeckResponse;
import com.platform.deck.card.service.model.response.PagedResponse;

import java.util.List;

public interface DeckService {
    DeckResponse createDeck(DeckRequest deckRequest, String userId);

    PagedResponse<DeckResponse> getDecks(String userId, int page, int pageSize);
}
