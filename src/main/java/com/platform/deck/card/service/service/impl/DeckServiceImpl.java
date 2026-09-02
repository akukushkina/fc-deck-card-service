package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.entity.Deck;
import com.platform.deck.card.service.mapper.DeckMapper;
import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.response.DeckResponse;
import com.platform.deck.card.service.repository.DeckRepository;
import com.platform.deck.card.service.service.DeckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeckServiceImpl implements DeckService {

    private final DeckMapper deckMapper;
    private final DeckRepository deckRepository;

    public DeckServiceImpl(DeckMapper deckMapper, DeckRepository deckRepository) {
        this.deckMapper = deckMapper;
        this.deckRepository = deckRepository;
    }

    @Override
    public DeckResponse createDeck(DeckRequest deckRequest, String userId) {

        log.info("Entering into createDeck method");

        Deck newDeck = deckMapper.mapToDeck(deckRequest, userId);

        deckRepository.save(newDeck);

        DeckResponse response = deckMapper.mapToDeckResponse(newDeck);

        return response;
    }
}
