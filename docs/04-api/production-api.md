# Production Service API (v1)

Overview
- Service: production-service
- Responsible for recording production batches and emitting events to update inventory.
- Base URL (gateway): `https://{host}/api/v1/productions`

Authentication
- JWT required.

Models (database reference)
- See `docs/03-db/production-erd.puml`.
  - production
    - id: UUID
    - product_id: UUID
    - quantity: int
    - production_date: date
    - notes: text

File structure (service)
- production-service/
  - controller/
    - ProductionController.java
  - service/
    - ProductionService.java
  - repository/
    - ProductionRepository.java
  - entity/
    - Production.java
  - dto/
    - ProductionRequest.java
    - ProductionResponse.java
  - kafka/
    - producer/
      - ProductionCompletedProducer.java
  - config/
    - KafkaConfig.java

Endpoints

1) Create production record
- Method: POST
- Path: `/api/v1/productions`
- Body:
  {
    "product_id":"product-uuid",
    "quantity": 100,
    "production_date":"2026-06-04",
    "notes":"morning batch"
  }
- Response: 201
  { "id":"uuid","message":"Production recorded" }

Behavior
- When a production is recorded successfully, the service MUST produce a `ProductionCompletedEvent` to Kafka (topic: `production.events`) with payload:
  {
    "production_id":"uuid",
    "product_id":"product-uuid",
    "quantity":100,
    "production_date":"2026-06-04"
  }
- Inventory-service consumes this event and increases stock accordingly.

Errors
- 400 Validation errors
- 401 Unauthorized
- 404 Product not found (if product id invalid)

