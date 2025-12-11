#!/bin/sh
WORKDIR="$(cd "$(dirname "$0")" && pwd)"

# Configuration. Adjust as needed
SCHEMA_NAME="silva"
PGHOST="localhost"
PGPORT=5434
PGUSER=postgres
PGDATABASE=postgres
SCHEMA_OWNER="$PGUSER"

# ANSI colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
BOLD='\033[1m'
RESET='\033[0m'

# Wait for DB to be ready (use pg_isready)
echo "Waiting for database to be ready at ${PGHOST}:${PGPORT}..."
until pg_isready -h "$PGHOST" -p "$PGPORT" -U "${PGUSER}" -d "${PGDATABASE}" > /dev/null 2>&1; do sleep 1; done
echo "Database is ready."

# Helper to run a SQL file if it exists
run_if_exists() {
  src="$1"
  if [ -f "$WORKDIR/$src" ]; then
    printf "%bApplying %s...%b\n" "$YELLOW" "$src" "$RESET"
    # Run psql using host/port: capture stderr into 'err', discard stdout
    err=$(psql -h "$PGHOST" -p "$PGPORT" -U "$PGUSER" -d "$PGDATABASE" -f "$WORKDIR/$src" 2>&1 1>/dev/null)
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
  # use psql against forwarded host/port
schema_err=$(echo "$sql" | psql -h "$PGHOST" -p "$PGPORT" -U "$PGUSER" -d "$PGDATABASE" 2>&1 1>/dev/null)
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

# main tables (often table.sql)
run_if_exists "schema/tables/table.sql"

# constraints / indexes / fkeys (if present)
run_if_exists "schema/tables/CONSTRAINTS_table.sql"
run_if_exists "schema/tables/INDEXES_table.sql"
run_if_exists "schema/tables/FKEYS_table.sql"

# other schema objects (safe generic order)
for dir in types tablespaces triggers functions packages procedures views synonyms directories mviews grants; do
  for f in "$WORKDIR/schema/$dir"/*.sql; do
    [ -e "$f" ] || continue
    rel="${f#$WORKDIR/}"
    run_if_exists "$rel"
  done
done

printf "%bMigration finished.%b\n" "$GREEN" "$RESET"
