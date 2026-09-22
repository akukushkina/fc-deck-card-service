package com.platform.deck.card.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.deck.card.service.model.response.CardResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.platform.deck.card.service.util.TestUtil.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InternalCallsController.class)
public class InternalCallsControllerTest {

    @MockBean
    InternalCallsService internalCallsService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    public static String INTER_URL = "/internal/decks/" + DECK_ID ;

    @Nested
    class DeckOwner {
        @Test
        void DeckOwner_ShouldReturnSuccess() throws Exception {
            when(internalCallsService.getOwner(DECK_ID))
                    .thenReturn(testOwnerResponse());

            mockMvc.perform(
                    get(INTER_URL + "/owner")
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(testOwnerResponse().getUserId()));
        }
    }

    @Nested
    class DueCards {
        @Test
        void DueCards_ShouldReturnDueCards() throws Exception {
            int limit = 10;

            List<CardResponse> cardResponseList = List.of(testCardResponse());

            when(internalCallsService.listDueCards(DECK_ID, 10))
                    .thenReturn(cardResponseList);

            mockMvc.perform(
                    get(INTER_URL + "/due-cards")
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }
    }

    @Nested
    class UpdateSm2 {
        @Test
        void UpdateSm2_ShouldUpdateSm2() throws Exception {

            when(internalCallsService.updateSm(CARD_ID, testSm2Request()))
                    .thenReturn(testSm2Response());

            mockMvc.perform(
                    patch("/internal/cards/" + CARD_ID + "/sm2")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testCardResponse()))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.easeFactor").value(testSm2Response().getEaseFactor()));        }
    }
}
