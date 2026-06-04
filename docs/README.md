# Gula Management System - Documentation

Dokumentasi lengkap untuk Gula Management System v1.0 (Sistem Manajemen Penjualan dan Inventori UMKM Gula Kelapa & Gula Aren).

## 📋 Struktur Dokumentasi

```
docs/
├── 01-prd/
│   └── PRD GulaHub.txt          # Product Requirements Document
│                                 # Latar belakang, tujuan, scope, user roles, modules
├── 02-sad/
│   ├── architecture.puml         # Software Architecture Diagram
│   └── structure                 # Directory structure untuk backend, frontend, infrastructure
│
├── 03-db/
│   ├── erd.puml                  # Master ERD
│   ├── product-erd.puml          # Product model
│   ├── inventory-erd.puml        # Inventory & transaction model
│   ├── production-erd.puml       # Production model
│   ├── order-erd.puml            # Order & order items model
│   ├── payment-erd.puml          # Payment model
│   └── customer-erd.puml         # Customer model
│
├── 04-api/
│   ├── v1-documentation.md       # INDEX - Quick reference semua endpoints
│   ├── product-api.md            # Product Service API (CRUD, caching)
│   ├── inventory-api.md          # Inventory Service API (stock in/out, transaction)
│   ├── production-api.md         # Production Service API (record production)
│   ├── order-api.md              # Order Service API (create order, manage status)
│   ├── payment-api.md            # Payment Service API (payment management)
│   ├── customer-api.md           # Customer Service API (customer data)
│   ├── notification-api.md       # Notification Service API (event consumers)
│   └── gateway-api.md            # API Gateway (routing, authentication)
│
├── 05-events/
│   └── kafka-events.md           # Kafka Event Architecture & Contracts
│                                 # Event schemas, producer/consumer mapping,
│                                 # error handling, deployment guide
│
└── 06-deployment/
    ├── docker-architecture.puml  # Docker deployment diagram
    └── deployment.md             # Deployment guide & DevOps instructions
```

---

## 🎯 Cara Menggunakan Dokumentasi Ini

### 1. Jika ingin memahami requirement produk
→ Baca: **`01-prd/PRD GulaHub.txt`**

### 2. Jika ingin melihat arsitektur sistem
→ Baca: **`02-sad/architecture.puml`** dan **`02-sad/structure`**

### 3. Jika ingin memahami model database
→ Baca: **`03-db/*.puml`** (Entity Relationship Diagram)

### 4. Jika ingin menggunakan API
→ Mulai dari: **`04-api/v1-documentation.md`** (INDEX)
→ Kemudian buka file service spesifik yang Anda butuhkan:
- Product Service: `04-api/product-api.md`
- Inventory Service: `04-api/inventory-api.md`
- Order Service: `04-api/order-api.md`
- Production Service: `04-api/production-api.md`
- Payment Service: `04-api/payment-api.md`
- Customer Service: `04-api/customer-api.md`
- Notification Service: `04-api/notification-api.md`
- API Gateway: `04-api/gateway-api.md`

### 5. Jika ingin memahami event-driven architecture (Kafka)
→ Baca: **`05-events/kafka-events.md`**
- Event schemas & payload contoh
- Producer/Consumer mapping
- Error handling & retry strategy
- Topic configuration
- Implementation checklist

### 6. Jika ingin deploy aplikasi
→ Baca: **`06-deployment/deployment.md`** dan **`06-deployment/docker-architecture.puml`**

---

## 🔗 Hubungan Antar Dokumentasi

```
PRD (01-prd)
  ↓
Architecture & Design (02-sad)
  ↓
Database Models (03-db)
  ↓
API Specifications (04-api)
  ├── Individual Service APIs (product-api.md, order-api.md, etc.)
  ├── Event Contracts (referenced from 05-events)
  └── Index (v1-documentation.md)
  ↓
Event Architecture (05-events/kafka-events.md)
  ↓
Deployment & DevOps (06-deployment)
```

---

## 📚 Quick Reference

### Service Base URLs
| Service | Base URL |
|---------|----------|
| Product | `/api/v1/products` |
| Inventory | `/api/v1/inventories` |
| Production | `/api/v1/productions` |
| Order | `/api/v1/orders` |
| Payment | `/api/v1/payments` |
| Customer | `/api/v1/customers` |
| Notification | `/api/v1/notifications` |
| Gateway | `https://{host}/api/v1/...` |

### Kafka Events
| Event | Producer | Consumer(s) | Topic |
|-------|----------|-------------|-------|
| ProductionCompletedEvent | production-service | inventory-service | `production.events` |
| OrderCreatedEvent | order-service | notification-service | `order.events` |
| PaymentSuccessEvent | payment-service | inventory-service, notification-service | `payment.events` |
| InventoryUpdatedEvent | inventory-service | dashboard-service | `inventory.events` |
| LowStockEvent | inventory-service | notification-service | `inventory.lowstock` |

### Technology Stack
- **Backend:** Spring Boot, Spring Cloud Gateway
- **Database:** MySQL
- **Cache:** Redis
- **Message Broker:** Apache Kafka
- **Frontend:** Svelte
- **Container:** Docker
- **Orchestration:** Kubernetes (future)

---

## 🚀 Development Workflow

### 1. Backend Development
```
a. Understand the feature requirement (PRD)
   ↓
b. Check database model (03-db)
   ↓
c. Read API specification (04-api/service-api.md)
   ↓
d. Check event contracts if service is event producer/consumer (05-events)
   ↓
e. Implement service (controller/service/entity/dto/kafka)
   ↓
f. Update documentation if any changes
```

### 2. Frontend Development
```
a. Read API specification (04-api/v1-documentation.md)
   ↓
b. Review individual endpoint docs (04-api/service-api.md)
   ↓
c. Generate types/API client from OpenAPI (optional, if available)
   ↓
d. Implement UI & API integration
```

### 3. DevOps / Deployment
```
a. Review deployment guide (06-deployment)
   ↓
b. Review docker architecture (06-deployment/docker-architecture.puml)
   ↓
c. Review Kafka setup (05-events/kafka-events.md → Deployment section)
   ↓
d. Deploy infrastructure (docker-compose.yml, K8s manifests)
```

---

## 📖 Documentation Standards

Semua dokumentasi API mengikuti struktur standar:
1. **Overview** - Deskripsi service
2. **Authentication** - Cara autentikasi
3. **Models** - Referensi ke database schema
4. **File Structure** - Struktur code service
5. **Endpoints** - List endpoint dengan method, path, request/response
6. **Events** - Kafka events yang diproduksi/dikonsumsi
7. **Business Rules** - Aturan bisnis penting
8. **Errors** - Status codes & error messages

---

## 🔍 Bagaimana Membaca File

### .puml Files (PlantUML)
- Format UML diagram untuk visualisasi
- Bisa dibuka di: PlantUML Editor, VS Code extension, atau online viewer
- Gunakan untuk memahami flow, architecture, dan data model

### .md Files (Markdown)
- Format dokumentasi teknis
- Bisa dibaca di: GitHub, VS Code, atau Markdown viewer
- Berisi: overview, endpoints, schema, contoh, best practices

### .txt Files
- Format plain text
- Berisi: PRD, requirements, specifications

---

## 📞 Navigasi Cepat

- **Melihat struktur database?** → `03-db/erd.puml`
- **Melihat endpoint product service?** → `04-api/product-api.md`
- **Melihat flow event Kafka?** → `05-events/kafka-events.md`
- **Ingin deploy?** → `06-deployment/deployment.md`
- **Ingin lihat semua endpoint cepat?** → `04-api/v1-documentation.md`

---

## 🔄 Update Dokumentasi

Ketika ada change/update:
1. Update file dokumentasi yang relevan
2. Jika ada change di API endpoint → update `04-api/service-api.md` + `04-api/v1-documentation.md`
3. Jika ada change di event contract → update `05-events/kafka-events.md`
4. Jika ada change di database model → update `03-db/*.puml`
5. Commit & push dengan message yang jelas

---

## 📞 Questions?
Lihat dokumentasi dan issue/PR di repository untuk discussion lebih lanjut.

---

**Last Updated:** June 4, 2026  
**Documentation Version:** 1.0

