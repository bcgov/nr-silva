package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.dto.comment.CommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.projection.comment.CommentProjection;
import ca.bc.gov.restapi.results.common.projection.comment.CommentSearchProjection;
import java.util.List;

public interface SilvicultureCommentRepository {
  List<CommentProjection> getCommentById(
      Long openingId, Long atuId, Long ssuId, Long ssuMId, Long projectId);

  List<CommentSearchProjection> searchComments(CommentSearchFilterDto filter, long page, long size);
}
