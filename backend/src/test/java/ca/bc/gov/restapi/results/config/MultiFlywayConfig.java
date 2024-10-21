package ca.bc.gov.restapi.results.config;

import ca.bc.gov.restapi.results.common.config.DataSourceConfig;
import ca.bc.gov.restapi.results.common.config.SilvaHikariConfig;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
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
   * @param dataSourceConfig the data source configuration
   * @return the configured Flyway instance for PostgreSQL
   */
  @Bean
  public Flyway flywayPostgres(DataSourceConfig dataSourceConfig) {
    return Flyway.configure()
        //We build the datasource as the original Bean uses Hikari, and it failed due to Timeout
        .dataSource(toDataSource(dataSourceConfig.getPostgres()))
        .locations("classpath:db/migration", "classpath:migration/postgres")
        .baselineOnMigrate(true)
        .load();
  }

  /**
   * Configures Flyway for Oracle database. This will allow the test to be able to recreate the
   * expected oracle database format.
   *
   * @param dataSourceConfig the data source configuration
   * @return the configured Flyway instance for Oracle
   */
  @Bean
  public Flyway flywayOracle(DataSourceConfig dataSourceConfig) {
    return Flyway.configure()
        //We build the datasource as the original Bean uses Hikari, and it failed due to Timeout
        .dataSource(toDataSource(dataSourceConfig.getOracle()))
        .locations("classpath:migration/oracle")
        .schemas("THE")
        .load();
  }

  /**
   * Converts Hikari configuration to a DataSource. The original Bean found at
   * {@link ca.bc.gov.restapi.results.postgres.config.PostgresPersistenceConfig} was failing due to
   * a timeout caused by HikariCP
   *
   * @param silvaHikariConfig the Hikari configuration
   * @return the DataSource
   */
  private DataSource toDataSource(SilvaHikariConfig silvaHikariConfig) {
    return DataSourceBuilder.create()
        .url(silvaHikariConfig.getUrl())
        .username(silvaHikariConfig.getUsername())
        .password(silvaHikariConfig.getPassword())
        .driverClassName(silvaHikariConfig.getDriverClassName())
        .build();
  }

}