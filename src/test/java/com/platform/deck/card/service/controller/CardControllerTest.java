package com.platform.deck.card.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.deck.card.service.model.request.CardRequest;
import com.platform.deck.card.service.model.request.CardUpdateRequest;
import com.platform.deck.card.service.model.response.CardResponse;
import com.platform.deck.card.service.model.response.PagedResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static com.platform.deck.card.service.util.TestUtil.*;
import static org.mockito.Mockito.*; 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
public class CardControllerTest {

    @MockBean
    private CardService cardService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Nested
    class CreateCard {

        @Test
        void CreateCard_ShouldReturnCreatedIfSuccess() throws Exception {

            when(cardService.addCard(USER_ID, DECK_ID, testCardRequest()))
                    .thenReturn(testCardResponse());

            mockMvc.perform(
                            post(BASE_URL)
                                    .header("X-User-Id", USER_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(testCardRequest()))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.front").value("Das Hund"))
                    .andExpect(jsonPath("$.back").value("A dog"));
        }


        @Test
        void CreateCard_ShouldReturnBadRequestWhenFrontIsNissing() throws Exception {
            CardRequest request = new CardRequest("", "A dog");

            mockMvc.perform(
                            post(BASE_URL)
                                    .header("X-User-Id", USER_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("Front content is required"));

        }

        @Test
        void CreateCard_ShouldReturnBadRequestWhenFrontIsMoreThan1000Char() throws Exception {
            CardRequest request = new CardRequest("r".repeat(1020), "A dog");

            mockMvc.perform(
                            post(BASE_URL)
                                    .header("X-User-Id", USER_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("Front content must be 1000 characters or less"));
        }


        @Test
        void CreateCard_ShouldReturnBadRequestWhenBackIsNissing() throws Exception {
            CardRequest request = new CardRequest("Das Hund", "");

            mockMvc.perform(
                            post(BASE_URL)
                                    .header("X-User-Id", USER_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("Back content is required"));

        }

        @Test
        void CreateCard_ShouldReturnBadRequestWhenBackIsMoreThan2000Char() throws Exception {
            CardRequest request = new CardRequest("Das Hound", "r".repeat(2019));

            mockMvc.perform(
                            post(BASE_URL)
                                    .header("X-User-Id", USER_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("Back content must be 2000 characters or less"));
        }

        @Test
        void createCard_ShouldReturnBadRequestWhenUserHeaderIsMissing() throws Exception {
            mockMvc.perform(
                            post(BASE_URL)
                                    .header("X-User-Id", "")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(testCardRequest()))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("X-User-Id must be a valid numeric MySQL ID"));
        }

    }

    @Nested
    public class ListCards {

        @Test
        void ListCards_ShouldReturnSuccess() throws Exception {
            List<CardResponse> cardResponseList = List.of(testCardResponse());
            PagedResponse<CardResponse> pagedResponse = testPagedResponse(cardResponseList);

            when(cardService.getCards(USER_ID, DECK_ID, 1, 10))
                    .thenReturn(pagedResponse);

            mockMvc.perform(
                            get(BASE_URL, DECK_ID)
                                    .header("X-User-Id", USER_ID)
                                    .param("page", "1")
                                    .param("size", "10")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray());
        }

        @Test
        void ListCards_ShouldReturnSanitizedWithDefaultPageSizeisZero() throws Exception {
            List<CardResponse> cardResponseList = List.of(testCardResponse());
            PagedResponse<CardResponse> pagedResponse = testPagedResponse(cardResponseList);

            when(cardService.getCards(USER_ID, DECK_ID, 0, 20))
                    .thenReturn(pagedResponse);

            mockMvc.perform(
                    get(BASE_URL, DECK_ID)
                            .header("X-User-Id", USER_ID)
                            .param("page", "0")
                            .param("size", "300")
            )
                    .andExpect(status().isOk());

            verify(cardService).getCards(USER_ID, DECK_ID, 0, 20);
        }

        @Test
        void ListCards_ShouldReturnSanitizedWithDefaultPageSizeisMinusZero() throws Exception {
            List<CardResponse> cardResponseList = List.of(testCardResponse());
            PagedResponse<CardResponse> pagedResponse = testPagedResponse(cardResponseList);

            when(cardService.getCards(USER_ID, DECK_ID, 0, 20))
                    .thenReturn(pagedResponse);

            mockMvc.perform(
                            get(BASE_URL, DECK_ID)
                                    .header("X-User-Id", USER_ID)
                                    .param("page", "-1")
                                    .param("size", "300")
                    )
                    .andExpect(status().isOk());

            verify(cardService).getCards(USER_ID, DECK_ID, 0, 20);
        }
    }

    @Nested
    public class GetCard{

        @Test
        void GetCard_ShoudReturnSuccessWhenCardIsPresent() throws Exception {

            when(cardService.fetchCard(USER_ID, DECK_ID, CARD_ID))
                    .thenReturn(testCardResponse());

            mockMvc.perform(
                    get(BASE_URL + "/" + CARD_ID, DECK_ID)
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.front").value("Das Hund"))
                    .andExpect(jsonPath("$.back").value("A dog"));
        }
    }

    @Nested
    public class UpdateCard {
        @Test
        void UpdateCard_ShouldReturnSuccessWhenFrontUpdated() throws Exception {
            CardUpdateRequest request = new CardUpdateRequest("Das Vase", "");
            CardResponse response = CardResponse.builder()
                    .id(CARD_ID)
                    .deckId(DECK_ID)
                    .front("Das Vase")
                    .back("A dog")
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            when(cardService.updateCard(USER_ID, DECK_ID, CARD_ID, request))
                    .thenReturn(response);

            mockMvc.perform(
                    patch(BASE_URL + "/" + CARD_ID, DECK_ID)
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.front").value("Das Vase"));
        }

        @Test
        void UpdateCard_ShouldReturnSuccessWhenBackUpdated() throws Exception {
            CardUpdateRequest request = new CardUpdateRequest("", "A cat");
            CardResponse response = CardResponse.builder()
                    .id(CARD_ID)
                    .deckId(DECK_ID)
                    .front("Das Hund")
                    .back("A cat")
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            when(cardService.updateCard(USER_ID, DECK_ID, CARD_ID, request))
                    .thenReturn(response);

            mockMvc.perform(
                            patch(BASE_URL + "/" + CARD_ID, DECK_ID)
                                    .header("X-User-Id", USER_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.back").value("A cat"));
        }

        @Test
        void UpdateCard_ShouldReturnSuccessWhenNothingUpdated() throws Exception {
            CardUpdateRequest request = new CardUpdateRequest("", "");

            when(cardService.updateCard(USER_ID, DECK_ID, CARD_ID, request))
                    .thenReturn(testCardResponse());

            mockMvc.perform(
                            patch(BASE_URL + "/" + CARD_ID, DECK_ID)
                                    .header("X-User-Id", USER_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.front").value("Das Hund"))
                    .andExpect(jsonPath("$.back").value("A dog"));
        }
    }

    @Nested
    public class DeleteCard {
        @Test
        void DeleteCard_ShouldReturnNoContentWhenCardDeleted() throws Exception {
            doNothing().when(cardService).deleteCard(USER_ID, DECK_ID, CARD_ID);

            mockMvc.perform(
                    delete(BASE_URL + "/" + CARD_ID, DECK_ID)
                            .header("X-User-Id", USER_ID)
            )
                    .andExpect(status().isNoContent());

        }
    }
}
