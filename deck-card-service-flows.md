# Deck-Card Service — Flow Diagrams

---

## 1. Create Deck

```mermaid
flowchart TD
    A([POST /api/v1/decks]) --> B[Extract X-User-Id header\nset as ownerId]
    B --> C{name present\nand ≤ 100 chars?}
    C -- No --> D[Return HTTP 400\nInvalid deck name]
    C -- Yes --> E{description present\nand > 500 chars?}
    E -- Yes --> F[Return HTTP 400\nDescription too long]
    E -- No --> G[Persist Deck to MongoDB\nownerId = X-User-Id]
    G --> H[Return HTTP 201\nDeck representation]
```

---

## 2. List Decks

```mermaid
flowchart TD
    A([GET /api/v1/decks]) --> B[Extract X-User-Id header]
    B --> C{page and pageSize\nparams valid?\npageSize ≤ 100}
    C -- No --> D[Use defaults\npage=0 pageSize=20]
    C -- Yes --> E[Query MongoDB\ndecks where ownerId = X-User-Id\nsorded by createdAt DESC]
    D --> E
    E --> F[Return HTTP 200\nPaginated deck list\nwith meta: page, pageSize,\ntotalElements, totalPages]
```

---

## 3. Get Deck by ID

```mermaid
flowchart TD
    A([GET /api/v1/decks/deckId]) --> B{Deck exists\nin MongoDB?}
    B -- No --> C[Return HTTP 404\nDeck not found]
    B -- Yes --> D{Deck ownerId\n== X-User-Id?}
    D -- No --> E[Return HTTP 403\nForbidden]
    D -- Yes --> F[Return HTTP 200\nDeck representation]
```

---

## 4. Update Deck

```mermaid
flowchart TD
    A([PATCH /api/v1/decks/deckId]) --> B{Deck exists\nin MongoDB?}
    B -- No --> C[Return HTTP 404\nDeck not found]
    B -- Yes --> D{Deck ownerId\n== X-User-Id?}
    D -- No --> E[Return HTTP 403\nForbidden]
    D -- Yes --> F{if name present\nname ≤ 100 chars?\nif description present\ndescription ≤ 500 chars?}
    F -- No --> G[Return HTTP 400\nValidation error]
    F -- Yes --> H[Update provided fields\nin MongoDB]
    H --> I[Return HTTP 200\nUpdated deck representation]
```

---

## 5. Delete Deck

```mermaid
flowchart TD
    A([DELETE /api/v1/decks/deckId]) --> B{Deck exists\nin MongoDB?}
    B -- No --> C[Return HTTP 404\nDeck not found]
    B -- Yes --> D{Deck ownerId\n== X-User-Id?}
    D -- No --> E[Return HTTP 403\nForbidden]
    D -- Yes --> F[Delete all cards\nwhere deckId matches]
    F --> G[Delete the Deck document]
    G --> H[Return HTTP 204\nNo content]
```

---

## 6. Create Card

```mermaid
flowchart TD
    A([POST /api/v1/decks/deckId/cards]) --> B{Deck exists\nin MongoDB?}
    B -- No --> C[Return HTTP 404\nDeck not found]
    B -- Yes --> D{Deck ownerId\n== X-User-Id?}
    D -- No --> E[Return HTTP 403\nForbidden]
    D -- Yes --> F{front non-empty\nand ≤ 1000 chars?}
    F -- No --> G[Return HTTP 400\nInvalid front content]
    F -- Yes --> H{back non-empty\nand ≤ 2000 chars?}
    H -- No --> I[Return HTTP 400\nInvalid back content]
    H -- Yes --> J[Persist Card to MongoDB\neaseFactor=2.5\ninterval=1\nnextReviewDate=today]
    J --> K[Return HTTP 201\nCard representation]
```

---

## 7. List Cards

```mermaid
flowchart TD
    A([GET /api/v1/decks/deckId/cards]) --> B{Deck exists\nin MongoDB?}
    B -- No --> C[Return HTTP 404\nDeck not found]
    B -- Yes --> D{Deck ownerId\n== X-User-Id?}
    D -- No --> E[Return HTTP 403\nForbidden]
    D -- Yes --> F{pageSize param\n≤ 200?}
    F -- No --> G[Use max pageSize=200]
    F -- Yes --> H[Query MongoDB\ncards where deckId matches\nsorted by createdAt ASC]
    G --> H
    H --> I[Return HTTP 200\nPaginated card list\nwith meta]
```

---

## 8. Update Card

```mermaid
flowchart TD
    A([PATCH /api/v1/decks/deckId/cards/cardId]) --> B{Deck exists\nin MongoDB?}
    B -- No --> C[Return HTTP 404\nDeck not found]
    B -- Yes --> D{Deck ownerId\n== X-User-Id?}
    D -- No --> E[Return HTTP 403\nForbidden]
    D -- Yes --> F{Card exists\nin MongoDB?}
    F -- No --> G[Return HTTP 404\nCard not found]
    F -- Yes --> H{Card ownerId\n== X-User-Id?}
    H -- No --> I[Return HTTP 403\nForbidden]
    H -- Yes --> J{if front present\nfront ≤ 1000 chars?\nif back present\nback ≤ 2000 chars?}
    J -- No --> K[Return HTTP 400\nValidation error]
    J -- Yes --> L[Update provided fields\nin MongoDB]
    L --> M[Return HTTP 200\nUpdated card representation]
```

---

## 9. Delete Card

```mermaid
flowchart TD
    A([DELETE /api/v1/decks/deckId/cards/cardId]) --> B{Card exists\nin MongoDB?}
    B -- No --> C[Return HTTP 404\nCard not found]
    B -- Yes --> D{Card ownerId\n== X-User-Id?}
    D -- No --> E[Return HTTP 403\nForbidden]
    D -- Yes --> F[Delete Card from MongoDB]
    F --> G[Return HTTP 204\nNo content]
```

---

## 10. Internal: Get Deck Owner (called by other services)

```mermaid
flowchart TD
    A([GET /internal/decks/deckId/owner]) --> B{Deck exists\nin MongoDB?}
    B -- No --> C[Return HTTP 404\nDeck not found]
    B -- Yes --> D[Return HTTP 200\nownerId and deckId]
```

---

## 12. Internal: Get Due Cards (called by study-service)

```mermaid
flowchart TD
    A([GET /internal/decks/deckId/due-cards?limit=50]) --> B{Deck exists\nin MongoDB?}
    B -- No --> C[Return HTTP 404\nDeck not found]
    B -- Yes --> D[Query cards where\ndeckId matches AND\nnextReviewDate ≤ today]
    D --> E{Result count\n> limit param?}
    E -- Yes --> F[Truncate to limit\ndefault max 50]
    E -- No --> G[Return all due cards]
    F --> H[Return HTTP 200\nList of due cards\nwith SM-2 state]
    G --> H
```

---

## 13. Internal: Update Card SM-2 State (called by study-service)

```mermaid
flowchart TD
    A([PATCH /internal/cards/cardId/sm2]) --> B{Card exists\nin MongoDB?}
    B -- No --> C[Return HTTP 404\nCard not found]
    B -- Yes --> D{Payload contains\neaseFactor, interval\nnextReviewDate?}
    D -- No --> E[Return HTTP 400\nMissing SM-2 fields]
    D -- Yes --> F[Atomically update\neaseFactor, interval\nnextReviewDate on Card]
    F --> G[Return HTTP 200\nUpdated card]
```
