package ca.bc.gov.restapi.results.config;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot 4.0 / GraalVM 25 compatibility configuration.
 * Ensures JPA entities are registered for AOT processing.
 */
@Configuration
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
public class SpringBoot4AotConfig {
}
