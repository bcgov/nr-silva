package ca.bc.gov.restapi.results.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

/**
 * Registers runtime hints for GraalVM native image compilation.
 * This is required for JPA EntityManagerFactory proxy creation.
 */
public class NativeRuntimeHints implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    // Register JPA EntityManagerFactory proxy for reflection
    hints.proxies().registerJdkProxy(
        EntityManagerFactory.class,
        EntityManagerFactoryInfo.class
    );
  }
}
