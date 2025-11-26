#!/bin/bash
set -euo pipefail

PGDATA="/var/lib/postgresql/data"

export PGDATA

init_db_if_needed() {
  if [ ! -s "$PGDATA/PG_VERSION" ]; then
    echo "Initializing database at $PGDATA"
    # Initialize database cluster as postgres user. Do not attempt to set password via initdb --pwfile;
    # instead set the postgres password after initialization using ALTER USER.
    /usr/lib/postgresql/17/bin/initdb -D "$PGDATA" --username=postgres

    # Configure pg_hba.conf. For quick startup we previously allowed all addresses,
    # but that is too permissive for production. Use PG_HBA_ALLOWED_NETWORKS to
    # control which CIDR ranges are permitted (space-separated). If not set,
    # default to localhost and a common in-cluster range.
    if [ -n "${PG_HBA_ALLOWED_NETWORKS:-}" ]; then
      PG_HBA_NETWORKS="$PG_HBA_ALLOWED_NETWORKS"
    else
      echo "ERROR: PG_HBA_ALLOWED_NETWORKS environment variable must be set to specify allowed client networks (e.g., '127.0.0.1/32 10.128.0.0/14')."
      exit 1
    fi
    for net in $PG_HBA_NETWORKS; do
      echo "host all all $net md5" >> "$PGDATA/pg_hba.conf"
    done

    # If a DB name is provided, create it and enable PostGIS. Also set postgres user password if provided.
    if [ -n "${POSTGRES_DB:-}" ]; then
      /usr/bin/pg_ctl -D "$PGDATA" -o "-c listen_addresses='localhost'" -w start
      createdb -U postgres "$POSTGRES_DB" || true
      psql -U postgres -d "$POSTGRES_DB" -c "CREATE EXTENSION IF NOT EXISTS postgis;" || true

      if [ -n "${POSTGRES_PASSWORD:-}" ]; then
        # Escape single quotes in password before embedding in SQL
        esc_pwd=$(printf "%s" "$POSTGRES_PASSWORD" | sed "s/'/'\\''/g")
        psql -U postgres -c "ALTER USER postgres WITH PASSWORD '$esc_pwd';" || true
      fi

      pg_ctl -D "$PGDATA" -m fast -w stop
    else
      # No DB specified; still set postgres password if provided
      if [ -n "${POSTGRES_PASSWORD:-}" ]; then
        /usr/bin/pg_ctl -D "$PGDATA" -o "-c listen_addresses='localhost'" -w start
        esc_pwd=$(printf "%s" "$POSTGRES_PASSWORD" | sed "s/'/'\\''/g")
        psql -U postgres -c "ALTER USER postgres WITH PASSWORD '$esc_pwd';" || true
        pg_ctl -D "$PGDATA" -m fast -w stop
      fi
    fi
  fi
}

case "$1" in
  postgres)
    # ensure env
    : ${POSTGRES_PASSWORD:=}
    init_db_if_needed
    exec /usr/lib/postgresql/17/bin/postgres -D "$PGDATA" -c listen_addresses='*'
    ;;
  "")
    # No args passed: ensure DB is initialized (covers cases where CMD was not provided)
    init_db_if_needed
    exec /usr/lib/postgresql/17/bin/postgres -D "$PGDATA" -c listen_addresses='*'
    ;;
  *)
    # allow running other commands (useful for debugging)
    exec "$@"
    ;;
esac
