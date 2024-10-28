package ca.bc.gov.restapi.results.config;

import ca.bc.gov.restapi.results.oracle.configuration.OracleHikariConfiguration;
import ca.bc.gov.restapi.results.oracle.configuration.OraclePersistenceConfiguration;
import ca.bc.gov.restapi.results.postgres.configuration.PostgresHikariConfiguration;
import ca.bc.gov.restapi.results.postgres.configuration.PostgresPersistenceConfiguration;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up multiple Flyway instances for different databases. This
 * resides inside the test package to avoid conflicts with the main application. This class is used
 * to configure Flyway for PostgreSQL and Oracle databases, so we can make use of the testcontainers
 * to recreate the expected database structure.
 */
@Configuration
public class MultiFlywayConfig {

  /**
   * Configures Flyway for PostgreSQL database. This will overwrite the original Flyway
   * configuration for PostgreSQL.
   *
   * @param postgresHikariConfiguration the PostgreSQL Hikari configuration
   * @return the configured Flyway instance for PostgreSQL
   */
  @Bean
  public Flyway flywayPostgres(PostgresHikariConfiguration postgresHikariConfiguration) {
    return Flyway.configure()
        //We build the datasource as the original Bean uses Hikari, and it failed due to Timeout
        .dataSource(toDataSource(postgresHikariConfiguration))
        .locations("classpath:db/migration", "classpath:migration/postgres")
        .baselineOnMigrate(true)
        .load();
  }

  /**
   * Configures Flyway for Oracle database. This will allow the test to be able to recreate the
   * expected oracle database format.
   *
   * @param oracleHikariConfiguration the Oracle Hikari configuration
   * @return the configured Flyway instance for Oracle
   */
  @Bean
  public Flyway flywayOracle(OracleHikariConfiguration oracleHikariConfiguration) {
    return Flyway.configure()
        //We build the datasource as the original Bean uses Hikari, and it failed due to Timeout
        .dataSource(toDataSource(oracleHikariConfiguration))
        .locations("classpath:migration/oracle")
        .schemas("THE")
        .load();
  }

  /**
   * Converts PostgreSQL Hikari configuration to a DataSource. The original Bean found at
   * {@link PostgresPersistenceConfiguration} was failing due to
   * a timeout caused by HikariCP
   *
   * @param postgresHikariConfiguration the PostgreSQL Hikari configuration
   * @return the DataSource for PostgreSQL
   */
  private DataSource toDataSource(PostgresHikariConfiguration postgresHikariConfiguration) {
    return DataSourceBuilder.create()
        .url(postgresHikariConfiguration.getUrl())
        .username(postgresHikariConfiguration.getUsername())
        .password(postgresHikariConfiguration.getPassword())
        .driverClassName(postgresHikariConfiguration.getDriverClassName())
        .build();
  }

  /**
   * Converts Oracle Hikari configuration to a DataSource. The original Bean found at
   * {@link OraclePersistenceConfiguration} was failing due to a
   * timeout caused by HikariCP.
   *
   * @param oracleHikariConfiguration the Oracle Hikari configuration
   * @return the DataSource for Oracle
   */
  private DataSource toDataSource(OracleHikariConfiguration oracleHikariConfiguration) {
    return DataSourceBuilder.create()
        .url(oracleHikariConfiguration.getUrl())
        .username(oracleHikariConfiguration.getUsername())
        .password(oracleHikariConfiguration.getPassword())
        .driverClassName(oracleHikariConfiguration.getDriverClassName())
        .build();
  }

}