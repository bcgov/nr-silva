package ca.bc.gov.restapi.results.common.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

/**
 * Configures Flyway migration locations based on the active primary database.
 *
 * <p>In GraalVM native image, @ConditionalOnProperty is evaluated at AOT build time (not
 * runtime), so the PRIMARY_DB value must be set as a Docker build arg so Spring Boot AOT bakes
 * the correct beans into the binary. This class reads PRIMARY_DB at runtime (via
 * Environment.getProperty) which is safe — it's a plain value lookup, not a conditional.
 *
 * <p>Flyway's ClassPathScanner fails in native image with "unsupported protocol: resource".
 * {@link FlywayRuntimeHints} pre-registers migration resources with Spring Boot's AOT processor,
 * and {@link NativeImageResourceProvider} replaces Flyway's scanner with Spring's
 * ApplicationContext.getResources() which uses the AOT-generated resource index.
 */
@Slf4j
@Configuration
@ImportRuntimeHints(FlywayRuntimeHints.class)
public class FlywayConfiguration {

  @Bean
  public FlywayConfigurationCustomizer flywayConfigurationCustomizer(
      Environment env, ApplicationContext context) {
    return configuration -> {
      String primaryDb = env.getProperty("PRIMARY_DB",
          env.getProperty("server.primary-db", "oracle"));
      log.info("Configuring Flyway for primary database: {}", primaryDb);

      List<String> locations = new ArrayList<>();
      locations.add("classpath:db/migration");

      if ("postgres".equals(primaryDb)) {
        locations.add("classpath:db/migration-dev");
      } else if (!"oracle".equals(primaryDb)) {
        throw new IllegalStateException("Unsupported value for primary database configuration " +
            "(property 'server.primary-db' or env var 'PRIMARY_DB'): '" +
            primaryDb + "'. Expected one of: oracle, postgres."
        );
      }

      configuration
          .schemas("silva")
          .defaultSchema("silva")
          .locations(locations.toArray(String[]::new))
          .resourceProvider(new NativeImageResourceProvider(context, locations));
    };
  }
}
