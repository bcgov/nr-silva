package ca.bc.gov.restapi.results.config;

import ca.bc.gov.restapi.results.oracle.config.OracleHikariConfig;
import ca.bc.gov.restapi.results.postgres.config.PostgresHikariConfig;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up multiple Flyway instances for different databases.
 * This resides inside the test package to avoid conflicts with the main application.
 * This class is used to configure Flyway for PostgreSQL and Oracle databases, so we can make use
 * of the testcontainers to recreate the expected database structure.
 */
@Configuration
public class MultiFlywayConfig {

  /**
   * Configures Flyway for PostgreSQL database.
   * This will overwrite the original Flyway configuration for PostgreSQL.
   * @param postgresHikariConfig the PostgreSQL Hikari configuration
   * @return the configured Flyway instance for PostgreSQL
   */
  @Bean
  public Flyway flywayPostgres(PostgresHikariConfig postgresHikariConfig) {
    return Flyway.configure()
        //We build the datasource as the original Bean uses Hikari, and it failed due to Timeout
        .dataSource(toDataSource(postgresHikariConfig))
        .locations("classpath:db/migration", "classpath:migration/postgres")
        .baselineOnMigrate(true)
        .load();
  }

  /**
   * Configures Flyway for Oracle database.
   * This will allow the test to be able to recreate the expected oracle database format.
   * @param oracleHikariConfig the Oracle Hikari configuration
   * @return the configured Flyway instance for Oracle
   */
  @Bean
  public Flyway flywayOracle(OracleHikariConfig oracleHikariConfig) {
    return Flyway.configure()
        //We build the datasource as the original Bean uses Hikari, and it failed due to Timeout
        .dataSource(toDataSource(oracleHikariConfig))
        .locations("classpath:migration/oracle")
        .schemas("THE")
        .load();
  }

  /**
   * Converts PostgreSQL Hikari configuration to a DataSource.
   * The original Bean found at {@link ca.bc.gov.restapi.results.postgres.config.PostgresPersistenceConfig}
   * was failing due to a timeout caused by HikariCP
   * @param postgresHikariConfig the PostgreSQL Hikari configuration
   * @return the DataSource for PostgreSQL
   */
  private DataSource toDataSource(PostgresHikariConfig postgresHikariConfig) {
    return DataSourceBuilder.create()
        .url(postgresHikariConfig.getUrl())
        .username(postgresHikariConfig.getUsername())
        .password(postgresHikariConfig.getPassword())
        .driverClassName(postgresHikariConfig.getDriverClassName())
        .build();
  }

  /**
   * Converts Oracle Hikari configuration to a DataSource.
   * The original Bean found at {@link ca.bc.gov.restapi.results.oracle.config.OraclePersistenceConfig}
   * was failing due to a timeout caused by HikariCP.
   * @param oracleHikariConfig the Oracle Hikari configuration
   * @return the DataSource for Oracle
   */
  private DataSource toDataSource(OracleHikariConfig oracleHikariConfig) {
    return DataSourceBuilder.create()
        .url(oracleHikariConfig.getUrl())
        .username(oracleHikariConfig.getUsername())
        .password(oracleHikariConfig.getPassword())
        .driverClassName(oracleHikariConfig.getDriverClassName())
        .build();
  }

}