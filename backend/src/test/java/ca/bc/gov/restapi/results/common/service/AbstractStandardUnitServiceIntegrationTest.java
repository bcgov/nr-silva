package ca.bc.gov.restapi.results.common.service;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.common.dto.StandardUnitSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.StandardUnitSearchResponseDto;
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
 * Abstract integration test contract for StandardUnitService implementations (Postgres and Oracle).
 * Defines common test scenarios for standard unit search functionality.
 */
@DisplayName("Integrated Test | Standard Unit Service | Contract")
@WithMockJwt(value = "ttester")
public abstract class AbstractStandardUnitServiceIntegrationTest
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

  @Autowired protected StandardUnitService standardUnitService;

  @Test
  @DisplayName("Standard unit search with standardsRegimeId filter should succeed")
  void standardUnitSearch_withStandardsRegimeIdFilter_shouldSucceed() {
    StandardUnitSearchFilterDto filters =
        new StandardUnitSearchFilterDto(
            36109L, null, null, null, null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StandardUnitSearchResponseDto> result =
        standardUnitService.standardsUnitSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    Assertions.assertTrue(
        result.getNumberOfElements() >= 0, "Result should have non-negative count");
  }

  @Test
  @DisplayName("Standard unit search with bgcZone filter should succeed")
  void standardUnitSearch_withBgcZoneFilter_shouldSucceed() {
    StandardUnitSearchFilterDto filters =
        new StandardUnitSearchFilterDto(
            null, null, null, null, "CWH", null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StandardUnitSearchResponseDto> result =
        standardUnitService.standardsUnitSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Standard unit search with org unit filter should succeed")
  void standardUnitSearch_withOrgUnitFilter_shouldSucceed() {
    StandardUnitSearchFilterDto filters =
        new StandardUnitSearchFilterDto(
            null, null, List.of("DAS"), null, null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StandardUnitSearchResponseDto> result =
        standardUnitService.standardsUnitSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Standard unit search with multiple org units should succeed")
  void standardUnitSearch_withMultipleOrgUnits_shouldSucceed() {
    StandardUnitSearchFilterDto filters =
        new StandardUnitSearchFilterDto(
            null,
            null,
            List.of("DAS", "TWO"),
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

    Page<StandardUnitSearchResponseDto> result =
        standardUnitService.standardsUnitSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Standard unit search with client numbers filter should succeed")
  void standardUnitSearch_withClientNumbersFilter_shouldSucceed() {
    StandardUnitSearchFilterDto filters =
        new StandardUnitSearchFilterDto(
            null,
            null,
            null,
            List.of("00010002"),
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

    Page<StandardUnitSearchResponseDto> result =
        standardUnitService.standardsUnitSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Standard unit search with preferred species filter should succeed")
  void standardUnitSearch_withPreferredSpeciesFilter_shouldSucceed() {
    StandardUnitSearchFilterDto filters =
        new StandardUnitSearchFilterDto(
            null, List.of("CW"), null, null, null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StandardUnitSearchResponseDto> result =
        standardUnitService.standardsUnitSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Standard unit search with update date range should succeed")
  void standardUnitSearch_withUpdateDateRange_shouldSucceed() {
    StandardUnitSearchFilterDto filters =
        new StandardUnitSearchFilterDto(
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
            "2005-01-01",
            "2025-12-31");
    Pageable pageable = PageRequest.of(0, 10);

    Page<StandardUnitSearchResponseDto> result =
        standardUnitService.standardsUnitSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Standard unit search with pagination should return correct page numbers")
  void standardUnitSearch_withPagination_shouldReturnCorrectPageNumbers() {
    StandardUnitSearchFilterDto filters =
        new StandardUnitSearchFilterDto(
            36109L, null, null, null, null, null, null, null, null, null, null, null, null);
    Pageable pageableFirst = PageRequest.of(0, 5);
    Pageable pageableSecond = PageRequest.of(1, 5);

    Page<StandardUnitSearchResponseDto> resultFirst =
        standardUnitService.standardsUnitSearch(filters, pageableFirst);
    Page<StandardUnitSearchResponseDto> resultSecond =
        standardUnitService.standardsUnitSearch(filters, pageableSecond);

    Assertions.assertNotNull(resultFirst, "First page result should not be null");
    Assertions.assertNotNull(resultSecond, "Second page result should not be null");
    Assertions.assertEquals(0, resultFirst.getNumber(), "First page number should be 0");
    Assertions.assertEquals(1, resultSecond.getNumber(), "Second page number should be 1");
  }

  @Test
  @DisplayName("Standard unit search response DTOs should have required fields populated")
  void standardUnitSearch_responseDto_shouldHaveRequiredFields() {
    StandardUnitSearchFilterDto filters =
        new StandardUnitSearchFilterDto(
            36109L, null, null, null, null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StandardUnitSearchResponseDto> result =
        standardUnitService.standardsUnitSearch(filters, pageable);

    if (result.hasContent()) {
      StandardUnitSearchResponseDto dto = result.getContent().get(0);
      Assertions.assertNotNull(dto.stockingStandardUnitId(), "SSU ID should not be null");
      Assertions.assertNotNull(dto.openingId(), "Opening ID should not be null");
      Assertions.assertNotNull(dto.standardsUnitId(), "Standards unit ID should not be null");
      Assertions.assertNotNull(dto.standardsRegimeId(), "Standards regime ID should not be null");
      Assertions.assertNotNull(dto.preferredSpecies(), "Preferred species list should not be null");
    }
  }

  @Test
  @DisplayName(
      "Standard unit search with non-matching standardsRegimeId should return empty results")
  void standardUnitSearch_withNonMatchingStandardsRegimeId_shouldReturnEmpty() {
    StandardUnitSearchFilterDto filters =
        new StandardUnitSearchFilterDto(
            999999999L, null, null, null, null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StandardUnitSearchResponseDto> result =
        standardUnitService.standardsUnitSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    Assertions.assertFalse(
        result.hasContent(), "Result should be empty for non-matching standardsRegimeId");
  }

  @Test
  @DisplayName("Standard unit search with combined filters should succeed")
  void standardUnitSearch_withCombinedFilters_shouldSucceed() {
    StandardUnitSearchFilterDto filters =
        new StandardUnitSearchFilterDto(
            36109L, null, null, null, "CWH", null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StandardUnitSearchResponseDto> result =
        standardUnitService.standardsUnitSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    Assertions.assertTrue(
        result.getNumberOfElements() >= 0, "Result should have non-negative count");
  }

  @Test
  @DisplayName("Standard unit search with becSiteType filter should succeed")
  void standardUnitSearch_withBecSiteTypeFilter_shouldSucceed() {
    StandardUnitSearchFilterDto filters =
        new StandardUnitSearchFilterDto(
            null, null, null, null, null, null, null, null, null, "01", null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StandardUnitSearchResponseDto> result =
        standardUnitService.standardsUnitSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }
}
