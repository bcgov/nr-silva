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
