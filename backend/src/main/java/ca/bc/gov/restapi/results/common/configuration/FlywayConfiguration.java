package ca.bc.gov.restapi.results.common.configuration;

import org.springframework.boot.flyway.autoconfigure.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FlywayConfiguration {

  @Bean
  public FlywayConfigurationCustomizer flywayConfigurationCustomizer(Environment env) {
    return configuration -> {
       String environmentName = env.getProperty("FLYWAY_ENVIRONMENT", "prod");

      if (environmentName.equals("prod")) {
        configuration.locations("classpath:db/migration");
      } else {
        configuration.locations("classpath:db/migration", "classpath:db/migration-dev");
      }
    };
  }
}
