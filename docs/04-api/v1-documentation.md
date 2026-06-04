# API Documentation - Gula Management System v1.0

## Daftar isi
- [Overview](#overview)
- [Service Documentation](#service-documentation)
- [Gateway & Authentication](#gateway--authentication)
- [Kafka Events](#kafka-events)

---

## Overview
Dokumentasi lengkap API untuk Gula Management System mencakup semua microservices dan kontrak event Kafka.

Untuk detail lengkap setiap service, lihat file dokumentasi per-service di folder ini.

---

## Service Documentation

### 📦 Product Service
- **File:** [`product-api.md`](./product-api.md)
- **Base URL:** `/api/v1/products`
- **Deskripsi:** Mengelola katalog produk dan caching di Redis
- **Model:** Tabel `product` (lihat `../03-db/product-erd.puml`)

### 📊 Inventory Service
- **File:** [`inventory-api.md`](./inventory-api.md)
- **Base URL:** `/api/v1/inventories`
- **Deskripsi:** Tracking stok, transaksi, dan notifikasi stok menipis
- **Model:** Tabel `inventory` dan `inventory_transaction` (lihat `../03-db/inventory-erd.puml`)

### 🏭 Production Service
- **File:** [`production-api.md`](./production-api.md)
- **Base URL:** `/api/v1/productions`
- **Deskripsi:** Mencatat hasil produksi dan emit event
- **Model:** Tabel `production` (lihat `../03-db/production-erd.puml`)

### 👥 Customer Service
- **File:** [`customer-api.md`](./customer-api.md)
- **Base URL:** `/api/v1/customers`
- **Deskripsi:** Mengelola data pelanggan
- **Model:** Tabel `customer` (lihat `../03-db/customer-erd.puml`)

### 📋 Order Service
- **File:** [`order-api.md`](./order-api.md)
- **Base URL:** `/api/v1/orders`
- **Deskripsi:** Membuat dan mengelola pesanan penjualan
- **Model:** Tabel `orders` dan `order_item` (lihat `../03-db/order-erd.puml`)

### 💳 Payment Service
- **File:** [`payment-api.md`](./payment-api.md)
- **Base URL:** `/api/v1/payments`
- **Deskripsi:** Mengelola pembayaran dan status pembayaran
- **Model:** Tabel `payment` (lihat `../03-db/payment-erd.puml`)

### 🔔 Notification Service
- **File:** [`notification-api.md`](./notification-api.md)
- **Base URL:** `/api/v1/notifications`
- **Deskripsi:** Mengelola notifikasi sistem (email/SMS/in-app) berdasarkan event
- **Event Consumers:** OrderCreatedEvent, PaymentSuccessEvent, LowStockEvent

---

## Gateway & Authentication

### 🌐 API Gateway
- **File:** [`gateway-api.md`](./gateway-api.md)
- **Service:** `gateway-service` (Spring Cloud Gateway)
- **Deskripsi:** Routing, JWT validation, dan cross-cutting concerns
- **Public Entrypoint:** `https://{host}/api/v1/...`

---

## Kafka Events

Untuk dokumentasi lengkap event Kafka (kontrak event, payload schema, producer/consumer mapping), lihat:
- **File:** [`../05-events/kafka-events.md`](../05-events/kafka-events.md)

---

## Summary Endpoints

### Product Service
| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| POST | `/api/v1/products` | Membuat produk baru |
| GET | `/api/v1/products` | List seluruh produk |
| GET | `/api/v1/products/{id}` | Detail produk |
| PUT | `/api/v1/products/{id}` | Update produk |
| DELETE | `/api/v1/products/{id}` | Hapus produk |

### Inventory Service
| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| GET | `/api/v1/inventories/product/{productId}` | Get inventory by product |
| POST | `/api/v1/inventories/{id}/in` | Stock in |
| POST | `/api/v1/inventories/{id}/out` | Stock out |
| POST | `/api/v1/inventories/{id}/adjust` | Adjust stock |
| GET | `/api/v1/inventories/{id}/transactions` | Transaction history |

### Production Service
| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| POST | `/api/v1/productions` | Create production record |
| GET | `/api/v1/productions` | List production history |

### Customer Service
| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| POST | `/api/v1/customers` | Create customer |
| GET | `/api/v1/customers` | List customers |
| PUT | `/api/v1/customers/{id}` | Update customer |
| DELETE | `/api/v1/customers/{id}` | Delete customer |

### Order Service
| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| POST | `/api/v1/orders` | Create order |
| GET | `/api/v1/orders` | List orders |
| GET | `/api/v1/orders/{id}` | Order detail |
| PATCH | `/api/v1/orders/{id}/status` | Update status |

### Payment Service
| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| POST | `/api/v1/payments` | Create payment |
| POST | `/api/v1/payments/{id}/confirm` | Confirm payment |
| GET | `/api/v1/payments` | List payments |

### Notification Service
| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| GET | `/api/v1/notifications` | List notifications |
| PATCH | `/api/v1/notifications/{id}/read` | Mark as read |

---

## Authentication
Semua endpoint (kecuali login/register) memerlukan JWT token di header:
```
Authorization: Bearer <JWT_TOKEN>
```

---

## Event Architecture
Lihat detail lengkap event Kafka di [`../05-events/kafka-events.md`](../05-events/kafka-events.md) untuk:
- Event contract dan payload schema
- Producer/Consumer mapping
- Topic naming conventions
- Retry & DLQ (Dead Letter Queue) strategy
