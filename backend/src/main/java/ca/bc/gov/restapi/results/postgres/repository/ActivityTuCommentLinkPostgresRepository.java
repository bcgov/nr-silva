package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTuCommentLinkEntity;
import ca.bc.gov.restapi.results.postgres.entity.activity.ActivityTuCommentLinkEntityId;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository for the {@code silva.activity_tu_comment_link} table. */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface ActivityTuCommentLinkPostgresRepository
    extends JpaRepository<ActivityTuCommentLinkEntity, ActivityTuCommentLinkEntityId> {}
