package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.SilvaOracleQueryConstants;
import ca.bc.gov.restapi.results.common.projection.comment.CommentProjection;
import ca.bc.gov.restapi.results.oracle.entity.comments.SilvicultureCommentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SilvicultureCommentRepository extends JpaRepository<SilvicultureCommentEntity, Long> {

  @Query(nativeQuery = true, value = SilvaOracleQueryConstants.GET_COMMENTS)
  List<CommentProjection> getCommentById(
      Long openingId,
      Long atuId,
      Long ssuId,
      Long ssuMId,
      Long projectId
  );

}
