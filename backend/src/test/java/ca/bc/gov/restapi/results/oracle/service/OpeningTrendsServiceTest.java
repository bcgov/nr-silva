package ca.bc.gov.restapi.results.oracle.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.oracle.entity.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Opening Trends Service")
class OpeningTrendsServiceTest {

  @Mock
  OpeningRepository openingRepository;
  private OpeningTrendsService openingTrendsService;

  @BeforeEach
  void setUp() {
    openingTrendsService = new OpeningTrendsService(openingRepository);
  }

  private List<OpeningTrendsProjection> mockOpeningsEntityList() {
    LocalDateTime entryTimestamp = LocalDateTime.now();

    OpeningTrendsProjection entity = new TestOpeningTrendsProjection(
        123456L,
        "userId",
        entryTimestamp,
        entryTimestamp,
        "APP",
        "DCR",
        "District Code",
        "00012797"
    );
    return List.of(entity);
  }

  @Test
  @DisplayName("Opening submission trends with no filters should succeed")
  void getOpeningSubmissionTrends_noFilters_shouldSucceed() throws Exception {
    LocalDateTime now = LocalDateTime.now();
    List<OpeningTrendsProjection> entities = mockOpeningsEntityList();
    when(openingRepository.getOpeningTrends(any(), any(), any(), any())).thenReturn(entities);

    List<OpeningsPerYearDto> list = openingTrendsService.getOpeningSubmissionTrends(
        LocalDate.now(),
        LocalDate.now(),
        null,
        null
    );

    String monthName = now.getMonth().name().toLowerCase();
    monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1, 3);

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(12, list.size());
    Assertions.assertEquals(now.getMonthValue(), list.get(0).month());
    Assertions.assertEquals(monthName, list.get(0).monthName());
    Assertions.assertEquals(1, list.get(0).amount());
  }

  @Test
  @DisplayName("Opening submission trends with Org Unit filter should succeed")
  void getOpeningSubmissionTrends_orgUnitFilter_shouldSucceed() throws Exception {
    LocalDateTime now = LocalDateTime.now();
    List<OpeningTrendsProjection> entities = mockOpeningsEntityList();
    when(openingRepository.getOpeningTrends(any(), any(), any(), any())).thenReturn(entities);

    List<OpeningsPerYearDto> list = openingTrendsService.getOpeningSubmissionTrends(
        now.toLocalDate(),
        now.toLocalDate(),
        List.of("AAA"),
        null
    );

    String monthName = now.getMonth().getDisplayName(TextStyle.SHORT, Locale.CANADA);

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(12, list.size());
    Assertions.assertEquals(now.getMonthValue(), list.get(0).month());
    Assertions.assertEquals(monthName, list.get(0).monthName());
    Assertions.assertEquals(1, list.get(0).amount());
  }

  @Test
  @DisplayName("Opening submission trends with Status filter should succeed")
  void getOpeningSubmissionTrends_statusFilter_shouldSucceed() {
    LocalDateTime now = LocalDateTime.now();
    List<OpeningTrendsProjection> entities = mockOpeningsEntityList();
    when(openingRepository.getOpeningTrends(any(), any(), any(), any())).thenReturn(entities);

    List<OpeningsPerYearDto> list = openingTrendsService.getOpeningSubmissionTrends(
        LocalDate.now(),
        LocalDate.now(),
        null,
        List.of("APP")
    );

    String monthName = now.getMonth().name().toLowerCase();
    monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1, 3);

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(12, list.size());
    Assertions.assertEquals(now.getMonthValue(), list.get(0).month());
    Assertions.assertEquals(monthName, list.get(0).monthName());
    Assertions.assertEquals(1, list.get(0).amount());
  }

  @Test
  @DisplayName("Opening submission trends with Status filter not matching should succeed")
  void getOpeningSubmissionTrends_statusFilterNot_shouldSucceed() {
    List<OpeningTrendsProjection> entities = mockOpeningsEntityList();
    when(openingRepository.getOpeningTrends(any(), any(), any(), any())).thenReturn(entities);

    List<OpeningsPerYearDto> list = openingTrendsService.getOpeningSubmissionTrends(
        LocalDate.now(),
        LocalDate.now().plusYears(1),
        null,
        List.of("UPD")
    );
    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(12, list.size());
    Assertions.assertEquals(1, list.get(0).amount());
    Assertions.assertEquals(0, list.get(1).amount());
    Assertions.assertEquals(0, list.get(2).amount());
    Assertions.assertEquals(0, list.get(3).amount());
    Assertions.assertEquals(0, list.get(4).amount());
    Assertions.assertEquals(0, list.get(5).amount());
    Assertions.assertEquals(0, list.get(6).amount());
    Assertions.assertEquals(0, list.get(7).amount());
    Assertions.assertEquals(0, list.get(8).amount());
    Assertions.assertEquals(0, list.get(9).amount());
    Assertions.assertEquals(0, list.get(10).amount());
    Assertions.assertEquals(0, list.get(11).amount());
  }

  @Test
  @DisplayName("Opening submission trends with Dates filter should succeed")
  void getOpeningSubmissionTrends_datesFilter_shouldSucceed() {
    List<OpeningTrendsProjection> entities = mockOpeningsEntityList();
    when(openingRepository.getOpeningTrends(any(), any(), any(), any())).thenReturn(entities);

    LocalDate now = LocalDate.now();
    LocalDate oneMonthBefore = now.minusMonths(1L);
    LocalDate oneMonthLater = now.plusMonths(1L);

    List<OpeningsPerYearDto> list = openingTrendsService.getOpeningSubmissionTrends(
        oneMonthBefore,
        oneMonthLater,
        null,
        null
    );

    String monthName = oneMonthBefore.getMonth().getDisplayName(TextStyle.SHORT, Locale.CANADA);
    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(12, list.size());
    Assertions.assertEquals(oneMonthBefore.getMonthValue(), list.get(0).month());
    Assertions.assertEquals(monthName, list.get(0).monthName());
    Assertions.assertEquals(1, list.get(1).amount());
  }

  @Test
  @DisplayName("Opening submission trends with no data should succeed")
  void getOpeningSubmissionTrends_noData_shouldSucceed() {
    when(openingRepository.getOpeningTrends(any(), any(), any(), any())).thenReturn(List.of());

    List<OpeningsPerYearDto> list = openingTrendsService.getOpeningSubmissionTrends(
        LocalDate.now(),
        LocalDate.now(),
        null,
        null
    );

    Assertions.assertTrue(list.isEmpty());
  }


  @Getter
  @AllArgsConstructor
  static
  class TestOpeningTrendsProjection implements OpeningTrendsProjection {
    private Long openingId;
    private String userId;
    private LocalDateTime entryTimestamp;
    private LocalDateTime updateTimestamp;
    private String status;
    private String orgUnitCode;
    private String orgUnitName;
    private String clientNumber;

  }

}