# Kafka Event Schemas (JSON Schema Format)

File ini berisi JSON Schema definitions untuk semua Kafka events dalam Gula Management System.

Gunakan schemas ini untuk:
- Validasi payload JSON
- Code generation (Java POJO, TypeScript types, OpenAPI)
- Documentation
- Testing

---

## ProductionCompletedEvent

**Topic:** `production.events`  
**Producer:** production-service  
**Consumers:** inventory-service

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "$id": "https://gulamanagement.local/events/ProductionCompletedEvent.schema.json",
  "title": "ProductionCompletedEvent",
  "description": "Event dipublish ketika production record berhasil dibuat",
  "type": "object",
  "properties": {
    "eventId": {
      "type": "string",
      "format": "uuid",
      "description": "Unique event identifier"
    },
    "eventTimestamp": {
      "type": "string",
      "format": "date-time",
      "description": "ISO 8601 timestamp"
    },
    "productionId": {
      "type": "string",
      "format": "uuid",
      "description": "Production record ID"
    },
    "productId": {
      "type": "string",
      "format": "uuid",
      "description": "Product ID yang diproduksi"
    },
    "quantity": {
      "type": "integer",
      "minimum": 1,
      "description": "Jumlah produk yang diproduksi"
    },
    "productionDate": {
      "type": "string",
      "format": "date",
      "description": "Tanggal produksi (YYYY-MM-DD)"
    },
    "notes": {
      "type": "string",
      "nullable": true,
      "description": "Catatan tambahan"
    }
  },
  "required": ["eventId", "eventTimestamp", "productionId", "productId", "quantity", "productionDate"],
  "additionalProperties": false
}
```

---

## OrderCreatedEvent

**Topic:** `order.events`  
**Producer:** order-service  
**Consumers:** notification-service

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "$id": "https://gulamanagement.local/events/OrderCreatedEvent.schema.json",
  "title": "OrderCreatedEvent",
  "description": "Event dipublish ketika order baru berhasil dibuat",
  "type": "object",
  "properties": {
    "eventId": {
      "type": "string",
      "format": "uuid",
      "description": "Unique event identifier"
    },
    "eventTimestamp": {
      "type": "string",
      "format": "date-time",
      "description": "ISO 8601 timestamp"
    },
    "orderId": {
      "type": "string",
      "format": "uuid",
      "description": "Order ID"
    },
    "customerId": {
      "type": "string",
      "format": "uuid",
      "description": "Customer ID"
    },
    "totalAmount": {
      "type": "number",
      "minimum": 0,
      "description": "Total amount (Rp)"
    },
    "items": {
      "type": "array",
      "minItems": 1,
      "items": {
        "type": "object",
        "properties": {
          "productId": {
            "type": "string",
            "format": "uuid"
          },
          "productName": {
            "type": "string"
          },
          "quantity": {
            "type": "integer",
            "minimum": 1
          },
          "price": {
            "type": "number",
            "minimum": 0
          },
          "subtotal": {
            "type": "number",
            "minimum": 0
          }
        },
        "required": ["productId", "productName", "quantity", "price", "subtotal"],
        "additionalProperties": false
      }
    },
    "orderDate": {
      "type": "string",
      "format": "date-time",
      "description": "Order creation timestamp"
    }
  },
  "required": ["eventId", "eventTimestamp", "orderId", "customerId", "totalAmount", "items", "orderDate"],
  "additionalProperties": false
}
```

---

## PaymentSuccessEvent

**Topic:** `payment.events`  
**Producer:** payment-service  
**Consumers:** inventory-service, notification-service

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "$id": "https://gulamanagement.local/events/PaymentSuccessEvent.schema.json",
  "title": "PaymentSuccessEvent",
  "description": "Event dipublish ketika payment status berhasil diubah menjadi SUCCESS",
  "type": "object",
  "properties": {
    "eventId": {
      "type": "string",
      "format": "uuid",
      "description": "Unique event identifier"
    },
    "eventTimestamp": {
      "type": "string",
      "format": "date-time",
      "description": "ISO 8601 timestamp"
    },
    "paymentId": {
      "type": "string",
      "format": "uuid",
      "description": "Payment record ID"
    },
    "orderId": {
      "type": "string",
      "format": "uuid",
      "description": "Order ID terkait"
    },
    "customerId": {
      "type": "string",
      "format": "uuid",
      "description": "Customer ID"
    },
    "amount": {
      "type": "number",
      "minimum": 0,
      "description": "Payment amount (Rp)"
    },
    "paymentMethod": {
      "type": "string",
      "enum": ["CASH", "BANK_TRANSFER", "OTHER"],
      "description": "Payment method"
    },
    "paymentDate": {
      "type": "string",
      "format": "date-time",
      "description": "Payment confirmation timestamp"
    }
  },
  "required": ["eventId", "eventTimestamp", "paymentId", "orderId", "customerId", "amount", "paymentMethod", "paymentDate"],
  "additionalProperties": false
}
```

---

## InventoryUpdatedEvent

**Topic:** `inventory.events`  
**Producer:** inventory-service  
**Consumers:** dashboard-service

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "$id": "https://gulamanagement.local/events/InventoryUpdatedEvent.schema.json",
  "title": "InventoryUpdatedEvent",
  "description": "Event dipublish ketika inventory stok berubah",
  "type": "object",
  "properties": {
    "eventId": {
      "type": "string",
      "format": "uuid",
      "description": "Unique event identifier"
    },
    "eventTimestamp": {
      "type": "string",
      "format": "date-time",
      "description": "ISO 8601 timestamp"
    },
    "inventoryId": {
      "type": "string",
      "format": "uuid",
      "description": "Inventory record ID"
    },
    "productId": {
      "type": "string",
      "format": "uuid",
      "description": "Product ID"
    },
    "previousStock": {
      "type": "integer",
      "minimum": 0,
      "description": "Stok sebelum perubahan"
    },
    "currentStock": {
      "type": "integer",
      "minimum": 0,
      "description": "Stok setelah perubahan"
    },
    "minimumStock": {
      "type": "integer",
      "minimum": 0,
      "description": "Minimum stock threshold"
    },
    "transactionType": {
      "type": "string",
      "enum": ["IN", "OUT", "ADJUSTMENT"],
      "description": "Tipe transaksi"
    },
    "changeQuantity": {
      "type": "integer",
      "description": "Perubahan stok (bisa positif atau negatif)"
    },
    "referenceType": {
      "type": "string",
      "enum": ["production", "order", "manual", "adjustment"],
      "description": "Sumber perubahan"
    },
    "referenceId": {
      "type": "string",
      "format": "uuid",
      "nullable": true,
      "description": "ID referensi (productionId atau orderId)"
    }
  },
  "required": ["eventId", "eventTimestamp", "inventoryId", "productId", "previousStock", "currentStock", "minimumStock", "transactionType", "changeQuantity", "referenceType"],
  "additionalProperties": false
}
```

---

## LowStockEvent

**Topic:** `inventory.lowstock`  
**Producer:** inventory-service  
**Consumers:** notification-service

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "$id": "https://gulamanagement.local/events/LowStockEvent.schema.json",
  "title": "LowStockEvent",
  "description": "Event dipublish ketika stok produk jatuh di bawah minimum threshold",
  "type": "object",
  "properties": {
    "eventId": {
      "type": "string",
      "format": "uuid",
      "description": "Unique event identifier"
    },
    "eventTimestamp": {
      "type": "string",
      "format": "date-time",
      "description": "ISO 8601 timestamp"
    },
    "inventoryId": {
      "type": "string",
      "format": "uuid",
      "description": "Inventory record ID"
    },
    "productId": {
      "type": "string",
      "format": "uuid",
      "description": "Product ID"
    },
    "productName": {
      "type": "string",
      "description": "Nama produk"
    },
    "currentStock": {
      "type": "integer",
      "minimum": 0,
      "description": "Stok saat ini"
    },
    "minimumStock": {
      "type": "integer",
      "minimum": 0,
      "description": "Minimum stock threshold"
    },
    "warningThreshold": {
      "type": "integer",
      "nullable": true,
      "description": "Warning threshold (e.g., 20% above minimum)"
    }
  },
  "required": ["eventId", "eventTimestamp", "inventoryId", "productId", "productName", "currentStock", "minimumStock"],
  "additionalProperties": false
}
```

---

## Contoh Penggunaan

### Java - Spring Boot dengan JSON Schema Validation

```java
// Add dependency: io.json-schema / everit-json-schema
// Maven: <artifactId>everit-json-schema</artifactId>

@Service
@Slf4j
public class ProductionEventValidator {
    
    private final Schema productionEventSchema;
    
    public ProductionEventValidator() {
        // Load schema dari classpath
        try (InputStream schemaInputStream = getClass()
            .getResourceAsStream("/schemas/ProductionCompletedEvent.schema.json")) {
            JSONObject schema = new JSONObject(IOUtils.toString(schemaInputStream, StandardCharsets.UTF_8));
            this.productionEventSchema = SchemaLoader.load(schema);
        }
    }
    
    public void validate(ProductionCompletedEvent event) {
        JSONObject json = new JSONObject(event);
        try {
            productionEventSchema.validate(json);
            log.info("Event validation passed");
        } catch (ValidationException e) {
            log.error("Event validation failed: {}", e.getMessage());
            throw e;
        }
    }
}
```

### TypeScript - Type Generation

```bash
# Generate types dari JSON Schema
npm install json-schema-to-typescript

# Command
json2ts -i ProductionCompletedEvent.schema.json -o ProductionCompletedEvent.ts
```

### Python - Event Validation

```python
from jsonschema import validate, ValidationError
import json

with open('ProductionCompletedEvent.schema.json') as f:
    schema = json.load(f)

event_data = {
    "eventId": "550e8400-e29b-41d4-a716-446655440000",
    "eventTimestamp": "2026-06-04T08:30:00.000Z",
    "productionId": "550e8400-e29b-41d4-a716-446655440000",
    "productId": "550e8400-e29b-41d4-a716-446655440001",
    "quantity": 100,
    "productionDate": "2026-06-04"
}

try:
    validate(instance=event_data, schema=schema)
    print("Validation passed")
except ValidationException as e:
    print(f"Validation failed: {e.message}")
```

---

## Best Practices

### 1. Versioning Events
Jika schema berubah, buat versi baru:
- `ProductionCompletedEvent.v1.schema.json`
- `ProductionCompletedEvent.v2.schema.json`

### 2. Backward Compatibility
- Jangan remove required fields
- Bisa add optional fields
- Use `additionalProperties: false` untuk strict validation

### 3. Storing Schemas
- Store schema di version control (Git)
- Atau gunakan Schema Registry (Confluent)
- Load pada startup aplikasi

### 4. Validation Strategy
- Validate di producer (sebelum publish)
- Optional: Validate di consumer (untuk extra safety)
- Log failed validations untuk debugging

---

## References

- [JSON Schema Official Docs](https://json-schema.org/)
- [Confluent Schema Registry](https://docs.confluent.io/platform/current/schema-registry/index.html)
- [JSON Schema to TypeScript Converter](https://github.com/bcherny/json-schema-to-typescript)

