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
          MemberCategory.DECLARED_FIELDS,
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

    // Register Hibernate core classes that are required for native compilation
    registerHibernateClasses(hints);

    // Register Hibernate resources
    hints.resources().registerPattern("META-INF/persistence.xml");
    hints.resources().registerPattern("org/hibernate/orm/event/jpa/persistence-unit-static-definition.xml");

    // Register Flyway migration resources for native image
    // This fixes the "unsupported protocol: resource" warning
    hints.resources().registerPattern("db/migration/*");
    hints.resources().registerPattern("db/migration/**");
  }

  private void registerHibernateClasses(RuntimeHints hints) {
    // Register Hibernate types that need reflection for JPA entity processing
    String[] hibernateClasses = {
        "org.hibernate.persister.entity.SingleTableEntityPersister",
        "org.hibernate.persister.entity.JoinedSubclassEntityPersister",
        "org.hibernate.persister.entity.UnionSubclassEntityPersister",
        "org.hibernate.persister.collection.OneToManyPersister",
        "org.hibernate.persister.collection.BasicCollectionPersister",
        "org.hibernate.type.StandardBasicTypes",
        "org.hibernate.type.SqlTypes",
        "org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl",
        "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl",
        "org.hibernate.event.internal.DefaultLoadEventListener",
        "org.hibernate.event.internal.DefaultPersistEventListener",
        "org.hibernate.event.internal.DefaultMergeEventListener",
        "org.hibernate.event.internal.DefaultDeleteEventListener",
        "org.hibernate.event.internal.DefaultFlushEventListener",
        "org.hibernate.event.internal.DefaultSaveOrUpdateEventListener",
        "org.hibernate.event.internal.DefaultSaveEventListener",
        "org.hibernate.event.internal.DefaultUpdateEventListener",
        "org.hibernate.event.internal.DefaultRefreshEventListener",
        "org.hibernate.event.internal.DefaultReplicateEventListener",
        "org.hibernate.event.internal.DefaultEvictEventListener",
        "org.hibernate.event.internal.DefaultLockEventListener",
        "org.hibernate.event.internal.DefaultAutoFlushEventListener",
        "org.hibernate.event.internal.DefaultFlushEntityEventListener",
        "org.hibernate.event.internal.DefaultPersistOnFlushEventListener",
        "org.hibernate.event.internal.DefaultDirtyCheckEventListener",
        "org.hibernate.event.internal.DefaultPostLoadEventListener"
    };

    for (String className : hibernateClasses) {
      try {
        Class<?> clazz = Class.forName(className);
        hints.reflection().registerType(clazz,
            MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
            MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
            MemberCategory.INVOKE_DECLARED_METHODS,
            MemberCategory.INVOKE_PUBLIC_METHODS,
            MemberCategory.DECLARED_FIELDS
        );
      } catch (ClassNotFoundException e) {
        // Class not available in classpath, skip
      }
    }
  }
}
