package ca.bc.gov.restapi.results.configuration;

import ca.bc.gov.restapi.results.common.configuration.DataSourceConfiguration;
import ca.bc.gov.restapi.results.common.configuration.SilvaHikariConfiguration;
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
   * @param dataSourceConfiguration the data source configuration
   * @return the configured Flyway instance for PostgreSQL
   */
  @Bean
  public Flyway flywayPostgres(DataSourceConfiguration dataSourceConfiguration) {
    return Flyway.configure()
        //We build the datasource as the original Bean uses Hikari, and it failed due to Timeout
        .dataSource(toDataSource(dataSourceConfiguration.getPostgres()))
        .locations("classpath:db/migration", "classpath:migration/postgres")
        .baselineOnMigrate(true)
        .load();
  }

  /**
   * Configures Flyway for Oracle database. This will allow the test to be able to recreate the
   * expected oracle database format.
   *
   * @param dataSourceConfiguration the data source configuration
   * @return the configured Flyway instance for Oracle
   */
  @Bean
  public Flyway flywayOracle(DataSourceConfiguration dataSourceConfiguration) {
    return Flyway.configure()
        //We build the datasource as the original Bean uses Hikari, and it failed due to Timeout
        .dataSource(toDataSource(dataSourceConfiguration.getOracle()))
        .locations("classpath:migration/oracle")
        .schemas("THE")
        .load();
  }

  /**
   * Converts Hikari configuration to a DataSource. The original Bean found at
   * {@link PostgresPersistenceConfiguration} was failing due to
   * a timeout caused by HikariCP
   *
   * @param silvaHikariConfiguration the Hikari configuration
   * @return the DataSource
   */
  private DataSource toDataSource(SilvaHikariConfiguration silvaHikariConfiguration) {
    return DataSourceBuilder.create()
        .url(silvaHikariConfiguration.getUrl())
        .username(silvaHikariConfiguration.getUsername())
        .password(silvaHikariConfiguration.getPassword())
        .driverClassName(silvaHikariConfiguration.getDriverClassName())
        .build();
  }

}