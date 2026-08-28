package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.response.DeckResponse;
import com.platform.deck.card.service.service.DeckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeckServiceImpl implements DeckService {
    @Override
    public DeckResponse createDeck(DeckRequest deckRequest, String userId) {
        return null;
    }
}
