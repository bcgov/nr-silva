package ca.bc.gov.restapi.results.oracle.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** This class holds configurations for the Oracle Hikari connection pool. */
@Data
@Configuration
@ConfigurationProperties("spring.datasource.oracle")
public class OracleHikariConfiguration {

  private String driverClassName;
  private String url;
  private String username;
  private String password;
  private long connectionTimeout;
  private long idleTimeout;
  private long maxLifetime;
  private long keepaliveTime;
  private String poolName;
  private int minimumIdle;
  private int maximumPoolSize;
  private long leakDetectionThreshold;
}
