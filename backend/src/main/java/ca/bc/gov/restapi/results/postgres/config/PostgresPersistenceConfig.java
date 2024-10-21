package ca.bc.gov.restapi.results.postgres.config;

import ca.bc.gov.restapi.results.common.config.SilvaHikariConfig;
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

  @Bean
  @ConfigurationProperties("spring.datasource.postgres")
  public SilvaHikariConfig postgresHikariConfig() {
    return new SilvaHikariConfig();
  }

  /** Creates a Postgres Datasource with all Hikari connection pool configuration. */
  @Bean
  public DataSource postgresDataSource(SilvaHikariConfig silvaHikariConfig) {
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(silvaHikariConfig.getUrl());
    config.setUsername(silvaHikariConfig.getUsername());
    config.setPassword(silvaHikariConfig.getPassword());
    config.setDriverClassName(silvaHikariConfig.getDriverClassName());
    config.setConnectionTimeout(silvaHikariConfig.getConnectionTimeout());
    config.setIdleTimeout(silvaHikariConfig.getIdleTimeout());
    config.setMaxLifetime(silvaHikariConfig.getMaxLifetime());
    config.setKeepaliveTime(silvaHikariConfig.getKeepaliveTime());
    config.setPoolName(silvaHikariConfig.getPoolName());
    config.setMinimumIdle(silvaHikariConfig.getMinimumIdle());
    config.setMaximumPoolSize(silvaHikariConfig.getMaximumPoolSize());
    config.setLeakDetectionThreshold(silvaHikariConfig.getLeakDetectionThreshold());

    return new HikariDataSource(config);
  }
}
