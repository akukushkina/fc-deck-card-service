package com.platform.deck.card.service.mapper;

import com.platform.deck.card.service.entity.Deck;
import com.platform.deck.card.service.model.request.DeckRequest;
import com.platform.deck.card.service.model.response.DeckResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeckMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "ownerId")
    @Mapping(source = "deckRequest.name", target = "name")
    @Mapping(source = "deckRequest.description", target = "description")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Deck mapToDeck(DeckRequest deckRequest, String userId);

    DeckResponse mapToDeckResponse(Deck newDeck);
}
