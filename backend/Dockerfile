### Builder
FROM eclipse-temurin:17.0.8.1_1-jdk-jammy AS build

# Install Maven
RUN apt update -y && \
    apt install -y wget tar && \
    wget https://archive.apache.org/dist/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.tar.gz && \
    tar xzf apache-maven-3.8.6-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.8.6 /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/bin/mvn && \
    rm apache-maven-3.8.6-bin.tar.gz && \
    apt-get clean

# Add Maven to the PATH environment variable
ENV MAVEN_HOME=/opt/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# Copy
WORKDIR /app
COPY pom.xml ./
COPY src ./src

# Build
RUN mvn clean package -DskipTests -Dskip.unit.tests=true -Dspring-boot.run.profiles=prod

### Deployer
FROM eclipse-temurin:17.0.12_7-jdk-jammy AS deploy

# Copy
WORKDIR /app
COPY --from=build /app/target/nr-results-backend.jar ./artifacts/nr-results-backend.jar
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
ENTRYPOINT ["/app/dockerfile-entrypoint.sh"]
