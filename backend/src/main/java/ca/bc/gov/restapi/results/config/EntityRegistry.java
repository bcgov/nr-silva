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

/**
 * Central registry for all JPA entities.
 * This is the ONLY place you need to add new entities for native image support.
 */
public final class EntityRegistry {

  /**
   * Oracle database entities.
   * Add new Oracle entities here only.
   */
  public static final Class<?>[] ORACLE_ENTITIES = {
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

  /**
   * Postgres database entities.
   * Add new Postgres entities here only.
   */
  public static final Class<?>[] POSTGRES_ENTITIES = {
      UserOpeningEntity.class,
      UserRecentOpeningEntity.class
  };

  /**
   * All entities combined (for reflection hints).
   */
  public static final Class<?>[] ALL_ENTITIES = concat(ORACLE_ENTITIES, POSTGRES_ENTITIES);

  /**
   * Get Oracle entity class names for PersistenceManagedTypes.
   */
  public static String[] getOracleEntityNames() {
    return toClassNames(ORACLE_ENTITIES);
  }

  /**
   * Get Postgres entity class names for PersistenceManagedTypes.
   */
  public static String[] getPostgresEntityNames() {
    return toClassNames(POSTGRES_ENTITIES);
  }

  private static String[] toClassNames(Class<?>[] classes) {
    String[] names = new String[classes.length];
    for (int i = 0; i < classes.length; i++) {
      names[i] = classes[i].getName();
    }
    return names;
  }

  private static Class<?>[] concat(Class<?>[] a, Class<?>[] b) {
    Class<?>[] result = new Class<?>[a.length + b.length];
    System.arraycopy(a, 0, result, 0, a.length);
    System.arraycopy(b, 0, result, a.length, b.length);
    return result;
  }
}
