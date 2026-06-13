# Run Locally

## Prerequisites
- Docker + Docker Compose
- (Optional, only if running backend without Docker) JDK 17 and Gradle 8+

## One-command run (recommended)

```bash
cd clothing-crm
docker compose up --build
```

When all 3 containers are up:
- Frontend: http://localhost
- Backend API: http://localhost:8080
- PostgreSQL: localhost:5432 (db `clothingcrm`, user `crm`, pass `crmpass`)

Open http://localhost and log in with one of the default accounts:

| Role     | Username  | Password    |
|----------|-----------|-------------|
| Admin    | admin     | admin123    |
| Employee | employee  | employee123 |

The admin/employee users + roles are created automatically by `DataInitializer` on first startup.

## Loading sample data
After the backend has started at least once (tables exist):

```bash
docker compose exec -T postgres psql -U crm -d clothingcrm < database/sample-data.sql
```

## Running the backend without Docker

```bash
cd backend
gradle bootJar
DB_URL=jdbc:postgresql://localhost:5432/clothingcrm DB_USER=crm DB_PASS=crmpass \
  java -jar build/libs/app.jar
```

Then open `frontend/pages/login.html` in a browser, OR serve `frontend/`
with any static server (e.g. `python3 -m http.server 5500 --directory frontend`)
and visit http://localhost:5500.

## Stopping

```bash
docker compose down            # keep DB volume
docker compose down -v         # also wipe the database
```
