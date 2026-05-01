package ca.bc.gov.restapi.results.common.service.impl;

import ca.bc.gov.restapi.results.common.dto.comment.CommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.comment.CommentSearchResponseDto;
import ca.bc.gov.restapi.results.common.enums.ActivityKindCode;
import ca.bc.gov.restapi.results.common.enums.CommentLocationCode;
import ca.bc.gov.restapi.results.common.projection.comment.CommentSearchProjection;
import ca.bc.gov.restapi.results.common.repository.SilvicultureCommentRepository;
import ca.bc.gov.restapi.results.common.service.CommentSearchService;
import ca.bc.gov.restapi.results.common.util.DateUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentSearchServiceImpl implements CommentSearchService {

  private final SilvicultureCommentRepository commentRepository;

  @Override
  public Page<CommentSearchResponseDto> searchComments(
      CommentSearchFilterDto filter, Pageable pageable) {
    DateUtil.validateDateRange(filter.getUpdateDateStart(), filter.getUpdateDateEnd());

    long offset = pageable.getOffset();
    long size = pageable.getPageSize();

    List<CommentSearchProjection> projections =
        commentRepository.searchComments(filter, offset, size);

    long total = 0;
    if (!projections.isEmpty()) {
      Long totalCount = projections.get(0).getTotalCount();
      total = totalCount != null ? totalCount : 0;
    }

    List<CommentSearchResponseDto> results = projections.stream().map(this::toDto).toList();

    return new PageImpl<>(results, pageable, total);
  }

  private CommentSearchResponseDto toDto(CommentSearchProjection projection) {
    CommentLocationCode location =
        projection.getCommentLocation() != null
            ? CommentLocationCode.valueOf(projection.getCommentLocation())
            : null;

    ActivityKindCode activityKind =
        projection.getActivityKind() != null
            ? ActivityKindCode.valueOf(projection.getActivityKind())
            : null;

    return new CommentSearchResponseDto(
        projection.getOpeningId(),
        location,
        activityKind,
        projection.getCommentText(),
        projection.getUpdateTimestamp());
  }
}
