package ca.bc.gov.restapi.results.oracle.service;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.common.exception.MaxPageSizeException;
import ca.bc.gov.restapi.results.common.pagination.PaginatedResult;
import ca.bc.gov.restapi.results.common.pagination.PaginationParameters;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.extensions.WiremockLogNotifier;
import ca.bc.gov.restapi.results.extensions.WithMockJwt;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchResponseDto;
import ca.bc.gov.restapi.results.oracle.dto.RecentOpeningDto;
import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

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
  @DisplayName("Get a list of recent openings without user")
  void getRecentOpenings_fetchNoUserPaginated_shouldSucceed() {

    PaginatedResult<RecentOpeningDto> paginatedResult =
        openingService.getRecentOpenings(new PaginationParameters(1, 1));

    Assertions.assertNotNull(paginatedResult);
    Assertions.assertEquals(1, paginatedResult.getPageIndex());
    Assertions.assertEquals(3, paginatedResult.getTotalPages());
    Assertions.assertFalse(paginatedResult.getData().isEmpty());
    Assertions.assertEquals(1, paginatedResult.getData().size());
  }

  @Test
  @DisplayName("Opening search file id happy path should succeed")
  void openingSearch_fileId_shouldSucceed() {

    PaginatedResult<OpeningSearchResponseDto> result =
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
                "101017"
            ),
            new PaginationParameters(0, 10)
        );

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageIndex());
    Assertions.assertEquals(10, result.getPerPage());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(1, result.getData().size());
    Assertions.assertEquals(101017, result.getData().get(0).getOpeningId());
    Assertions.assertEquals(" 514",result.getData().get(0).getOpeningNumber());
    Assertions.assertEquals(OpeningCategoryEnum.FTML, result.getData().get(0).getCategory());
    Assertions.assertEquals(OpeningStatusEnum.FG, result.getData().get(0).getStatus());
    Assertions.assertEquals("12K", result.getData().get(0).getCuttingPermitId());
    Assertions.assertFalse(result.isHasNextPage());
  }

  @Test
  @DisplayName("Opening search org unit happy path should succeed")
  void openingSearch_orgUnit_shouldSucceed() {

    PaginatedResult<OpeningSearchResponseDto> result =
        openingService.openingSearch(
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
                null
            ),
            new PaginationParameters(0, 10)
        );

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageIndex());
    Assertions.assertEquals(10, result.getPerPage());
    Assertions.assertEquals(1, result.getTotalPages());
    Assertions.assertEquals(3, result.getData().size());
    Assertions.assertEquals(1524010, result.getData().get(0).getOpeningId());
    Assertions.assertNull(result.getData().get(0).getOpeningNumber());
    Assertions.assertEquals(OpeningCategoryEnum.NREQ, result.getData().get(0).getCategory());
    Assertions.assertEquals(OpeningStatusEnum.FG, result.getData().get(0).getStatus());
    Assertions.assertFalse(result.isHasNextPage());
  }

  @Test
  @DisplayName("Opening search no records found should succeed")
  void openingSearch_noRecordsFound_shouldSucceed() {
    PaginatedResult<OpeningSearchResponseDto> result =
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
            new PaginationParameters(0, 10)
        );

    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getPageIndex());
    Assertions.assertEquals(10, result.getPerPage());
    Assertions.assertEquals(0, result.getTotalPages());
    Assertions.assertTrue(result.getData().isEmpty());
    Assertions.assertFalse(result.isHasNextPage());
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
                new PaginationParameters(0, 2999)
            )
    );
  }
}
