package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.entity.Card;
import com.platform.deck.card.service.mapper.CardMapper;
import com.platform.deck.card.service.model.request.CardRequest;
import com.platform.deck.card.service.model.response.CardResponse;
import com.platform.deck.card.service.repository.CardRepository;
import com.platform.deck.card.service.service.CardService;
import com.platform.deck.card.service.service.ServiceValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public void deleteCardsFromDeck(String deckId, String userId) {

    }
}
