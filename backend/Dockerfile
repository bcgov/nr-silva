### Builder
FROM ghcr.io/graalvm/native-image:ol8-java17-22.3.3 AS build

# Copy
WORKDIR /app
COPY pom.xml mvnw ./
COPY src ./src
COPY .mvn/ ./.mvn
COPY InstallCert.java .

# Build
RUN ./mvnw package -Pnative -DskipTests -Dskip.unit.tests=true -Dspring-boot.run.profiles=prod && \
  javac InstallCert.java

### Deployer
FROM eclipse-temurin:17.0.9_9-jdk-jammy AS deploy
ARG PORT=8080

# Copy
WORKDIR /app
COPY --from=build /app/target/*.jar /app/*.class ./artifacts/
COPY dockerfile-entrypoint.sh ./
RUN mkdir config dump public && \
    chmod -R g+w . && \
    chmod g+x dockerfile-entrypoint.sh && \
    chmod g+w ${JAVA_HOME}/lib/security/cacerts

# Port and health check
USER 1001
EXPOSE ${PORT}
HEALTHCHECK CMD curl -f http://localhost:${PORT}/actuator/health | grep '"status":"UP"'

# Startup
ENTRYPOINT ["/app/dockerfile-entrypoint.sh"]