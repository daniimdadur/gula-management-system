# Customer Service API (v1)

Overview
- Service: customer-service (or can be part of order-service depending on deployment)
- Responsible for managing customer data used in orders and notifications.
- Base URL (gateway): `https://{host}/api/v1/customers`

Authentication
- JWT required for protected operations; listing/viewing may be restricted to internal roles.

Models (database reference)
- See `docs/03-db/customer-erd.puml`.
  - customer
    - id: UUID
    - name: varchar
    - email: varchar
    - address: text

File structure (service)
- customer-service/
  - controller/
    - CustomerController.java
  - service/
    - CustomerService.java
  - repository/
    - CustomerRepository.java
  - entity/
    - Customer.java
  - dto/
    - CustomerRequest.java
    - CustomerResponse.java
  - config/

Endpoints

1) Create customer
- Method: POST
- Path: `/api/v1/customers`
- Body:
  { "name":"John","email":"john@example.com","address":"..." }
- Response: 201

2) Update customer
- Method: PUT
- Path: `/api/v1/customers/{id}`

3) Get customer
- Method: GET
- Path: `/api/v1/customers/{id}`

4) List customers
- Method: GET
- Path: `/api/v1/customers`

Errors
- 400 Validation
- 404 Not found

