package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.entity.Deck;
import com.platform.deck.card.service.exception.ForbiddenException;
import com.platform.deck.card.service.exception.ResourceNotFoundException;
import com.platform.deck.card.service.mapper.DeckMapper;
import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.response.DeckResponse;
import com.platform.deck.card.service.model.response.PagedResponse;
import com.platform.deck.card.service.repository.DeckRepository;
import com.platform.deck.card.service.service.CardService;
import com.platform.deck.card.service.service.DeckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DeckServiceImpl implements DeckService {

    private final DeckMapper deckMapper;
    private final DeckRepository deckRepository;
    private final CardService cardService;

    public DeckServiceImpl(DeckMapper deckMapper, DeckRepository deckRepository, CardService cardService) {
        this.deckMapper = deckMapper;
        this.deckRepository = deckRepository;
        this.cardService = cardService;
    }

    @Override
    public DeckResponse createDeck(DeckRequest deckRequest, String userId) {

        log.info("Entering into createDeck method");

        Deck newDeck = deckMapper.mapToDeck(deckRequest, userId);

        deckRepository.save(newDeck);

        DeckResponse response = deckMapper.mapToDeckResponse(newDeck);

        log.info("Response is: {}", response);
        return response;
    }

    @Override
    public PagedResponse<DeckResponse> getDecks(String userId, int page, int pageSize) {
        log.info("Entering into getDecks method");

        Pageable sanitizedPageable = PageRequest.of(page, pageSize);

        Page<Deck> deckPage = deckRepository.findByOwnerIdOrderByCreatedAtDesc(userId, sanitizedPageable);

        List<DeckResponse> responses = deckPage.getContent().stream()
                .map(deckMapper::mapToDeckResponse)
                .toList();

        PagedResponse<DeckResponse> pagedResponse = PagedResponse.<DeckResponse>builder()
                .content(responses)
                .page(deckPage.getNumber())
                .pageSize(deckPage.getSize())
                .totalElements(deckPage.getTotalElements())
                .totalPages(deckPage.getTotalPages())
                .build();

        log.info("List of decks for userId {}, number of decks: {}", userId, pagedResponse.getTotalElements());

        return pagedResponse;
    }

    @Override
    public DeckResponse showDeck(String userId, String deckId) {

            Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new ResourceNotFoundException("No deck found with id: " + deckId));

            if(!deck.getOwnerId().equals(userId)){
                log.info("User id is not matching, userId {}", userId);
                throw new ForbiddenException("Access denied");
            }

            DeckResponse response = deckMapper.mapToDeckResponse(deck);
            log.info("Deck is: {}", response);

            return response;
    }

    @Override
    public DeckResponse updateDeck(String userId, String deckId, DeckRequest request) {
        log.info("Entered into update deck method. Deck id: {}", deckId);
        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new ResourceNotFoundException("No deck found with id: " + deckId));

        if(!deck.getOwnerId().equals(userId)){
            log.info("User id is not matching, userId {}", userId);
            throw new ForbiddenException("Access denied");
        }

        if(request.getName() != null) deck.setName(request.getName());
        if(request.getDescription() != null) deck.setDescription(request.getDescription());

        deckRepository.save(deck);

        DeckResponse response = deckMapper.mapToDeckResponse(deck);
        log.info("Deck successfully updated {}", response);

        return response;
    }

    @Override
    public void deleteDeck(String userId, String deckId) {
        log.info("Entered into delete deck method, deck id: {}", deckId);

        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new ResourceNotFoundException("No deck found with id: " + deckId));

        if(!deck.getOwnerId().equals(userId)){
            log.info("User id is not matching, userId {}", userId);
            throw new ForbiddenException("Access denied");
        }

        cardService.deleteCardsFromDeck(deckId, userId);

        deckRepository.deleteById(deckId);
    }
}
