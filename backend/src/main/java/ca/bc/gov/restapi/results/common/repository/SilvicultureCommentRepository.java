package ca.bc.gov.restapi.results.common.repository;

import ca.bc.gov.restapi.results.common.projection.comment.CommentProjection;

import java.util.List;

public interface SilvicultureCommentRepository {
  List<CommentProjection> getCommentById(
      Long openingId,
      Long atuId,
      Long ssuId,
      Long ssuMId,
      Long projectId
  );
}
