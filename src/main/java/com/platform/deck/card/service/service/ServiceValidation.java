package com.platform.deck.card.service.service;

import com.platform.deck.card.service.entity.Card;
import com.platform.deck.card.service.entity.Deck;
import com.platform.deck.card.service.exception.ForbiddenException;
import com.platform.deck.card.service.exception.ResourceNotFoundException;
import com.platform.deck.card.service.repository.CardRepository;
import com.platform.deck.card.service.repository.DeckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServiceValidation {

    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;

    public ServiceValidation(DeckRepository deckRepository, CardRepository cardRepository) {
        this.deckRepository = deckRepository;
        this.cardRepository = cardRepository;
    }


    public Deck validateDeckOwnership(String deckId, String userId){

        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new ResourceNotFoundException("No deck found with id: " + deckId));

        if(!deck.getOwnerId().equals(userId)){
            log.info("User id is not matching, userId {}", userId);
            throw new ForbiddenException("Access denied");
        }
        return deck;
    }

    public Card validateCardOwnership(String cardId, String userId){

        Card card = cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("No card found wit id: " + cardId));

        if(!card.getOwnerId().equals(userId)){
            log.info("UserId is not matching ownerId. UserId: {}", userId);
            throw new ForbiddenException("Access denied");
        }

        return card;
    }
}
