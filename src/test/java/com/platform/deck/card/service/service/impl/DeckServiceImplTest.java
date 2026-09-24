package com.platform.deck.card.service.service.impl;

import com.platform.deck.card.service.entity.Deck;
import com.platform.deck.card.service.mapper.DeckMapper;
import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.request.DeckUpdateRequest;
import com.platform.deck.card.service.model.response.DeckResponse;
import com.platform.deck.card.service.model.response.PagedResponse;
import com.platform.deck.card.service.repository.DeckRepository;
import com.platform.deck.card.service.service.CardService;
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
public class DeckServiceImplTest {

    @Mock
    private DeckMapper deckMapper;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private CardService cardService;

    @Mock
    private ServiceValidation serviceValidation;

    @InjectMocks
    private DeckServiceImpl deckService;

    private Deck deck;
    private DeckResponse deckResponse;

    @BeforeEach
    void setUp() {
        deck = testDeck();
        deckResponse = testDeckResponse();
    }

    @Nested
    class CreateDeck {

        @Test
        void createDeck_ShouldReturnDeckResponse_WhenSuccessful() {

            DeckRequest request = testDeckRequest();

            when(deckMapper.mapToDeck(request, USER_ID)).thenReturn(deck);
            when(deckRepository.save(deck)).thenReturn(deck);
            when(deckMapper.mapToDeckResponse(deck)).thenReturn(deckResponse);

            DeckResponse actualResponse = deckService.createDeck(request, USER_ID);

            assertNotNull(actualResponse);
            assertEquals(deckResponse.id(), actualResponse.id());
            assertEquals(deckResponse.name(), actualResponse.name());

            InOrder inOrder = inOrder(deckMapper, deckRepository);
            inOrder.verify(deckMapper).mapToDeck(request, USER_ID);
            inOrder.verify(deckRepository).save(deck);
            inOrder.verify(deckMapper).mapToDeckResponse(deck);
        }
    }

    @Nested
    class GetDecks {

        @Test
        void getDecks_ShouldReturnPagedResponse_WhenSuccessful() {

            int page = 0;
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page, pageSize);

            List<Deck> deckList = List.of(deck);
            Page<Deck> deckPage = new PageImpl<>(deckList, pageable, 1);

            when(deckRepository.findByOwnerIdOrderByCreatedAtDesc(USER_ID, pageable))
                    .thenReturn(deckPage);
            when(deckMapper.mapToDeckResponse(deck)).thenReturn(deckResponse);

            PagedResponse<DeckResponse> actualResponse = deckService.getDecks(USER_ID, page, pageSize);

            assertNotNull(actualResponse);
            assertEquals(1, actualResponse.getContent().size());
            assertEquals(deckResponse, actualResponse.getContent().get(0));
            assertEquals(0, actualResponse.getPage());
            assertEquals(10, actualResponse.getPageSize());
            assertEquals(1, actualResponse.getTotalElements());
            assertEquals(1, actualResponse.getTotalPages());

            InOrder inOrder = inOrder(deckRepository, deckMapper);
            inOrder.verify(deckRepository).findByOwnerIdOrderByCreatedAtDesc(USER_ID, pageable);
            inOrder.verify(deckMapper).mapToDeckResponse(deck);
        }
    }

    @Nested
    class ShowDeck {

        @Test
        void showDeck_ShouldReturnDeckResponse_WhenSuccessful() {

            when(serviceValidation.validateDeckOwnership(DECK_ID, USER_ID)).thenReturn(deck);
            when(deckMapper.mapToDeckResponse(deck)).thenReturn(deckResponse);

            DeckResponse actualResponse = deckService.showDeck(USER_ID, DECK_ID);

            assertNotNull(actualResponse);
            assertEquals(deckResponse.id(), actualResponse.id());

            InOrder inOrder = inOrder(serviceValidation, deckMapper);
            inOrder.verify(serviceValidation).validateDeckOwnership(DECK_ID, USER_ID);
            inOrder.verify(deckMapper).mapToDeckResponse(deck);
        }
    }

    @Nested
    class UpdateDeck {

        @Test
        void updateDeck_ShouldReturnUpdatedDeckResponse_WhenSuccessful() {

            DeckUpdateRequest request = testDeckUpdateRequest();

            DeckResponse updatedResponse = DeckResponse.builder()
                    .id("145")
                    .ownerId("567")
                    .name("German Verbs")
                    .description("Language cards")
                    .build();

            when(serviceValidation.validateDeckOwnership(DECK_ID, USER_ID)).thenReturn(deck);
            when(deckRepository.save(deck)).thenReturn(deck);
            when(deckMapper.mapToDeckResponse(deck)).thenReturn(updatedResponse);

            DeckResponse actualResponse = deckService.updateDeck(USER_ID, DECK_ID, request);

            assertNotNull(actualResponse);
            assertEquals("Language cards", actualResponse.description());

            InOrder inOrder = inOrder(serviceValidation, deckRepository, deckMapper);
            inOrder.verify(serviceValidation).validateDeckOwnership(DECK_ID, USER_ID);
            inOrder.verify(deckRepository).save(deck);
            inOrder.verify(deckMapper).mapToDeckResponse(deck);
        }
    }

    @Nested
    class DeleteDeck {

        @Test
        void deleteDeck_ShouldDeleteCardsAndDeck_WhenSuccessful() {

            when(serviceValidation.validateDeckOwnership(DECK_ID, USER_ID)).thenReturn(deck);
            doNothing().when(cardService).deleteCardsFromDeck(DECK_ID);
            doNothing().when(deckRepository).deleteById(DECK_ID);

            deckService.deleteDeck(USER_ID, DECK_ID);

            InOrder inOrder = inOrder(serviceValidation, cardService, deckRepository);
            inOrder.verify(serviceValidation).validateDeckOwnership(DECK_ID, USER_ID);
            inOrder.verify(cardService).deleteCardsFromDeck(DECK_ID);
            inOrder.verify(deckRepository).deleteById(DECK_ID);
        }
    }
}
