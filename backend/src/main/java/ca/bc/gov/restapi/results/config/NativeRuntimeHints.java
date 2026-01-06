package ca.bc.gov.restapi.results.config;

import ca.bc.gov.restapi.results.oracle.converter.UuidToBytesConverter;
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

    // Register all entities from EntityRegistry for reflection with FULL access
    // Native images require access to fields (for Hibernate property access),
    // constructors (for entity instantiation), and methods (for getters/setters)
    for (Class<?> entity : EntityRegistry.ALL_ENTITIES) {
      hints.reflection().registerType(entity,
          MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
          MemberCategory.INVOKE_DECLARED_METHODS,
          MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
          MemberCategory.INVOKE_PUBLIC_METHODS,
          MemberCategory.UNSAFE_ALLOCATED
      );
    }

    // Register JPA AttributeConverters for reflection (Hibernate needs to instantiate them)
    hints.reflection().registerType(UuidToBytesConverter.class,
        MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
        MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
        MemberCategory.INVOKE_DECLARED_METHODS,
        MemberCategory.INVOKE_PUBLIC_METHODS
    );

    // Register Hibernate resources
    hints.resources().registerPattern("META-INF/persistence.xml");
    hints.resources().registerPattern("org/hibernate/orm/event/jpa/persistence-unit-static-definition.xml");

    // Register Flyway migration resources for native image
    // This fixes the "unsupported protocol: resource" warning
    hints.resources().registerPattern("db/migration/*");
    hints.resources().registerPattern("db/migration/**");
  }
}
