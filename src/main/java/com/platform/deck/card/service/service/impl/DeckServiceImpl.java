package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.entity.Deck;
import com.platform.deck.card.service.mapper.DeckMapper;
import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.response.DeckResponse;
import com.platform.deck.card.service.model.response.PagedResponse;
import com.platform.deck.card.service.repository.DeckRepository;
import com.platform.deck.card.service.service.DeckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

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

        log.info("Response is: {}", response);
        return response;
    }

    @Override
    public PagedResponse<DeckResponse> getDecks(String userId, int page, int pageSize) {
        log.info("Entering into getDecks method");

        Pageable sanitizedPageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Deck> deckPage = deckRepository.findByOwnerIdOrderByCreatedAtDesc(userId, sanitizedPageable);

        List<DeckResponse> responses = new ArrayList<>();

        for(Deck deck : deckPage.getContent()){
            responses.add(deckMapper.mapToDeckResponse(deck));
        }

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
}
