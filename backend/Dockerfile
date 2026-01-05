### Builder
FROM ghcr.io/graalvm/native-image-community:25.0.1 AS build

# Copy
WORKDIR /app
COPY pom.xml mvnw ./
COPY src ./src
COPY .mvn/ ./.mvn
COPY settings.xml ./settings.xml

# Build
RUN ./mvnw -s settings.xml clean package -Pnative -DskipTests -Dskip.unit.tests=true -Dspring-boot.run.profiles=prod

### Deployer
FROM gcr.io/distroless/java-base:nonroot
ARG PORT=8080

# Copy
WORKDIR /app
COPY --from=build /app/target/results ./results

# User, port and health check
USER 1001
EXPOSE ${PORT}
HEALTHCHECK CMD curl -f http://localhost:${PORT}/actuator/health | grep '"status":"UP"'

ENV SPRING_PROFILES_ACTIVE=container,prod

# Startup
ENTRYPOINT ["/app/results"]
