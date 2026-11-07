package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.common.dto.comment.CommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.projection.comment.CommentProjection;
import ca.bc.gov.restapi.results.common.projection.comment.CommentSearchProjection;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.postgres.SilvaPostgresQueryConstants;
import ca.bc.gov.restapi.results.postgres.entity.comment.SilvicultureCommentEntity;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CRUD operations and custom queries against the `silviculture_comment`
 * table and related tables/queries in PostgreSQL.
 */
@Repository
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public interface SilvicultureCommentPostgresRepository
    extends JpaRepository<SilvicultureCommentEntity, Long>, SilvicultureCommentRepository {

  /**
   * Allocates the id of a new comment. The sequence is first moved past the highest id in the table
   * because the ETL loads rows with their original ids without advancing the sequence. The
   * advisory lock keeps concurrent allocations from computing the same value.
   */
  @Query(
      nativeQuery = true,
      value =
          """
          WITH lock AS (SELECT pg_advisory_xact_lock(hashtext('silva.silviculture_comment_id_seq')))
          SELECT setval('silva.silviculture_comment_id_seq',
                 GREATEST((SELECT COALESCE(MAX(silviculture_comment_id), 0)
                           FROM silva.silviculture_comment),
                          (SELECT last_value FROM silva.silviculture_comment_id_seq)) + 1)
          FROM lock
          """)
  Long nextCommentId();

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.GET_COMMENTS)
  List<CommentProjection> getCommentById(
      Long openingId, Long atuId, Long ssuId, Long ssuMId, Long projectId);

  @Override
  @Query(nativeQuery = true, value = SilvaPostgresQueryConstants.COMMENT_SEARCH)
  List<CommentSearchProjection> searchComments(
      @Param("filter") CommentSearchFilterDto filter,
      @Param("page") long page,
      @Param("size") long size);
}
