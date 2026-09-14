package com.platform.deck.card.service.service;

import com.platform.deck.card.service.model.internal.OwnerResponse;
import com.platform.deck.card.service.model.internal.Sm2UpdateRequest;
import com.platform.deck.card.service.model.internal.Sm2UpdateResponse;
import com.platform.deck.card.service.model.response.CardResponse;

import java.util.List;

public interface InternalCallsService {
    OwnerResponse getOwner(String deckId);

    List<CardResponse> listDueCards(String deckId, int limit);

    Sm2UpdateResponse updateSm(String cardId, Sm2UpdateRequest request);
}
