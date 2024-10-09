package ca.bc.gov.restapi.results.config;

import ca.bc.gov.restapi.results.oracle.config.OracleHikariConfig;
import ca.bc.gov.restapi.results.postgres.config.PostgresHikariConfig;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultiFlywayConfig {

  @Bean
  public Flyway flywayPostgres(PostgresHikariConfig postgresHikariConfig) {
    return Flyway.configure()
        .dataSource(toDataSource(postgresHikariConfig))
        .locations("classpath:db/migration", "classpath:migration/postgres")
        .baselineOnMigrate(true)
        .load();
  }

  @Bean
  public Flyway flywayOracle(OracleHikariConfig postgresHikariConfig) {
    return Flyway.configure()
        .dataSource(toDataSource(postgresHikariConfig))
        .locations("classpath:migration/oracle")
        .schemas("THE")
        .load();
  }

  private DataSource toDataSource(PostgresHikariConfig postgresHikariConfig) {
    return DataSourceBuilder.create()
        .url(postgresHikariConfig.getUrl())
        .username(postgresHikariConfig.getUsername())
        .password(postgresHikariConfig.getPassword())
        .driverClassName(postgresHikariConfig.getDriverClassName())
        .build();
  }

  private DataSource toDataSource(OracleHikariConfig postgresHikariConfig) {
    return DataSourceBuilder.create()
        .url(postgresHikariConfig.getUrl())
        .username(postgresHikariConfig.getUsername())
        .password(postgresHikariConfig.getPassword())
        .driverClassName(postgresHikariConfig.getDriverClassName())
        .build();
  }

}
