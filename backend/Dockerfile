### Builder
FROM ghcr.io/graalvm/native-image:22.3.3 AS build

# Copy
WORKDIR /app
COPY pom.xml mvnw ./
COPY src ./src
COPY .mvn/ ./.mvn

# Build
RUN ./mvnw -Pnative -DskipTests -Dskip.unit.tests=true -Dspring-boot.run.profiles=prod native:compile

### Deployer
FROM gcr.io/distroless/java-base:nonroot AS deploy

# Copy
WORKDIR /app
COPY --from=build /app/target/nr-results-backend ./nr-results-backend

COPY dockerfile-entrypoint.sh ./
RUN mkdir config dump public && \
    chmod -R g+w . && \
    chmod g+x dockerfile-entrypoint.sh && \
    chmod g+w ${JAVA_HOME}/lib/security/cacerts

# Port and health check
USER 1001
EXPOSE 8080
HEALTHCHECK CMD curl -f http://localhost:8080/actuator/health | grep '"status":"UP"'

# Startup
ENTRYPOINT ["/app/nr-results-backend","--spring.profiles.active=container,prod"]
