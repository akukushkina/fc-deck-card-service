# Flashcard Platform — TODO

Track progress across all areas of the deck-card-service and the broader platform.

---

## deck-card-service — Code

### Immediate fixes
- [ ] Move `show-details: never` under `management.endpoint.health` in `application.yml`
- [ ] Remove `jjwt.version` property from `pom.xml`
- [ ] Fix `@EnableMongoAuditing` on main application class
- [ ] Remove `@Valid` from `CardService` and `DeckService` interface methods
- [ ] Fix typo in `validateCardOwnership` error message (`wit` → `with`)
- [ ] Remove unused `ArrayList` import from `DeckServiceImpl`

### Features still to build
- [ ] CSV import endpoint (`POST /api/v1/decks/{deckId}/cards/import`)
- [ ] Add `logback-spring.xml` for structured JSON logging

---

## deck-card-service — Tests

- [ ] Unit tests — `DeckServiceImpl`
  - [ ] `createDeck` — happy path
  - [ ] `showDeck` — 404 when not found
  - [ ] `showDeck` — 403 when wrong owner
  - [ ] `updateDeck` — happy path
  - [ ] `deleteDeck` — cascades to cards
  - [ ] `getDecks` — pagination sanitization
- [ ] Unit tests — `CardServiceImpl`
  - [ ] `addCard` — happy path
  - [ ] `fetchCard` — 404 when not found
  - [ ] `fetchCard` — card belongs to wrong deck
  - [ ] `updateCard` — partial update (null fields skipped)
  - [ ] `deleteCard` — happy path
  - [ ] `deleteCardsFromDeck` — called on deck delete
- [ ] Unit tests — `ServiceValidation`
  - [ ] `validateDeckOwnership` — 404, 403
  - [ ] `validateCardOwnership` — 404, 403
- [ ] Integration tests with Testcontainers (MongoDB)
  - [ ] `POST /api/v1/decks` — 201
  - [ ] `GET /api/v1/decks` — paginated
  - [ ] `DELETE /api/v1/decks/{deckId}` — cascades cards
  - [ ] `POST /api/v1/decks/{deckId}/cards` — 201
  - [ ] `GET /internal/decks/{deckId}/due-cards` — returns due cards
  - [ ] `PATCH /internal/cards/{cardId}/sm2` — updates SM-2 fields
- [ ] Add JaCoCo coverage gate (≥ 80%) to `pom.xml`
- [ ] Remove `-DskipTests` from `maven.yml` once tests pass

---

## deck-card-service — CI/CD Pipeline

- [ ] Add unit test stage to `maven.yml`
- [ ] Add integration test stage to `maven.yml`
- [ ] Add ECR push step on `release` branch (requires AWS setup)
- [ ] Add EKS deploy step (requires AWS setup)
- [ ] Add rollback on failed rollout
- [ ] Add Trivy container image scan step

---

## deck-card-service — Kubernetes Manifests

- [ ] `Deployment` manifest
  - [ ] Image from ECR
  - [ ] Environment variables from Secrets/ConfigMaps
  - [ ] Resource requests and limits
  - [ ] Liveness probe — `/actuator/health/liveness`
  - [ ] Readiness probe — `/actuator/health/readiness`
  - [ ] Graceful shutdown (`terminationGracePeriodSeconds: 35`)
- [ ] `Service` manifest (ClusterIP)
- [ ] `HorizontalPodAutoscaler` manifest
- [ ] `PodDisruptionBudget` manifest
- [ ] `ConfigMap` for non-sensitive config
- [ ] `Secret` for `MONGODB_URI`

---

## AWS Infrastructure

- [ ] Decide: CDK or Terraform
- [ ] VPC — subnets, security groups, NAT gateway
- [ ] EKS cluster + node groups
- [ ] ECR repositories (one per service)
- [ ] MongoDB — DocumentDB or MongoDB Atlas
- [ ] MSK (Managed Kafka)
- [ ] S3 bucket (batch-service file uploads)
- [ ] Secrets Manager — DB URIs, API keys
- [ ] IAM roles per service (least privilege)
- [ ] IAM OIDC provider for GitHub Actions
- [ ] ECR lifecycle policy (delete untagged images > 7 days)

---

## CI/CD — Platform Wide

- [ ] OIDC role for GitHub Actions → AWS (no stored credentials)
- [ ] ECR login step using OIDC
- [ ] Deploy to EKS from `release` branch
- [ ] Auto-rollback on failed readiness probe

---

## Observability

- [ ] Confirm `logback-spring.xml` produces JSON in all services
- [ ] Deploy Prometheus + Grafana (or use CloudWatch)
- [ ] Create Grafana dashboard — HTTP error rate, latency, JVM heap
- [ ] Set up alerts
  - [ ] 5xx error rate > 1% over 5 minutes
  - [ ] p99 latency > 300ms
  - [ ] Pod restarts > 2 in 10 minutes
  - [ ] MongoDB connection errors
  - [ ] JVM heap > 85%
- [ ] Configure OTel Java Agent in Kubernetes manifests
- [ ] Verify trace propagation across services

---

## Security

- [ ] Confirm internal endpoints (`/internal/**`) are blocked at the gateway
- [ ] Secrets Manager integration — inject secrets at deploy time
- [ ] No secrets in `application.yml` or Git history
- [ ] IAM policies reviewed for least privilege
- [ ] Trivy scan passing (no CRITICAL/HIGH CVEs)

---

## Reliability and Scalability

- [ ] HPA configured and tested
- [ ] PodDisruptionBudget configured
- [ ] Feign client timeouts configured in all services that call deck-card-service
- [ ] MongoDB indexes verified in production
- [ ] Load test — verify service handles expected traffic

---

## Generative AI (batch-service)

- [ ] File upload endpoint (`POST /api/v1/jobs/import`)
- [ ] S3 file storage
- [ ] Amazon Bedrock integration (Claude)
- [ ] Async job processing via Kafka
- [ ] Job status tracking (pending → processing → completed/failed)
- [ ] Feign client to deck-card-service for card creation
- [ ] Notification to user on job completion/failure
- [ ] AI timeout and retry policy

---

## Documentation

- [ ] Update `devops-knowledge-base.md` port reference (8081 → 8080)
- [ ] Add `logback-spring.xml` explanation to knowledge base
- [ ] Complete operational runbook for deck-card-service
- [ ] Runbooks for all other services
- [ ] Architecture diagram updated with notifications-service
- [ ] API documentation via Swagger UI verified

---

## Other Services (not started)

- [ ] study-service
- [ ] stats-service
- [ ] notifications-service
- [ ] batch-service
- [ ] api-gateway
