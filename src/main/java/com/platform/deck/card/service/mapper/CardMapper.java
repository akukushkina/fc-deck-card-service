package com.platform.deck.card.service.mapper;

import com.platform.deck.card.service.entity.Card;
import com.platform.deck.card.service.model.request.CardRequest;
import com.platform.deck.card.service.model.response.CardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(target = "front", source = "request.front")
    @Mapping(target = "back", source = "request.back")
    @Mapping(target = "deckId", source = "deckId")
    @Mapping(target = "ownerId", source = "userId")
    @Mapping(target = "easeFactor", ignore = true)
    @Mapping(target = "interval", ignore = true)
    @Mapping(target = "nextReviewDate", ignore = true)
    Card mapToCard(CardRequest request, String deckId, String userId);

    CardResponse mapToCardResponse(Card newCard);
}
