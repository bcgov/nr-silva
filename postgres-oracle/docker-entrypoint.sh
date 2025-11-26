#!/bin/bash
set -euo pipefail

PGDATA="/var/lib/postgresql/data"

export PGDATA

init_db_if_needed() {
  if [ ! -s "$PGDATA/PG_VERSION" ]; then
    echo "Initializing database at $PGDATA"
    /usr/lib/postgresql/17/bin/initdb -D "$PGDATA" --username=postgres --pwfile=<(echo "$POSTGRES_PASSWORD" 2>/dev/null || true)
    # Allow all local connections for quick startup; in production tighten pg_hba.conf
    echo "host all all 0.0.0.0/0 md5" >> "$PGDATA/pg_hba.conf"
    chown -R postgres:postgres "$PGDATA"
    # enable postgis extension on default DB if provided
    if [ -n "${POSTGRES_DB:-}" ] && [ "$POSTGRES_DB" != "" ]; then
      /usr/bin/pg_ctl -D "$PGDATA" -o "-c listen_addresses='localhost'" -w start
      createdb -U postgres "$POSTGRES_DB" || true
      psql -U postgres -d "$POSTGRES_DB" -c "CREATE EXTENSION IF NOT EXISTS postgis;" || true
      pg_ctl -D "$PGDATA" -m fast -w stop
    fi
  fi
}

case "$1" in
  postgres)
    # ensure env
    : ${POSTGRES_PASSWORD:=}
    init_db_if_needed
    exec /usr/lib/postgresql/17/bin/postgres -D "$PGDATA"
    ;;
  "")
    exec /usr/lib/postgresql/17/bin/postgres -D "$PGDATA"
    ;;
  *)
    # allow running other commands (useful for debugging)
    exec "$@"
    ;;
esac
