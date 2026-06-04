# Kafka Event Architecture - Gula Management System v1.0

## Overview
Dokumen ini menjelaskan arsitektur event berbasis Kafka untuk Gula Management System. Semua event menggunakan pub-sub pattern asynchronous untuk komunikasi antar microservices.

---

## Event Topology

### Producer & Consumer Mapping

```
Production Service ──┐
                     ├──> ProductionCompletedEvent ──> Inventory Service
                     └──> (Optional) EventLog Service

Order Service ──────────> OrderCreatedEvent ──> Notification Service

Payment Service ────┐
                    ├──> PaymentSuccessEvent ──┬──> Inventory Service
                    ├──> PaymentSuccessEvent ──├──> Notification Service
                    └──> PaymentSuccessEvent ──└──> (Optional) Analytics Service

Inventory Service ──┐
                    ├──> InventoryUpdatedEvent ──> Dashboard Service
                    └──> LowStockEvent ──────────> Notification Service
```

---

## Event Contracts

### 1. ProductionCompletedEvent

**Producer:** production-service  
**Consumers:** inventory-service  
**Topic:** `production.events` (atau `production-completed`)  
**Priority:** HIGH  

#### Schema
```json
{
  "eventId": "string (UUID)",
  "eventTimestamp": "string (ISO 8601)",
  "productionId": "string (UUID)",
  "productId": "string (UUID)",
  "quantity": "integer (> 0)",
  "productionDate": "string (YYYY-MM-DD)",
  "notes": "string (nullable)"
}
```

#### Contoh Payload
```json
{
  "eventId": "9f5c9d8e-1a2b-4c3d-8e9f-5a6b7c8d9e0f",
  "eventTimestamp": "2026-06-04T08:30:00.000Z",
  "productionId": "550e8400-e29b-41d4-a716-446655440000",
  "productId": "550e8400-e29b-41d4-a716-446655440001",
  "quantity": 100,
  "productionDate": "2026-06-04",
  "notes": "Morning batch production"
}
```

#### Consumer Behavior (inventory-service)
1. Terima event ProductionCompletedEvent
2. Validasi productId existing
3. Lookup inventory record berdasarkan productId
4. Tambahkan quantity ke current_stock
5. Buat inventory_transaction record dengan:
   - transaction_type: `IN`
   - reference_type: `production`
   - reference_id: productionId
   - notes: dari event notes
6. Jika current_stock > minimum_stock, tidak ada action
7. Jika current_stock >= minimum_stock setelah update, cancel previous LowStockEvent (optional)

---

### 2. OrderCreatedEvent

**Producer:** order-service  
**Consumers:** notification-service, (optional: payment-service, analytics-service)  
**Topic:** `order.events` (atau `order-created`)  
**Priority:** HIGH  

#### Schema
```json
{
  "eventId": "string (UUID)",
  "eventTimestamp": "string (ISO 8601)",
  "orderId": "string (UUID)",
  "customerId": "string (UUID)",
  "totalAmount": "number (decimal, > 0)",
  "items": [
    {
      "productId": "string (UUID)",
      "productName": "string",
      "quantity": "integer (> 0)",
      "price": "number (decimal, > 0)",
      "subtotal": "number (decimal, > 0)"
    }
  ],
  "orderDate": "string (ISO 8601)"
}
```

#### Contoh Payload
```json
{
  "eventId": "9f5c9d8e-1a2b-4c3d-8e9f-5a6b7c8d9e0f",
  "eventTimestamp": "2026-06-04T10:15:00.000Z",
  "orderId": "550e8400-e29b-41d4-a716-446655440002",
  "customerId": "550e8400-e29b-41d4-a716-446655440003",
  "totalAmount": 50000.00,
  "items": [
    {
      "productId": "550e8400-e29b-41d4-a716-446655440001",
      "productName": "Gula Aren 250gr",
      "quantity": 2,
      "price": 20000.00,
      "subtotal": 40000.00
    },
    {
      "productId": "550e8400-e29b-41d4-a716-446655440004",
      "productName": "Gula Kelapa 500gr",
      "quantity": 1,
      "price": 10000.00,
      "subtotal": 10000.00
    }
  ],
  "orderDate": "2026-06-04T10:15:00.000Z"
}
```

#### Consumer Behavior (notification-service)
1. Terima event OrderCreatedEvent
2. Lookup customer email/phone dari customerId (via sync call atau local cache)
3. Format notification message:
   - Untuk Email: "Pesanan #orderId telah dibuat. Total: Rp 50.000"
   - Untuk SMS/WhatsApp: "Pesanan #{orderId} diterima. #{items.count} item, Total: Rp 50.000"
4. Sender notifikasi (email/SMS/in-app)
5. Log notification history

---

### 3. PaymentSuccessEvent

**Producer:** payment-service  
**Consumers:** inventory-service, notification-service  
**Topic:** `payment.events` (atau `payment-success`)  
**Priority:** CRITICAL  

#### Schema
```json
{
  "eventId": "string (UUID)",
  "eventTimestamp": "string (ISO 8601)",
  "paymentId": "string (UUID)",
  "orderId": "string (UUID)",
  "customerId": "string (UUID)",
  "amount": "number (decimal, > 0)",
  "paymentMethod": "enum (CASH, BANK_TRANSFER, OTHER)",
  "paymentDate": "string (ISO 8601)"
}
```

#### Contoh Payload
```json
{
  "eventId": "9f5c9d8e-1a2b-4c3d-8e9f-5a6b7c8d9e0f",
  "eventTimestamp": "2026-06-04T11:00:00.000Z",
  "paymentId": "550e8400-e29b-41d4-a716-446655440005",
  "orderId": "550e8400-e29b-41d4-a716-446655440002",
  "customerId": "550e8400-e29b-41d4-a716-446655440003",
  "amount": 50000.00,
  "paymentMethod": "BANK_TRANSFER",
  "paymentDate": "2026-06-04T11:00:00.000Z"
}
```

#### Consumer Behavior (inventory-service)
1. Terima event PaymentSuccessEvent
2. Lookup order details dari orderId (via order-service sync call atau event cache)
3. Untuk setiap order_item:
   - Lookup inventory berdasarkan productId
   - Reduce current_stock sebesar quantity
   - Validasi: current_stock tidak boleh negatif. Jika akan negatif:
     - Log error dan publish `PaymentFailedEvent` / `OrderCancelledEvent` (compensating transaction)
     - JANGAN kurangi stock
   - Buat inventory_transaction dengan:
     - transaction_type: `OUT`
     - reference_type: `order`
     - reference_id: orderId
     - notes: "Payment confirmed"
4. Jika current_stock < minimum_stock, publish LowStockEvent
5. Publish InventoryUpdatedEvent dengan order_id, items, dan status success

#### Consumer Behavior (notification-service)
1. Terima event PaymentSuccessEvent
2. Lookup customer email/phone & order details
3. Format notifikasi:
   - "Pembayaran berhasil diterima untuk pesanan #orderId. Kami akan segera memproses pesanan Anda."
4. Send notifikasi
5. Log

---

### 4. InventoryUpdatedEvent

**Producer:** inventory-service  
**Consumers:** dashboard-service, (optional: analytics-service, reporting-service)  
**Topic:** `inventory.events` (atau `inventory-updated`)  
**Priority:** MEDIUM  

#### Schema
```json
{
  "eventId": "string (UUID)",
  "eventTimestamp": "string (ISO 8601)",
  "inventoryId": "string (UUID)",
  "productId": "string (UUID)",
  "previousStock": "integer",
  "currentStock": "integer",
  "minimumStock": "integer",
  "transactionType": "enum (IN, OUT, ADJUSTMENT)",
  "changeQuantity": "integer",
  "referenceType": "string (production, order, manual, etc)",
  "referenceId": "string (UUID, nullable)"
}
```

#### Contoh Payload
```json
{
  "eventId": "9f5c9d8e-1a2b-4c3d-8e9f-5a6b7c8d9e0f",
  "eventTimestamp": "2026-06-04T11:05:00.000Z",
  "inventoryId": "550e8400-e29b-41d4-a716-446655440006",
  "productId": "550e8400-e29b-41d4-a716-446655440001",
  "previousStock": 120,
  "currentStock": 118,
  "minimumStock": 10,
  "transactionType": "OUT",
  "changeQuantity": -2,
  "referenceType": "order",
  "referenceId": "550e8400-e29b-41d4-a716-446655440002"
}
```

#### Consumer Behavior (dashboard-service)
1. Terima event InventoryUpdatedEvent
2. Update dashboard cache (Redis key: `dashboard_summary`) dengan:
   - Total Produk
   - Total Stok (sum dari semua current_stock)
   - Low Stock Products (filter: current_stock < minimum_stock)
3. Optional: Store event untuk historical data & analytics

---

### 5. LowStockEvent

**Producer:** inventory-service  
**Consumers:** notification-service  
**Topic:** `inventory.lowstock` (atau `low-stock`)  
**Priority:** HIGH  

#### Schema
```json
{
  "eventId": "string (UUID)",
  "eventTimestamp": "string (ISO 8601)",
  "inventoryId": "string (UUID)",
  "productId": "string (UUID)",
  "productName": "string",
  "currentStock": "integer",
  "minimumStock": "integer",
  "warningThreshold": "integer (optional, e.g., 20% above minimum)"
}
```

#### Contoh Payload
```json
{
  "eventId": "9f5c9d8e-1a2b-4c3d-8e9f-5a6b7c8d9e0f",
  "eventTimestamp": "2026-06-04T09:30:00.000Z",
  "inventoryId": "550e8400-e29b-41d4-a716-446655440006",
  "productId": "550e8400-e29b-41d4-a716-446655440001",
  "productName": "Gula Aren 250gr",
  "currentStock": 8,
  "minimumStock": 10,
  "warningThreshold": 12
}
```

#### Consumer Behavior (notification-service)
1. Terima event LowStockEvent
2. Query inventory record untuk produk
3. Format notifikasi:
   - "⚠️ STOK MENIPIS: Gula Aren 250gr tersisa {currentStock} pcs (minimum: {minimumStock}). Segera lakukan produksi!"
4. Send notifikasi ke admin gudang & owner (via email/SMS/in-app)
5. Optional: Create alert/ticket di sistem internal

---

## Topic Configuration

### Naming Convention
- Format: `{service}.{entity}` atau `{service}-{action}`
- Contoh:
  - `production.events`
  - `order.events`
  - `payment.events`
  - `inventory.events`
  - `inventory.lowstock`

### Topic Settings (Recommended)
```
num_partitions: 3 (untuk parallelisasi)
replication_factor: 2 (untuk high availability)
retention_ms: 604800000 (7 hari)
compression_type: snappy
```

### Partition Strategy
- **Key:** `productId` (untuk ProductionCompletedEvent, InventoryUpdatedEvent, LowStockEvent)
  - Ensures ordering untuk produk yang sama
- **Key:** `orderId` (untuk OrderCreatedEvent, PaymentSuccessEvent)
  - Ensures ordering untuk order yang sama
- **Key:** null
  - Round-robin jika tidak ada dependency

---

## Error Handling & Retry Strategy

### Consumer Error Scenarios

#### 1. Transient Error (network timeout, temporary service down)
- **Action:** Retry dengan exponential backoff
  - Attempt 1: +1s
  - Attempt 2: +2s
  - Attempt 3: +4s
  - Attempt 4: +8s
  - Max retries: 5
- **After max retries:** Send ke Dead Letter Topic (DLT)

#### 2. Persistent Error (validation failed, business rule violated)
- **Action:** Log error, send ke DLT immediately
- **Do NOT retry**
- **Manual intervention required**

#### 3. Idempotency
- Setiap event harus punya unique `eventId`
- Consumer HARUS check apakah event sudah diproses (query database dengan eventId)
- Jika sudah diproses: skip atau apply idempotent update

### Dead Letter Topic (DLT)
- **Naming:** `{topic}.dlq` (e.g., `production.events.dlq`)
- **Retention:** 30 hari
- **Monitoring:** Alert jika ada event di DLT
- **Recovery:** Manual review dan replay setelah issue fixed

---

## Implementation Checklist (Per Service)

### Producer Implementation (e.g., production-service)
- [ ] Define event schema (JSON)
- [ ] Create event class (Java POJO)
- [ ] Create Kafka producer bean (KafkaProducerConfig)
- [ ] Create producer service (KafkaProducer.java)
- [ ] Generate unique eventId (UUID) dan eventTimestamp (Instant.now())
- [ ] Catch & log producer exceptions
- [ ] Implement producer interceptor untuk monitoring

### Consumer Implementation (e.g., inventory-service)
- [ ] Create event listener class (KafkaConsumer.java)
- [ ] Define topic & group_id
- [ ] Add @KafkaListener annotation
- [ ] Implement idempotency check (query eventId di database)
- [ ] Implement business logic (stock update, validation, etc)
- [ ] Add error handling dengan retry logic
- [ ] Implement DLT listener untuk failed events
- [ ] Add logging & monitoring

### Application Configuration (application.yml)
```yaml
spring:
  kafka:
    bootstrap-servers: kafka:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      bootstrap-servers: kafka:9092
      group-id: inventory-service-group-v1
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: com.gulamanagement.events
      max-poll-records: 100
      session-timeout-ms: 30000

logging:
  level:
    org.apache.kafka: WARN
    org.springframework.kafka: INFO
```

---

## Event Flow Diagrams

### Order → Payment → Inventory → Notification Flow
```
┌─────────────────┐
│  Order Service  │ (Admin creates order via API)
└────────┬────────┘
         │
         ├─> Create Order in Database
         │   status: CREATED
         │
         ├─> Publish OrderCreatedEvent
         │   ↓
         ├─────────────────────────────> Notification Service
         │                               (send order confirmation)
         │
         └─> Return 201 to client
         
Time passes...

┌──────────────────────┐
│ Payment Service      │ (Admin confirms payment via API)
└─────────┬────────────┘
          │
          ├─> Update Payment status: SUCCESS
          │
          ├─> Publish PaymentSuccessEvent
          │   ↓
          ├──────────────────────────────> Inventory Service
          │ (deduct stock for order items)
          │ ├─> Validate stock sufficient
          │ ├─> Deduct quantity
          │ ├─> Check if low stock
          │ ├─> Publish InventoryUpdatedEvent
          │ └─> If low stock, publish LowStockEvent
          │                                ↓
          │                    Notification Service
          │                    (send low stock alert)
          │
          └─────────────────────────────> Notification Service
                              (send payment confirmation)
```

---

## Monitoring & Observability

### Key Metrics to Track
- **Producer:**
  - Event publish rate (events/sec per topic)
  - Publishing latency (p50, p95, p99)
  - Publish errors rate

- **Consumer:**
  - Consumption lag (offset lag)
  - Processing latency (event received → processing complete)
  - Consumer errors rate
  - DLT message count

### Tools
- **Kafka UI:** http://kafka-ui:8080 (optional)
- **Spring Boot Actuator:** /actuator/metrics
- **Custom Metrics:** Micrometer (Prometheus format)

### Example Prometheus Queries
```
# Consumer lag for inventory-service
kafka_consumer_lag{group_id="inventory-service-group-v1"}

# Event publish rate
rate(kafka_producer_records_total[1m])

# Errors
kafka_producer_record_send_errors_total
```

---

## Testing

### Unit Tests (Event Serialization)
- Test JSON serialization/deserialization
- Test schema validation
- Test null/invalid field handling

### Integration Tests (Producer)
- Mock Kafka broker (Testcontainers)
- Publish event
- Assert message in topic

### Integration Tests (Consumer)
- Mock Kafka broker
- Publish event to topic
- Assert database/service state changed correctly
- Assert idempotency (publish same event twice, expect no duplicate changes)

### Contract Tests (Pact)
- Production Service ↔ Inventory Service (ProductionCompletedEvent contract)
- Payment Service ↔ Inventory Service (PaymentSuccessEvent contract)
- etc.

---

## Deployment & Operations

### Docker Compose (Local Development)
```yaml
services:
  kafka:
    image: confluentinc/cp-kafka:7.x
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1

  zookeeper:
    image: confluentinc/cp-zookeeper:7.x
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
```

### Topic Creation (CLI)
```bash
# Create topic
kafka-topics --create --topic production.events \
  --partitions 3 --replication-factor 2 \
  --bootstrap-server kafka:9092

# List topics
kafka-topics --list --bootstrap-server kafka:9092

# Describe topic
kafka-topics --describe --topic production.events \
  --bootstrap-server kafka:9092
```

### Consumer Group Management
```bash
# List consumer groups
kafka-consumer-groups --list --bootstrap-server kafka:9092

# Describe consumer group
kafka-consumer-groups --describe \
  --group inventory-service-group-v1 \
  --bootstrap-server kafka:9092

# Reset offset (careful!)
kafka-consumer-groups --reset-offsets \
  --group inventory-service-group-v1 \
  --topic production.events \
  --to-earliest --execute \
  --bootstrap-server kafka:9092
```

---

## References
- [Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring Kafka](https://spring.io/projects/spring-kafka)
- [Event Sourcing Pattern](https://martinfowler.com/eaaDev/EventSourcing.html)
- [Transactional Outbox Pattern](https://microservices.io/patterns/messaging/transactional-outbox.html)

