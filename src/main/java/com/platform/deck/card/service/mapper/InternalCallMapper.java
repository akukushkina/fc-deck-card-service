package com.platform.deck.card.service.mapper;

import com.platform.deck.card.service.entity.Card;
import com.platform.deck.card.service.entity.Deck;
import com.platform.deck.card.service.model.internal.OwnerResponse;
import com.platform.deck.card.service.model.internal.Sm2UpdateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InternalCallMapper {
    @Mapping(source = "id", target = "deckId")
    @Mapping(source = "ownerId", target = "userId")
    OwnerResponse mapToInternalResponse(Deck deck);

    Sm2UpdateResponse mapToSm2Response(Card card);
}
