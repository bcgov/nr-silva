package ca.bc.gov.restapi.results.common.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchFilterDto;
import ca.bc.gov.restapi.results.common.dto.StockingStandardsSearchResponseDto;
import ca.bc.gov.restapi.results.common.projection.StockingStandardsSearchProjection;
import ca.bc.gov.restapi.results.common.repository.StockingStandardsRepository;
import ca.bc.gov.restapi.results.common.service.impl.AbstractStockingStandardsService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
@DisplayName("Unit Test | AbstractStockingStandardsService")
class AbstractStockingStandardsServiceTest {

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

  /**
   * Creates a minimal projection mock. All stubs use lenient() so that tests that only call a
   * subset of getters do not trigger UnnecessaryStubbingException.
   */
  private StockingStandardsSearchProjection projection(
      String speciesCodes,
      String speciesNames,
      String orgCodes,
      String orgNames,
      String clientNumbers,
      String fspIds,
      Long totalCount) {
    StockingStandardsSearchProjection p = Mockito.mock(StockingStandardsSearchProjection.class);
    lenient().when(p.getStandardsRegimeId()).thenReturn(1L);
    lenient().when(p.getStandardsRegimeName()).thenReturn("Regime A");
    lenient().when(p.getExpiryDate()).thenReturn(null);
    lenient().when(p.getStandardsObjective()).thenReturn(null);
    lenient().when(p.getPreferredSpeciesCodes()).thenReturn(speciesCodes);
    lenient().when(p.getPreferredSpeciesNames()).thenReturn(speciesNames);
    lenient().when(p.getOrgUnitCodes()).thenReturn(orgCodes);
    lenient().when(p.getOrgUnitNames()).thenReturn(orgNames);
    lenient().when(p.getClientNumbers()).thenReturn(clientNumbers);
    lenient().when(p.getFspIds()).thenReturn(fspIds);
    lenient().when(p.getBgcList()).thenReturn(null);
    lenient().when(p.getMofDefaultStandardInd()).thenReturn("N");
    lenient().when(p.getApprovedDate()).thenReturn(LocalDateTime.of(2024, 1, 1, 0, 0));
    lenient().when(p.getTotalCount()).thenReturn(totalCount);
    return p;
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

  private StockingStandardsSearchFilterDto defaultFilters() {
    return new StockingStandardsSearchFilterDto();
  }

  // -------------------------------------------------------------------------
  // parsePreferredSpecies — covers the loop in parsePreferredSpecies()
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("parsePreferredSpecies: codes and names paired correctly")
  void parsePreferredSpecies_codesAndNamesPairedCorrectly() {
    StockingStandardsSearchProjection p =
        projection("CW,FD", "Western Redcedar||Douglas Fir", null, null, null, null, 1L);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(2, dto.preferredSpecies().size());
    Assertions.assertEquals("CW", dto.preferredSpecies().get(0).code());
    Assertions.assertEquals("Western Redcedar", dto.preferredSpecies().get(0).description());
    Assertions.assertEquals("FD", dto.preferredSpecies().get(1).code());
    Assertions.assertEquals("Douglas Fir", dto.preferredSpecies().get(1).description());
  }

  @Test
  @DisplayName("parsePreferredSpecies: blank name stored as null description")
  void parsePreferredSpecies_blankName_nullDescription() {
    StockingStandardsSearchProjection p = projection("CW", "   ", null, null, null, null, 1L);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(1, dto.preferredSpecies().size());
    Assertions.assertNull(dto.preferredSpecies().get(0).description());
  }

  @Test
  @DisplayName("parsePreferredSpecies: more codes than names fills extras with null")
  void parsePreferredSpecies_moreCodesThanNames_nullForExtras() {
    StockingStandardsSearchProjection p =
        projection("CW,FD,PL", "Cedar", null, null, null, null, 1L);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(3, dto.preferredSpecies().size());
    Assertions.assertEquals("Cedar", dto.preferredSpecies().get(0).description());
    Assertions.assertNull(dto.preferredSpecies().get(1).description());
    Assertions.assertNull(dto.preferredSpecies().get(2).description());
  }

  @Test
  @DisplayName("parsePreferredSpecies: null codes returns empty list")
  void parsePreferredSpecies_nullCodes_returnsEmpty() {
    StockingStandardsSearchProjection p = projection(null, null, null, null, null, null, 1L);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertTrue(dto.preferredSpecies().isEmpty());
  }

  // -------------------------------------------------------------------------
  // parseCodeDescriptionList (org units) — covers the loop in parseCodeDescriptionList()
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("parseCodeDescriptionList: org unit codes and names paired correctly")
  void parseCodeDescriptionList_orgUnits_pairedCorrectly() {
    StockingStandardsSearchProjection p =
        projection(null, null, "DAS,TWO", "District A||District Two", null, null, 1L);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(2, dto.orgUnits().size());
    Assertions.assertEquals("DAS", dto.orgUnits().get(0).code());
    Assertions.assertEquals("District A", dto.orgUnits().get(0).description());
    Assertions.assertEquals("TWO", dto.orgUnits().get(1).code());
    Assertions.assertEquals("District Two", dto.orgUnits().get(1).description());
  }

  @Test
  @DisplayName("parseCodeDescriptionList: blank org unit name stored as null description")
  void parseCodeDescriptionList_blankName_nullDescription() {
    StockingStandardsSearchProjection p = projection(null, null, "DAS", "  ", null, null, 1L);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(1, dto.orgUnits().size());
    Assertions.assertNull(dto.orgUnits().get(0).description());
  }

  @Test
  @DisplayName("parseCodeDescriptionList: null org unit codes returns empty list")
  void parseCodeDescriptionList_nullCodes_returnsEmpty() {
    StockingStandardsSearchProjection p = projection(null, null, null, null, null, null, 1L);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertTrue(dto.orgUnits().isEmpty());
  }

  // -------------------------------------------------------------------------
  // buildClientMap + buildClientList — covers searchByClientNumbers call and stream
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("buildClientMap: calls searchByClientNumbers once for all unique client numbers")
  void buildClientMap_callsSearchByClientNumbersOnce() {
    StockingStandardsSearchProjection p1 =
        projection(null, null, null, null, "00099001,00099002", null, 2L);
    StockingStandardsSearchProjection p2 =
        projection(null, null, null, null, "00099002,00099003", null, 2L);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong()))
        .thenReturn(List.of(p1, p2));
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), anyList()))
        .thenReturn(List.of(client("00099001"), client("00099002"), client("00099003")));

    Page<StockingStandardsSearchResponseDto> result =
        service.searchStockingStandards(defaultFilters(), PageRequest.of(0, 10));

    // One batch call, not N+1
    verify(forestClientService).searchByClientNumbers(anyInt(), anyInt(), anyList());
    Assertions.assertEquals(2, result.getContent().size());
    Assertions.assertEquals(2, result.getContent().get(0).clients().size());
    Assertions.assertEquals(2, result.getContent().get(1).clients().size());
  }

  @Test
  @DisplayName("buildClientList: client not found in map is excluded from list")
  void buildClientList_clientMissingFromMap_excluded() {
    StockingStandardsSearchProjection p =
        projection(null, null, null, null, "00099001,00099999", null, 1L);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));
    // Only one client returned — 00099999 not found
    when(forestClientService.searchByClientNumbers(anyInt(), anyInt(), anyList()))
        .thenReturn(List.of(client("00099001")));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(1, dto.clients().size());
    Assertions.assertEquals("00099001", dto.clients().get(0).clientNumber());
  }

  @Test
  @DisplayName("buildClientMap: empty client numbers list skips searchByClientNumbers")
  void buildClientMap_emptyNumbers_skipsServiceCall() {
    StockingStandardsSearchProjection p = projection(null, null, null, null, null, null, 1L);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Mockito.verifyNoInteractions(forestClientService);
    Assertions.assertTrue(dto.clients().isEmpty());
  }

  // -------------------------------------------------------------------------
  // parseFspIds
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("parseFspIds: comma-separated IDs parsed to list")
  void parseFspIds_csv_parsedToList() {
    StockingStandardsSearchProjection p = projection(null, null, null, null, null, "1234,5678", 1L);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(List.of("1234", "5678"), dto.fspIds());
  }

  @Test
  @DisplayName("parseFspIds: null fspIds returns empty list")
  void parseFspIds_null_returnsEmpty() {
    StockingStandardsSearchProjection p = projection(null, null, null, null, null, null, 1L);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertTrue(dto.fspIds().isEmpty());
  }

  // -------------------------------------------------------------------------
  // isRegimeExpired
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("isRegimeExpired: past expiry date returns true")
  void isRegimeExpired_past_returnsTrue() {
    StockingStandardsSearchProjection p = projection(null, null, null, null, null, null, 1L);
    lenient()
        .when(p.getExpiryDate())
        .thenReturn(
            LocalDate.now(ZoneId.of(SilvaConstants.VANCOUVER_ZONE_ID)).minusDays(1).atStartOfDay());
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertTrue(dto.isExpired());
  }

  @Test
  @DisplayName("isRegimeExpired: future expiry date returns false")
  void isRegimeExpired_future_returnsFalse() {
    StockingStandardsSearchProjection p = projection(null, null, null, null, null, null, 1L);
    lenient().when(p.getExpiryDate()).thenReturn(LocalDateTime.now().plusYears(1));
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertFalse(dto.isExpired());
  }

  // -------------------------------------------------------------------------
  // Edge cases
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("Empty projections returns empty page with zero total")
  void emptyProjections_returnsEmptyPage() {
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of());

    Page<StockingStandardsSearchResponseDto> result =
        service.searchStockingStandards(defaultFilters(), PageRequest.of(0, 10));

    Assertions.assertEquals(0L, result.getTotalElements());
    Assertions.assertTrue(result.getContent().isEmpty());
  }

  @Test
  @DisplayName("totalCount null in first projection does not throw")
  void totalCount_null_doesNotThrow() {
    StockingStandardsSearchProjection p = projection(null, null, null, null, null, null, null);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    Assertions.assertDoesNotThrow(
        () -> service.searchStockingStandards(defaultFilters(), PageRequest.of(0, 10)));
  }

  @Test
  @DisplayName("Invalid date range throws ResponseStatusException")
  void invalidDateRange_throwsException() {
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
            "2025-01-01",
            null);

    Assertions.assertThrows(
        org.springframework.web.server.ResponseStatusException.class,
        () -> service.searchStockingStandards(filters, PageRequest.of(0, 10)));
  }

  // -------------------------------------------------------------------------
  // parseBgcList
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("parseBgcList: multiple rows are split into individual entries")
  void parseBgcList_multipleRows_splitCorrectly() {
    StockingStandardsSearchProjection p = projection(null, null, null, null, null, null, 1L);
    lenient().when(p.getBgcList()).thenReturn("CWH.mm.-.-.-.-.-||IDFdk4.01.A.-.-.-.-");
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertEquals(2, dto.bgcList().size());
    Assertions.assertEquals("CWH.mm.-.-.-.-.-", dto.bgcList().get(0));
    Assertions.assertEquals("IDFdk4.01.A.-.-.-.-", dto.bgcList().get(1));
  }

  @Test
  @DisplayName("parseBgcList: null bgcList returns empty list")
  void parseBgcList_null_returnsEmpty() {
    StockingStandardsSearchProjection p = projection(null, null, null, null, null, null, 1L);
    lenient().when(p.getBgcList()).thenReturn(null);
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertTrue(dto.bgcList().isEmpty());
  }

  // -------------------------------------------------------------------------
  // isDefaultStandard
  // -------------------------------------------------------------------------

  @Test
  @DisplayName("isDefaultStandard: mofDefaultStandardInd Y maps to true")
  void isDefaultStandard_Y_returnsTrue() {
    StockingStandardsSearchProjection p = projection(null, null, null, null, null, null, 1L);
    lenient().when(p.getMofDefaultStandardInd()).thenReturn("Y");
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertTrue(dto.isDefaultStandard());
  }

  @Test
  @DisplayName("isDefaultStandard: mofDefaultStandardInd N maps to false")
  void isDefaultStandard_N_returnsFalse() {
    StockingStandardsSearchProjection p = projection(null, null, null, null, null, null, 1L);
    lenient().when(p.getMofDefaultStandardInd()).thenReturn("N");
    when(repository.stockingStandardsSearch(any(), anyLong(), anyLong())).thenReturn(List.of(p));

    StockingStandardsSearchResponseDto dto =
        service
            .searchStockingStandards(defaultFilters(), PageRequest.of(0, 10))
            .getContent()
            .get(0);

    Assertions.assertFalse(dto.isDefaultStandard());
  }
}
