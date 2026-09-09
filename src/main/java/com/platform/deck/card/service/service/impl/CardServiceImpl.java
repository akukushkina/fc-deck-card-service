package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.mapper.CardMapper;
import com.platform.deck.card.service.repository.CardRepository;
import com.platform.deck.card.service.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepository repository;
    private final CardMapper cardMapper;

    public CardServiceImpl(CardRepository repository, CardMapper cardMapper) {
        this.repository = repository;
        this.cardMapper = cardMapper;
    }

    @Override
    public void deleteCardsFromDeck(String deckId, String userId) {

    }
}
