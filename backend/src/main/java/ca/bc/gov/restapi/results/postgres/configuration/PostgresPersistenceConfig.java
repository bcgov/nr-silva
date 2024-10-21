package ca.bc.gov.restapi.results.postgres.configuration;

import ca.bc.gov.restapi.results.common.configuration.DataSourceConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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

  /** Creates a Postgres Datasource with all Hikari connection pool configuration. */
  @Bean
  public DataSource postgresDataSource(DataSourceConfig dataSourceConfig) {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(dataSourceConfig.getPostgres().getUrl());
    config.setUsername(dataSourceConfig.getPostgres().getUsername());
    config.setPassword(dataSourceConfig.getPostgres().getPassword());
    config.setDriverClassName(dataSourceConfig.getPostgres().getDriverClassName());
    config.setConnectionTimeout(dataSourceConfig.getPostgres().getConnectionTimeout());
    config.setIdleTimeout(dataSourceConfig.getPostgres().getIdleTimeout());
    config.setMaxLifetime(dataSourceConfig.getPostgres().getMaxLifetime());
    config.setKeepaliveTime(dataSourceConfig.getPostgres().getKeepaliveTime());
    config.setPoolName(dataSourceConfig.getPostgres().getPoolName());
    config.setMinimumIdle(dataSourceConfig.getPostgres().getMinimumIdle());
    config.setMaximumPoolSize(dataSourceConfig.getPostgres().getMaximumPoolSize());
    config.setLeakDetectionThreshold(dataSourceConfig.getPostgres().getLeakDetectionThreshold());

    return new HikariDataSource(config);
  }
}
