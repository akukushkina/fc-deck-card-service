package com.platform.deck.card.service.controller;

import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.response.DeckResponse;
import com.platform.deck.card.service.service.DeckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api/v1/decks")
public class DeckController extends BaseApiController{

    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @PostMapping("/deck/create")
    public ResponseEntity<DeckResponse> createDeck(@RequestBody DeckRequest deckRequest, @RequestHeader("X-User-Id") String userId){
        log.info("Entered into create deck method");

        DeckResponse response = deckService.createDeck(deckRequest, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
