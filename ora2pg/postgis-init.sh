#!/bin/sh
# Create a named Docker volume and run a PostGIS container with your workspace mounted.
WORKDIR="$(cd "$(dirname "$0")" && pwd)"
VOLUME_NAME="nr_silva_pgdata"
CONTAINER_NAME="postgis_nr_silva"
IMAGE="postgis/postgis:17-3.6-alpine"
POSTGRES_USER="postgres"
POSTGRES_PASSWORD="postgres"
POSTGRES_DB="nr_silva"
HOST_PORT=5433

docker volume create "$VOLUME_NAME" >/dev/null || true

docker run -d \
  --name "$CONTAINER_NAME" \
  -p ${HOST_PORT}:5432 \
  -e POSTGRES_USER="$POSTGRES_USER" \
  -e POSTGRES_PASSWORD="$POSTGRES_PASSWORD" \
  -e POSTGRES_DB="$POSTGRES_DB" \
  -v "${VOLUME_NAME}:/var/lib/postgresql/data" \
  -v "${WORKDIR}:/workspace:ro" \
  "$IMAGE"
echo "Started container $CONTAINER_NAME (image: $IMAGE)."
echo "DB: $POSTGRES_USER / $POSTGRES_DB on port $HOST_PORT"
