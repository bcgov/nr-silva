package ca.bc.gov.restapi.results.configuration;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
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

  @Bean
  public Flyway flywayPostgres(@Qualifier("postgresDataSource") DataSource dataSource) {
    return Flyway.configure()
        //We build the datasource as the original Bean uses Hikari, and it failed due to Timeout
        .dataSource(dataSource)
        .locations("classpath:db/migration", "classpath:migration/postgres")
        .baselineOnMigrate(true)
        .load();
  }

  @Bean
  public Flyway flywayOracle(@Qualifier("oracleDataSource") DataSource dataSource) {
    return Flyway.configure()
        //We build the datasource as the original Bean uses Hikari, and it failed due to Timeout
        .dataSource(dataSource)
        .locations("classpath:migration/oracle")
        .schemas("THE")
        .load();
  }


}