package com.platform.deck.card.service.controller;

import com.platform.deck.card.service.model.request.CardRequest;
import com.platform.deck.card.service.model.response.CardResponse;
import com.platform.deck.card.service.service.CardService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/decks/{deckId}/cards")
public class CardController extends BaseApiController{

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping()
    public ResponseEntity<CardResponse> createCard(@RequestHeader("X-User-Id") String userId,
                                                   @PathVariable("deckId") String deckId,
                                                   @Valid @RequestBody CardRequest request){
        log.info("Entering into create card method. Deck id: {}", deckId);

        CardResponse response = cardService.addCard(userId, deckId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
