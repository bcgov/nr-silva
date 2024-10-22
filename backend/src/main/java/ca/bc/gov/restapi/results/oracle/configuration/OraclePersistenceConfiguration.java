package ca.bc.gov.restapi.results.oracle.configuration;

import ca.bc.gov.restapi.results.common.configuration.DataSourceConfiguration;
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
public class OraclePersistenceConfiguration {


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
  public DataSource oracleDataSource(DataSourceConfiguration dataSourceConfiguration) {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(dataSourceConfiguration.getOracle().getUrl());
    config.setUsername(dataSourceConfiguration.getOracle().getUsername());
    config.setPassword(dataSourceConfiguration.getOracle().getPassword());
    config.setDriverClassName(dataSourceConfiguration.getOracle().getDriverClassName());
    config.setConnectionTimeout(dataSourceConfiguration.getOracle().getConnectionTimeout());
    config.setIdleTimeout(dataSourceConfiguration.getOracle().getIdleTimeout());
    config.setMaxLifetime(dataSourceConfiguration.getOracle().getMaxLifetime());
    config.setKeepaliveTime(dataSourceConfiguration.getOracle().getKeepaliveTime());
    config.setPoolName(dataSourceConfiguration.getOracle().getPoolName());
    config.setMinimumIdle(dataSourceConfiguration.getOracle().getMinimumIdle());
    config.setMaximumPoolSize(dataSourceConfiguration.getOracle().getMaximumPoolSize());
    config.setLeakDetectionThreshold(dataSourceConfiguration.getOracle().getLeakDetectionThreshold());

    return new HikariDataSource(config);
  }
}
