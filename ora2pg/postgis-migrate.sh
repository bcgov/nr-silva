#!/bin/sh
# Wait for Postgres to be ready and apply SQL files from schema/ in a safe order.
# Configuration. Adjust as needed
WORKDIR="$(cd "$(dirname "$0")" && pwd)"
CONTAINER_NAME="postgis_nr_silva"
POSTGRES_USER="postgres"
POSTGRES_DB="nr_silva"
SCHEMA_NAME="silva"
SCHEMA_OWNER="$POSTGRES_USER"

# ANSI colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BOLD='\033[1m'
RESET='\033[0m'

# Default mode
MODE=""

# Check if --mode is passed as a parameter
while [ "$#" -gt 0 ]; do
  case "$1" in
    --mode)
      case "$2" in
        1) MODE="tables";;
        2) MODE="constraints";;
        3) MODE="both";;
        *)
          echo "Invalid mode: $2. Valid options are 1, 2, or 3."
          exit 1
          ;;
      esac
      shift 2
      ;;
    *)
      echo "Unknown option: $1"
      exit 1
      ;;
  esac
done

# If --mode is not passed, prompt the user
if [ -z "$MODE" ]; then
  printf "%bSelect migration mode:%b\n" "$YELLOW" "$RESET"
  printf "  1) tables only\n  2) constraints only\n  3) tables + constraints (default)\n"
  printf "Enter choice [1/2/3]: "
  read -r choice
  case "$choice" in
    1) MODE="tables";;
    2) MODE="constraints";;
    3) MODE="both";;
    *)
      echo "Invalid choice. Aborting the migration."
      exit 1
      ;;
  esac
fi

# Wait for DB to be ready
echo "Waiting for database to be ready..."
docker exec "$CONTAINER_NAME" bash -c "until pg_isready -U ${POSTGRES_USER} -d ${POSTGRES_DB} > /dev/null 2>&1; do sleep 1; done"
echo "Database is ready."

# Helper to run a SQL file if it exists
run_if_exists() {
  src="$1"
  if [ -f "$WORKDIR/$src" ]; then
    printf "%bApplying %s...%b\n" "$YELLOW" "$src" "$RESET"
    # Run psql: discard stdout, capture stderr into 'err'. Exit on failure and print only error details.
    err=$(docker exec -i "$CONTAINER_NAME" psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f "/workspace/$src" 2>&1 1>/dev/null)
    rc=$?
    if [ $rc -ne 0 ]; then
      printf "%bFailed to apply %s (exit %d)%b\n" "$RED" "$src" "$rc" "$RESET"
      printf "%b%s%b\n" "$RED" "$err" "$RESET"
      exit $rc
    fi
    printf "%bApplied %s%b\n" "$GREEN" "$src" "$RESET"
    printf "%b-------------------------------%b\n" "$BOLD" "$RESET"
  fi
}

sql=$(cat <<SQL
CREATE SCHEMA IF NOT EXISTS "$SCHEMA_NAME" AUTHORIZATION "$SCHEMA_OWNER";
GRANT ALL ON SCHEMA "$SCHEMA_NAME" TO "$SCHEMA_OWNER";
ALTER ROLE "$SCHEMA_OWNER" SET search_path = "$SCHEMA_NAME", public;
SQL
)

printf "%bCreating schema %s in %s...%b\n" "$YELLOW" "$SCHEMA_NAME" "$POSTGRES_DB" "$RESET"
schema_err=$(echo "$sql" | docker exec -i "$CONTAINER_NAME" psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" 2>&1 1>/dev/null)
schema_rc=$?
if [ $schema_rc -ne 0 ]; then
  printf "%bFailed to create schema (exit %d)%b\n" "$RED" "$schema_rc" "$RESET"
  printf "%b%s%b\n" "$RED" "$schema_err" "$RESET"
  exit $schema_rc
fi
printf "%bSchema %s ready%b\n" "$GREEN" "$SCHEMA_NAME" "$RESET"
printf "%b===============================%b\n" "$BOLD" "$RESET"
printf "%bStarting migration...%b\n" "$YELLOW" "$RESET"

# Order: sequences, sequence_values, tables (core), constraints/indexes/fkeys, indexes, triggers, functions, views, grants
for f in schema/sequences/*.sql; do [ -e "$WORKDIR/$f" ] && run_if_exists "$f"; done
for f in schema/sequence_values/*.sql; do [ -e "$WORKDIR/$f" ] && run_if_exists "$f"; done

if [ "$MODE" = "tables" ] || [ "$MODE" = "both" ]; then
  # main tables (often table.sql)
  run_if_exists "schema/tables/table.sql"
  run_if_exists "schema/tables/INDEXES_table.sql"
fi
if [ "$MODE" = "constraints" ] || [ "$MODE" = "both" ]; then
  # constraints / fkeys (if present)
  run_if_exists "schema/tables/CONSTRAINTS_table.sql"
  run_if_exists "schema/tables/FKEYS_table.sql"
fi

# other schema objects (safe generic order)
for dir in types tablespaces triggers functions packages procedures views synonyms directories mviews grants; do
  for f in "$WORKDIR/schema/$dir"/*.sql; do
    [ -e "$f" ] || continue
    rel="${f#$WORKDIR/}"
    run_if_exists "$rel"
  done
done

printf "%bMigration finished.%b\n" "$GREEN" "$RESET"
