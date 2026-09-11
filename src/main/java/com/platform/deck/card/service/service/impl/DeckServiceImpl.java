package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.entity.Deck;
import com.platform.deck.card.service.mapper.DeckMapper;
import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.request.DeckUpdateRequest;
import com.platform.deck.card.service.model.response.DeckResponse;
import com.platform.deck.card.service.model.response.PagedResponse;
import com.platform.deck.card.service.repository.DeckRepository;
import com.platform.deck.card.service.service.CardService;
import com.platform.deck.card.service.service.DeckService;
import com.platform.deck.card.service.service.ServiceValidation;
import jakarta.validation.Valid;
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
    private final ServiceValidation serviceValidation;

    public DeckServiceImpl(DeckMapper deckMapper, DeckRepository deckRepository, CardService cardService, ServiceValidation serviceValidation) {
        this.deckMapper = deckMapper;
        this.deckRepository = deckRepository;
        this.cardService = cardService;
        this.serviceValidation = serviceValidation;
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

            Deck deck = serviceValidation.validateDeckOwnership(deckId, userId);

            DeckResponse response = deckMapper.mapToDeckResponse(deck);
            log.info("Deck is: {}", response);

            return response;
    }

    @Override
    public DeckResponse updateDeck(String userId, String deckId, @Valid DeckUpdateRequest request) {
        log.info("Entered into update deck method. Deck id: {}", deckId);
        Deck deck = serviceValidation.validateDeckOwnership(deckId, userId);

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

        serviceValidation.validateDeckOwnership(deckId, userId);

        cardService.deleteCardsFromDeck(deckId);

        deckRepository.deleteById(deckId);
    }


}
