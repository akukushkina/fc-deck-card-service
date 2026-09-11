package com.platform.deck.card.service.controller;

import com.platform.deck.card.service.service.InternalCallsService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InternalCallsController {

    private final InternalCallsService internalCallsService;

    public InternalCallsController(InternalCallsService internalCallsService) {
        this.internalCallsService = internalCallsService;
    }


}
