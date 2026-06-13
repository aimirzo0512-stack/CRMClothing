# Clothing Store CRM

A full-stack CRM for a clothing store. Built as a university software-engineering project.

## Tech Stack
- **Backend:** Java 17, Spring Boot 3, Gradle, Spring Security + JWT, Spring Data JPA, PostgreSQL
- **Frontend:** HTML / CSS / JavaScript (dark theme, responsive), Chart.js
- **Infra:** Docker, Docker Compose, GitHub Actions, AWS EC2, Nginx, DuckDNS

## Folder Structure
```
clothing-crm/
├── backend/                  Spring Boot app (Gradle)
│   ├── build.gradle
│   ├── settings.gradle
│   ├── Dockerfile
│   └── src/main/
│       ├── java/com/crm/clothing/
│       │   ├── ClothingCrmApplication.java
│       │   ├── config/        (CorsConfig, DataInitializer)
│       │   ├── controller/    (Auth, Customer, Product, Order, Report, Dashboard)
│       │   ├── dto/
│       │   ├── entity/        (User, Role, Customer, Product, Order, OrderItem)
│       │   ├── exception/
│       │   ├── repository/
│       │   ├── security/      (JWT filter, util, UserDetailsService, SecurityConfig)
│       │   └── service/
│       └── resources/
│           └── application.yml
├── frontend/                  Static site
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── index.html
│   ├── css/styles.css
│   ├── js/                    (api.js, auth.js, app.js, dashboard.js, ...)
│   └── pages/                 (login, register, dashboard, customers, products, orders, reports, forms)
├── database/
│   ├── schema.sql
│   ├── sample-data.sql
│   └── ER_DIAGRAM.md
├── .github/workflows/deploy.yml
├── docker-compose.yml
├── docs/
│   ├── RUN_LOCALLY.md
│   └── DEPLOY_TO_AWS.md
└── README.md
```

## Quick Start
See `docs/RUN_LOCALLY.md` and `docs/DEPLOY_TO_AWS.md`.

## Default Accounts
- **Admin:** `admin` / `admin123`
- **Employee:** `employee` / `employee123`
