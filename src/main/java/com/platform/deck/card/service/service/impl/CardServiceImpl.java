package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.entity.Card;
import com.platform.deck.card.service.mapper.CardMapper;
import com.platform.deck.card.service.model.request.CardRequest;
import com.platform.deck.card.service.model.request.CardUpdateRequest;
import com.platform.deck.card.service.model.response.CardResponse;
import com.platform.deck.card.service.model.response.PagedResponse;
import com.platform.deck.card.service.repository.CardRepository;
import com.platform.deck.card.service.service.CardService;
import com.platform.deck.card.service.service.ServiceValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final ServiceValidation serviceValidation;

    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper, ServiceValidation serviceValidation) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.serviceValidation = serviceValidation;
    }


    @Override
    public CardResponse addCard(String userId, String deckId, CardRequest request) {

        serviceValidation.validateDeckOwnership(deckId, userId);

        Card newCard = cardMapper.mapToCard(request, deckId, userId);

        cardRepository.save(newCard);

        CardResponse response = cardMapper.mapToCardResponse(newCard);
        log.info("New card added. Card id: {}", response.id());

        return response;
    }

    @Override
    public PagedResponse<CardResponse> getCards(String userId, String deckId, int page, int pageSize) {
        log.info("Entered into getCards method. Deck id: {}", deckId);

        serviceValidation.validateDeckOwnership(deckId, userId);

        Pageable sanitizedPageable = PageRequest.of(page, pageSize);

        Page<Card> cardPage = cardRepository.findByDeckIdAndOwnerIdOrderByCreatedAtDesc(deckId, userId, sanitizedPageable);

        List<CardResponse> responses = cardPage.getContent().stream()
                .map(cardMapper::mapToCardResponse)
                .toList();

        PagedResponse<CardResponse> pagedResponse = PagedResponse.<CardResponse>builder()
                .content(responses)
                .page(cardPage.getNumber())
                .pageSize(cardPage.getSize())
                .totalElements(cardPage.getTotalElements())
                .totalPages(cardPage.getTotalPages())
                .build();

        log.info("List of cards for userId{}, in deck id {}. Total number: {}", userId, deckId, pagedResponse.getTotalPages());
        return pagedResponse;
    }

    @Override
    public CardResponse updateCard(String userId, String deckId, String cardId, CardUpdateRequest request) {
        log.info("Entered into update card method. CardId: {}", cardId);

        serviceValidation.validateDeckOwnership(deckId, userId);
        Card card = serviceValidation.validateCardOwnership(cardId, userId);

        if(request.front() != null) card.setFront(request.front());
        if(request.back() != null) card.setBack(request.back());

        cardRepository.save(card);

        CardResponse response = cardMapper.mapToCardResponse(card);
        log.info("Card updated: {}", response);

        return response;
    }

    @Override
    public void deleteCard(String userId, String deckId, String cardId) {
        log.info("Entered into delete card method");

        serviceValidation.validateDeckOwnership(deckId, userId);
        serviceValidation.validateCardOwnership(cardId, userId);

        cardRepository.deleteById(cardId);
    }


    @Override
    public void deleteCardsFromDeck(String deckId) {
        log.info("Entered into delete all cards in deck");
        cardRepository.deleteByDeckId(deckId);
    }
}
