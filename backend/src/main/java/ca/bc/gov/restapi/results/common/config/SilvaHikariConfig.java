package ca.bc.gov.restapi.results.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

/**
 * This class holds configurations for the Hikari connection pool.
 */
@Getter
@Setter
@Configuration
public class SilvaHikariConfig {

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
