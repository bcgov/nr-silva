package ca.bc.gov.restapi.results.oracle.service;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.common.exception.MaxPageSizeException;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DisplayName("Integrated Test | Opening Service")
@WithMockJwt(value = "ttester")
class OpeningServiceTest extends AbstractTestContainerIntegrationTest {

  @RegisterExtension
  static WireMockExtension clientApiStub = WireMockExtension
      .newInstance()
      .options(
          wireMockConfig()
              .port(10000)
              .notifier(new WiremockLogNotifier())
              .asynchronousResponseEnabled(true)
              .stubRequestLoggingDisabled(false)
      )
      .configureStaticDsl(true)
      .build();

  @Autowired
  private OpeningService openingService;

  @Test
  @DisplayName("Opening search file id happy path should succeed")
  void openingSearch_fileId_shouldSucceed() {

    Page<OpeningSearchResponseDto> result =
        openingService.openingSearch(
            new OpeningSearchFiltersDto(
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
                null,
                "103"
            ),
            PageRequest.of(0, 10)
        );

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageable().getPageNumber());
    Assertions.assertEquals(10, result.getPageable().getPageSize());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(1, result.getContent().size());
    Assertions.assertEquals(103, result.getContent().get(0).getOpeningId());
    Assertions.assertNull(result.getContent().get(0).getOpeningNumber());
    Assertions.assertEquals(OpeningCategoryEnum.FTML, result.getContent().get(0).getCategory());
    Assertions.assertEquals(OpeningStatusEnum.APP, result.getContent().get(0).getStatus());
    Assertions.assertEquals("14T", result.getContent().get(0).getCuttingPermitId());
    Assertions.assertEquals("49/14S", result.getContent().get(0).getTimberMark());
    Assertions.assertEquals("14-71", result.getContent().get(0).getCutBlockId());
    Assertions.assertEquals(new BigDecimal("14.9"),
        result.getContent().get(0).getOpeningGrossAreaHa());
    Assertions.assertEquals(LocalDateTime.of(2024, 1, 24, 0, 0, 0),
        result.getContent().get(0).getDisturbanceStartDate());
    Assertions.assertEquals("TFL49", result.getContent().get(0).getForestFileId());
    Assertions.assertEquals("TWO", result.getContent().get(0).getOrgUnitCode());
    Assertions.assertEquals("Org two", result.getContent().get(0).getOrgUnitName());
    Assertions.assertNull(result.getContent().get(0).getClientNumber());
    Assertions.assertEquals(LocalDateTime.of(2024, 1, 24, 0, 0, 0),
        result.getContent().get(0).getRegenDelayDate());
    Assertions.assertEquals(LocalDateTime.of(2024, 1, 24, 0, 0, 0),
        result.getContent().get(0).getEarlyFreeGrowingDate());
    Assertions.assertEquals(LocalDateTime.of(2024, 1, 24, 0, 0, 0),
        result.getContent().get(0).getLateFreeGrowingDate());
    Assertions.assertEquals("IDIR@TTESTER", result.getContent().get(0).getEntryUserId());
    Assertions.assertFalse(result.getContent().get(0).getSubmittedToFrpa());
    Assertions.assertFalse(result.hasNext());
  }

  @Test
  @DisplayName("Opening search org unit happy path should succeed")
  void openingSearch_orgUnit_shouldSucceed() {

    Page<OpeningSearchResponseDto> result =
        openingService.openingSearch(
            new OpeningSearchFiltersDto(
                List.of("TWO"),
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
                null
            ),
            PageRequest.of(0, 10)
        );

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageable().getPageNumber());
    Assertions.assertEquals(10, result.getPageable().getPageSize());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(1, result.getContent().size());
    Assertions.assertEquals(103, result.getContent().get(0).getOpeningId());
    Assertions.assertNull(result.getContent().get(0).getOpeningNumber());
    Assertions.assertEquals(OpeningCategoryEnum.FTML, result.getContent().get(0).getCategory());
    Assertions.assertEquals(OpeningStatusEnum.APP, result.getContent().get(0).getStatus());
    Assertions.assertEquals("14T", result.getContent().get(0).getCuttingPermitId());
    Assertions.assertEquals("49/14S", result.getContent().get(0).getTimberMark());
    Assertions.assertEquals("14-71", result.getContent().get(0).getCutBlockId());
    Assertions.assertEquals(new BigDecimal("14.9"),
        result.getContent().get(0).getOpeningGrossAreaHa());
    Assertions.assertEquals(LocalDateTime.of(2024, 1, 24, 0, 0, 0),
        result.getContent().get(0).getDisturbanceStartDate());
    Assertions.assertEquals("TFL49", result.getContent().get(0).getForestFileId());
    Assertions.assertEquals("TWO", result.getContent().get(0).getOrgUnitCode());
    Assertions.assertEquals("Org two", result.getContent().get(0).getOrgUnitName());
    Assertions.assertNull(result.getContent().get(0).getClientNumber());
    Assertions.assertEquals(LocalDateTime.of(2024, 1, 24, 0, 0, 0),
        result.getContent().get(0).getRegenDelayDate());
    Assertions.assertEquals(LocalDateTime.of(2024, 1, 24, 0, 0, 0),
        result.getContent().get(0).getEarlyFreeGrowingDate());
    Assertions.assertEquals(LocalDateTime.of(2024, 1, 24, 0, 0, 0),
        result.getContent().get(0).getLateFreeGrowingDate());
    Assertions.assertEquals("IDIR@TTESTER", result.getContent().get(0).getEntryUserId());
    Assertions.assertFalse(result.getContent().get(0).getSubmittedToFrpa());
    Assertions.assertFalse(result.hasNext());
  }

  @Test
  @DisplayName("Opening search no records found should succeed")
  void openingSearch_noRecordsFound_shouldSucceed() {
    Page<OpeningSearchResponseDto> result =
        openingService.openingSearch(
            new OpeningSearchFiltersDto(
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
                null,
                "ABCD"
            ),
            PageRequest.of(0, 10)
        );

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
    Assertions.assertThrows(
        MaxPageSizeException.class,
        () ->
            openingService.openingSearch(
                new OpeningSearchFiltersDto(
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
                    null,
                    "FTML"
                ),
                PageRequest.of(0, 2999)
            )
    );
  }
}
