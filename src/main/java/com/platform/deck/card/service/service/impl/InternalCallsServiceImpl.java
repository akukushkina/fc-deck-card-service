package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.entity.Card;
import com.platform.deck.card.service.entity.Deck;
import com.platform.deck.card.service.exception.ResourceNotFoundException;
import com.platform.deck.card.service.mapper.CardMapper;
import com.platform.deck.card.service.mapper.InternalCallMapper;
import com.platform.deck.card.service.model.internal.OwnerResponse;
import com.platform.deck.card.service.model.internal.Sm2UpdateRequest;
import com.platform.deck.card.service.model.internal.Sm2UpdateResponse;
import com.platform.deck.card.service.model.response.CardResponse;
import com.platform.deck.card.service.repository.CardRepository;
import com.platform.deck.card.service.repository.DeckRepository;
import com.platform.deck.card.service.service.InternalCallsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class InternalCallsServiceImpl implements InternalCallsService {

    private final DeckRepository deckRepository;
    private final InternalCallMapper internalCallMapper;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public InternalCallsServiceImpl(DeckRepository deckRepository, InternalCallMapper internalCallMapper, CardMapper cardMapper, CardRepository cardRepository) {
        this.deckRepository = deckRepository;
        this.internalCallMapper = internalCallMapper;
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    @Override
    public OwnerResponse getOwner(String deckId) {

        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new ResourceNotFoundException("No deck found for id: " + deckId));

        OwnerResponse response = internalCallMapper.mapToInternalResponse(deck);
        log.info("User id for deck {} is: {}", deckId, response.getUserId());

        return response;
    }

    @Override
    public List<CardResponse> listDueCards(String deckId, int limit) {
        if(!deckRepository.existsById(deckId)) throw new ResourceNotFoundException("No deck found for id: " + deckId);

        List<Card> cards = cardRepository.findByDeckIdAndNextReviewDateLessThanEqual(deckId, LocalDate.now(), limit);

        List<CardResponse> responses = cards.stream()
                .map(cardMapper::mapToCardResponse)
                .toList();


        log.info("Cards to review: {}", responses.size());
        return responses;
    }

    @Override
    public Sm2UpdateResponse updateSm(String cardId, Sm2UpdateRequest request) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("No card found with id " + cardId));

        card.setEaseFactor(request.easeFactor());
        card.setInterval(request.interval());
        card.setNextReviewDate(request.nextReviewDate());

        cardRepository.save(card);

        Sm2UpdateResponse response = internalCallMapper.mapToSm2Response(card);
        log.info("Card updated, new ease factor is: {}", response.getEaseFactor());

        return response;
    }
}
