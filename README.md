# AeroScale

AeroScale is a backend system for a high-concurrency e-commerce platform focused on **flash-sale workflows**.

The project is currently being developed as a **modular monolith**, with the architecture intentionally designed around clear domain boundaries and loose coupling so that individual modules can be extracted into microservices in the future.

---

## Architecture

AeroScale currently follows a modular-monolith architecture.

```text
AeroScale
│
├── Auth Module
│   └── Authentication & Security
│
├── Buyer Module
│   ├── Buyer
│   ├── Addresses
│   └── Cart
│
├── Product Module
│   └── Product management & stock
│
├── Seller Module
│   └── Seller management
│
└── Order Module
    ├── Orders
    ├── Order Items
    └── Checkout
```

The long-term goal is to evolve these domain boundaries into independently deployable microservices.

### Current architectural principles

* Modules communicate through service-level contracts rather than directly accessing each other's repositories where possible.
* Entities are not shared across domain boundaries.
* Cross-module references use IDs rather than JPA relationships.
* Historical order data is stored as snapshots where necessary.
* Transaction boundaries are explicitly defined around critical business operations.
* The current monolith is not treated as a collection of tightly coupled CRUD classes; domain boundaries are maintained with future service extraction in mind.

---

## Implemented Modules

### Auth Module

Responsible for:

* Authentication
* JWT-based security
* Authenticated user context
* Authorization/security integration

---

### Product Module

Responsible for:

* Product management
* Product availability
* Product stock
* Product pricing
* Seller association through IDs

The Product module avoids direct JPA relationships with other domain entities where those relationships would create unnecessary coupling.

---

### Buyer Module

Responsible for:

* Buyer information
* Buyer addresses
* Cart management

The Buyer/Cart functionality is currently part of the same module while maintaining separate service responsibilities.

---

### Order Module

The Order module handles the checkout workflow.

Current checkout flow:

```text
Authenticated Buyer
        │
        ▼
     Checkout
        │
        ├── Validate Cart
        │
        ├── Validate Shipping Address
        │
        ├── Create Address Snapshot
        │
        ├── Reserve/Validate Product Stock
        │
        ├── Snapshot Product Price
        │
        ├── Create Order Items
        │
        ├── Calculate Order Total
        │
        ├── Persist Order
        │
        └── Clear Cart
```

### Order data model

```text
Order
│
├── buyerId
├── shippingAddressSnapshot
├── totalAmount
├── status
├── createdAt
├── updatedAt
│
└── OrderItems
    ├── productId
    ├── quantity
    ├── unitPriceAtPurchase
    └── subtotal
```

The Order module intentionally does not maintain JPA relationships with Buyer, Product, or Seller entities.

This allows an Order to retain historical information independently of future changes to other modules.

---

## Flash-Sale Considerations

A major goal of AeroScale is to support high-concurrency flash-sale scenarios.

Areas being considered include:

* Concurrent stock updates
* Optimistic locking
* Stock reservation
* Transactional checkout
* Duplicate checkout prevention
* Idempotency
* Payment failures and stock release
* High-volume order creation
* Event-driven communication
* Future distributed transactions

The current implementation focuses on establishing correct domain boundaries and transactional behavior before introducing distributed-system complexity.

---

## Technology Stack

* **Java**
* **Spring Boot**
* **Spring Security**
* **Spring Data JPA**
* **Hibernate**
* **Jakarta Validation**
* **Lombok**
* **JWT**
* **Relational Database**

---

## Project Structure

The codebase is organized around domain modules rather than a single global controller/service/repository structure.

```text
src/main/java/com/miniProject/AeroScale/

├── AuthModule/
│
├── BuyerModule/
│
├── product/
│
├── seller/
│
└── order/
```

Each module contains its own relevant layers such as:

```text
controller/
service/
repository/
entity/
dto/
exception/
```

The exact structure may evolve as the project grows.

---

## API

### Checkout

```http
POST /api/v1/orders/checkout
```

The checkout request currently requires the shipping address ID.

Example:

```json
{
  "shippingAddressId": "UUID"
}
```

The buyer identity is obtained from the authenticated security context rather than being supplied by the client.

The server obtains the buyer's cart and constructs the Order from the server-side cart state.

---

## Design Decisions

### IDs instead of cross-module JPA relationships

Instead of:

```java
@ManyToOne
private Product product;
```

the Order module stores:

```java
private UUID productId;
```

This prevents Order from becoming dependent on the Product entity and makes future service extraction easier.

---

### Address snapshots

Orders store a snapshot of the shipping address used at checkout.

This ensures that changing a buyer's current address later does not modify historical orders.

---

### Price snapshots

Order items store the unit price at the time of purchase.

For example:

```text
Product price at checkout: ₹499

OrderItem:
unitPriceAtPurchase = ₹499
```

If the product price later changes to ₹699, the historical order remains ₹499.

---

## Future Direction

The planned evolution is approximately:

```text
Current

                    AeroScale Monolith
                           │
       ┌───────────┬───────┼────────┬───────────┐
       ▼           ▼       ▼        ▼           ▼
     Auth        Buyer   Product   Seller      Order
```

toward:

```text
Future

 Auth Service
      │
      ├── Buyer Service
      │
      ├── Product / Inventory Service
      │
      ├── Seller Service
      │
      ├── Cart Service
      │
      ├── Order Service
      │
      └── Payment Service
```

The current architecture deliberately avoids premature microservice infrastructure while maintaining boundaries that make future extraction realistic.

---

## Development Status

| Module                  | Status      |
| ----------------------- | ----------- |
| Authentication          | Implemented |
| Product                 | Implemented |
| Buyer                   | In Progress |
| Cart                    | In Progress |
| Seller                  | Initial     |
| Order / Checkout        | In Progress |
| Payment                 | Planned     |
| Inventory               | Planned     |
| Flash-Sale Engine       | Planned     |
| Microservice Extraction | Future      |

---

## Getting Started

### Prerequisites

Make sure the following are installed:

* Java
* Maven
* A relational database
* Git

### Clone the repository

```bash
git clone <repository-url>
cd AeroScale
```

### Configure the application

Update the application's configuration with the required:

* Database credentials
* JWT configuration
* Other environment-specific properties

### Run the application

Using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

---

## Development Philosophy

AeroScale is being developed with two goals in mind:

1. Build a correct and maintainable backend today.
2. Avoid architectural decisions that make future scaling unnecessarily difficult.

The project therefore favors **practical modularity over premature distributed-system complexity**.

The monolith comes first.

The microservices come later — when there is an actual reason to split them.
