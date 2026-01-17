package ca.bc.gov.restapi.results.oracle.service;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.common.exception.MaxPageSizeException;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchExactFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Integrated Test | Opening Search Service")
@WithMockJwt(value = "ttester")
class OpeningSearchServiceTest extends AbstractTestContainerIntegrationTest {

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

  @Autowired private OpeningSearchService openingSearchService;

  @Test
  @DisplayName("Opening search file id happy path should succeed")
  void openingSearch_fileId_shouldSucceed() {

    Page<OpeningSearchResponseDto> result =
        openingSearchService.openingSearch(
            new OpeningSearchFiltersDto(
                null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, "101017"),
            PageRequest.of(0, 10));

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageable().getPageNumber());
    Assertions.assertEquals(10, result.getPageable().getPageSize());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(1, result.getContent().size());
    Assertions.assertEquals(101017, result.getContent().get(0).getOpeningId());
    // Opening number is now composed (e.g., "92K 014 0.0 514" instead of just "514")
    Assertions.assertTrue(result.getContent().get(0).getOpeningNumber().contains("514"));
    Assertions.assertEquals(OpeningCategoryEnum.FTML, result.getContent().get(0).getCategory());
    Assertions.assertEquals(OpeningStatusEnum.FG, result.getContent().get(0).getStatus());
    Assertions.assertEquals("12K", result.getContent().get(0).getCuttingPermitId());
  }

  @Test
  @DisplayName("Opening search org unit happy path should succeed")
  void openingSearch_orgUnit_shouldSucceed() {

    Page<OpeningSearchResponseDto> result =
        openingSearchService.openingSearch(
            new OpeningSearchFiltersDto(
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
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null),
            PageRequest.of(0, 10));

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageable().getPageNumber());
    Assertions.assertEquals(10, result.getPageable().getPageSize());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(9, result.getContent().size());
    Assertions.assertEquals(1796497, result.getContent().get(0).getOpeningId());
    // Opening number may be "--" if not available
    Assertions.assertNotNull(result.getContent().get(0).getOpeningNumber());
  }

  @Test
  @DisplayName("Opening search no records found should succeed")
  void openingSearch_noRecordsFound_shouldSucceed() {
    Page<OpeningSearchResponseDto> result =
        openingSearchService.openingSearch(
            new OpeningSearchFiltersDto(
                null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, "ABCD"),
            PageRequest.of(0, 10));

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageable().getPageNumber());
    Assertions.assertEquals(10, result.getPageable().getPageSize());
    Assertions.assertEquals(0, result.getTotalPages());
    Assertions.assertTrue(result.getContent().isEmpty());
    Assertions.assertFalse(result.hasNext());
  }

  @Test
  @DisplayName("Opening search max page exception should fail")
  void openingSearch_maxPageException_shouldFail() {
    OpeningSearchFiltersDto filterDto =
        new OpeningSearchFiltersDto(
            null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, "FTML");
    PageRequest pagination = PageRequest.of(0, 2999);
    Assertions.assertThrows(
        MaxPageSizeException.class,
        () -> openingSearchService.openingSearch(filterDto, pagination));
  }

  @Test
  @DisplayName("Opening search exact with mapsheet grid should succeed")
  void openingSearchExact_mapsheetGrid_shouldSucceed() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            0L, null, null, null, null, null, null, null, null, null, null, null, null, null, "93",
            null, null, null, null, null);

    Page<OpeningSearchResponseDto> result =
        openingSearchService.openingSearchExact(filterDto, PageRequest.of(0, 10));

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.getContent().size() > 0);
  }

  @Test
  @DisplayName("Opening search exact with sub opening number should succeed")
  void openingSearchExact_subOpeningNumber_shouldSucceed() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            0L, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, "514");

    Page<OpeningSearchResponseDto> result =
        openingSearchService.openingSearchExact(filterDto, PageRequest.of(0, 10));

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.getContent().size() > 0);
    // Verify the composed opening number is properly formatted (not just raw DB value)
    String openingNumber = result.getContent().get(0).getOpeningNumber();
    Assertions.assertNotNull(openingNumber);
    Assertions.assertFalse(openingNumber.equals("514")); // Should be composed, not raw
  }

  @Test
  @DisplayName("Opening search exact with no records found should succeed")
  void openingSearchExact_noRecordsFound_shouldSucceed() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            999999L, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null);

    Page<OpeningSearchResponseDto> result =
        openingSearchService.openingSearchExact(filterDto, PageRequest.of(0, 10));

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.getContent().isEmpty());
  }

  @Test
  @DisplayName("Opening search exact max page exception should fail")
  void openingSearchExact_maxPageException_shouldFail() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            0L, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null);
    PageRequest pagination = PageRequest.of(0, 2999);
    Assertions.assertThrows(
        MaxPageSizeException.class,
        () -> openingSearchService.openingSearchExact(filterDto, pagination));
  }

  @Test
  @DisplayName("Opening search exact with invalid entry date range should fail")
  void openingSearchExact_invalidEntryDateRange_shouldFail() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            0L,
            null,
            null,
            null,
            null,
            "2025-01-16",
            "2025-01-01",
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

    Assertions.assertThrows(
        ResponseStatusException.class,
        () -> openingSearchService.openingSearchExact(filterDto, PageRequest.of(0, 10)));
  }

  @Test
  @DisplayName("Opening search exact with valid date format should succeed")
  void openingSearchExact_validDateFormat_shouldSucceed() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            0L,
            null,
            null,
            null,
            null,
            "2001-01-01",
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
            null,
            null);

    Page<OpeningSearchResponseDto> result =
        openingSearchService.openingSearchExact(filterDto, PageRequest.of(0, 10));

    Assertions.assertNotNull(result);
  }

  @Test
  @DisplayName("Opening search exact with invalid mapsheet grid should fail")
  void openingSearchExact_invalidMapsheetGrid_shouldFail() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            0L, null, null, null, null, null, null, null, null, null, null, null, null, null, "99",
            null, null, null, null, null);

    Assertions.assertThrows(
        ResponseStatusException.class,
        () -> openingSearchService.openingSearchExact(filterDto, PageRequest.of(0, 10)));
  }

  @Test
  @DisplayName("Opening search exact with invalid mapsheet letter should fail")
  void openingSearchExact_invalidMapsheetLetter_shouldFail() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            0L, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            "X", null, null, null, null);

    Assertions.assertThrows(
        ResponseStatusException.class,
        () -> openingSearchService.openingSearchExact(filterDto, PageRequest.of(0, 10)));
  }

  @Test
  @DisplayName("Opening search exact with invalid mapsheet quad should fail")
  void openingSearchExact_invalidMapsheetQuad_shouldFail() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            0L, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, "5", null, null);

    Assertions.assertThrows(
        ResponseStatusException.class,
        () -> openingSearchService.openingSearchExact(filterDto, PageRequest.of(0, 10)));
  }

  @Test
  @DisplayName("Opening search exact with invalid mapsheet sub quad should fail")
  void openingSearchExact_invalidMapsheetSubQuad_shouldFail() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            0L, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, "6", null);

    Assertions.assertThrows(
        ResponseStatusException.class,
        () -> openingSearchService.openingSearchExact(filterDto, PageRequest.of(0, 10)));
  }

  @Test
  @DisplayName("Opening search exact with valid entry date range should succeed")
  void openingSearchExact_validEntryDateRange_shouldSucceed() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            0L,
            null,
            null,
            null,
            null,
            "2001-01-01",
            "2025-12-31",
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

    Page<OpeningSearchResponseDto> result =
        openingSearchService.openingSearchExact(filterDto, PageRequest.of(0, 10));

    Assertions.assertNotNull(result);
    // Should succeed without throwing exception
  }

  @Test
  @DisplayName("Opening search exact with valid mapsheet fields should succeed")
  void openingSearchExact_validMapsheetFields_shouldSucceed() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            0L, null, null, null, null, null, null, null, null, null, null, null, null, null, "93",
            "O", "045", "0", "0", null);

    Page<OpeningSearchResponseDto> result =
        openingSearchService.openingSearchExact(filterDto, PageRequest.of(0, 10));

    Assertions.assertNotNull(result);
    // Should succeed without throwing exception
  }
}
