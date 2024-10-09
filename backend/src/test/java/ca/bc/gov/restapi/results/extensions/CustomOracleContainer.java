package ca.bc.gov.restapi.results.extensions;

import java.time.Duration;
import java.util.UUID;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * CustomOracleContainer extends OracleContainer to provide a customized Oracle database container.
 * This is because by default, the library expects the Oracle database to come from a different
 * image.
 */
public class CustomOracleContainer extends OracleContainer {

  /**
   * Constructs a CustomOracleContainer with predefined settings. Sets the Docker image, database
   * name, username, and a random password.
   */
  public CustomOracleContainer() {
    super(
        DockerImageName
            .parse("gvenzl/oracle-free:23.5-slim-faststart")
            .asCompatibleSubstituteFor("gvenzl/oracle-xe")
    );

    this.withDatabaseName("legacyfsa")
        .withUsername("THE")
        .withPassword(UUID.randomUUID().toString().substring(24));
  }

  /**
   * Overrides the waitUntilContainerStarted method to set a custom startup timeout. The Oracle
   * image tends to be slow, so we set a longer timeout.
   */
  @Override
  protected void waitUntilContainerStarted() {
    getWaitStrategy()
        .withStartupTimeout(Duration.ofMinutes(10))
        .waitUntilReady(this);
  }

}
