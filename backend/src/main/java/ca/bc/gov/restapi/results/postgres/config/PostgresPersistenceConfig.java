package ca.bc.gov.restapi.results.postgres.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** This class holds persistence configurations for the Postgres database. */
@Configuration
public class PostgresPersistenceConfig {

  @Autowired private PostgresHikariConfig postgresHikariConfig;

  @Bean
  @ConfigurationProperties("spring.datasource.postgres")
  public DataSourceProperties postgresDataSourceProperties() {
    return new DataSourceProperties();
  }

  /** Creates a Postgres Datasource with all Hikari connection pool configuration. */
  @Bean
  public DataSource postgresDataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(postgresHikariConfig.getUrl());
    config.setUsername(postgresHikariConfig.getUsername());
    config.setPassword(postgresHikariConfig.getPassword());
    config.setDriverClassName(postgresHikariConfig.getDriverClassName());
    config.setConnectionTimeout(postgresHikariConfig.getConnectionTimeout());
    config.setIdleTimeout(postgresHikariConfig.getIdleTimeout());
    config.setMaxLifetime(postgresHikariConfig.getMaxLifetime());
    config.setKeepaliveTime(postgresHikariConfig.getKeepaliveTime());
    config.setPoolName(postgresHikariConfig.getPoolName());
    config.setMinimumIdle(postgresHikariConfig.getMinimumIdle());
    config.setMaximumPoolSize(postgresHikariConfig.getMaximumPoolSize());

    return new HikariDataSource(config);
  }
}
