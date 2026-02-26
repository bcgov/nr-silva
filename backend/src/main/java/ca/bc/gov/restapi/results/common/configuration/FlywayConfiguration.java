package ca.bc.gov.restapi.results.common.configuration;

import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FlywayConfiguration {

  @Bean
  public FlywayConfigurationCustomizer flywayConfigurationCustomizer(Environment env) {
    return configuration -> {
       String primaryDatabase = env.getProperty("PRIMARY_DB", "oracle");
       
      if (primaryDatabase.equals("oracle")) {
        configuration.locations("classpath:db/migration");
      } else if (primaryDatabase.equals("postgres")) {
        configuration.locations("classpath:db/migration", "classpath:db/migration-dev");
      } else {
        throw new RuntimeException("Unsupported database: " + primaryDatabase);
      }
    };
  }
}
