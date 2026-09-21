package com.platform.deck.card.service.util;

import com.platform.deck.card.service.model.internal.OwnerResponse;
import com.platform.deck.card.service.model.internal.Sm2UpdateRequest;
import com.platform.deck.card.service.model.internal.Sm2UpdateResponse;
import com.platform.deck.card.service.model.request.CardRequest;
import com.platform.deck.card.service.model.request.CardUpdateRequest;
import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.request.DeckUpdateRequest;
import com.platform.deck.card.service.model.response.CardResponse;
import com.platform.deck.card.service.model.response.DeckResponse;
import com.platform.deck.card.service.model.response.PagedResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class TestUtil {

   public static final String DECK_ID = "deck-123";
   public static final String CARD_ID = "card-456";
   public static final String USER_ID = "789";
   public static final String BASE_URL = "/api/v1/decks/" + DECK_ID + "/cards";

    public static OwnerResponse testOwnerResponse(){
        return OwnerResponse.builder()
                .deckId("123")
                .userId("456")
                .build();
    }

    public static Sm2UpdateRequest testSm2Request(){
        return Sm2UpdateRequest.builder()
                .easeFactor(2.5f)
                .interval(1)
                .nextReviewDate(LocalDate.now())
                .build();
    }

    public static Sm2UpdateResponse testSm2Response(){
        return Sm2UpdateResponse.builder()
                .easeFactor(2.5f)
                .interval(1)
                .nextReviewDate(LocalDate.now())
                .build();
    }

    public static CardRequest testCardRequest(){
        return CardRequest.builder()
                .front("Das Hund")
                .back("A dog")
                .build();
    }

    public static CardUpdateRequest testCardUpdateRequest(){
        return CardUpdateRequest.builder()
                .front("Das Hund")
                .back("A dog")
                .build();
    }

    public static DeckRequest testDeckRequest(){
        return DeckRequest.builder()
                .name("German")
                .description("")
                .build();
    }

    public static DeckUpdateRequest testDeckUpdateRequest(){
        return DeckUpdateRequest.builder()
                .name("")
                .description("Language cards")
                .build();
    }

    public static CardResponse testCardResponse(){
        return CardResponse.builder()
                .id("1239")
                .deckId("145")
                .ownerId("768")
                .front("Das Hund")
                .back("A dog")
                .easeFactor(2.5f)
                .interval(1)
                .nextReviewDate(LocalDate.now())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    public static DeckResponse testDeckResponse(){
        return DeckResponse.builder()
                .id("145")
                .ownerId("567")
                .name("German")
                .description("")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    public static <T> PagedResponse<T> testPagedResponse(List<T> content) {
        int size = content.size();
        return PagedResponse.<T>builder()
                .content(content)
                .page(0)
                .pageSize(size == 0 ? 20 : size)
                .totalElements(size)
                .totalPages(1)
                .build();
    }

}
