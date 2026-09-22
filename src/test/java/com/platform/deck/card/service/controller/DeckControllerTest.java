package com.platform.deck.card.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.request.DeckUpdateRequest;
import com.platform.deck.card.service.model.response.DeckResponse;
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

@WebMvcTest(DeckController.class)
public class DeckControllerTest {

    @MockBean
    private DeckService deckService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    public static String DECK_URL = "/api/v1/decks";

    @Nested
    class CreateDeck {

        @Test
        void CreateDeck_ShoudReturnSuccessWhenDeckCreated() throws Exception {
            when(deckService.createDeck(testDeckRequest(), USER_ID))
                    .thenReturn(testDeckResponse());

            mockMvc.perform(
                    post(DECK_URL)
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testDeckRequest()))
            )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("German"))
                    .andExpect(jsonPath("$.description").value(""));
        }

    @Test
    void CreateDeck_ShoudReturnBadRequestWhenNameMissed() throws Exception {
        DeckRequest request = new DeckRequest("", "German Verbs");

        mockMvc.perform(
                        post(DECK_URL)
                                .header("X-User-Id", USER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Name is required"));
    }

    @Test
    void CreateDeck_ShoudReturnBadRequestWhenNameIsMoreThan100Char() throws Exception {
        DeckRequest request = new DeckRequest("v".repeat(120), "German Verbs");

        mockMvc.perform(
                        post(DECK_URL)
                                .header("X-User-Id", USER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Deck name must be 100 characters or less"));
    }

    @Test
    void CreateDeck_ShoudReturnBadRequestWhenDescriptionIsMoreThan500Char() throws Exception {
        DeckRequest request = new DeckRequest("v", "German Verbs".repeat(500));

        mockMvc.perform(
                        post(DECK_URL)
                                .header("X-User-Id", USER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Description must be 500 characters or less"));
    }

    @Test
    void CreateDeck_ShoudReturnBadRequestWhenHeaderIsMissing() throws Exception {
        DeckRequest request = new DeckRequest("German", "German Verbs");

        mockMvc.perform(
                        post(DECK_URL)
                                .header("X-User-Id", "")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("X-User-Id must be a valid numeric MySQL ID"));
    }
    }


    @Nested
    class ListDecks {

        @Test
        void ListDecks_ShouldReturnSuccessWhenCorrectUserId() throws Exception{
            List<DeckResponse> deckResponseList = List.of(testDeckResponse());
            PagedResponse<DeckResponse> pagedResponse = testPagedResponse(deckResponseList);

            when(deckService.getDecks(USER_ID, 1, 10))
                    .thenReturn(pagedResponse);

            mockMvc.perform(
                    get(DECK_URL)
                            .header("X-User-Id", USER_ID)
                            .param("page", "1")
                            .param("size", "10")
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray());
        }

        @Test
        void ListDecks_ShouldReturnSanitizedWithDefaultPageSizeisZero() throws Exception {
            List<DeckResponse> deckResponseList = List.of(testDeckResponse());
            PagedResponse<DeckResponse> pagedResponse = testPagedResponse(deckResponseList);

            when(deckService.getDecks(USER_ID, 0, 20))
                    .thenReturn(pagedResponse);

            mockMvc.perform(
                            get(DECK_URL)
                                    .header("X-User-Id", USER_ID)
                                    .param("page", "0")
                                    .param("size", "300")
                    )
                    .andExpect(status().isOk());

            verify(deckService).getDecks(USER_ID, 0, 20);
        }

        @Test
        void ListDecks_ShouldReturnSanitizedWithDefaultPageSizeIsMinusZero() throws Exception {
            List<DeckResponse> deckResponseList = List.of(testDeckResponse());
            PagedResponse<DeckResponse> pagedResponse = testPagedResponse(deckResponseList);

            when(deckService.getDecks(USER_ID, 0, 20))
                    .thenReturn(pagedResponse);

            mockMvc.perform(
                            get(DECK_URL)
                                    .header("X-User-Id", USER_ID)
                                    .param("page", "-1")
                                    .param("size", "300")
                    )
                    .andExpect(status().isOk());

            verify(deckService).getDecks(USER_ID, 0, 20);
        }

    }

    @Nested
    class GetDeck {
        @Test
        void GetDeck_ShouldReturnSuccess() throws Exception {
            when(deckService.showDeck(USER_ID, DECK_ID))
                    .thenReturn(testDeckResponse());

            mockMvc.perform(
                    get(DECK_URL + "/" + DECK_ID)
                            .header("X-User-Id", USER_ID)
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(testDeckResponse().name()))
                    .andExpect(jsonPath("$.description").value(testDeckResponse().description()));
        }
    }

    @Nested
    class UpdateDeck{
        @Test
        void UpdateDeck_ShouldReturnSuccessWhenNameUpdated() throws Exception {
            DeckUpdateRequest request = new DeckUpdateRequest("Language", "");
            DeckResponse response = DeckResponse.builder()
                    .id("145")
                    .ownerId("567")
                    .name("Language")
                    .description("")
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            when(deckService.updateDeck(USER_ID, DECK_ID, request))
                    .thenReturn(response);

            mockMvc.perform(
                    patch(DECK_URL + "/" + DECK_ID)
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Language"))
                    .andExpect(jsonPath("$.description").value(""));

        }

        @Test
        void UpdateDeck_ShouldReturnSuccessWhenDescriptionUpdated() throws Exception {
            DeckUpdateRequest request = new DeckUpdateRequest("", "Language");
            DeckResponse response = DeckResponse.builder()
                    .id("145")
                    .ownerId("567")
                    .name("German")
                    .description("Language")
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            when(deckService.updateDeck(USER_ID, DECK_ID, request))
                    .thenReturn(response);

            mockMvc.perform(
                            patch(DECK_URL + "/" + DECK_ID)
                                    .header("X-User-Id", USER_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("German"))
                    .andExpect(jsonPath("$.description").value("Language"));

        }

        @Test
        void UpdateDeck_ShouldReturnSuccessWhenNameAndDescriptionUpdated() throws Exception {
            DeckUpdateRequest request = new DeckUpdateRequest("English", "Verbs");
            DeckResponse response = DeckResponse.builder()
                    .id("145")
                    .ownerId("567")
                    .name("English")
                    .description("Verbs")
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            when(deckService.updateDeck(USER_ID, DECK_ID, request))
                    .thenReturn(response);

            mockMvc.perform(
                            patch(DECK_URL + "/" + DECK_ID)
                                    .header("X-User-Id", USER_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("English"))
                    .andExpect(jsonPath("$.description").value("Verbs"));

        }

        @Test
        void UpdateDeck_ShouldReturnSuccessWhenNothingUpdated() throws Exception {
            DeckUpdateRequest request = new DeckUpdateRequest("", "");

            when(deckService.updateDeck(USER_ID, DECK_ID, request))
                    .thenReturn(testDeckResponse());

            mockMvc.perform(
                            patch(DECK_URL + "/" + DECK_ID)
                                    .header("X-User-Id", USER_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(testDeckResponse().name()))
                    .andExpect(jsonPath("$.description").value(testDeckResponse().description()));

        }
    }
//    DeleteDeck — happy path → 204
    @Nested
    class DeleteDeck {
        @Test
        void DeleteDeck_ShouldReturnNoContentWhenDeckDeleted() throws Exception {
            doNothing().when(deckService).deleteDeck(USER_ID, DECK_ID);

            mockMvc.perform(
                    delete(DECK_URL + "/" + DECK_ID)
                            .header("X-User-Id", USER_ID)
            )
                    .andExpect(status().isNoContent());
        }
}
}
