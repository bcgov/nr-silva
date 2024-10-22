package ca.bc.gov.restapi.results.postgres.configuration;

import ca.bc.gov.restapi.results.common.configuration.DataSourceConfiguration;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** This class holds persistence configurations for the Postgres database. */
@Configuration
public class PostgresPersistenceConfiguration {

  @Bean
  @ConfigurationProperties("spring.datasource.postgres")
  public DataSourceProperties postgresDataSourceProperties() {
    return new DataSourceProperties();
  }

  /** Creates a Postgres Datasource with all Hikari connection pool configuration. */
  @Bean
  public DataSource postgresDataSource(DataSourceConfiguration dataSourceConfiguration) {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(dataSourceConfiguration.getPostgres().getUrl());
    config.setUsername(dataSourceConfiguration.getPostgres().getUsername());
    config.setPassword(dataSourceConfiguration.getPostgres().getPassword());
    config.setDriverClassName(dataSourceConfiguration.getPostgres().getDriverClassName());
    config.setConnectionTimeout(dataSourceConfiguration.getPostgres().getConnectionTimeout());
    config.setIdleTimeout(dataSourceConfiguration.getPostgres().getIdleTimeout());
    config.setMaxLifetime(dataSourceConfiguration.getPostgres().getMaxLifetime());
    config.setKeepaliveTime(dataSourceConfiguration.getPostgres().getKeepaliveTime());
    config.setPoolName(dataSourceConfiguration.getPostgres().getPoolName());
    config.setMinimumIdle(dataSourceConfiguration.getPostgres().getMinimumIdle());
    config.setMaximumPoolSize(dataSourceConfiguration.getPostgres().getMaximumPoolSize());
    config.setLeakDetectionThreshold(dataSourceConfiguration.getPostgres().getLeakDetectionThreshold());

    return new HikariDataSource(config);
  }
}
