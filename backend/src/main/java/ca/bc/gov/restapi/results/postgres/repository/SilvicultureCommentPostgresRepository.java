package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.projection.comment.CommentProjection;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresQueryConstants;
import ca.bc.gov.restapi.results.postgres.entity.comment.SilvicultureCommentEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for CRUD operations and custom queries against the
 * `silva.org_unit` and related tables in PostgreSQL.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface SilvicultureCommentPostgresRepository extends JpaRepository<SilvicultureCommentEntity, Long>,
    SilvicultureCommentRepository {

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_COMMENTS)
  List<CommentProjection> getCommentById(
      Long openingId,
      Long atuId,
      Long ssuId,
      Long ssuMId,
      Long projectId
  );

}
