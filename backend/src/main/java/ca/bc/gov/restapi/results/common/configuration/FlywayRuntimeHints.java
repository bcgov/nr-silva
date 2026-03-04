package ca.bc.gov.restapi.results.common.configuration;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Registers Flyway SQL migration resources with Spring Boot's native image AOT processor.
 *
 * <p>In GraalVM native image, Flyway's {@code ClassPathScanner} fails with
 * "unsupported protocol: resource" because it uses {@code ClassLoader.getResources(directory)}
 * for directory enumeration, which is not supported in native image. Spring Boot 3.x AOT
 * processes these hints at build time, creating a resource index that makes
 * {@code ApplicationContext.getResources(pattern)} work correctly at runtime.
 *
 * <p>This is used together with {@link NativeImageResourceProvider} which replaces Flyway's
 * scanner with Spring's resource loading.
 */
public class FlywayRuntimeHints implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(@NonNull RuntimeHints hints, @Nullable ClassLoader classLoader) {
    hints.resources().registerPattern("db/migration/*.sql");
    hints.resources().registerPattern("db/migration-dev/*.sql");
  }
}
