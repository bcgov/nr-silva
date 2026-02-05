package ca.bc.gov.restapi.results.common.service.opening;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchExactFiltersDto;
import ca.bc.gov.restapi.results.common.dto.opening.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.common.exception.MaxPageSizeException;
import ca.bc.gov.restapi.results.common.service.OpeningSearchService;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;

@DisplayName("Integrated Test | Opening Search Service | Contract")
@WithMockJwt(value = "ttester")
public abstract class AbstractOpeningSearchServiceIntegrationTest<T extends OpeningSearchService>
    extends AbstractTestContainerIntegrationTest {

  @Autowired protected OpeningSearchService openingSearchService;

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
  @DisplayName("Opening search exact with mapsheet key should succeed")
  void openingSearchExact_mapsheetKey_shouldSucceed() {
    OpeningSearchExactFiltersDto filterDto =
        new OpeningSearchExactFiltersDto(
            0L, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, "514");

    Page<OpeningSearchResponseDto> result =
        openingSearchService.openingSearchExact(filterDto, PageRequest.of(0, 10));

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.getContent().size() > 0);
    // Verify the composed mapsheet key is properly formatted (not just raw DB value)
    String mapsheetKey = result.getContent().get(0).getMapsheetKey();
    Assertions.assertNotNull(mapsheetKey);
    Assertions.assertFalse(mapsheetKey.equals("514")); // Should be composed, not raw
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
