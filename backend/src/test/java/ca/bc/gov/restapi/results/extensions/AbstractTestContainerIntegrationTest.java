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
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ExtendWith({SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
public abstract class AbstractTestContainerIntegrationTest {

  static final PostgreSQLContainer postgres;
  static final OracleContainer oracle;

  static {
    postgres = new PostgreSQLContainer("postgres:13")
        .withDatabaseName("silva")
        .withUsername("silva")
        .withPassword(UUID.randomUUID().toString());
    oracle = new CustomOracleContainer();

    postgres.start();
    oracle.start();
  }

/*  @Autowired
  private Flyway flywayPostgres;*/

/*  @Autowired
  private Flyway flywayOracle;*/

  @BeforeEach
  public void setUp() {
    //flywayPostgres.migrate();
    //flywayOracle.migrate();
  }

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
    //registry.add("spring.datasource.oracle.url", postgres::getJdbcUrl);
    //registry.add("spring.datasource.oracle.username", postgres::getUsername);
    //registry.add("spring.datasource.oracle.password", postgres::getPassword);
    // Overwrite the Flyway for Oracle with the testcontainer configuration
    //registry.add("spring.flyway.oracle.url", postgres::getJdbcUrl);
    //registry.add("spring.flyway.oracle.user", postgres::getUsername);
    //registry.add("spring.flyway.oracle.password", postgres::getPassword);
  }
}

