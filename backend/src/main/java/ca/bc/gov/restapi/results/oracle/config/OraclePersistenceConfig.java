package ca.bc.gov.restapi.results.oracle.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/** This class holds persistence configurations for the Oracle database. */
@Configuration
public class OraclePersistenceConfig {

  @Autowired private OracleHikariConfig oracleHikariConfig;

  @Bean
  @ConfigurationProperties("spring.datasource.oracle")
  public DataSourceProperties oracleDataSourceProperties() {
    return new DataSourceProperties();
  }

  /** Creates a Postgres Datasource with all Hikari connection pool configuration. */
  @Bean
  @Primary
  public DataSource oracleDataSource() {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(oracleHikariConfig.getUrl());
    config.setUsername(oracleHikariConfig.getUsername());
    config.setPassword(oracleHikariConfig.getPassword());
    config.setDriverClassName(oracleHikariConfig.getDriverClassName());
    config.setConnectionTimeout(oracleHikariConfig.getConnectionTimeout());
    config.setIdleTimeout(oracleHikariConfig.getIdleTimeout());
    config.setMaxLifetime(oracleHikariConfig.getMaxLifetime());
    config.setKeepaliveTime(oracleHikariConfig.getKeepaliveTime());
    config.setPoolName(oracleHikariConfig.getPoolName());
    config.setMinimumIdle(oracleHikariConfig.getMinimumIdle());
    config.setMaximumPoolSize(oracleHikariConfig.getMaximumPoolSize());
    config.setLeakDetectionThreshold(oracleHikariConfig.getLeakDetectionThreshold());

    return new HikariDataSource(config);
  }
}
