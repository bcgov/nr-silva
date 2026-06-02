package ca.bc.gov.restapi.results.common.service;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchResponseDto;
import ca.bc.gov.restapi.results.common.enums.StockingStandardsCommentLocationCode;
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

@DisplayName("Integrated Test | Stocking Standards Comment Search Service | Contract")
@WithMockJwt(value = "ttester")
public abstract class AbstractStockingStandardsCommentSearchServiceIntegrationTest
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
  @DisplayName("Comment search with matching searchTerm should return non-null result")
  void commentSearch_matchingSearchTerm_shouldReturnNonNull() {
    StockingStandardsCommentSearchFilterDto filter =
        new StockingStandardsCommentSearchFilterDto("Baseline", null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsCommentSearchResponseDto> result =
        stockingStandardsService.stockingStandardsCommentSearch(filter, pageable);

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.getTotalElements() >= 0);
  }

  @Test
  @DisplayName("Comment search with non-matching searchTerm should return empty page")
  void commentSearch_nonMatchingSearchTerm_shouldReturnEmpty() {
    StockingStandardsCommentSearchFilterDto filter =
        new StockingStandardsCommentSearchFilterDto(
            "ZZZNOMATCHZZZXXX999", null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsCommentSearchResponseDto> result =
        stockingStandardsService.stockingStandardsCommentSearch(filter, pageable);

    Assertions.assertEquals(0L, result.getTotalElements());
  }

  @Test
  @DisplayName("Comment search with STANDARDS_NAME location filter should succeed")
  void commentSearch_withStandardsNameLocation_shouldSucceed() {
    StockingStandardsCommentSearchFilterDto filter =
        new StockingStandardsCommentSearchFilterDto(
            "Baseline",
            List.of(StockingStandardsCommentLocationCode.STANDARDS_NAME),
            null,
            null,
            null,
            null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsCommentSearchResponseDto> result =
        stockingStandardsService.stockingStandardsCommentSearch(filter, pageable);

    Assertions.assertNotNull(result);
  }

  @Test
  @DisplayName("Comment search with ADDITIONAL_STANDARDS location filter should succeed")
  void commentSearch_withAdditionalStandardsLocation_shouldSucceed() {
    StockingStandardsCommentSearchFilterDto filter =
        new StockingStandardsCommentSearchFilterDto(
            "limited",
            List.of(StockingStandardsCommentLocationCode.ADDITIONAL_STANDARDS),
            null,
            null,
            null,
            null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsCommentSearchResponseDto> result =
        stockingStandardsService.stockingStandardsCommentSearch(filter, pageable);

    Assertions.assertNotNull(result);
  }

  @Test
  @DisplayName("Comment search with date range should succeed")
  void commentSearch_withDateRange_shouldSucceed() {
    StockingStandardsCommentSearchFilterDto filter =
        new StockingStandardsCommentSearchFilterDto(
            "Baseline", null, null, null, "2000-01-01", "2030-12-31");
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsCommentSearchResponseDto> result =
        stockingStandardsService.stockingStandardsCommentSearch(filter, pageable);

    Assertions.assertNotNull(result);
  }

  @Test
  @DisplayName("Comment search with pagination should respect page size")
  void commentSearch_withPagination_shouldRespectPageSize() {
    StockingStandardsCommentSearchFilterDto filter =
        new StockingStandardsCommentSearchFilterDto("Baseline", null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 1);

    Page<StockingStandardsCommentSearchResponseDto> result =
        stockingStandardsService.stockingStandardsCommentSearch(filter, pageable);

    Assertions.assertNotNull(result);
    Assertions.assertTrue(result.getContent().size() <= 1);
  }

  @Test
  @DisplayName("Comment search result should carry pagination metadata")
  void commentSearch_shouldCarryPaginationMetadata() {
    StockingStandardsCommentSearchFilterDto filter =
        new StockingStandardsCommentSearchFilterDto("Baseline", null, null, null, null, null);
    Pageable pageable = PageRequest.of(0, 10);

    Page<StockingStandardsCommentSearchResponseDto> result =
        stockingStandardsService.stockingStandardsCommentSearch(filter, pageable);

    Assertions.assertNotNull(result.getPageable());
    Assertions.assertEquals(0, result.getNumber());
    Assertions.assertEquals(10, result.getSize());
  }
}
