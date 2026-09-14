package com.platform.deck.card.service.repository;

import com.platform.deck.card.service.entity.Deck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeckRepository extends MongoRepository<Deck, String> {

    Page<Deck> findByOwnerIdOrderByCreatedAtDesc(String ownerId, Pageable pageable);


    Deck findByIdAndOwnerId(String userId, String deckId);
}
