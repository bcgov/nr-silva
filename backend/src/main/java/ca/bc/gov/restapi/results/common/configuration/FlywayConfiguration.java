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
      String primaryDatabase = env.getProperty("server.primary-db",
          env.getProperty("PRIMARY_DB", "oracle"));
      if (primaryDatabase.equals("oracle")) {
        configuration.locations("classpath:db/migration");
      } else if (primaryDatabase.equals("postgres")) {
        configuration.locations("classpath:db/migration", "classpath:db/migration-dev");
      } else {
        throw new IllegalStateException("Unsupported value for primary database configuration " +
            "(property 'server.primary-db' or env var 'PRIMARY_DB'): '" +
            primaryDatabase + "'. Expected one of: oracle, postgres."
        );
      }
    };
  }
}
