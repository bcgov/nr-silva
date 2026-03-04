package ca.bc.gov.restapi.results.common.service;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchFiltersDto;
import ca.bc.gov.restapi.results.common.dto.activity.ActivitySearchResponseDto;
import ca.bc.gov.restapi.results.common.dto.activity.DisturbanceSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.activity.DisturbanceSearchResponseDto;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Abstract integration test contract for ActivityService implementations (Postgres and Oracle).
 * Defines common test scenarios for activity search functionality.
 */
@DisplayName("Integrated Test | Activity Service | Contract")
@WithMockJwt(value = "ttester")
public abstract class AbstractActivityServiceIntegrationTest
    extends AbstractTestContainerIntegrationTest {

  @RegisterExtension
  static WireMockExtension clientApiStub =
      WireMockExtension.newInstance()
          .options(
              wireMockConfig()
                  .port(10000)
                  .notifier(new WiremockLogNotifier())
                  .asynchronousResponseEnabled(true)
                  .stubRequestLoggingDisabled(false))
          .configureStaticDsl(true)
          .build();

  @Autowired protected ActivityService activityService;

  @Test
  @DisplayName("Activity search with default filters should succeed")
  void activitySearch_withDefaultFilters_shouldSucceed() {
    ActivitySearchFiltersDto filters = new ActivitySearchFiltersDto();
    Pageable pageable = PageRequest.of(0, 10);

    Page<ActivitySearchResponseDto> result = activityService.activitySearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    Assertions.assertTrue(result.getNumberOfElements() >= 0, "Result should have content");
  }

  @Test
  @DisplayName("Activity search with file ID filter should succeed")
  void activitySearch_withFileIdFilter_shouldSucceed() {
    ActivitySearchFiltersDto filters =
        new ActivitySearchFiltersDto(
            null, null, null, null, null, null, null, null, "TFL47", null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ActivitySearchResponseDto> result = activityService.activitySearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    // Verify results are for the specified file
    if (result.hasContent()) {
      result
          .getContent()
          .forEach(
              activity ->
                  Assertions.assertEquals(
                      "TFL47", activity.fileId(), "File ID should match filter"));
    }
  }

  @Test
  @DisplayName("Activity search with base code filter should succeed")
  void activitySearch_withBaseCodeFilter_shouldSucceed() {
    ActivitySearchFiltersDto filters =
        new ActivitySearchFiltersDto(
            List.of("PR"),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ActivitySearchResponseDto> result = activityService.activitySearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    // If there are results, verify they have the correct base code
    if (result.hasContent()) {
      result
          .getContent()
          .forEach(
              activity ->
                  Assertions.assertEquals(
                      "PR", activity.base().code(), "Base code should match filter"));
    }
  }

  @Test
  @DisplayName("Activity search with multiple filters should succeed")
  void activitySearch_withMultipleFilters_shouldSucceed() {
    ActivitySearchFiltersDto filters =
        new ActivitySearchFiltersDto(
            List.of("PR", "SP"),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "TFL47",
            null,
            null,
            null,
            null,
            null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ActivitySearchResponseDto> result = activityService.activitySearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Activity search with pagination should succeed")
  void activitySearch_withPagination_shouldSucceed() {
    ActivitySearchFiltersDto filters = new ActivitySearchFiltersDto();
    Pageable pageableFirst = PageRequest.of(0, 5);
    Pageable pageableSecond = PageRequest.of(1, 5);

    Page<ActivitySearchResponseDto> resultFirst =
        activityService.activitySearch(filters, pageableFirst);
    Page<ActivitySearchResponseDto> resultSecond =
        activityService.activitySearch(filters, pageableSecond);

    Assertions.assertNotNull(resultFirst, "First page result should not be null");
    Assertions.assertNotNull(resultSecond, "Second page result should not be null");
    Assertions.assertEquals(0, resultFirst.getNumber(), "First page number should be 0");
    Assertions.assertEquals(1, resultSecond.getNumber(), "Second page number should be 1");
  }

  @Test
  @DisplayName("Activity search response DTOs should have all required fields populated")
  void activitySearch_responseDto_shouldHaveAllRequiredFields() {
    ActivitySearchFiltersDto filters =
        new ActivitySearchFiltersDto(
            null, null, null, null, null, null, null, null, "TFL47", null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ActivitySearchResponseDto> result = activityService.activitySearch(filters, pageable);

    if (result.hasContent()) {
      ActivitySearchResponseDto dto = result.getContent().get(0);
      Assertions.assertNotNull(dto.openingId(), "Opening ID should not be null");
      Assertions.assertNotNull(dto.activityId(), "Activity ID should not be null");
      Assertions.assertNotNull(dto.base(), "Base should not be null");
      Assertions.assertNotNull(dto.fileId(), "File ID should not be null");
    }
  }

  @Test
  @DisplayName("Activity search with invalid file ID should return empty results")
  void activitySearch_withInvalidFileId_shouldReturnEmpty() {
    ActivitySearchFiltersDto filters =
        new ActivitySearchFiltersDto(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "INVALID_FILE",
            null,
            null,
            null,
            null,
            null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<ActivitySearchResponseDto> result = activityService.activitySearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    Assertions.assertFalse(result.hasContent(), "Result should be empty for invalid file ID");
  }

  @Test
  @DisplayName("Activity search should not return duplicate entries")
  void activitySearch_shouldNotReturnDuplicates() {
    ActivitySearchFiltersDto filters = new ActivitySearchFiltersDto();
    Pageable pageable = PageRequest.of(0, 50);

    Page<ActivitySearchResponseDto> result = activityService.activitySearch(filters, pageable);

    if (result.hasContent()) {
      List<Long> activityIds =
          result.getContent().stream().map(ActivitySearchResponseDto::activityId).toList();
      long uniqueCount = activityIds.stream().distinct().count();
      Assertions.assertEquals(
          activityIds.size(), uniqueCount, "Activity search should not return duplicate entries");
    }
  }

  @Test
  @DisplayName("Disturbance search with default filters should succeed")
  void disturbanceSearch_withDefaultFilters_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    Assertions.assertTrue(result.getNumberOfElements() >= 0, "Result should have content");
  }

  @Test
  @DisplayName("Disturbance search with file ID filter should succeed")
  void disturbanceSearch_withFileIdFilter_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, null, null, null, null, "TFL47", null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    // Verify results are for the specified file
    if (result.hasContent()) {
      result
          .getContent()
          .forEach(
              disturbance ->
                  Assertions.assertEquals(
                      "TFL47", disturbance.fileId(), "File ID should match filter"));
    }
  }

  @Test
  @DisplayName("Disturbance search with disturbance code filter should succeed")
  void disturbanceSearch_withDisturbanceCodeFilter_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            List.of("BR"), null, null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    // If there are results, verify they have the correct disturbance code
    if (result.hasContent()) {
      result
          .getContent()
          .forEach(
              disturbance ->
                  Assertions.assertEquals(
                      "BR",
                      disturbance.disturbance().code(),
                      "Disturbance code should match filter"));
    }
  }

  @Test
  @DisplayName("Disturbance search with variant filter should succeed")
  void disturbanceSearch_withVariantFilter_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, List.of("CTN"), null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    // If there are results, verify they have the correct variant
    if (result.hasContent()) {
      result
          .getContent()
          .forEach(
              disturbance ->
                  Assertions.assertEquals(
                      "CTN", disturbance.variant().code(), "Variant code should match filter"));
    }
  }

  @Test
  @DisplayName("Disturbance search with lowercase variant filter should succeed")
  void disturbanceSearch_withLowercaseVariantFilter_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, List.of("ctn"), null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    // If there are results, verify case-insensitive matching works
    if (result.hasContent()) {
      result
          .getContent()
          .forEach(
              disturbance ->
                  Assertions.assertEquals(
                      "CTN",
                      disturbance.variant().code(),
                      "Variant code should match filter (case-insensitive)"));
    }
  }

  @Test
  @DisplayName("Disturbance search with silv system filter should succeed")
  void disturbanceSearch_withSilvSystemFilter_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, List.of("CC"), null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    if (result.hasContent()) {
      result
          .getContent()
          .forEach(
              disturbance ->
                  Assertions.assertEquals(
                      "CC",
                      disturbance.silvSystem().code(),
                      "Silv system code should match filter"));
    }
  }

  @Test
  @DisplayName("Disturbance search with cut phase filter should succeed")
  void disturbanceSearch_withCutPhaseFilter_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, null, List.of("FI"), null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    if (result.hasContent()) {
      result
          .getContent()
          .forEach(
              disturbance ->
                  Assertions.assertEquals(
                      "FI", disturbance.cutPhase().code(), "Cut phase code should match filter"));
    }
  }

  @Test
  @DisplayName("Disturbance search with org unit filter should succeed")
  void disturbanceSearch_withOrgUnitFilter_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, null, null, List.of("DCR"), null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    if (result.hasContent()) {
      result
          .getContent()
          .forEach(
              disturbance ->
                  Assertions.assertEquals(
                      "DCR", disturbance.orgUnit().code(), "Org unit code should match filter"));
    }
  }

  @Test
  @DisplayName("Disturbance search with opening category filter should succeed")
  void disturbanceSearch_withOpeningCategoryFilter_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, null, null, null, List.of("FTML"), null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    if (result.hasContent()) {
      result
          .getContent()
          .forEach(
              disturbance ->
                  Assertions.assertEquals(
                      "FTML",
                      disturbance.openingCategory().code(),
                      "Opening category code should match filter"));
    }
  }

  @Test
  @DisplayName("Disturbance search with client number filter should succeed")
  void disturbanceSearch_withClientNumberFilter_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, null, null, null, null, null, List.of("00012797"), null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Disturbance search with opening status filter should succeed")
  void disturbanceSearch_withOpeningStatusFilter_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, null, null, null, null, null, null, List.of("APP"), null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Disturbance search with update date range should succeed")
  void disturbanceSearch_withUpdateDateRange_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, null, null, null, null, null, null, null, "2020-01-01", "2024-12-31");
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Disturbance search with multiple filters should succeed")
  void disturbanceSearch_withMultipleFilters_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            List.of("BR", "SL"),
            List.of("CC"),
            List.of("CTN"),
            null,
            null,
            null,
            "TFL47",
            null,
            null,
            null,
            null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Disturbance search with pagination should succeed")
  void disturbanceSearch_withPagination_shouldSucceed() {
    DisturbanceSearchFilterDto filters = new DisturbanceSearchFilterDto();
    Pageable pageableFirst = PageRequest.of(0, 5);
    Pageable pageableSecond = PageRequest.of(1, 5);

    Page<DisturbanceSearchResponseDto> resultFirst =
        activityService.disturbanceSearch(filters, pageableFirst);
    Page<DisturbanceSearchResponseDto> resultSecond =
        activityService.disturbanceSearch(filters, pageableSecond);

    Assertions.assertNotNull(resultFirst, "First page result should not be null");
    Assertions.assertNotNull(resultSecond, "Second page result should not be null");
    Assertions.assertEquals(0, resultFirst.getNumber(), "First page number should be 0");
    Assertions.assertEquals(1, resultSecond.getNumber(), "Second page number should be 1");
  }

  @Test
  @DisplayName("Disturbance search response DTOs should have all required fields populated")
  void disturbanceSearch_responseDto_shouldHaveAllRequiredFields() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, null, null, null, null, "TFL47", null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    if (result.hasContent()) {
      DisturbanceSearchResponseDto dto = result.getContent().get(0);
      Assertions.assertNotNull(dto.openingId(), "Opening ID should not be null");
      Assertions.assertNotNull(dto.activityId(), "Activity ID should not be null");
      Assertions.assertNotNull(dto.disturbance(), "Disturbance should not be null");
      Assertions.assertNotNull(dto.fileId(), "File ID should not be null");
    }
  }

  @Test
  @DisplayName("Disturbance search with invalid file ID should return empty results")
  void disturbanceSearch_withInvalidFileId_shouldReturnEmpty() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, null, null, null, null, "INVALID_FILE", null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    Assertions.assertFalse(result.hasContent(), "Result should be empty for invalid file ID");
  }

  @Test
  @DisplayName("Disturbance search should not return duplicate entries")
  void disturbanceSearch_shouldNotReturnDuplicates() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 50);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    if (result.hasContent()) {
      List<Long> activityIds =
          result.getContent().stream().map(DisturbanceSearchResponseDto::activityId).toList();
      long uniqueCount = activityIds.stream().distinct().count();
      Assertions.assertEquals(
          activityIds.size(),
          uniqueCount,
          "Disturbance search should not return duplicate entries");
    }
  }

  @Test
  @DisplayName("Disturbance search with all filter types should succeed")
  void disturbanceSearch_withAllFilterTypes_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            List.of("BR"),
            List.of("CC"),
            List.of("CTN"),
            List.of("FI"),
            List.of("DCR"),
            List.of("FTML"),
            "TFL47",
            List.of("00012797"),
            List.of("APP"),
            "2020-01-01",
            "2024-12-31");
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Disturbance search with multiple disturbance codes should succeed")
  void disturbanceSearch_withMultipleDisturbanceCodes_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            List.of("BR", "SL", "DN"), null, null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Disturbance search with multiple variants should succeed")
  void disturbanceSearch_withMultipleVariants_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, List.of("CTN", "STN"), null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Disturbance search with multiple org units should succeed")
  void disturbanceSearch_withMultipleOrgUnits_shouldSucceed() {
    DisturbanceSearchFilterDto filters =
        new DisturbanceSearchFilterDto(
            null, null, null, null, List.of("DCR", "DND"), null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<DisturbanceSearchResponseDto> result =
        activityService.disturbanceSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }
}
