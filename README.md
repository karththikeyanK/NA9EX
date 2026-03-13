# NA9EX — Vehicle Seat Booking Manager

A full-stack web application for managing vehicle seat bookings, built to digitalize the end-to-end operations of a travel/transport company — from ticket creation and seat allocation to driver management and revenue tracking.

---

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Overview](#api-overview)
- [Screenshots](#screenshots)
- [License](#license)

---

## Features

### Admin Panel
- View real-time ticket counts broken down by gender (male / female) before booking confirmation
- Add and manage vehicles, drivers, and tickets
- View all booking details across vehicles
- Track monthly revenue reports

### Driver / Cleaner Portal
- View upcoming and current bookings assigned to their vehicle only
- Monitor cumulative revenue (monthly or to-date)
- View total booking count for their vehicle

### Booking Management
- Transfer a ticket to another vehicle
- Export / download a ticket to an external source
- Cancel a ticket with automatic seat release
- View full ticket details
- Send ticket confirmation details directly to the customer

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java, Spring Boot |
| Database | MySQL (Cloud SQL) |
| Containerization | Docker |
| CI/CD | GitHub Actions |
| Cloud | Google Cloud Platform (Cloud Run, Cloud SQL) |
| API Testing | Postman |

---

## Architecture

```
Client (Browser / Mobile)
        │
        ▼
   REST API Layer (Spring Boot)
        │
   ┌────┴────────────────────┐
   │                         │
Admin Service          Booking Service
   │                         │
   └──────────┬──────────────┘
              │
         MySQL (Cloud SQL)
```

The application follows a layered microservice-ready architecture:
- **Controller layer** — exposes RESTful endpoints for admin, driver, and booking modules
- **Service layer** — encapsulates business logic including gender-based ticket aggregation and revenue calculation
- **Repository layer** — JPA/Hibernate ORM with MySQL
- **Deployment** — containerized via Docker, deployed on GCP Cloud Run with managed Cloud SQL

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker
- MySQL (local) or GCP Cloud SQL instance

### Local Setup

```bash
# Clone the repository
git clone https://github.com/karththikeyanK/NA9EX.git
cd NA9EX

# Configure environment variables
cp .env.example .env
# Edit .env with your DB credentials and GCP config

# Build the project
mvn clean install

# Run with Docker
docker-compose up --build
```

The API will be available at `http://localhost:8080`.

### Environment Variables

| Variable | Description |
|---|---|
| `DB_URL` | JDBC connection URL for MySQL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key for JWT token signing |

---

## API Overview

| Method | Endpoint | Role | Description |
|---|---|---|---|
| `GET` | `/api/admin/tickets/count` | Admin | Get male/female ticket count |
| `POST` | `/api/admin/vehicle` | Admin | Add a new vehicle |
| `POST` | `/api/admin/driver` | Admin | Add a new driver |
| `GET` | `/api/admin/bookings` | Admin | View all bookings |
| `GET` | `/api/admin/revenue/monthly` | Admin | Monthly revenue report |
| `GET` | `/api/driver/bookings` | Driver | View own vehicle bookings |
| `POST` | `/api/bookings/transfer` | Admin | Transfer ticket to another vehicle |
| `POST` | `/api/bookings/cancel` | Admin | Cancel a ticket |
| `GET` | `/api/bookings/{id}` | Admin | View ticket details |
| `POST` | `/api/bookings/{id}/notify` | Admin | Send ticket details to customer |

> Full API documentation available via Postman collection in `/docs/postman`.

---

## Deployment

The application is containerized and deployed on **Google Cloud Platform**:

- **Cloud Run** — hosts the Spring Boot containerized service with auto-scaling
- **Cloud SQL (MySQL)** — managed relational database
- **GitHub Actions** — CI/CD pipeline that builds, tests, and deploys on every push to `main`

```bash
# Manual Docker build and push to GCR
docker build -t gcr.io/<your-project>/na9ex .
docker push gcr.io/<your-project>/na9ex
```

---

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

> Built by [Karththikeyan Kamalakkanan](https://github.com/karththikeyanK)
