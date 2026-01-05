package ca.bc.gov.restapi.results.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

/**
 * Registers runtime hints for GraalVM native image compilation.
 * Uses EntityRegistry as single source of truth for all entities.
 */
public class NativeRuntimeHints implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    // Register JPA EntityManagerFactory proxy for reflection
    hints.proxies().registerJdkProxy(
        EntityManagerFactory.class,
        EntityManagerFactoryInfo.class
    );

    // Register all entities from EntityRegistry for reflection
    for (Class<?> entity : EntityRegistry.ALL_ENTITIES) {
      hints.reflection().registerType(entity,
          MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
          MemberCategory.INVOKE_DECLARED_METHODS,
          MemberCategory.INVOKE_PUBLIC_METHODS);
    }

    // Register Hibernate resources
    hints.resources().registerPattern("META-INF/orm.xml");
    hints.resources().registerPattern("org/hibernate/orm/event/jpa/persistence-unit-static-definition.xml");
  }
}
