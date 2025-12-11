#!/bin/sh
# Stop/remove PostGIS container + volume (hard) or reset DB to a fresh state (soft).

# Configuration. Adjust as needed
CONTAINER_NAME="postgis_nr_silva"
VOLUME_NAME="nr_silva_pgdata"
POSTGRES_USER="postgres"
POSTGRES_DB="nr_silva"
SCHEMA_NAME="silva"

confirm() {
  if [ "$1" = "--force" ]; then
    return 0
  fi
  printf "%s " "$2"
  read ans
  [ "$ans" = "yes" ] || [ "$ans" = "y" ] || [ "$ans" = "Y" ] || return 1
  return 0
}

if [ "$1" = "--force" ]; then
  MODE="$2"   # allow passing "hard" or "soft" as second arg when using --force
else
  printf "Choose delete type: (h)ard or (s)oft [h/s]: "
  read choice
  case "$choice" in
    h|H) MODE="hard" ;;
    s|S) MODE="soft" ;;
    *) echo "Aborted: invalid choice."; exit 1 ;;
  esac
fi

if [ "$MODE" = "hard" ]; then
  if ! confirm "$1" "This will remove container '$CONTAINER_NAME' and volume '$VOLUME_NAME'. Type 'yes' to continue:"; then
    echo "Aborted."
    exit 1
  fi

  echo "Removing container (if exists): $CONTAINER_NAME"
  docker rm -f "$CONTAINER_NAME" >/dev/null 2>&1 || true

  echo "Removing volume (if exists): $VOLUME_NAME"
  docker volume rm "$VOLUME_NAME" >/dev/null 2>&1 || true

  echo "Done (hard delete)."
  exit 0
fi

if [ "$MODE" = "soft" ]; then
  if ! confirm "$1" "This will wipe data in database '$POSTGRES_DB' inside container '$CONTAINER_NAME' and recreate a fresh DB. Type 'yes' to continue:"; then
    echo "Aborted."
    exit 1
  fi

  # Check container running
  if [ -z "$(docker ps -q -f name="^/${CONTAINER_NAME}$")" ]; then
    echo "Container '$CONTAINER_NAME' is not running. Start it and re-run this script, or run:"
    echo "  docker start $CONTAINER_NAME"
    exit 1
  fi

  echo "Terminating connections to database $POSTGRES_DB..."
  docker exec -i "$CONTAINER_NAME" psql -U "$POSTGRES_USER" -d postgres -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '$POSTGRES_DB' AND pid <> pg_backend_pid();" >/dev/null

  echo "Dropping and recreating database $POSTGRES_DB..."
  docker exec -i "$CONTAINER_NAME" psql -U "$POSTGRES_USER" -d postgres -c "DROP DATABASE IF EXISTS \"$POSTGRES_DB\";"
  docker exec -i "$CONTAINER_NAME" psql -U "$POSTGRES_USER" -d postgres -c "CREATE DATABASE \"$POSTGRES_DB\";"

  echo "Recreating commonly used extensions in the fresh DB (postgis, uuid-ossp, pgcrypto)..."
  docker exec -i "$CONTAINER_NAME" psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "CREATE EXTENSION IF NOT EXISTS postgis;"
  docker exec -i "$CONTAINER_NAME" psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";"
  docker exec -i "$CONTAINER_NAME" psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "CREATE EXTENSION IF NOT EXISTS pgcrypto;"

  echo "Recreating schema $SCHEMA_NAME (if you need a different name change POSTGRES_SCHEMA in this script)..."
  docker exec -i "$CONTAINER_NAME" psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "CREATE SCHEMA IF NOT EXISTS "$SCHEMA_NAME" AUTHORIZATION \"$POSTGRES_USER\";"

  echo "Soft reset complete. Database '$POSTGRES_DB' is now fresh with selected extensions and schema '$SCHEMA_NAME'."
  exit 0
fi

echo "Aborted: unknown mode."
exit 1
