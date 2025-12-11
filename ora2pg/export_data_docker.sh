#!/bin/sh
# Export data (COPY) for tables listed in config/app_tables.txt using Docker ora2pg

namespace="."
TABLES_FILE="$namespace/config/app_tables.txt"

DOCKER_CMD="docker run --rm --name ora2pg -v $(pwd):/workspace -w /workspace georgmoser/ora2pg"

mkdir -p "$namespace/data"

echo "Exporting data..."
echo "Running: Docker ora2pg -t COPY -o data.sql -b $namespace/data -c $namespace/config/ora2pg.conf -a \"$(cat $TABLES_FILE)\""
$DOCKER_CMD ora2pg -t COPY -o data.sql -b $namespace/data -c $namespace/config/ora2pg.conf -a "$(cat $TABLES_FILE)"

echo "Done. Data files in: $namespace/data"
exit 0
