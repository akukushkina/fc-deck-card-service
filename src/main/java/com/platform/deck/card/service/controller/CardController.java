package com.platform.deck.card.service.controller;

import com.platform.deck.card.service.model.request.CardRequest;
import com.platform.deck.card.service.model.request.CardUpdateRequest;
import com.platform.deck.card.service.model.response.CardResponse;
import com.platform.deck.card.service.model.response.PagedResponse;
import com.platform.deck.card.service.service.CardService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping()
    public ResponseEntity<PagedResponse<CardResponse>> listCards(@RequestHeader("X-User-Id") String userId,
                                                                 @PathVariable("deckId") String deckId,
                                                                 @PageableDefault(page = 0, size = 200)Pageable pageable
                                                                 ){
        log.info("Entering into list cards method");

        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        if(page < 0){
            page = 0;
        }
        if(pageSize < 200){
            pageSize = 200;
        }

        PagedResponse<CardResponse> response = cardService.getCards(userId, deckId, page, pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<CardResponse> updateCard(@RequestHeader("X-User-Id") String userId,
                                                   @PathVariable("deckId") String deckId,
                                                   @PathVariable("cardId") String cardId,
                                                   @RequestBody CardUpdateRequest request){
        log.info("Entered into update card method");

        CardResponse response = cardService.updateCard(userId, deckId, cardId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
