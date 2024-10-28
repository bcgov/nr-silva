package ca.bc.gov.restapi.results.postgres.configuration;

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
public class PostgresPersistenceConfiguration {

  @Autowired private PostgresHikariConfiguration postgresHikariConfiguration;

  @Bean
  @ConfigurationProperties("spring.datasource.postgres")
  public DataSourceProperties postgresDataSourceProperties() {
    return new DataSourceProperties();
  }

  /** Creates a Postgres Datasource with all Hikari connection pool configuration. */
  @Bean
  public DataSource postgresDataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(postgresHikariConfiguration.getUrl());
    config.setUsername(postgresHikariConfiguration.getUsername());
    config.setPassword(postgresHikariConfiguration.getPassword());
    config.setDriverClassName(postgresHikariConfiguration.getDriverClassName());
    config.setConnectionTimeout(postgresHikariConfiguration.getConnectionTimeout());
    config.setIdleTimeout(postgresHikariConfiguration.getIdleTimeout());
    config.setMaxLifetime(postgresHikariConfiguration.getMaxLifetime());
    config.setKeepaliveTime(postgresHikariConfiguration.getKeepaliveTime());
    config.setPoolName(postgresHikariConfiguration.getPoolName());
    config.setMinimumIdle(postgresHikariConfiguration.getMinimumIdle());
    config.setMaximumPoolSize(postgresHikariConfiguration.getMaximumPoolSize());
    config.setLeakDetectionThreshold(postgresHikariConfiguration.getLeakDetectionThreshold());

    return new HikariDataSource(config);
  }
}
