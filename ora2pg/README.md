# Ora2Pg — Local Migration Helper

## Overview
This repository contains SQL files exported by Ora2Pg and helper scripts to manage a local PostGIS database. The scripts allow you to export, migrate, and manage data and schema between Oracle and PostGIS. The identified tables and objects have been exported into the `schema/` directory, and data files are stored in the `data/` directory.

---

## Prerequisites
- **Docker**: Ensure Docker is installed and running on your system.
- **psql**: (Optional) Install the PostgreSQL client if you want to interact with the database directly from your host.
- **Shell**: Use a shell environment (e.g., Bash) to execute the provided scripts.

---

## Directory Structure
- **config/**: Contains Ora2Pg configuration files.
  - `ora2pg.conf`: Main configuration file for Ora2Pg.
  - `app_tables.txt`: Optional list of tables to filter exports and reports.
- **schema/**: Contains SQL files for schema objects (tables, sequences, constraints, etc.).
- **data/**: Contains exported data files and `dump.sql` for restoring the database.

---

## Scripts and Usage

### 1. **Exporting Schema**
#### `export_schema.sh`
- Exports all object types defined in the `EXPORT_TYPE` variable to the `schema/` directory.
- **Usage**:
  ```bash
  chmod +x export_schema.sh
  ./export_schema.sh
  ```

#### `export_schema_docker.sh`
- Same as `export_schema.sh`, but runs Ora2Pg in a Docker container.
- **Usage**:
  ```bash
  chmod +x export_schema_docker.sh
  ./export_schema_docker.sh
  ```

#### `export_schema_filter_tables_docker.sh`
- Exports only the tables listed in `config/app_tables.txt` (filtered export).
- **Usage**:
  ```bash
  chmod +x export_schema_filter_tables_docker.sh
  ./export_schema_filter_tables_docker.sh
  ```

---

### 2. **Exporting Data**
#### `export_data_docker.sh`
- Exports data from the **destination database** to the `data/` directory.
- **Usage**:
  ```bash
  chmod +x export_data_docker.sh
  ./export_data_docker.sh
  ```

---

### 3. **Migrating Schema and Data**
#### `postgis-migrate.sh`
- Applies SQL files from the `schema/` directory to the PostGIS database in a controlled order:
  - Sequences → Tables → Constraints → Other objects.
- Now supports **customized migration** using the `--mode` parameter:
  - `--mode 1`: Migrate tables only.
  - `--mode 2`: Migrate constraints only.
  - `--mode 3`: Migrate both tables and constraints.
- **Usage**:
  ```bash
  chmod +x postgis-migrate.sh
  ./postgis-migrate.sh --mode 1
  ./postgis-migrate.sh --mode 2
  ./postgis-migrate.sh --mode 3
  ```

---

### 4. **Importing Data**
#### `import_all.sh`
- Imports data from the `data/` directory into the destination database.
- **Usage**:
  ```bash
  ./import_all.sh -a -d <database_name> -o <owner> -U <username> -h <host> -p <port> -y
  ```
- **Example**:
  ```bash
  ./import_all.sh -a -d nr_silva -o postgres -U postgres -h localhost -p 5433 -y
  ```

---

### 5. **Restoring from `dump.sql`**
#### Docker Environment
- Restore the database from `dump.sql` using Docker:
  ```bash
  docker exec -i <container_name> psql -U <username> -d <database_name> < /path/to/dump.sql
  ```
- **Example**:
  ```bash
  docker exec -i postgis_nr_silva psql -U postgres -d nr_silva < data/dump.sql
  ```

#### Non-Docker Environment
- Restore the database from `dump.sql` using `psql`:
  ```bash
  psql -U <username> -d <database_name> -h <host> -p <port> < /path/to/dump.sql
  ```
- **Example**:
  ```bash
  psql -U postgres -d nr_silva -h localhost -p 5433 < data/dump.sql
  ```

---

### 6. **Managing the PostGIS Container**
#### `postgis-init.sh`
- Starts a PostGIS container with the workspace mounted read-only at `/workspace`.
- **Usage**:
  ```bash
  chmod +x postgis-init.sh
  ./postgis-init.sh
  ```

#### `postgis-delete.sh`
- Cleans up the PostGIS container and volume:
  - **Soft Reset**: Drops and recreates the database.
  - **Hard Reset**: Removes the container and volume.
- **Usage**:
  ```bash
  chmod +x postgis-delete.sh
  ./postgis-delete.sh --force soft
  ./postgis-delete.sh --force hard
  ```

---

## Recommended Workflow

### Option 1: Using `dump.sql`
1. Start a fresh PostGIS container:
   ```bash
   ./postgis-init.sh
   ```
2. Restore the database from `dump.sql`:
   ```bash
   docker exec -i postgis_nr_silva psql -U postgres -d nr_silva < data/dump.sql
   ```

---

### Option 2: Without `dump.sql`
1. Start a fresh PostGIS container:
   ```bash
   ./postgis-init.sh
   ```
2. Run the migration script to create the database structure (tables only):
   ```bash
   ./postgis-migrate.sh --mode 1
   ```
3. Import data from the `data/` directory:
   ```bash
   ./import_all.sh -a -d nr_silva -o postgres -U postgres -h localhost -p 5433 -y
   ```
4. Run the migration script again to apply constraints (foreign keys, etc.):
   ```bash
   ./postgis-migrate.sh --mode 2
   ```

---

### Cleanup (if needed)
- Soft reset:
  ```bash
  ./postgis-delete.sh --force soft
  ```
- Hard delete:
  ```bash
  ./postgis-delete.sh --force hard
  ```
