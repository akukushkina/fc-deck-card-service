package com.platform.deck.card.service.repository;

import com.platform.deck.card.service.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CardRepository extends MongoRepository<Card, String> {

    Page<Card> findByDeckIdOrderByCreatedAtAsc(String deckId, Pageable pageable);

    void deleteByDeckId(String deckId);

    List<Card> findByDeckIdAndNextReviewDateLessThanEqual(String deckId, LocalDate date);

    Page<Card> findByDeckIdAndOwnerIdOrderByCreatedAtDesc(String deckId, String userId, Pageable sanitizedPageable);
}
