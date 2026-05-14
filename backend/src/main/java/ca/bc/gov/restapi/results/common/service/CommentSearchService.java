package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.comment.CommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.comment.CommentSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentSearchService {
  Page<CommentSearchResponseDto> searchComments(CommentSearchFilterDto filter, Pageable pageable);
}
