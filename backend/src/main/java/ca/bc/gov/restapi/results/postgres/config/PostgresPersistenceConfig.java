package ca.bc.gov.restapi.results.postgres.config;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** This class holds persistence configurations for the Postgres database. */
@Configuration
public class PostgresPersistenceConfig {

  @Bean
  @ConfigurationProperties("spring.datasource.postgres")
  public DataSourceProperties postgresDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource postgresDataSource() {
    return postgresDataSourceProperties().initializeDataSourceBuilder().build();
  }
}
