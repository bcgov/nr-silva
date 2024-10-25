package ca.bc.gov.restapi.results.extensions;

import java.util.UUID;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.oracle.OracleContainer;

/**
 * Abstract base class for integration tests using Testcontainers for PostgreSQL and Oracle
 * databases. This class instantiate and manage the ephemeral databases for the tests. This allows
 * the application to run against the real database engines without the need to manually spin them
 * up. It will also avoid conflicts in case of a database instance running in the host machine. It
 * also helps to keep the tests isolated and avoid side effects, such as data type
 * incompatibilities.
 */
@Testcontainers
@ExtendWith({SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public abstract class AbstractTestContainerIntegrationTest {

  /**
   * PostgreSQL container instance.
   */
  static final PostgreSQLContainer postgres;
  /**
   * Oracle container instance.
   */
  static final OracleContainer oracle;

  // Static fields declared like this are instantiated first by the JVM
  static {
    postgres = new PostgreSQLContainer("postgres:13")
        .withDatabaseName("silva")
        .withUsername("silva")
        .withPassword(UUID.randomUUID().toString());
    oracle = new CustomOracleContainer();

    postgres.start();
    oracle.start();
  }

  @Autowired
  private Flyway flywayPostgres;

  @Autowired
  private Flyway flywayOracle;

  /**
   * Migrate the databases using Flyway before each test. As we're using flyway, there's no need to
   * worry about duplicate insertion
   */
  @BeforeEach
  public void setUp() {
    flywayPostgres.migrate();
    flywayOracle.migrate();
  }

  /**
   * Register dynamic properties from the testcontainers. This will overwrite the application
   * properties for the databases with the testcontainers configuration. allowing the application to
   * connect to the ephemeral databases. As the username and password is randomly generated, there's
   * no need to worry about conflicts.
   *
   * @param registry the dynamic property registry from spring itself
   */
  @DynamicPropertySource
  static void registerDynamicProperties(DynamicPropertyRegistry registry) {
    // Overwrite the Postgres datasource with the testcontainer configuration
    registry.add("spring.datasource.postgres.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.postgres.username", postgres::getUsername);
    registry.add("spring.datasource.postgres.password", postgres::getPassword);
    // Overwrite the Flyway for Postgres with the testcontainer configuration
    registry.add("spring.flyway.postgres.url", postgres::getJdbcUrl);
    registry.add("spring.flyway.postgres.user", postgres::getUsername);
    registry.add("spring.flyway.postgres.password", postgres::getPassword);
    // Overwrite the Oracle datasource with the testcontainer configuration
    registry.add("spring.datasource.oracle.url", oracle::getJdbcUrl);
    registry.add("spring.datasource.oracle.username", oracle::getUsername);
    registry.add("spring.datasource.oracle.password", oracle::getPassword);
    // Overwrite the Flyway for Oracle with the testcontainer configuration
    registry.add("spring.flyway.oracle.url", oracle::getJdbcUrl);
    registry.add("spring.flyway.oracle.user", oracle::getUsername);
    registry.add("spring.flyway.oracle.password", oracle::getPassword);
  }
}

