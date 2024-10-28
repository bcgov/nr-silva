package ca.bc.gov.restapi.results.oracle.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private OracleHikariConfiguration oracleHikariConfiguration;

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
  public DataSource oracleDataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(oracleHikariConfiguration.getUrl());
    config.setUsername(oracleHikariConfiguration.getUsername());
    config.setPassword(oracleHikariConfiguration.getPassword());
    config.setDriverClassName(oracleHikariConfiguration.getDriverClassName());
    config.setConnectionTimeout(oracleHikariConfiguration.getConnectionTimeout());
    config.setIdleTimeout(oracleHikariConfiguration.getIdleTimeout());
    config.setMaxLifetime(oracleHikariConfiguration.getMaxLifetime());
    config.setKeepaliveTime(oracleHikariConfiguration.getKeepaliveTime());
    config.setPoolName(oracleHikariConfiguration.getPoolName());
    config.setMinimumIdle(oracleHikariConfiguration.getMinimumIdle());
    config.setMaximumPoolSize(oracleHikariConfiguration.getMaximumPoolSize());
    config.setLeakDetectionThreshold(oracleHikariConfiguration.getLeakDetectionThreshold());

    return new HikariDataSource(config);
  }
}
