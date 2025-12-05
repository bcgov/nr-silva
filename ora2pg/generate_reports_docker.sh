#!/bin/sh

namespace="."
unit_cost=5

# Docker command prefix for ora2pg
DOCKER_CMD="docker run --rm --name ora2pg -v $(pwd):/workspace -w /workspace georgmoser/ora2pg"

# Generate reports (use -a to pass the filtered tables from config/app_tables.txt)
echo "Generating tables report..."
$DOCKER_CMD ora2pg -t SHOW_TABLE -c $namespace/config/ora2pg.conf -a "$(cat $namespace/config/app_tables.txt)" > $namespace/reports/tables.txt

echo "Generating columns report..."
$DOCKER_CMD ora2pg -t SHOW_COLUMN -c $namespace/config/ora2pg.conf -a "$(cat $namespace/config/app_tables.txt)" > $namespace/reports/columns.txt

echo "Generating migration assessment report..."
$DOCKER_CMD ora2pg -t SHOW_REPORT -c $namespace/config/ora2pg.conf --dump_as_html --cost_unit_value $unit_cost --estimate_cost -a "$(cat $namespace/config/app_tables.txt)" > $namespace/reports/report.html

echo "Reports generated."
