package ca.bc.gov.restapi.results.oracle.config;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OraclePersistenceConfig {

  @Bean
  @ConfigurationProperties("spring.datasource.oracle")
  public DataSourceProperties oracleDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Primary
  public DataSource oracleDataSource() {
    return oracleDataSourceProperties().initializeDataSourceBuilder().build();
  }
}
