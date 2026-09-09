package com.platform.deck.card.service.controller;

import com.platform.deck.card.service.service.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CardController extends BaseApiController{

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
}
