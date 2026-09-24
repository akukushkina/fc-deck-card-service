package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.entity.Card;
import com.platform.deck.card.service.mapper.CardMapper;
import com.platform.deck.card.service.model.request.CardRequest;
import com.platform.deck.card.service.model.request.CardUpdateRequest;
import com.platform.deck.card.service.model.response.CardResponse;
import com.platform.deck.card.service.model.response.PagedResponse;
import com.platform.deck.card.service.repository.CardRepository;
import com.platform.deck.card.service.service.ServiceValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.platform.deck.card.service.util.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {

    @Mock
    CardRepository cardRepository;

    @Mock
    CardMapper cardMapper;

    @Mock
    ServiceValidation serviceValidation;

    @InjectMocks
    CardServiceImpl cardService;

    private CardRequest request;
    private Card card;
    private CardResponse expectedResponse;

    @BeforeEach
    void setUp() {
        request = testCardRequest();
        card = testCard();
        expectedResponse = testCardResponse();
    }

    @Nested
    class AddCard {

        @Test
        void AddCard_ShouldReturnSuccessWhenCardAdded() {
            when(cardMapper.mapToCard(request, DECK_ID, USER_ID)).thenReturn(card);
            when(cardRepository.save(card)).thenReturn(card);
            when(cardMapper.mapToCardResponse(card)).thenReturn(testCardResponse());

            CardResponse actualResponse = cardService.addCard(USER_ID, DECK_ID, request);

            assertNotNull(actualResponse);
            assertEquals(expectedResponse.id(), actualResponse.id());
            assertEquals(expectedResponse.front(), actualResponse.front());

            InOrder inOrder = inOrder(serviceValidation, cardMapper, cardRepository);
            inOrder.verify(serviceValidation).validateDeckOwnership(DECK_ID, USER_ID);
            inOrder.verify(cardMapper).mapToCard(request, DECK_ID, USER_ID);
            inOrder.verify(cardRepository).save(card);
            inOrder.verify(cardMapper).mapToCardResponse(card);
        }
    }

    @Nested
    class GetCards {
        @Test
        void GetCards_ShouldReturnSuccessWhenCardsFound() {
            int page = 0;
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page, pageSize);

            List<Card> cardList = List.of(card);
            Page<Card> cardPage = new PageImpl<>(cardList, pageable, 1);

            when(cardRepository.findByDeckIdAndOwnerIdOrderByCreatedAtDesc(DECK_ID, USER_ID, pageable))
                    .thenReturn(cardPage);

            when(cardMapper.mapToCardResponse(card)).thenReturn(expectedResponse);

            PagedResponse<CardResponse> actualResponse = cardService.getCards(USER_ID, DECK_ID, page, pageSize);

            assertNotNull(actualResponse);
            assertEquals(1, actualResponse.getContent().size());
            assertEquals(expectedResponse, actualResponse.getContent().get(0));
            assertEquals(0, actualResponse.getPage());
            assertEquals(10, actualResponse.getPageSize());
            assertEquals(1, actualResponse.getTotalElements());
            assertEquals(1, actualResponse.getTotalPages());

            InOrder inOrder = inOrder(serviceValidation, cardRepository, cardMapper);
            inOrder.verify(serviceValidation).validateDeckOwnership(DECK_ID, USER_ID);
            inOrder.verify(cardRepository).findByDeckIdAndOwnerIdOrderByCreatedAtDesc(DECK_ID, USER_ID, pageable);
            inOrder.verify(cardMapper).mapToCardResponse(card);
        }
    }

//    fetchCard — happy path
    @Nested
    class FetchCard{
        @Test
        void FetchCard_ShouldReturnCardWithGivenId() {

            Card card = testCard();
            CardResponse cardResponse = testCardResponse();

            when(cardRepository.findByIdandDeckId(USER_ID, DECK_ID))
                    .thenReturn(card);

            when(cardMapper.mapToCardResponse(card)).thenReturn(cardResponse);

            CardResponse actualResponse = cardService.fetchCard(USER_ID, DECK_ID, CARD_ID);

            assertNotNull(actualResponse);
            assertEquals(expectedResponse.id(), cardResponse.id());
            assertEquals(expectedResponse.front(), cardResponse.front());

            InOrder inOrder = inOrder(serviceValidation, cardRepository, cardMapper);
            inOrder.verify(serviceValidation).validateDeckOwnership(DECK_ID, USER_ID);
            inOrder.verify(cardRepository).findByIdandDeckId(USER_ID, DECK_ID);
            inOrder.verify(cardMapper).mapToCardResponse(card);
        }
}

    @Nested
    class UpdateCard {

        @Test
        void updateCard_ShouldReturnUpdatedCardResponse_WhenSuccessful() {

            CardUpdateRequest request = new CardUpdateRequest("Das Vase", "A flower holder");

            CardResponse updatedResponse = CardResponse.builder()
                    .id(CARD_ID)
                    .deckId(DECK_ID)
                    .front("Das Vase")
                    .back("A flower holder")
                    .build();

            when(serviceValidation.validateCardOwnership(CARD_ID, USER_ID)).thenReturn(card);
            when(cardRepository.save(card)).thenReturn(card);
            when(cardMapper.mapToCardResponse(card)).thenReturn(updatedResponse);

            CardResponse actualResponse = cardService.updateCard(USER_ID, DECK_ID, CARD_ID, request);

            assertNotNull(actualResponse);
            assertEquals("Das Vase", actualResponse.front());
            assertEquals("A flower holder", actualResponse.back());

            InOrder inOrder = inOrder(serviceValidation, cardRepository, cardMapper);
            inOrder.verify(serviceValidation).validateDeckOwnership(DECK_ID, USER_ID);
            inOrder.verify(serviceValidation).validateCardOwnership(CARD_ID, USER_ID);
            inOrder.verify(cardRepository).save(card);
            inOrder.verify(cardMapper).mapToCardResponse(card);
        }
    }

    @Nested
    class DeleteCard {

        @Test
        void deleteCard_ShouldDeleteCard_WhenSuccessful() {
            when(serviceValidation.validateCardOwnership(CARD_ID, USER_ID)).thenReturn(card);
            doNothing().when(cardRepository).deleteById(CARD_ID);

            // When
            cardService.deleteCard(USER_ID, DECK_ID, CARD_ID);

            // Then
            InOrder inOrder = inOrder(serviceValidation, cardRepository);
            inOrder.verify(serviceValidation).validateDeckOwnership(DECK_ID, USER_ID);
            inOrder.verify(serviceValidation).validateCardOwnership(CARD_ID, USER_ID);
            inOrder.verify(cardRepository).deleteById(CARD_ID);
        }
    }

    @Nested
    class DeleteCardsFromDeck {

        @Test
        void deleteCardsFromDeck_ShouldDeleteAllCards_WhenSuccessful() {

            doNothing().when(cardRepository).deleteByDeckId(DECK_ID);

            cardService.deleteCardsFromDeck(DECK_ID);

            verify(cardRepository, times(1)).deleteByDeckId(DECK_ID);
        }
    }
}
