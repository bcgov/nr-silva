package ca.bc.gov.restapi.results.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** This class gets configuration from the application.yml file for both databases. */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfiguration {

  private String driverClassName;
  private String url;
  private String username;
  private String password;
}
