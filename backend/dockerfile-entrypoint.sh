#!/bin/sh

java \
    -Djava.security.egd=file:/dev/./urandom \
    ${JAVA_OPTS} \
    -jar \
    -Dspring.profiles.active=prod \
    /app/artifacts/nr-results-backend.jar