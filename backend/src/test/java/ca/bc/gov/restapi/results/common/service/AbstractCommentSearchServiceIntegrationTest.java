package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.comment.CommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.comment.CommentSearchResponseDto;
import ca.bc.gov.restapi.results.common.enums.CommentLocationCode;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Abstract integration test contract for CommentSearchService implementations (Postgres and
 * Oracle). Defines common test scenarios for comment search functionality.
 */
@DisplayName("Integrated Test | Comment Search Service | Contract")
@WithMockJwt(value = "ttester")
public abstract class AbstractCommentSearchServiceIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  @Autowired protected CommentSearchService commentSearchService;

  @Test
  @DisplayName("Comment search with matching searchTerm should return results")
  void commentSearch_withMatchingSearchTerm_shouldReturnResults() {
    CommentSearchFilterDto filter =
        new CommentSearchFilterDto("good", null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<CommentSearchResponseDto> result = commentSearchService.searchComments(filter, pageable);

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.getTotalElements() >= 1, "Should find at least one comment");
  }

  @Test
  @DisplayName("Comment search with non-matching searchTerm should return empty")
  void commentSearch_withNonMatchingSearchTerm_shouldReturnEmpty() {
    CommentSearchFilterDto filter =
        new CommentSearchFilterDto("ZZZNOMATCHZZZXXX999", null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<CommentSearchResponseDto> result = commentSearchService.searchComments(filter, pageable);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getTotalElements());
  }

  @Test
  @DisplayName("Comment search with commentLocation=OPENING should return OPENING results")
  void commentSearch_withCommentLocationOpening_shouldReturnOpeningResults() {
    CommentSearchFilterDto filter =
        new CommentSearchFilterDto("far", CommentLocationCode.OPENING, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<CommentSearchResponseDto> result = commentSearchService.searchComments(filter, pageable);

    Assertions.assertNotNull(result);
    result
        .getContent()
        .forEach(
            dto ->
                Assertions.assertEquals(
                    CommentLocationCode.OPENING,
                    dto.commentLocation(),
                    "All results should have OPENING location"));
  }

  @Test
  @DisplayName("Comment search with commentLocation=ACTIVITIES should return empty for test data")
  void commentSearch_withCommentLocationActivities_shouldReturnEmpty() {
    CommentSearchFilterDto filter =
        new CommentSearchFilterDto("far", CommentLocationCode.ACTIVITIES, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<CommentSearchResponseDto> result = commentSearchService.searchComments(filter, pageable);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getTotalElements());
  }

  @Test
  @DisplayName("Comment search result should have required fields populated")
  void commentSearch_result_shouldHaveRequiredFieldsPopulated() {
    CommentSearchFilterDto filter =
        new CommentSearchFilterDto("good", null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<CommentSearchResponseDto> result = commentSearchService.searchComments(filter, pageable);

    Assertions.assertFalse(result.getContent().isEmpty(), "Expected at least one result");
    CommentSearchResponseDto first = result.getContent().get(0);
    Assertions.assertNotNull(first.openingId(), "openingId should not be null");
    Assertions.assertNotNull(first.commentLocation(), "commentLocation should not be null");
    Assertions.assertNotNull(first.commentText(), "commentText should not be null");
  }

  @Test
  @DisplayName("Comment search with pagination should respect page and size")
  void commentSearch_withPagination_shouldRespectPageAndSize() {
    CommentSearchFilterDto filter = new CommentSearchFilterDto("far", null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 1);

    Page<CommentSearchResponseDto> result = commentSearchService.searchComments(filter, pageable);

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.getContent().size() <= 1, "Page size should be respected");
    Assertions.assertEquals(0, result.getNumber());
    Assertions.assertEquals(1, result.getSize());
  }

  @Test
  @DisplayName("Comment search with orgUnits filter should succeed")
  void commentSearch_withOrgUnitsFilter_shouldSucceed() {
    CommentSearchFilterDto filter =
        new CommentSearchFilterDto("far", null, null, List.of("DAS"), null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<CommentSearchResponseDto> result = commentSearchService.searchComments(filter, pageable);

    Assertions.assertNotNull(result);
  }

  @Test
  @DisplayName("Comment search with valid date range should succeed")
  void commentSearch_withValidDateRange_shouldSucceed() {
    CommentSearchFilterDto filter =
        new CommentSearchFilterDto("far", null, null, null, "2010-01-01", "2030-12-31");
    Pageable pageable = PageRequest.of(0, 10);

    Page<CommentSearchResponseDto> result = commentSearchService.searchComments(filter, pageable);

    Assertions.assertNotNull(result);
  }

  @Test
  @DisplayName("Comment search case-insensitive should match uppercase term")
  void commentSearch_withUpperCaseSearchTerm_shouldMatchCaseInsensitive() {
    CommentSearchFilterDto lower = new CommentSearchFilterDto("good", null, null, null, null, null);
    CommentSearchFilterDto upper = new CommentSearchFilterDto("GOOD", null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    long lowerCount = commentSearchService.searchComments(lower, pageable).getTotalElements();
    long upperCount = commentSearchService.searchComments(upper, pageable).getTotalElements();

    Assertions.assertEquals(lowerCount, upperCount, "Search should be case-insensitive");
  }
}
