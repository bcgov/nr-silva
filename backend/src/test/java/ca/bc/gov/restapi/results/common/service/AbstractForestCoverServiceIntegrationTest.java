package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.cover.ForestCoverSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.cover.ForestCoverSearchResponseDto;
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
import org.springframework.web.server.ResponseStatusException;

/**
 * Abstract integration test contract for ForestCoverService implementations (Postgres and Oracle).
 * Defines common test scenarios for forest cover search functionality.
 */
@DisplayName("Integrated Test | Forest Cover Service | Contract")
@WithMockJwt(value = "ttester")
public abstract class AbstractForestCoverServiceIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  @Autowired protected ForestCoverService forestCoverService;

  @Test
  @DisplayName("Forest cover search with default filters should succeed")
  void forestCoverSearch_withDefaultFilters_shouldSucceed() {
    ForestCoverSearchFilterDto filters = new ForestCoverSearchFilterDto();
    Pageable pageable = PageRequest.of(0, 10);

    Page<ForestCoverSearchResponseDto> result =
        forestCoverService.forestCoverSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    Assertions.assertTrue(
        result.getNumberOfElements() >= 0, "Result should have non-negative count");
  }

  @Test
  @DisplayName("Forest cover search with file ID filter should succeed")
  void forestCoverSearch_withFileIdFilter_shouldSucceed() {
    ForestCoverSearchFilterDto filters =
        new ForestCoverSearchFilterDto(
            null, null, null, null, null, "TFL47", null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ForestCoverSearchResponseDto> result =
        forestCoverService.forestCoverSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    if (result.hasContent()) {
      result
          .getContent()
          .forEach(
              fc -> Assertions.assertEquals("TFL47", fc.fileId(), "File ID should match filter"));
    }
  }

  @Test
  @DisplayName("Forest cover search with opening status filter should succeed")
  void forestCoverSearch_withOpeningStatusFilter_shouldSucceed() {
    ForestCoverSearchFilterDto filters =
        new ForestCoverSearchFilterDto(
            null, null, null, null, List.of("APP"), null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ForestCoverSearchResponseDto> result =
        forestCoverService.forestCoverSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Forest cover search with multiple opening statuses should succeed")
  void forestCoverSearch_withMultipleOpeningStatuses_shouldSucceed() {
    ForestCoverSearchFilterDto filters =
        new ForestCoverSearchFilterDto(
            null, null, null, null, List.of("APP", "AMD"), null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ForestCoverSearchResponseDto> result =
        forestCoverService.forestCoverSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Forest cover search with org unit filter should succeed")
  void forestCoverSearch_withOrgUnitFilter_shouldSucceed() {
    ForestCoverSearchFilterDto filters =
        new ForestCoverSearchFilterDto(
            null, null, null, null, null, null, List.of("DCR"), null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ForestCoverSearchResponseDto> result =
        forestCoverService.forestCoverSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Forest cover search with multiple org units should succeed")
  void forestCoverSearch_withMultipleOrgUnits_shouldSucceed() {
    ForestCoverSearchFilterDto filters =
        new ForestCoverSearchFilterDto(
            null, null, null, null, null, null, List.of("DCR", "DND"), null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ForestCoverSearchResponseDto> result =
        forestCoverService.forestCoverSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Forest cover search with opening category filter should succeed")
  void forestCoverSearch_withOpeningCategoryFilter_shouldSucceed() {
    ForestCoverSearchFilterDto filters =
        new ForestCoverSearchFilterDto(
            null, null, null, null, null, null, null, List.of("FTML"), null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ForestCoverSearchResponseDto> result =
        forestCoverService.forestCoverSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Forest cover search with update date range should succeed")
  void forestCoverSearch_withUpdateDateRange_shouldSucceed() {
    ForestCoverSearchFilterDto filters =
        new ForestCoverSearchFilterDto(
            null, null, null, null, null, null, null, null, "2020-01-01", "2024-12-31");
    Pageable pageable = PageRequest.of(0, 10);

    Page<ForestCoverSearchResponseDto> result =
        forestCoverService.forestCoverSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Forest cover search with pagination should succeed")
  void forestCoverSearch_withPagination_shouldSucceed() {
    ForestCoverSearchFilterDto filters = new ForestCoverSearchFilterDto();
    Pageable pageableFirst = PageRequest.of(0, 5);
    Pageable pageableSecond = PageRequest.of(1, 5);

    Page<ForestCoverSearchResponseDto> resultFirst =
        forestCoverService.forestCoverSearch(filters, pageableFirst);
    Page<ForestCoverSearchResponseDto> resultSecond =
        forestCoverService.forestCoverSearch(filters, pageableSecond);

    Assertions.assertNotNull(resultFirst, "First page result should not be null");
    Assertions.assertNotNull(resultSecond, "Second page result should not be null");
    Assertions.assertEquals(0, resultFirst.getNumber(), "First page number should be 0");
    Assertions.assertEquals(1, resultSecond.getNumber(), "Second page number should be 1");
  }

  @Test
  @DisplayName("Forest cover search response DTOs should have all required fields populated")
  void forestCoverSearch_responseDto_shouldHaveAllRequiredFields() {
    ForestCoverSearchFilterDto filters =
        new ForestCoverSearchFilterDto(
            null, null, null, null, null, "TFL47", null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ForestCoverSearchResponseDto> result =
        forestCoverService.forestCoverSearch(filters, pageable);

    if (result.hasContent()) {
      ForestCoverSearchResponseDto dto = result.getContent().get(0);
      Assertions.assertNotNull(dto.forestCoverId(), "Forest cover ID should not be null");
      Assertions.assertNotNull(dto.fileId(), "File ID should not be null");
      Assertions.assertNotNull(dto.openingId(), "Opening ID should not be null");
      Assertions.assertNotNull(dto.damageAgents(), "Damage agents list should not be null");
    }
  }

  @Test
  @DisplayName("Forest cover search with invalid file ID should return empty results")
  void forestCoverSearch_withInvalidFileId_shouldReturnEmpty() {
    ForestCoverSearchFilterDto filters =
        new ForestCoverSearchFilterDto(
            null, null, null, null, null, "INVALID_FILE_999999", null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ForestCoverSearchResponseDto> result =
        forestCoverService.forestCoverSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    Assertions.assertFalse(result.hasContent(), "Result should be empty for invalid file ID");
  }

  @Test
  @DisplayName("Forest cover search should not return duplicate forest cover IDs")
  void forestCoverSearch_shouldNotReturnDuplicates() {
    ForestCoverSearchFilterDto filters = new ForestCoverSearchFilterDto();
    Pageable pageable = PageRequest.of(0, 50);

    Page<ForestCoverSearchResponseDto> result =
        forestCoverService.forestCoverSearch(filters, pageable);

    if (result.hasContent()) {
      List<Long> forestCoverIds =
          result.getContent().stream().map(ForestCoverSearchResponseDto::forestCoverId).toList();
      long uniqueCount = forestCoverIds.stream().distinct().count();
      Assertions.assertEquals(
          forestCoverIds.size(),
          uniqueCount,
          "Forest cover search should not return duplicate entries");
    }
  }

  @Test
  @DisplayName("Forest cover search with invalid date range should throw exception")
  void forestCoverSearch_withInvalidDateRange_shouldThrowException() {
    ForestCoverSearchFilterDto filters =
        new ForestCoverSearchFilterDto(
            null, null, null, null, null, null, null, null, "2024-12-31", "2020-01-01");
    Pageable pageable = PageRequest.of(0, 10);

    Assertions.assertThrows(
        ResponseStatusException.class,
        () -> forestCoverService.forestCoverSearch(filters, pageable),
        "Should throw ResponseStatusException for invalid date range");
  }

  @Test
  @DisplayName("Forest cover search with combined filters should succeed")
  void forestCoverSearch_withCombinedFilters_shouldSucceed() {
    ForestCoverSearchFilterDto filters =
        new ForestCoverSearchFilterDto(
            null, null, null, null, List.of("APP"), "TFL47", List.of("DCR"), null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ForestCoverSearchResponseDto> result =
        forestCoverService.forestCoverSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }
}
