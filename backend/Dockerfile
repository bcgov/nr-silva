### Builder
FROM ghcr.io/graalvm/native-image:ol8-java17-22.3.3 AS build

# Copy
WORKDIR /app
COPY pom.xml mvnw ./
COPY src ./src
COPY .mvn/ ./.mvn

# Build
RUN ./mvnw package -Pnative -DskipTests

### Deployer
FROM gcr.io/distroless/java-base:nonroot AS deploy
ARG PORT=8080

# Copy
WORKDIR /app
COPY --from=build /app/target/results ./results

# Port and health check
USER 1001
EXPOSE ${PORT}
HEALTHCHECK CMD curl -f http://localhost:${PORT}/actuator/health | grep '"status":"UP"'

# Startup
ENTRYPOINT ["/app/results"]
