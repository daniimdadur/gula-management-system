# Payment Service API (v1)

Overview
- Service: payment-service
- Responsible for managing payment records and emitting PaymentSuccessEvent on successful payments.
- Base URL (gateway): `https://{host}/api/v1/payments`

Authentication
- JWT required.

Models (database reference)
- See `docs/03-db/payment-erd.puml`.
  - payment
    - id: UUID
    - order_id: UUID
    - payment_method: enum (CASH, BANK_TRANSFER, OTHER)
    - amount: decimal
    - status: enum (PENDING, SUCCESS, FAILED)
    - payment_date: datetime

File structure (service)
- payment-service/
  - controller/
    - PaymentController.java
  - service/
    - PaymentService.java
  - repository/
    - PaymentRepository.java
  - entity/
    - Payment.java
  - dto/
    - PaymentRequest.java
    - PaymentResponse.java
  - kafka/
    - producer/
      - PaymentSuccessProducer.java
  - config/
    - KafkaConfig.java

Endpoints

1) Create/Initiate payment
- Method: POST
- Path: `/api/v1/payments`
- Body:
  {
    "order_id":"order-uuid",
    "payment_method":"BANK_TRANSFER",
    "amount":50000.00
  }
- Response: 201
  { "payment_id":"uuid","status":"PENDING" }

2) Confirm payment (manual/async callback)
- Method: POST
- Path: `/api/v1/payments/{id}/confirm`
- Body: { "status":"SUCCESS", "payment_date":"2026-06-04T10:00:00Z" }
- Behavior:
  - Update payment status to SUCCESS
  - Produce `PaymentSuccessEvent` (topic: `payment.events`) with payload:
    {
      "payment_id":"uuid",
      "order_id":"order-uuid",
      "amount":50000.00
    }
- Response: 200

3) Get payment by order
- Method: GET
- Path: `/api/v1/payments/order/{orderId}`

Events
- Produces:
  - `PaymentSuccessEvent` consumed by `inventory-service` (to deduct stock) and `notification-service` (to notify customer)

Business rules
- On `SUCCESS`, payment-service MUST produce PaymentSuccessEvent.
- Amount must match the order total (optional validation against order-service via sync call or event-driven verification).

Errors
- 400 Validation errors
- 404 Payment not found

