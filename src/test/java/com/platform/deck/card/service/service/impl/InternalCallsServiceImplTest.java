package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.entity.Card;
import com.platform.deck.card.service.entity.Deck;
import com.platform.deck.card.service.mapper.CardMapper;
import com.platform.deck.card.service.mapper.InternalCallMapper;
import com.platform.deck.card.service.model.internal.OwnerResponse;
import com.platform.deck.card.service.model.internal.Sm2UpdateRequest;
import com.platform.deck.card.service.model.internal.Sm2UpdateResponse;
import com.platform.deck.card.service.model.response.CardResponse;
import com.platform.deck.card.service.repository.CardRepository;
import com.platform.deck.card.service.repository.DeckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.platform.deck.card.service.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InternalCallsServiceImplTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private InternalCallMapper internalCallMapper;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private InternalCallsServiceImpl internalCallsService;

    private Deck deck;
    private Card card;
    private OwnerResponse ownerResponse;
    private CardResponse cardResponse;
    private Sm2UpdateResponse sm2UpdateResponse;

    @BeforeEach
    void setUp() {
        deck = testDeck();
        card = testCard();
        ownerResponse = testOwnerResponse();
        cardResponse = testCardResponse();
        sm2UpdateResponse = testSm2Response();
    }

    @Nested
    class GetOwner {

        @Test
        void getOwner_ShouldReturnOwnerResponse_WhenDeckExists() {

            when(deckRepository.findById(DECK_ID)).thenReturn(Optional.of(deck));
            when(internalCallMapper.mapToInternalResponse(deck)).thenReturn(ownerResponse);

            OwnerResponse actualResponse = internalCallsService.getOwner(DECK_ID);

            assertNotNull(actualResponse);
            assertEquals(ownerResponse.getUserId(), actualResponse.getUserId());
            assertEquals(ownerResponse.getDeckId(), actualResponse.getDeckId());

            InOrder inOrder = inOrder(deckRepository, internalCallMapper);
            inOrder.verify(deckRepository).findById(DECK_ID);
            inOrder.verify(internalCallMapper).mapToInternalResponse(deck);
        }
    }

    @Nested
    class ListDueCards {

        @Test
        void listDueCards_ShouldReturnListOfCardResponses_WhenDeckExists() {

            int limit = 10;
            List<Card> dueCards = List.of(card);

            when(deckRepository.existsById(DECK_ID)).thenReturn(true);
            when(cardRepository.findByDeckIdAndNextReviewDateLessThanEqual(eq(DECK_ID), any(LocalDate.class), eq(limit)))
                    .thenReturn(dueCards);
            when(cardMapper.mapToCardResponse(card)).thenReturn(cardResponse);

            List<CardResponse> actualResponses = internalCallsService.listDueCards(DECK_ID, limit);

            assertNotNull(actualResponses);
            assertEquals(1, actualResponses.size());
            assertEquals(cardResponse, actualResponses.get(0));

            InOrder inOrder = inOrder(deckRepository, cardRepository, cardMapper);
            inOrder.verify(deckRepository).existsById(DECK_ID);
            inOrder.verify(cardRepository).findByDeckIdAndNextReviewDateLessThanEqual(eq(DECK_ID), any(LocalDate.class), eq(limit));
            inOrder.verify(cardMapper).mapToCardResponse(card);
        }
    }

    @Nested
    class UpdateSm {

        @Test
        void updateSm_ShouldReturnSm2UpdateResponse_WhenCardExists() {

            Sm2UpdateRequest request = testSm2Request();

            when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(card));
            when(cardRepository.save(card)).thenReturn(card);
            when(internalCallMapper.mapToSm2Response(card)).thenReturn(sm2UpdateResponse);

            Sm2UpdateResponse actualResponse = internalCallsService.updateSm(CARD_ID, request);

            assertNotNull(actualResponse);
            assertEquals(sm2UpdateResponse.getEaseFactor(), actualResponse.getEaseFactor());
            assertEquals(sm2UpdateResponse.getInterval(), actualResponse.getInterval());

            InOrder inOrder = inOrder(cardRepository, internalCallMapper);
            inOrder.verify(cardRepository).findById(CARD_ID);
            inOrder.verify(cardRepository).save(card);
            inOrder.verify(internalCallMapper).mapToSm2Response(card);
        }
    }
}
