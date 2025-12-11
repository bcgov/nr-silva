#!/bin/sh
#-------------------------------------------------------------------------------
#
# Modified Ora2Pg generated script to use Docker georgmoser/ora2pg image
#
#-------------------------------------------------------------------------------
EXPORT_TYPE="SEQUENCE SEQUENCE_VALUES TABLE PACKAGE VIEW GRANT TRIGGER FUNCTION PROCEDURE TABLESPACE PARTITION TYPE MVIEW DBLINK SYNONYM DIRECTORY"
SOURCE_TYPE="PACKAGE VIEW TRIGGER FUNCTION PROCEDURE PARTITION TYPE MVIEW"
namespace="."
unit_cost=5

# Docker command prefix for ora2pg
DOCKER_CMD="docker run --rm --name ora2pg -v $(pwd):/workspace -w /workspace georgmoser/ora2pg"

# Generate reports
echo "Generating tables report..."
$DOCKER_CMD ora2pg -t SHOW_TABLE -c $namespace/config/ora2pg.conf > $namespace/reports/tables.txt

echo "Generating columns report..."
$DOCKER_CMD ora2pg -t SHOW_COLUMN -c $namespace/config/ora2pg.conf > $namespace/reports/columns.txt

echo "Generating migration assessment report..."
$DOCKER_CMD ora2pg -t SHOW_REPORT -c $namespace/config/ora2pg.conf --dump_as_html --cost_unit_value $unit_cost --estimate_cost > $namespace/reports/report.html

# Export schema objects
echo "Exporting schema objects..."
for etype in $(echo $EXPORT_TYPE | tr " " "\n")
do
        ltype=`echo $etype | tr '[:upper:]' '[:lower:]'`
        ltype=`echo $ltype | sed 's/y$/ie/'`
        ltype=`echo $ltype | sed 's/s$//'`
    if [ "$etype" = "TABLE" ]; then
        blob_to_lo=""
    else
        blob_to_lo=""
    fi
        echo "Running: Docker ora2pg -p -t $etype -o $ltype.sql -b $namespace/schema/${ltype}s -c $namespace/config/ora2pg.conf ${blob_to_lo}"
        $DOCKER_CMD ora2pg -p -t $etype -o $ltype.sql -b $namespace/schema/${ltype}s -c $namespace/config/ora2pg.conf ${blob_to_lo}
    ret=`grep "Nothing found" $namespace/schema/${ltype}s/$ltype.sql 2> /dev/null`
    if [ ! -z "$ret" ]; then
        rm $namespace/schema/${ltype}s/$ltype.sql
    fi
done

# Export source objects
echo "Exporting source objects..."
for etype in $(echo $SOURCE_TYPE | tr " " "\n")
do
        ltype=`echo $etype | tr '[:upper:]' '[:lower:]'`
        ltype=`echo $ltype | sed 's/y$/ie/'`
        echo "Running: Docker ora2pg -t $etype -o $ltype.sql -b $namespace/sources/${ltype}s -c $namespace/config/ora2pg.conf"
        $DOCKER_CMD ora2pg -t $etype -o $ltype.sql -b $namespace/sources/${ltype}s -c $namespace/config/ora2pg.conf
    ret=`grep "Nothing found" $namespace/sources/${ltype}s/$ltype.sql 2> /dev/null`
    if [ ! -z "$ret" ]; then
        rm $namespace/sources/${ltype}s/$ltype.sql
    fi
done

echo
echo
echo "To extract data use the following command:"
echo
echo "docker run --rm --name ora2pg -v \$(pwd):/workspace -w /workspace georgmoser/ora2pg ora2pg -t COPY -o data.sql -b $namespace/data -c $namespace/config/ora2pg.conf"
echo

exit 0
