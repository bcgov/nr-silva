package ca.bc.gov.restapi.results.common.service.opening;

import ca.bc.gov.restapi.results.common.projection.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.common.repository.OpeningRepository;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Opening Trends Service | Contract")
public abstract class AbstractOpeningTrendsServiceTest<T extends OpeningTrendsService> {

  @Mock
  protected OpeningRepository openingRepository;

  protected OpeningTrendsService openingTrendsService;

  protected abstract T createService(OpeningRepository openingRepository);

  @BeforeEach
  void setup() {
    openingTrendsService = createService(openingRepository);
  }

  protected List<OpeningTrendsProjection> mockOpeningsEntityList() {

    OpeningTrendsProjection entity = new TestOpeningTrendsProjection(
        LocalDate.now().getYear(),
        LocalDate.now().getMonthValue(),
        "APP",
        1
    );
    return List.of(entity);
  }

  @Test
  @DisplayName("Opening submission trends with no filters should succeed")
  void getOpeningSubmissionTrends_noFilters_shouldSucceed() {
    LocalDateTime now = LocalDateTime.now();
    List<OpeningTrendsProjection> entities = mockOpeningsEntityList();
    when(openingRepository.getOpeningTrends(anyString(), anyString(), anyList(), anyList()))
        .thenReturn(entities);

    List<OpeningsPerYearDto> list = openingTrendsService.getOpeningSubmissionTrends(
        LocalDate.now().minusMonths(4),
        LocalDate.now().plusMonths(2),
        null,
        null
    );

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(7, list.size());
    Assertions.assertEquals(now.minusMonths(4).getMonthValue(), list.get(0).month());
    Assertions.assertEquals(1, list.get(4).amount());
  }

  @Test
  @DisplayName("Opening submission trends with Org Unit filter should succeed")
  void getOpeningSubmissionTrends_orgUnitFilter_shouldSucceed() {
    LocalDateTime now = LocalDateTime.now();
    List<OpeningTrendsProjection> entities = mockOpeningsEntityList();
    when(openingRepository.getOpeningTrends(any(), any(), any(), any())).thenReturn(entities);

    List<OpeningsPerYearDto> list = openingTrendsService.getOpeningSubmissionTrends(
        now.toLocalDate().minusMonths(1),
        now.toLocalDate().plusMonths(1),
        List.of("AAA"),
        null
    );

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(now.minusMonths(1).getMonthValue(), list.get(0).month());
    Assertions.assertEquals(1, list.get(1).amount());
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

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(1, list.size());
    Assertions.assertEquals(now.getMonthValue(), list.get(0).month());
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
    Assertions.assertEquals(13, list.size());
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
    Assertions.assertEquals(0, list.get(12).amount());
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

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(oneMonthBefore.getMonthValue(), list.get(0).month());
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
  static class TestOpeningTrendsProjection implements OpeningTrendsProjection {

    int year;
    int month;
    String status;
    long count;
  }
}
