package com.platform.deck.card.service.controller;

import com.platform.deck.card.service.model.internal.OwnerResponse;
import com.platform.deck.card.service.model.internal.Sm2UpdateRequest;
import com.platform.deck.card.service.model.internal.Sm2UpdateResponse;
import com.platform.deck.card.service.model.response.CardResponse;
import com.platform.deck.card.service.service.InternalCallsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/internal")
public class InternalCallsController {

    private final InternalCallsService internalCallsService;

    public InternalCallsController(InternalCallsService internalCallsService) {
        this.internalCallsService = internalCallsService;
    }


    @GetMapping("/decks/{deckId}/owner")
    public ResponseEntity<OwnerResponse> getDeckOwner(@PathVariable("deckId") String deckId){
        log.info("Entered into getDeckOwner method");

        OwnerResponse response = internalCallsService.getOwner(deckId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/decks/{deckId}/due-cards")
    public ResponseEntity<List<CardResponse>> getDueCards(@PathVariable("deckId") String deckId,
                                                          @RequestParam(defaultValue = "50") int limit){
        log.info("Entered into getDueCards method");

        if (limit < 1 || limit > 50) limit = 50;

        List<CardResponse> response = internalCallsService.listDueCards(deckId, limit);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/cards/{cardId}/sm2")
    public ResponseEntity<Sm2UpdateResponse> updateSm2(@PathVariable("cardId") String cardId,
                                                       @RequestBody @Valid Sm2UpdateRequest request){
        log.info("Entered into update SM2 in card {}", cardId);

        Sm2UpdateResponse response = internalCallsService.updateSm(cardId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
