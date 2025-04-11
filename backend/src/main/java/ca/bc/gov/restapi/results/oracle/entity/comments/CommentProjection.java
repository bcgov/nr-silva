package ca.bc.gov.restapi.results.oracle.entity.comments;

public interface CommentProjection {
  String getCommentSourceCode();
  String getCommentSourceName();
  String getCommentTypeCode();
  String getCommentTypeName();
  String getCommentText();
}
