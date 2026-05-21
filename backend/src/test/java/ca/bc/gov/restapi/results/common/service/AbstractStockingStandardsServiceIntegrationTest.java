package ca.bc.gov.restapi.results.common.service;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchResponseDto;
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
 * Abstract integration test contract for StockingStandardsService implementations (Postgres and
 * Oracle). Defines common test scenarios for stocking standards search functionality.
 */
@DisplayName("Integrated Test | Stocking Standards Service | Contract")
@WithMockJwt(value = "ttester")
public abstract class AbstractStockingStandardsServiceIntegrationTest
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

  @Autowired protected StockingStandardsService stockingStandardsService;

  @Test
  @DisplayName("Stocking standards search with standardsRegimeId filter should succeed")
  void stockingStandardsSearch_withStandardsRegimeIdFilter_shouldSucceed() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            36109L, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    Assertions.assertTrue(
        result.getNumberOfElements() >= 0, "Result should have non-negative count");
  }

  @Test
  @DisplayName("Stocking standards search with bgcZone filter should succeed")
  void stockingStandardsSearch_withBgcZoneFilter_shouldSucceed() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, null, "CWH", null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Stocking standards search with org unit filter should succeed")
  void stockingStandardsSearch_withOrgUnitFilter_shouldSucceed() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            null,
            null,
            List.of("DAS"),
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

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Stocking standards search with client numbers filter should succeed")
  void stockingStandardsSearch_withClientNumbersFilter_shouldSucceed() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
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
            null,
            null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Stocking standards search with preferred species filter should succeed")
  void stockingStandardsSearch_withPreferredSpeciesFilter_shouldSucceed() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            null,
            List.of("CW"),
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

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Stocking standards search with fspId filter should succeed")
  void stockingStandardsSearch_withFspIdFilter_shouldSucceed() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, "1234", null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Stocking standards search with update date range should succeed")
  void stockingStandardsSearch_withUpdateDateRange_shouldSucceed() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
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
            "2005-01-01",
            "2025-12-31");
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Stocking standards search with pagination should return correct page numbers")
  void stockingStandardsSearch_withPagination_shouldReturnCorrectPageNumbers() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            36109L, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Pageable pageableFirst = PageRequest.of(0, 5);
    Pageable pageableSecond = PageRequest.of(1, 5);

    Page<StockingStandardsSearchResponseDto> resultFirst =
        stockingStandardsService.stockingStandardsSearch(filters, pageableFirst);
    Page<StockingStandardsSearchResponseDto> resultSecond =
        stockingStandardsService.stockingStandardsSearch(filters, pageableSecond);

    Assertions.assertNotNull(resultFirst, "First page result should not be null");
    Assertions.assertNotNull(resultSecond, "Second page result should not be null");
    Assertions.assertEquals(0, resultFirst.getNumber(), "First page number should be 0");
    Assertions.assertEquals(1, resultSecond.getNumber(), "Second page number should be 1");
  }

  @Test
  @DisplayName("Stocking standards search response DTO should have required fields populated")
  void stockingStandardsSearch_responseDto_shouldHaveRequiredFields() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            36109L, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    if (result.hasContent()) {
      StockingStandardsSearchResponseDto dto = result.getContent().get(0);
      Assertions.assertNotNull(dto.standardsRegimeId(), "Regime ID should not be null");
      Assertions.assertNotNull(dto.preferredSpecies(), "Preferred species list should not be null");
      Assertions.assertNotNull(dto.orgUnits(), "Org units list should not be null");
      Assertions.assertNotNull(dto.clients(), "Clients list should not be null");
      Assertions.assertNotNull(dto.fspIds(), "FSP IDs list should not be null");
    }
  }

  @Test
  @DisplayName(
      "Stocking standards search with non-matching standardsRegimeId should return empty results")
  void stockingStandardsSearch_withNonMatchingStandardsRegimeId_shouldReturnEmpty() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            999999999L,
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

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
    Assertions.assertFalse(
        result.hasContent(), "Result should be empty for non-matching standardsRegimeId");
  }

  @Test
  @DisplayName("Stocking standards search with becSiteType filter should succeed")
  void stockingStandardsSearch_withBecSiteTypeFilter_shouldSucceed() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, null, null, null, null, null, null, "01", null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Stocking standards search with becSeral filter should succeed")
  void stockingStandardsSearch_withBecSeralFilter_shouldSucceed() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, null, null, null, null, null, null, null, "CL", null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Stocking standards search with bgcSubZone filter should succeed")
  void stockingStandardsSearch_withBgcSubZoneFilter_shouldSucceed() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            null, null, null, null, null, null, "wm", null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Stocking standards search with multiple preferred species should succeed")
  void stockingStandardsSearch_withMultiplePreferredSpecies_shouldSucceed() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            null,
            List.of("CW", "FD"),
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

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    Assertions.assertNotNull(result, "Result should not be null");
  }

  @Test
  @DisplayName("Stocking standards search response DTO preferred species list should never be null")
  void stockingStandardsSearch_preferredSpeciesList_neverNull() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
            36109L, null, null, null, null, null, null, null, null, null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsSearchResponseDto> result =
        stockingStandardsService.stockingStandardsSearch(filters, pageable);

    result
        .getContent()
        .forEach(
            dto ->
                Assertions.assertNotNull(
                    dto.preferredSpecies(), "preferredSpecies list must never be null"));
  }

  @Test
  @DisplayName(
      "Stocking standards search with invalid date range should throw ResponseStatusException")
  void stockingStandardsSearch_withInvalidDateRange_shouldThrowException() {
    StockingStandardsSearchFilterDto filters =
        new StockingStandardsSearchFilterDto(
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
            "2025-12-31",
            "2025-01-01");
    Pageable pageable = PageRequest.of(0, 10);

    Assertions.assertThrows(
        org.springframework.web.server.ResponseStatusException.class,
        () -> stockingStandardsService.stockingStandardsSearch(filters, pageable),
        "Should throw ResponseStatusException for end-before-start date range");
  }
}
