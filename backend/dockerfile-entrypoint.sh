#!/bin/sh

java -cp /app/artifacts/ InstallCert --quiet "${DATABASE_HOST}:${DATABASE_PORT}"
keytool -exportcert -alias "${DATABASE_HOST}-1" -keystore jssecacerts -storepass changeit -file oracle.cer
keytool -importcert -alias orakey -noprompt -cacerts -storepass changeit -file oracle.cer

java \
    -Djava.security.egd=file:/dev/./urandom \
    ${JAVA_OPTS} \
    -jar \
    -Dspring.profiles.active=prod \
    /app/artifacts/nr-silva-backend.jar