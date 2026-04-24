package ca.bc.gov.restapi.results.common.projection.comment;

import java.time.LocalDateTime;

/** Projection for a comment search result row, including the opening it belongs to. */
public interface CommentSearchProjection {

  Long getOpeningId();

  String getCommentLocation();

  String getActivityKind();

  String getCommentText();

  LocalDateTime getUpdateTimestamp();

  Long getTotalCount();
}
