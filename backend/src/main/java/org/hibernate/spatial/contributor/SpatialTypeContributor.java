package org.hibernate.spatial.contributor;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.config.spi.StandardConverters;
import org.hibernate.service.ServiceRegistry;

/**
 * Shadows {@code org.hibernate.spatial.contributor.SpatialTypeContributor} from hibernate-spatial
 * to restore the {@code hibernate.integration.spatial.enabled} guard that was accidentally dropped
 * in 6.6.x.
 *
 * <p>In Spring Boot's fat-JAR layout, {@code BOOT-INF/classes/} is loaded before {@code
 * BOOT-INF/lib/}, so this class takes precedence over the one in {@code hibernate-spatial-*.jar}.
 * The ServiceLoader entry in hibernate-spatial's {@code META-INF/services} resolves to
 * <em>this</em> class.
 *
 * <p>Root cause: when {@code hibernate.integration.spatial.enabled=false} (e.g. the Oracle {@code
 * EntityManagerFactory}), the upstream 6.6.x version unconditionally calls {@link
 * ContributorResolver#resolveSpatialtypeContributorImplementor}, which instantiates {@code
 * OracleDialectContributor}, which in turn constructs {@code OracleJDBCTypeFactory}. That
 * constructor reflectively looks up {@code oracle.sql.StructDescriptor.createDescriptor}, which was
 * removed in {@code ojdbc11} 23.x, causing a {@link RuntimeException} at startup.
 */
public class SpatialTypeContributor implements TypeContributor {

  @Override
  public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
    if (!isSpatialEnabled(serviceRegistry)) {
      return;
    }
    ContributorImplementor contributor =
        ContributorResolver.resolveSpatialtypeContributorImplementor(serviceRegistry);
    if (contributor == null) {
      return;
    }
    contributor.contributeJavaTypes(typeContributions, serviceRegistry);
    contributor.contributeJdbcTypes(typeContributions, serviceRegistry);
  }

  private boolean isSpatialEnabled(ServiceRegistry serviceRegistry) {
    ConfigurationService cfgService = serviceRegistry.getService(ConfigurationService.class);
    if (cfgService == null) {
      return true;
    }
    return cfgService.getSetting(
        "hibernate.integration.spatial.enabled", StandardConverters.BOOLEAN, Boolean.TRUE);
  }
}
