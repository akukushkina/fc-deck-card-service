package com.platform.deck.card.service.controller;

import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.request.DeckUpdateRequest;
import com.platform.deck.card.service.model.response.DeckResponse;
import com.platform.deck.card.service.model.response.PagedResponse;
import com.platform.deck.card.service.service.DeckService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @PostMapping()
    public ResponseEntity<DeckResponse> createDeck(@Valid @RequestBody DeckRequest deckRequest, @RequestHeader("X-User-Id") String userId){
        log.info("Entered into create deck method");

        DeckResponse response = deckService.createDeck(deckRequest, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<PagedResponse<DeckResponse>> listDecks(@RequestHeader("X-User-Id") String userId, @PageableDefault(page = 0, size = 20) Pageable pageable){
        log.info("Entered into ListDecks method");

        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        if(page < 0 ) {
            page = 0;
        }
        if(pageSize < 1 || pageSize > 100) {
            pageSize = 20;
        }

        PagedResponse<DeckResponse> response = deckService.getDecks(userId, page, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{deckId}")
    public ResponseEntity<DeckResponse> getDeck(@RequestHeader("X-User-Id") String userId, @PathVariable("deckId") String deckId){
        log.info("Entering into get deck by id method");

        DeckResponse response = deckService.showDeck(userId, deckId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{deckId}")
    public ResponseEntity<DeckResponse> updateDeck(@RequestHeader("X-User-Id") String userId,
                                                   @PathVariable("deckId") String deckId,
                                                   @RequestBody @Valid DeckUpdateRequest request){
        log.info("Entering into update deck by id method");

        DeckResponse response = deckService.updateDeck(userId, deckId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<Void> deleteDeck(@RequestHeader("X-User-Id") String userId,
                                             @PathVariable("deckId") String deckId){
        log.info("Entered into delete deck method");

        deckService.deleteDeck(userId, deckId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
