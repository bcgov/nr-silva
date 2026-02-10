# Backend Service

This document provides instructions to run the backend service and configure it for different database environments (Oracle and PostgreSQL).

---

## Prerequisites

1. **Java**: Ensure Java 17 is installed.
2. **Maven**: Install Maven (or use the provided `mvnw` wrapper).
3. **Databases**:
   - Oracle Database (if using Oracle as the primary database).
   - PostgreSQL Database (always required; used to store user preferences, even when Oracle is configured as the primary database).
4. **Environment Variables**:
   - `PRIMARY_DB`: Specifies the primary database (`oracle` or `postgres`).
   - `FLYWAY_ENVIRONMENT`: Specifies the Flyway migration environment (`dev`, `prod`).

---

## Running the Backend

### 1. Clone the Repository
```bash
git clone <repository-url>
cd <repo-directory>/backend
```

### 2. Set Environment Variables

#### Example for Oracle:
```bash
export PRIMARY_DB=oracle
export FLYWAY_ENVIRONMENT=dev
```

#### Example for PostgreSQL:
```bash
export PRIMARY_DB=postgres
export FLYWAY_ENVIRONMENT=dev
```

### 3. Run the Application

Using Maven Wrapper:
```bash
./mvnw spring-boot:run
```

---

## Configuration

### 1. Database Configuration

The backend uses the `application.yml` file for database configuration. The `server.primary-db` property determines the primary database.

```yaml
server:
  primary-db: ${PRIMARY_DB:oracle}
```

### 2. Flyway Migrations

Flyway migrations are determined by the `FLYWAY_ENVIRONMENT` variable.

- **Shared/Baseline Migrations**: Located in `src/main/resources/db/migration/`. These migrations are applied in all environments (including `prod` and non-`prod`).
- **Development-Only Migrations**: Located in `src/main/resources/db/migration-dev/`. For non-`prod` values of `FLYWAY_ENVIRONMENT` (for example, `dev`), Flyway loads migrations from **both** `db/migration/` and `db/migration-dev/`.
- **Production Environment**: When `FLYWAY_ENVIRONMENT=prod`, only the shared/baseline migrations in `src/main/resources/db/migration/` are applied.

---

## Testing

### Run Tests for Oracle:
```bash
./mvnw clean install -Dflyway-environment=dev -Dserver.primary-db=oracle
```

### Run Tests for PostgreSQL:
```bash
./mvnw clean install -Dflyway-environment=dev -Dserver.primary-db=postgres
```

---

## Notes

- Ensure the database is running and accessible before starting the backend.
