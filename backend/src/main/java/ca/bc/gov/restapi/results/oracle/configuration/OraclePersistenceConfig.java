package ca.bc.gov.restapi.results.oracle.configuration;

import ca.bc.gov.restapi.results.common.configuration.DataSourceConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * This class holds persistence configurations for the Oracle database.
 */
@Configuration
public class OraclePersistenceConfig {


  @Bean
  @ConfigurationProperties("spring.datasource.oracle")
  public DataSourceProperties oracleDataSourceProperties() {
    return new DataSourceProperties();
  }

  /**
   * Creates a Postgres Datasource with all Hikari connection pool configuration.
   */
  @Bean
  @Primary
  public DataSource oracleDataSource(DataSourceConfig dataSourceConfig) {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(dataSourceConfig.getOracle().getUrl());
    config.setUsername(dataSourceConfig.getOracle().getUsername());
    config.setPassword(dataSourceConfig.getOracle().getPassword());
    config.setDriverClassName(dataSourceConfig.getOracle().getDriverClassName());
    config.setConnectionTimeout(dataSourceConfig.getOracle().getConnectionTimeout());
    config.setIdleTimeout(dataSourceConfig.getOracle().getIdleTimeout());
    config.setMaxLifetime(dataSourceConfig.getOracle().getMaxLifetime());
    config.setKeepaliveTime(dataSourceConfig.getOracle().getKeepaliveTime());
    config.setPoolName(dataSourceConfig.getOracle().getPoolName());
    config.setMinimumIdle(dataSourceConfig.getOracle().getMinimumIdle());
    config.setMaximumPoolSize(dataSourceConfig.getOracle().getMaximumPoolSize());
    config.setLeakDetectionThreshold(dataSourceConfig.getOracle().getLeakDetectionThreshold());

    return new HikariDataSource(config);
  }
}
