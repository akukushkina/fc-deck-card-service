package com.platform.deck.card.service.service;

import com.platform.deck.card.service.model.request.CardRequest;
import com.platform.deck.card.service.model.request.CardUpdateRequest;
import com.platform.deck.card.service.model.response.CardResponse;
import com.platform.deck.card.service.model.response.PagedResponse;

public interface CardService {

    void deleteCardsFromDeck(String deckId);

    CardResponse addCard(String userId, String deckId, CardRequest request);

    PagedResponse<CardResponse> getCards(String userId, String deckId, int page, int pageSize);

    CardResponse updateCard(String userId, String deckId, String cardId, CardUpdateRequest request);

    void deleteCard(String userId, String deckId, String cardId);
}
