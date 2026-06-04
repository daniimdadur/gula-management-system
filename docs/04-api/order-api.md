# Order Service API (v1)

Overview
- Service: order-service
- Responsible for creating and managing sales orders. Produces `OrderCreatedEvent` when order is created.
- Base URL (gateway): `https://{host}/api/v1/orders`

Authentication
- JWT required.

Models (database reference)
- See `docs/03-db/order-erd.puml`.
  - orders
    - id: UUID
    - customer_id: UUID
    - order_date: datetime
    - total_amount: decimal
    - status: enum (CREATED, PAID, PACKED, SHIPPED, COMPLETED, CANCELLED)
  - order_item
    - id: UUID
    - order_id: UUID
    - product_id: UUID
    - product_name: varchar
    - price: decimal
    - quantity: int
    - subtotal: decimal

File structure (service)
- order-service/
  - controller/
    - OrderController.java
  - service/
    - OrderService.java
  - repository/
    - OrderRepository.java
    - OrderItemRepository.java
  - entity/
    - Order.java
    - OrderItem.java
  - dto/
    - CreateOrderRequest.java
    - OrderResponse.java
  - kafka/
    - producer/
      - OrderCreatedProducer.java
  - config/
    - KafkaConfig.java

Endpoints

1) Create order
- Method: POST
- Path: `/api/v1/orders`
- Body:
  {
    "customer_id":"customer-uuid",
    "items":[
      {"product_id":"prod-1","product_name":"Gula Aren 250gr","price":20000.00,"quantity":2}
    ]
  }
- Behavior:
  - Calculate subtotal per item and total_amount
  - Persist order and items with status `CREATED`
  - Produce `OrderCreatedEvent` on topic `order.events` with order id, customer id, items and total
- Response: 201
  { "order_id":"uuid","status":"CREATED" }

2) Get order by id
- Method: GET
- Path: `/api/v1/orders/{id}`
- Response: 200: full order with items

3) Update order status
- Method: PATCH
- Path: `/api/v1/orders/{id}/status`
- Body: { "status":"PAID" }
- Response: 200

Events
- Produces:
  - `OrderCreatedEvent` (topic: `order.events`) consumed by `notification-service` (and potentially payment-service)
- Consumes: none by default, but can listen for payment success to update status.

Business rules
- When order status changes to PAID, payment-service will produce PaymentSuccessEvent.
- Order cancellation should restore stock via inventory-service or through compensating events.

Errors
- 400 Bad Request
- 404 Order not found
- 409 Conflict (e.g., duplicate order)

