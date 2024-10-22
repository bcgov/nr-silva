package ca.bc.gov.restapi.results.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

/**
 * This class holds configurations for the Hikari connection pool.
 */
@Getter
@Setter
@Configuration
public class SilvaHikariConfiguration {

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
