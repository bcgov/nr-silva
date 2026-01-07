package ca.bc.gov.restapi.results.config;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;

/**
 * Early initialization configuration for AOT compilation.
 * This ensures managed types are available before repository processing.
 */
@Configuration
@Order(Integer.MIN_VALUE) // Highest priority
@RegisterReflectionForBinding({
    // Oracle entities
    ca.bc.gov.restapi.results.oracle.entity.ClientAcronymEntity.class,
    ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity.class,
    ca.bc.gov.restapi.results.oracle.entity.OpenCategoryCodeEntity.class,
    ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity.class,
    ca.bc.gov.restapi.results.oracle.entity.ResultsElectronicSubmissionEntity.class,
    ca.bc.gov.restapi.results.oracle.entity.activities.ActivityTreatmentUnitEntity.class,
    ca.bc.gov.restapi.results.oracle.entity.comments.SilvicultureCommentEntity.class,
    ca.bc.gov.restapi.results.oracle.entity.cover.ForestCoverEntity.class,
    ca.bc.gov.restapi.results.oracle.entity.opening.OpeningAttachmentEntity.class,
    ca.bc.gov.restapi.results.oracle.entity.opening.OpeningEntity.class,
    // Postgres entities
    ca.bc.gov.restapi.results.postgres.entity.UserOpeningEntity.class,
    ca.bc.gov.restapi.results.postgres.entity.UserRecentOpeningEntity.class
})
public class AotEntityInitialization {

    /**
     * Pre-initialize Oracle managed types for AOT compatibility.
     */
    @Bean(name = "aotOracleManagedTypes")
    @Order(Integer.MIN_VALUE)
    public PersistenceManagedTypes aotOracleManagedTypes() {
        return PersistenceManagedTypes.of(EntityRegistry.getOracleEntityNames());
    }

    /**
     * Pre-initialize Postgres managed types for AOT compatibility.
     */
    @Bean(name = "aotPostgresManagedTypes")
    @Order(Integer.MIN_VALUE)
    public PersistenceManagedTypes aotPostgresManagedTypes() {
        return PersistenceManagedTypes.of(EntityRegistry.getPostgresEntityNames());
    }
}
