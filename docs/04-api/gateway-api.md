# API Gateway (v1) - Routing and Authentication

Overview
- Service: gateway-service (Spring Cloud Gateway)
- Responsible for authentication (JWT verification), routing to microservices, and cross-cutting concerns (rate-limiting, CORS, TLS termination when deployed behind a proxy).
- Public entrypoint for frontend: `https://{host}/api/v1/...`

Responsibilities
- Route paths to downstream services. Example routes:
  - `/api/v1/products/**` -> product-service
  - `/api/v1/inventories/**` -> inventory-service
  - `/api/v1/productions/**` -> production-service
  - `/api/v1/orders/**` -> order-service
  - `/api/v1/payments/**` -> payment-service
  - `/api/v1/notifications/**` -> notification-service
  - `/api/v1/customers/**` -> customer-service

Authentication & Authorization
- JWT validation performed at gateway.
- Gateway attaches user/roles claims to downstream `X-User-Id` / `X-User-Roles` headers (or forwards Authorization header).

Example application.yml snippets (routing)
```
spring:
  cloud:
    gateway:
      routes:
        - id: product
          uri: lb://product-service
          predicates:
            - Path=/api/v1/products/**
```

Cross-cutting features
- Rate limiting (per IP or per API key)
- CORS policies for frontend
- TLS termination (recommended at ingress/proxy level)

Health
- Gateway should expose `/actuator/health` for monitoring and readiness checks.

