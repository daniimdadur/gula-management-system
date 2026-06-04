# Product Service API (v1)

Overview
- Service: product-service
- Responsible for managing product catalog (CRUD) and product cache in Redis.
- Base URL (gateway): `https://{host}/api/v1/products` (gateway routes to product-service)

Authentication
- All endpoints require a valid JWT in `Authorization: Bearer <token>`.

Models (database reference)
- See `docs/03-db/product-erd.puml` for the canonical model. Fields:
  - id: UUID
  - code: string
  - name: string
  - category: string
  - description: text
  - weight: decimal
  - price: decimal
  - image_url: string
  - status: boolean

File structure (service)
- product-service/
  - controller/
    - ProductController.java
  - service/
    - ProductService.java
  - repository/
    - ProductRepository.java
  - entity/
    - Product.java
  - dto/
    - ProductRequest.java
    - ProductResponse.java
  - kafka/
    - producer/
    - consumer/
  - config/
    - RedisConfig.java

Endpoints

1) List products
- Method: GET
- Path: `/api/v1/products`
- Query parameters:
  - `page` (int, optional)
  - `size` (int, optional)
  - `q` (string, optional) search by name or code
  - `category` (string, optional)
- Response: 200
  {
    "data": [
      { "id":"uuid","code":"P001","name":"Gula Aren 250gr","category":"Gula","price":20000,"status":true }
    ],
    "page": 0,
    "size": 10,
    "total": 1
  }

2) Get product detail
- Method: GET
- Path: `/api/v1/products/{id}`
- Response: 200
  {
    "id":"uuid",
    "code":"P001",
    "name":"Gula Aren 250gr",
    "category":"Gula",
    "description":"...",
    "weight":0.25,
    "price":20000.00,
    "image_url":"https://...",
    "status":true
  }

3) Create product
- Method: POST
- Path: `/api/v1/products`
- Body: application/json
  {
    "code":"P002",
    "name":"Gula Kelapa 500gr",
    "category":"Gula",
    "description":"...",
    "weight":0.5,
    "price":35000.00,
    "image_url":"https://...",
    "status":true
  }
- Response: 201 Created
  { "id":"uuid", "message":"Product created" }

4) Update product
- Method: PUT
- Path: `/api/v1/products/{id}`
- Body: same as create (partial updates supported via PATCH if implemented)
- Response: 200

5) Delete product
- Method: DELETE
- Path: `/api/v1/products/{id}`
- Response: 204 No Content

Events (Kafka)
- Product updates may produce cache invalidation events or `ProductUpdatedEvent` on topic `product.events` (optional).

Caching
- Product list cache key: `products` (TTL 10 minutes)
- Product detail cache key: `product:{id}` (TTL 10 minutes)

Errors
- 400 Bad Request – validation errors
- 401 Unauthorized – missing/invalid JWT
- 404 Not Found – product not found
- 500 Internal Server Error

Notes
- Validate price >= 0 and weight >= 0.
- Product code should be unique.

