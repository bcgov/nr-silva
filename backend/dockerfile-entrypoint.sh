#!/bin/sh

java \
    -Djava.security.egd=file:/dev/./urandom \
    ${JAVA_OPTS} \
    -jar \
    /app/artifacts/nr-results-backend.jar