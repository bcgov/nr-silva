package ca.bc.gov.restapi.results.common.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class FlywayConfiguration {

  @Bean
  public FlywayConfigurationCustomizer flywayConfigurationCustomizer(Environment env) {
    return configuration -> {
      String primaryDatabase = env.getProperty("server.primary-db",
          env.getProperty("PRIMARY_DB", "oracle"));
      log.info("Configuring Flyway for primary database: {}", primaryDatabase);
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
