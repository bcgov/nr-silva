package ca.bc.gov.restapi.results.extensions;

import java.util.UUID;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.oracle.OracleContainer;
import org.testcontainers.utility.DockerImageName;

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

  static final PostgreSQLContainer postgres;
  static final OracleContainer oracle;
  static final Flyway flywayPostgres;
  static final Flyway flywayOracle;

  // Static fields declared like this are instantiated first by the JVM
  static {
    String env = System.getenv("FLYWAY_ENVIRONMENT");
    if (env == null || env.isBlank()) {
      env = "prod";
    }
    String[] postgresLocations;
    if ("prod".equals(env)) {
      postgresLocations = new String[] {
          "classpath:db/migration",
          "classpath:migration/postgres/default"
      };
    } else {
        postgresLocations = new String[] {
            "classpath:db/migration",
            "classpath:db/migration-dev",
            "classpath:migration/postgres/default",
            "classpath:migration/postgres/dev"
        };
    }

    postgres = new PostgreSQLContainer(
        DockerImageName.parse("postgis/postgis:17-master")
            .asCompatibleSubstituteFor("postgres"))
        .withDatabaseName("silva")
        .withUsername("silva")
        .withPassword(UUID.randomUUID().toString());
    oracle = new CustomOracleContainer();

    postgres.start();
    oracle.start();

    flywayPostgres =
        Flyway
            .configure()
            .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
            .locations(postgresLocations)
            .baselineOnMigrate(true)
            .load();

    flywayOracle =
        Flyway
            .configure()
            .dataSource(oracle.getJdbcUrl(), oracle.getUsername(), oracle.getPassword())
            .locations("classpath:migration/oracle")
            .schemas("THE")
            .baselineOnMigrate(true)
            .load();
  }

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
    registry.add("spring.datasource.jdbcUrl", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.hikari.username", postgres::getUsername);
    registry.add("spring.datasource.hikari.password", postgres::getPassword);

    registry.add("spring.oracle.url", oracle::getJdbcUrl);
    registry.add("spring.oracle.jdbcUrl", oracle::getJdbcUrl);
    registry.add("spring.oracle.username", oracle::getUsername);
    registry.add("spring.oracle.password", oracle::getPassword);
  }
}

