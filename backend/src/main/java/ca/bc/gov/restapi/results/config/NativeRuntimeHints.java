package ca.bc.gov.restapi.results.config;

import ca.bc.gov.restapi.results.oracle.entity.ClientAcronymEntity;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import ca.bc.gov.restapi.results.oracle.entity.ResultsElectronicSubmissionEntity;
import ca.bc.gov.restapi.results.oracle.entity.activities.ActivityTreatmentUnitEntity;
import ca.bc.gov.restapi.results.oracle.entity.comments.SilvicultureCommentEntity;
import ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverEntity;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentEntity;
import ca.bc.gov.restapi.results.oracle.entity.opening.OpeningEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity;
import ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;

/**
 * Registers runtime hints for GraalVM native image compilation.
 * This is required for JPA EntityManagerFactory proxy creation and entity reflection.
 */
public class NativeRuntimeHints implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    // Register JPA EntityManagerFactory proxy for reflection
    hints.proxies().registerJdkProxy(
        EntityManagerFactory.class,
        EntityManagerFactoryInfo.class
    );

    // Register all Oracle entities for reflection (required for multi-datasource native builds)
    Class<?>[] oracleEntities = {
        ClientAcronymEntity.class,
        CutBlockOpenAdminEntity.class,
        OpenCategoryCodeEntity.class,
        OrgUnitEntity.class,
        ResultsElectronicSubmissionEntity.class,
        ActivityTreatmentUnitEntity.class,
        SilvicultureCommentEntity.class,
        ForestCoverEntity.class,
        OpeningAttachmentEntity.class,
        OpeningEntity.class
    };

    // Register all Postgres entities for reflection
    Class<?>[] postgresEntities = {
        UserOpeningEntity.class,
        UserRecentOpeningEntity.class
    };

    // Register all entity classes with full reflection support
    for (Class<?> entity : oracleEntities) {
      hints.reflection().registerType(entity,
          MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
          MemberCategory.INVOKE_DECLARED_METHODS,
          MemberCategory.INVOKE_PUBLIC_METHODS);
    };

    for (Class<?> entity : postgresEntities) {
      hints.reflection().registerType(entity,
          MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
          MemberCategory.INVOKE_DECLARED_METHODS,
          MemberCategory.INVOKE_PUBLIC_METHODS);
    }
  }
}
