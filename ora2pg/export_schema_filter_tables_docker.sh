#!/bin/sh
#-----------------------------------------------------------------------------------------------
#
# Modified Ora2Pg generated script to use Docker georgmoser/ora2pg image and filter tables
#
#-----------------------------------------------------------------------------------------------
EXPORT_TYPE="TABLE"
namespace="."
unit_cost=5

# Docker command prefix for ora2pg
DOCKER_CMD="docker run --rm --name ora2pg -v $(pwd):/workspace -w /workspace georgmoser/ora2pg"

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
        echo "Running: Docker ora2pg -p -t $etype -o $ltype.sql -b $namespace/schema/${ltype}s -c $namespace/config/ora2pg.conf -a \"$(cat $namespace/config/app_tables.txt)\" ${blob_to_lo}"
        $DOCKER_CMD ora2pg -p -t $etype -o $ltype.sql -b $namespace/schema/${ltype}s -c $namespace/config/ora2pg.conf -a "$(cat $namespace/config/app_tables.txt)" ${blob_to_lo}
    ret=`grep "Nothing found" $namespace/schema/${ltype}s/$ltype.sql 2> /dev/null`
    if [ ! -z "$ret" ]; then
        rm $namespace/schema/${ltype}s/$ltype.sql
    fi
done

exit 0
