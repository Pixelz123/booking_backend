# 🏨 Booking Backend

A monolithic backend for a hotel booking platform, emphasizing **concurrent booking** capabilities using distributed locking and caching via Redis. The system features key services collaboratively operating to support user auth, property listing, and safe booking flows.

---

## Table of Contents

- [Key Features](#key-features)
- [Architecture Overview](#architecture-overview)
  - [Booking Service](#booking-service)
  - [Redis Service](#redis-service)
  - [User Service](#user-service)
  - [Property Service](#property-service)
  - [Auth Service](#auth-service)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Local Setup](#local-setup)
- [Booking Flow](#booking-flow)
- [API Endpoints](#api-endpoints)
- [Concurrency Control](#concurrency-control)
- [Caching Strategy](#caching-strategy)

---

## Key Features

- 💏 **Concurrent Booking**: Prevents double-booking via Redis-backed distributed locks on a per-property-date basis.
- ⚡ **Fast Access**: Property data is quickly fetched from Redis cache to reduce DB pressure.
- 🔐 **JWT-Based Auth**: Role-based access control distinguishing `guest` and `host`.
- 📋 **Microservice-like Modular Design** within a monolith: Contains distinct service modules.

---

## Architecture Overview

A diagrammatic representation of service modules and data flow:

```
+-------------------------------------------------------------+
|                      Booking Backend                       |
| +------------------+    +------------------+   +----------+|
| |   Booking        |    |    Redis         |   | Database ||
| |   Service        |    |    Service        |   |  (SQL)   ||
| |------------------|    |------------------|   +----------+|
| |• Creates booking |<-->|• Locks per date  |◄──▶            |
| |• Uses Redis lock |    |• Caches property|                |
| +------------------+    +------------------+                |
|       ↑                                                    |
|       |                                                    |
| +------------------+   +------------------+   +----------+ |
| | User Service     |   | Property Service |   | Auth     | |
| |------------------|   |------------------|   | Service  | |
| |• Manages guests/hosts|• Adds/listings  |   |• JWT &   | |
| |• Manages bookings |   |• Metadata queries|   |• RBAC     | |
| +------------------+   +------------------+   +----------+ |
+------------------------------------------------------------+
```

---

### Booking Service

- **Responsibilities**:
  - Accepts booking requests.
  - Coordinates with Redis Service to lock a specific *property + date*.
  - Validates availability in DB.
  - Creates booking record if available.
  - Publishes booking status events.

### Redis Service

- Interfaces with Redis to:
  - **Lock/Unlock**: Ensures only one booking per date/property at a time using `SETNX` + TTL.
  - **Cache Properties**: Stores frequently accessed property info.

### User Service

- Manages user accounts, separated by roles:
  - `guest` users book stays.
  - `host` users list properties.
- Endpoints: registration, profile, listing user bookings.

### Property Service

- Hosts can list new properties.
- Fetches property metadata from DB and caches via RedisService.
- Endpoints for search and detail queries.

### Auth Service

- JWT authentication and role-based access:
  - Ensures only hosts can list properties.
  - Guests can book, hosts can view their own bookings.

---

## Tech Stack

| Layer       | Tech                    |
| ----------- | ----------------------- |
| Backend     | Java + Spring Boot      |
| Database    | PostgreSQL (or any SQL) |
| Cache/Locks | Redis                   |
| Auth        | JWT + Spring Security   |
| Build Tool  | Gradle                  |
| Dev Tools   | JUnit, Mockito          |

---

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL
- Redis (version ≥ 6)
- Gradle

### Local Setup

```bash
# Clone repository
git clone https://github.com/Pixelz123/booking_backend.git
cd booking_backend

# Configure application.yml or environment variables with DB and Redis settings

# Build the project
gradle build

# Run the application
gradle bootRun
```

---

## Booking Flow

1. Guest requests booking for *property* on *date*.
2. **BookingService** calls **RedisService** to acquire lock (`lock:{propertyId}:{date}`).
3. If lock acquired:
   - Check DB for existing booking on the same date.
   - If free, insert booking.
   - Release lock in Redis.
4. Return success or failure.
5. Optionally, release lock automatically after TTL to handle crashes.

---

## API Endpoints

### Auth

| Route            | Method | Description              |
| ---------------- | ------ | ------------------------ |
| `/auth/register` | POST   | User signup (guest/host) |
| `/auth/login`    | POST   | Returns JWT token        |

### Users

| Route                  | Method | Description                |
| ---------------------- | ------ | -------------------------- |
| `/users/me`            | GET    | Fetch profile              |
| `/users/{id}/bookings` | GET    | Guest’s or host’s bookings |

### Properties

| Route              | Method | Description            |
| ------------------ | ------ | ---------------------- |
| `/properties`      | GET    | List/search            |
| `/properties/{id}` | GET    | View details           |
| `/properties`      | POST   | Create new (host-only) |

### Bookings

| Route       | Method | Description        |
| ----------- | ------ | ------------------ |
| `/bookings` | GET    | Guest’s bookings   |
| `/bookings` | POST   | Create new booking |

---

## Concurrency Control

*BookingService* achieves safe concurrency by:

- Using Redis locks (`SETNX`) scoped as `lock:{propertyId}:{date}`.
- Publishers confirm or abort actions based on lock status.
- TTLs ensure stale locks expire to avoid deadlocks.

---

## Caching Strategy

Properties are cached client-side via **RedisService** for:

- Faster queries (`GET` operations hit Redis first).
- Decreased load on the database.
- Automatic invalidation on updates.


