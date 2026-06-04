# Notification Service API (v1)

Overview
- Service: notification-service
- Responsible for sending system notifications (email/SMS/in-app) based on events such as OrderCreated, PaymentSuccess, LowStock.
- Base URL (gateway): `https://{host}/api/v1/notifications`

Authentication
- JWT required for management endpoints. Consumers of events do not need HTTP access.

File structure (service)
- notification-service/
  - controller/
    - NotificationController.java
  - service/
    - NotificationService.java
  - repository/
    - NotificationRepository.java (optional for history)
  - entity/
    - Notification.java
  - dto/
    - NotificationRequest.java
  - kafka/
    - consumer/
      - OrderCreatedConsumer.java
      - PaymentSuccessConsumer.java
      - LowStockConsumer.java
  - config/
    - KafkaConfig.java

Endpoints (management)

1) List notifications
- Method: GET
- Path: `/api/v1/notifications`

2) Send test notification (admin)
- Method: POST
- Path: `/api/v1/notifications/test`
- Body: { "type":"LOW_STOCK","message":"...","recipient":"..." }

Kafka events (consumed)
- `OrderCreatedEvent` — notify admin / sales when a new order is created.
- `PaymentSuccessEvent` — send payment confirmation to customer and trigger fulfillment.
- `LowStockEvent` — notify warehouse/admin about low stock.

Event payload examples
- LowStockEvent
  {
    "product_id":"uuid",
    "product_name":"Gula Aren 250gr",
    "current_stock":5,
    "minimum_stock":10
  }

Notes
- Notification delivery channels (email/SMS/WhatsApp) are pluggable via adapters.
- Notification history can be stored for audit.

