package ca.bc.gov.restapi.results.common.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.stockingstandards.StockingStandardsCommentSearchResponseDto;
import ca.bc.gov.restapi.results.common.enums.StockingStandardsCommentLocationCode;
import ca.bc.gov.restapi.results.common.projection.StockingStandardsCommentSearchProjection;
import ca.bc.gov.restapi.results.common.repository.StockingStandardsRepository;
import ca.bc.gov.restapi.results.common.service.ForestClientService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | AbstractStockingStandardsService — comment search")
class AbstractStockingStandardsServiceCommentSearchTest {

  @Mock private StockingStandardsRepository repository;
  @Mock private ForestClientService forestClientService;

  private AbstractStockingStandardsService service;

  @BeforeEach
  void setup() {
    service = new AbstractStockingStandardsService(repository, forestClientService) {};
  }

  // -------------------------------------------------------------------------
  // Helpers
  // -------------------------------------------------------------------------

  private StockingStandardsCommentSearchProjection projection(
      String commentLocation,
      LocalDateTime expiryDate,
      String commentText,
      String orgCodes,
      String orgNames,
      String clientNumbers,
      String fspIds,
      Long totalCount) {
    StockingStandardsCommentSearchProjection p =
        Mockito.mock(StockingStandardsCommentSearchProjection.class);
    lenient().when(p.getStandardsRegimeId()).thenReturn(1L);
    lenient().when(p.getCommentLocation()).thenReturn(commentLocation);
    lenient().when(p.getExpiryDate()).thenReturn(expiryDate);
    lenient().when(p.getCommentText()).thenReturn(commentText);
    lenient().when(p.getUpdateTimestamp()).thenReturn(LocalDateTime.of(2024, 1, 1, 0, 0));
    lenient().when(p.getApprovedTimestamp()).thenReturn(LocalDateTime.of(2023, 6, 1, 0, 0));
    lenient().when(p.getOrgUnitCodes()).thenReturn(orgCodes);
    lenient().when(p.getOrgUnitNames()).thenReturn(orgNames);
    lenient().when(p.getClientNumbers()).thenReturn(clientNumbers);
    lenient().when(p.getFspIds()).thenReturn(fspIds);
    lenient().when(p.getTotalCount()).thenReturn(totalCount);
    return p;
  }

  private StockingStandardsCommentSearchProjection defaultProjection() {
    return projection("STANDARDS_NAME", null, "Some text", null, null, null, null, 1L);
  }

  private StockingStandardsCommentSearchFilterDto defaultFilter() {
    return new StockingStandardsCommentSearchFilterDto("Baseline", null, null, null, null, null);
  }

  private ForestClientDto client(String number) {
    return ForestClientDto.builder()
        .clientNumber(number)
        .clientName("Client " + number)
        .legalFirstName("")
        .legalMiddleName("")
        .acronym("CLI")
        .build();
  }

  // -------------------------------------------------------------------------
  // commentLocation — enum mapping
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("commentLocation STANDARDS_NAME mapped to enum")
  void commentLocation_standardsName_mappedToEnum() {
    StockingStandardsCommentSearchProjection p =
        projection("STANDARDS_NAME", null, "text", null, null, null, null, 1L);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(
        StockingStandardsCommentLocationCode.STANDARDS_NAME, dto.commentLocation());
  }

  @Test
  @DisplayName("commentLocation ADDITIONAL_STANDARDS mapped to enum")
  void commentLocation_additionalStandards_mappedToEnum() {
    StockingStandardsCommentSearchProjection p =
        projection("ADDITIONAL_STANDARDS", null, "text", null, null, null, null, 1L);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(
        StockingStandardsCommentLocationCode.ADDITIONAL_STANDARDS, dto.commentLocation());
  }

  @Test
  @DisplayName("commentLocation STANDARDS_OBJECTIVE mapped to enum")
  void commentLocation_standardsObjective_mappedToEnum() {
    StockingStandardsCommentSearchProjection p =
        projection("STANDARDS_OBJECTIVE", null, "text", null, null, null, null, 1L);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(
        StockingStandardsCommentLocationCode.STANDARDS_OBJECTIVE, dto.commentLocation());
  }

  // -------------------------------------------------------------------------
  // isStockingStandardsExpired
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("isStockingStandardsExpired: past expiry date returns true")
  void isStockingStandardsExpired_pastDate_returnsTrue() {
    StockingStandardsCommentSearchProjection p =
        projection(
            "STANDARDS_NAME", LocalDateTime.now().minusDays(1), "text", null, null, null, null, 1L);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertTrue(dto.isExpired());
  }

  @Test
  @DisplayName("isStockingStandardsExpired: future expiry date returns false")
  void isStockingStandardsExpired_futureDate_returnsFalse() {
    StockingStandardsCommentSearchProjection p =
        projection(
            "STANDARDS_NAME", LocalDateTime.now().plusYears(1), "text", null, null, null, null, 1L);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertFalse(dto.isExpired());
  }

  @Test
  @DisplayName("isStockingStandardsExpired: null expiry date returns false")
  void isStockingStandardsExpired_nullDate_returnsFalse() {
    StockingStandardsCommentSearchProjection p =
        projection("STANDARDS_NAME", null, "text", null, null, null, null, 1L);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertFalse(dto.isExpired());
  }

  // -------------------------------------------------------------------------
  // commentText — blank → null
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("commentText blank string returns null in response")
  void commentText_blank_returnsNull() {
    StockingStandardsCommentSearchProjection p =
        projection("STANDARDS_NAME", null, "   ", null, null, null, null, 1L);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertNull(dto.commentText());
  }

  @Test
  @DisplayName("commentText non-blank string kept as-is")
  void commentText_nonBlank_keptAsIs() {
    StockingStandardsCommentSearchProjection p =
        projection("STANDARDS_NAME", null, "Baseline standard", null, null, null, null, 1L);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals("Baseline standard", dto.commentText());
  }

  // -------------------------------------------------------------------------
  // timestamps
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("updateTimestamp and approvedTimestamp mapped from projection")
  void timestamps_mappedFromProjection() {
    LocalDateTime update = LocalDateTime.of(2024, 3, 15, 10, 30);
    LocalDateTime approved = LocalDateTime.of(2022, 9, 1, 0, 0);
    StockingStandardsCommentSearchProjection p = defaultProjection();
    lenient().when(p.getUpdateTimestamp()).thenReturn(update);
    lenient().when(p.getApprovedTimestamp()).thenReturn(approved);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(update, dto.updateTimestamp());
    Assertions.assertEquals(approved, dto.approvedTimestamp());
  }

  // -------------------------------------------------------------------------
  // orgUnits (parseCodeDescriptionList shared helper)
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("orgUnits codes and names paired correctly")
  void orgUnits_pairedCorrectly() {
    StockingStandardsCommentSearchProjection p =
        projection(
            "STANDARDS_NAME", null, "text", "DAS,TWO", "District A||District Two", null, null, 1L);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(2, dto.orgUnits().size());
    Assertions.assertEquals("DAS", dto.orgUnits().get(0).code());
    Assertions.assertEquals("District A", dto.orgUnits().get(0).description());
  }

  @Test
  @DisplayName("orgUnits null returns empty list")
  void orgUnits_null_returnsEmpty() {
    StockingStandardsCommentSearchProjection p = defaultProjection();
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertTrue(dto.orgUnits().isEmpty());
  }

  @Test
  @DisplayName("orgUnits blank name returns null description")
  void orgUnits_blankName_returnsNullDescription() {
    StockingStandardsCommentSearchProjection p =
        projection("STANDARDS_NAME", null, "text", "DAS", "  ", null, null, 1L);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(1, dto.orgUnits().size());
    Assertions.assertNull(dto.orgUnits().get(0).description());
  }

  // -------------------------------------------------------------------------
  // fspIds
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("fspIds comma-separated parsed to list")
  void fspIds_parsed() {
    StockingStandardsCommentSearchProjection p =
        projection("STANDARDS_NAME", null, "text", null, null, null, "1234,5678", 1L);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(List.of("1234", "5678"), dto.fspIds());
  }

  @Test
  @DisplayName("fspIds null returns empty list")
  void fspIds_null_returnsEmpty() {
    StockingStandardsCommentSearchProjection p = defaultProjection();
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    StockingStandardsCommentSearchResponseDto dto =
        service
            .stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertTrue(dto.fspIds().isEmpty());
  }

  // -------------------------------------------------------------------------
  // ForestClient batch enrichment
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("clients batch-loaded from ForestClientService")
  void clients_batchLoaded() {
    StockingStandardsCommentSearchProjection p1 =
        projection("STANDARDS_NAME", null, "text", null, null, "00099001,00099002", null, 2L);
    StockingStandardsCommentSearchProjection p2 =
        projection("STANDARDS_NAME", null, "text", null, null, "00099002,00099003", null, 2L);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p1, p2));
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), anyList()))
        .thenReturn(List.of(client("00099001"), client("00099002"), client("00099003")));

    Page<StockingStandardsCommentSearchResponseDto> result =
        service.stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10));

    verify(forestClientService).searchByClientNumbers(anyInt(), anyInt(), anyList());
    Assertions.assertEquals(2, result.getContent().get(0).clients().size());
    Assertions.assertEquals(2, result.getContent().get(1).clients().size());
  }

  @Test
  @DisplayName("clients empty when no client numbers present in projections")
  void clients_emptyWhenNoClientNumbers() {
    StockingStandardsCommentSearchProjection p = defaultProjection();
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    service.stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10));

    Mockito.verifyNoInteractions(forestClientService);
  }

  // -------------------------------------------------------------------------
  // Edge cases
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("Empty projections returns empty page with zero total")
  void emptyProjections_returnsEmptyPage() {
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of());

    Page<StockingStandardsCommentSearchResponseDto> result =
        service.stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10));

    Assertions.assertEquals(0L, result.getTotalElements());
    Assertions.assertTrue(result.getContent().isEmpty());
  }

  @Test
  @DisplayName("totalCount null in projection does not throw")
  void totalCount_null_doesNotThrow() {
    StockingStandardsCommentSearchProjection p =
        projection("STANDARDS_NAME", null, "text", null, null, null, null, null);
    when(repository.stockingStandardsCommentSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p));

    Assertions.assertDoesNotThrow(
        () -> service.stockingStandardsCommentSearch(defaultFilter(), PageRequest.of(0, 10)));
  }

  @Test
  @DisplayName("Invalid date range throws ResponseStatusException")
  void invalidDateRange_throwsException() {
    StockingStandardsCommentSearchFilterDto filter =
        new StockingStandardsCommentSearchFilterDto(
            "Baseline", null, null, null, "2025-12-31", "2025-01-01");

    Assertions.assertThrows(
        org.springframework.web.server.ResponseStatusException.class,
        () -> service.stockingStandardsCommentSearch(filter, PageRequest.of(0, 10)));
  }
}
