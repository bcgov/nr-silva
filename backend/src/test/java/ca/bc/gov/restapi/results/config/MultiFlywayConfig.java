package ca.bc.gov.restapi.results.config;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiFlywayConfig {

  /*@Bean
  @ConfigurationProperties(prefix = "spring.datasource.postgres")
  public DataSource postgresDataSource() {
    return DataSourceBuilder.create().build();
  }*/

/*  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.oracle")
  public DataSource oracleDataSource() {
    return DataSourceBuilder.create().build();
  }*/

  /*@Bean
  public Flyway flywayPostgres(@Qualifier("postgresDataSource") DataSource postgresDataSource) {
    return Flyway.configure()
        .dataSource(postgresDataSource)
        .locations("classpath:db/migration/postgres")
        .load();
  }*/

  /*@Bean
  public Flyway flywayOracle(@Qualifier("oracleDataSource") DataSource oracleDataSource) {
    return Flyway.configure()
        .dataSource(oracleDataSource)
        .locations("classpath:db/migration/oracle")
        .schemas("THE")
        .load();
  }*/

}
