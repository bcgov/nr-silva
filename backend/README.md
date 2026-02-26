# Backend Service

This document provides instructions to run the backend service and configure it for different database development models.

Currently, we have two models for database development and deployment:
- Hybrid: Default model that uses Oracle as the primary database and PostgreSQL for metadata.
- Postgres: The new model that uses only PostgreSQL as the primary database.

---

## Prerequisites

1. **Java**: Ensure Java 17 is installed.
2. **Maven**: Install Maven (or use the provided `mvnw` wrapper).
3. **Databases**:
   - Oracle Database (if using Oracle as the primary database).
   - PostgreSQL Database (always required; used to store user preferences, even when Oracle is configured as the primary database).

---

## Running the Backend

### 1. Clone the Repository
```bash
git clone <repository-url>
cd <repo-directory>/backend
```

### 2. Set Environment Variables

#### Example for Oracle (hybrid model):
```bash
export PRIMARY_DB=oracle
```

#### Example for PostgreSQL:
```bash
export PRIMARY_DB=postgres
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

Flyway migrations are determined by the `PRIMARY_DB` variable that determines the database development model (hybrid vs postgres).

- **Shared/Baseline Migrations**: Located in `src/main/resources/db/migration/`. These migrations are applied for both hybrid and postgres deployment models.
- **Postgres-Only Migrations**: Located in `src/main/resources/db/migration-dev/`. For `PRIMARY_DB=postgres`, Flyway loads migrations from **both** `db/migration/` and `db/migration-dev/`.
- **Production Environment**: When `PRIMARY_DB=oracle`, only the shared/baseline migrations in `src/main/resources/db/migration/` are applied.

---

## Testing

### Run Tests for Oracle:
```bash
./mvnw clean install -Dserver.primary-db=oracle
```

### Run Tests for PostgreSQL:
```bash
./mvnw clean install -Dserver.primary-db=postgres
```

### Loading Test Data on Local PostgreSQL Environment

To load test data into your local PostgreSQL environment, you can use the tools provided in the [`ora2pg/tools/README.md`](../ora2pg/tools/README.md) file. This document provides detailed instructions on combining migration files and loading them into your PostgreSQL database.

---

## Notes

- Ensure the database is running and accessible before starting the backend.
