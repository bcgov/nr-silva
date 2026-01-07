package ca.bc.gov.restapi.results.config;

import ca.bc.gov.restapi.results.oracle.converter.UuidToBytesConverter;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Registers runtime hints for GraalVM native image compilation.
 * Uses EntityRegistry as single source of truth for all entities.
 */
public class NativeRuntimeHints implements RuntimeHintsRegistrar {

  private static final Logger log = LoggerFactory.getLogger(NativeRuntimeHints.class);

  @Override
  public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    // GraalVM 25 Compatibility: Force early entity class loading for AOT metamodel
    System.out.println("NativeRuntimeHints: Pre-loading entities for GraalVM 25 compatibility");
    for (Class<?> entity : EntityRegistry.ALL_ENTITIES) {
      try {
        // Force class initialization to ensure availability during AOT processing
        Class.forName(entity.getName(), true, classLoader);
      } catch (ClassNotFoundException e) {
        System.err.println("Warning: Could not pre-load entity: " + entity.getName());
      }
    }

    // Register JPA EntityManagerFactory proxy for reflection
    hints.proxies().registerJdkProxy(
        EntityManagerFactory.class,
        EntityManagerFactoryInfo.class
    );

    // Register all entities from EntityRegistry for reflection
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

    hints.reflection().registerType(UuidToBytesConverter.class,
        MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
        MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
        MemberCategory.INVOKE_DECLARED_METHODS,
        MemberCategory.INVOKE_PUBLIC_METHODS
    );

    registerHibernateClasses(hints);

    registerJpaMetamodelClasses(hints);

    registerSpringBoot4JpaClasses(hints);

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

  private void registerJpaMetamodelClasses(RuntimeHints hints) {
    // Register the specific JPA metamodel classes mentioned in runtime error
    String[] jpaMetamodelClasses = {
        "org.hibernate.metamodel.model.domain.internal.JpaMetamodelImpl",
        "org.hibernate.metamodel.model.domain.internal.MappingMetamodelImpl",
        "org.springframework.data.jpa.repository.support.JpaEntityInformationSupport",
        "org.springframework.data.jpa.repository.support.JpaRepositoryFactory",
        "org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean",
        "org.springframework.data.repository.core.support.RepositoryFactorySupport",
        "org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport"
    };

    for (String className : jpaMetamodelClasses) {
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
        log.debug("Could not register JPA metamodel class {}: {}", className, e.getMessage());
      }
    }

    // Register entity-specific metamodel classes for all entities
    for (Class<?> entity : EntityRegistry.ALL_ENTITIES) {
      try {
        // Try to register the Hibernate-generated metamodel class (Entity_)
        String metamodelClassName = entity.getName() + "_";
        Class<?> metamodelClass = Class.forName(metamodelClassName);
        hints.reflection().registerType(metamodelClass,
            MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
            MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
            MemberCategory.INVOKE_DECLARED_METHODS,
            MemberCategory.INVOKE_PUBLIC_METHODS,
            MemberCategory.DECLARED_FIELDS
        );
      } catch (ClassNotFoundException e) {
        log.debug("Could not register metamodel class {}: {}", metamodelClassName, e.getMessage());
      }
    }
  }

  /**
   * Spring Boot 4.0 specific JPA native configuration.
   * Registers classes needed for PersistenceManagedTypes pattern.
   */
  private void registerSpringBoot4JpaClasses(RuntimeHints hints) {
    // Register PersistenceManagedTypes and related classes for Spring Boot 4.0
    try {
      hints.reflection().registerType(
          TypeReference.of("org.springframework.boot.orm.jpa.PersistenceManagedTypes"),
          builder -> builder
              .withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS, MemberCategory.INVOKE_DECLARED_METHODS)
      );
    } catch (Exception e) {
      log.warn("Could not register PersistenceManagedTypes for native hints", e);
    }

    // Register EntityManagerFactoryBuilder ManagedTypes inner class
    try {
      hints.reflection().registerType(
          TypeReference.of("org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder$ManagedTypes"),
          builder -> builder
              .withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS, MemberCategory.INVOKE_DECLARED_METHODS)
      );
    } catch (Exception e) {
      log.warn("Could not register ManagedTypes for native hints", e);
    }

    // Register Spring Boot 4.0 JPA annotations and classes that might be needed at runtime
    String[] springBoot4JpaClasses = {
        "org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform",
        "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy",
        "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy",
        "org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer"
    };

    for (String className : springBoot4JpaClasses) {
      try {
        hints.reflection().registerType(
            TypeReference.of(className),
            builder -> builder
                .withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                           MemberCategory.INVOKE_DECLARED_METHODS,
                           MemberCategory.DECLARED_FIELDS)
        );
      } catch (Exception e) {
        log.debug("Could not register {} for Spring Boot 4.0 native hints", className);
      }
    }
  }
}
