# Inventory Service API (v1)

Overview
- Service: inventory-service
- Responsible for tracking product stock, transactions, and low-stock notifications.
- Base URL (gateway): `https://{host}/api/v1/inventories`

Authentication
- All endpoints require JWT.

Models (database reference)
- See `docs/03-db/inventory-erd.puml`.
  - inventory
    - id: UUID
    - product_id: UUID
    - current_stock: int
    - minimum_stock: int
  - inventory_transaction
    - id: UUID
    - inventory_id: UUID
    - transaction_type: enum (IN, OUT, ADJUSTMENT)
    - quantity: int
    - reference_type: varchar (order, production, manual)
    - reference_id: UUID
    - notes: text

File structure (service)
- inventory-service/
  - controller/
    - InventoryController.java
    - TransactionController.java
  - service/
    - InventoryService.java
    - TransactionService.java
  - repository/
    - InventoryRepository.java
    - InventoryTransactionRepository.java
  - entity/
    - Inventory.java
    - InventoryTransaction.java
  - dto/
    - InventoryResponse.java
    - InventoryTransactionRequest.java
  - kafka/
    - consumer/
      - PaymentSuccessConsumer.java (consumes PaymentSuccessEvent)
      - ProductionCompletedConsumer.java
    - producer/
      - InventoryUpdatedProducer.java (produces InventoryUpdatedEvent)
  - config/
    - KafkaConfig.java

Endpoints

1) Get inventory by product
- Method: GET
- Path: `/api/v1/inventories/product/{productId}`
- Response: 200
  {
    "id":"uuid",
    "product_id":"product-uuid",
    "current_stock":100,
    "minimum_stock":10
  }

2) Stock In (e.g., after production)
- Method: POST
- Path: `/api/v1/inventories/{inventoryId}/in`
- Body:
  {
    "quantity": 50,
    "reference_type":"production",
    "reference_id":"production-uuid",
    "notes":"batch x"
  }
- Response: 200
  { "new_stock":150 }

3) Stock Out (e.g., after order)
- Method: POST
- Path: `/api/v1/inventories/{inventoryId}/out`
- Body:
  {
    "quantity": 2,
    "reference_type":"order",
    "reference_id":"order-uuid",
    "notes":"order #123"
  }
- Response: 200
  { "new_stock":98 }

4) Adjust stock
- Method: POST
- Path: `/api/v1/inventories/{inventoryId}/adjust`
- Body: { "quantity": -5, "notes":"audit" }

5) Inventory transaction history
- Method: GET
- Path: `/api/v1/inventories/{inventoryId}/transactions`
- Response: list of inventory_transaction records

Business rules enforced by API
- Stock must not become negative. Attempts to reduce below zero should return 400 with message `Insufficient stock`.
- All stock changes must produce an `InventoryUpdatedEvent` on Kafka with new stock and product id.
- If `current_stock` < `minimum_stock` after update, produce a `LowStockEvent` for notification-service.

Kafka events
- Consumes:
  - `PaymentSuccessEvent` (from payment-service) — reduces stock according to order items.
  - `ProductionCompletedEvent` (from production-service) — increases stock.
- Produces:
  - `InventoryUpdatedEvent` (topic: `inventory.events`)
  - `LowStockEvent` (topic: `inventory.lowstock`)

Errors
- 400 Bad Request – invalid request or insufficient stock
- 404 Not Found – inventory record not found
- 500 Internal Server Error

