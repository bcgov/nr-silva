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

**NOTE:** There is no need to set the `FLYWAY_ENVIRONMENT` environment variable when running the Oracle-primary instance, as the goal is to run the backend deployed to the production environment.

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
./mvnw clean install -Dserver.primary-db=oracle
```

**NOTE:** There is no need to pass the `flyway-environment` property or set the `FLYWAY_ENVIRONMENT` environment variable when running the Oracle tests, as the goal is to run the tests deployed to production.

### Run Tests for PostgreSQL:
```bash
./mvnw clean install -Dflyway-environment=dev -Dserver.primary-db=postgres
```

### Loading Test Data on Local PostgreSQL Environment

To load test data into your local PostgreSQL environment, you can use the tools provided in the [`ora2pg/tools/README.md`](../ora2pg/tools/README.md) file. This document provides detailed instructions on combining migration files and loading them into your PostgreSQL database.

---

## Notes

- Ensure the database is running and accessible before starting the backend.
