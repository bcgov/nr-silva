package ca.bc.gov.restapi.results.postgres.service;

import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.postgres.dto.DashboardFiltersDto;
import ca.bc.gov.restapi.results.postgres.dto.FreeGrowingMilestonesDto;
import ca.bc.gov.restapi.results.postgres.dto.MyRecentActionsRequestsDto;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntity;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsActivityRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsLastYearRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class DashboardMetricsServiceTest {

  @Mock
  OpeningsLastYearRepository openingsLastYearRepository;

  @Mock
  OpeningsActivityRepository openingsActivityRepository;

  @Mock
  LoggedUserService loggedUserService;

  private DashboardMetricsService dashboardMetricsService;

  private static final Sort SORT_BY_ID = Sort.by("openingId").ascending();

  private List<OpeningsLastYearEntity> mockOpeningsEntityList() {
    LocalDateTime entryTimestamp = LocalDateTime.now();

    OpeningsLastYearEntity entity = new OpeningsLastYearEntity();
    entity.setOpeningId(123456L);
    entity.setUserId("userId");
    entity.setEntryTimestamp(entryTimestamp);
    entity.setUpdateTimestamp(entryTimestamp);
    entity.setStatus("APP");
    entity.setOrgUnitCode("DCR");
    entity.setClientNumber("00012797");
    return List.of(entity);
  }

  @BeforeEach
  void setup() {
    dashboardMetricsService =
        new DashboardMetricsService(
            openingsLastYearRepository, openingsActivityRepository, loggedUserService);
  }

  @Test
  @DisplayName("Free growing milestones without filters should succeed")
  void getFreeGrowingMilestoneChartData_noFilters_shouldSucceed() {
    List<OpeningsLastYearEntity> entities = mockOpeningsEntityList();
    when(openingsLastYearRepository.findAll(SORT_BY_ID)).thenReturn(entities);

    DashboardFiltersDto filtersDto = new DashboardFiltersDto(null, null, null, null, null);
    List<FreeGrowingMilestonesDto> list =
        dashboardMetricsService.getFreeGrowingMilestoneChartData(filtersDto);

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(4, list.size());
    Assertions.assertEquals(0, list.get(0).index());
    Assertions.assertEquals("0 - 5 months", list.get(0).label());
    Assertions.assertEquals(1, list.get(0).amount());
    Assertions.assertEquals(new BigDecimal("100.00"), list.get(0).percentage());

    Assertions.assertEquals(1, list.get(1).index());
    Assertions.assertEquals("6 - 11 months", list.get(1).label());
    Assertions.assertEquals(0, list.get(1).amount());
    Assertions.assertEquals(new BigDecimal("0.00"), list.get(1).percentage());

    Assertions.assertEquals(2, list.get(2).index());
    Assertions.assertEquals("12 - 17 months", list.get(2).label());
    Assertions.assertEquals(0, list.get(2).amount());
    Assertions.assertEquals(new BigDecimal("0.00"), list.get(2).percentage());

    Assertions.assertEquals(3, list.get(3).index());
    Assertions.assertEquals("18 months", list.get(3).label());
    Assertions.assertEquals(0, list.get(3).amount());
    Assertions.assertEquals(new BigDecimal("0.00"), list.get(3).percentage());
  }

  @Test
  @DisplayName("Free growing milestones with org unit filter should succeed")
  void getFreeGrowingMilestoneChartData_orgUnitFilter_shouldSucceed() {
    List<OpeningsLastYearEntity> entities = mockOpeningsEntityList();
    when(openingsLastYearRepository.findAll(SORT_BY_ID)).thenReturn(entities);

    DashboardFiltersDto filtersDto = new DashboardFiltersDto("AAA", null, null, null, null);
    List<FreeGrowingMilestonesDto> list =
        dashboardMetricsService.getFreeGrowingMilestoneChartData(filtersDto);

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(4, list.size());
    Assertions.assertEquals(0, list.get(0).index());
    Assertions.assertEquals("0 - 5 months", list.get(0).label());
    Assertions.assertEquals(0, list.get(0).amount());
    Assertions.assertEquals(BigDecimal.ZERO, list.get(0).percentage());

    Assertions.assertEquals(1, list.get(1).index());
    Assertions.assertEquals("6 - 11 months", list.get(1).label());
    Assertions.assertEquals(0, list.get(1).amount());
    Assertions.assertEquals(BigDecimal.ZERO, list.get(1).percentage());

    Assertions.assertEquals(2, list.get(2).index());
    Assertions.assertEquals("12 - 17 months", list.get(2).label());
    Assertions.assertEquals(0, list.get(2).amount());
    Assertions.assertEquals(BigDecimal.ZERO, list.get(2).percentage());

    Assertions.assertEquals(3, list.get(3).index());
    Assertions.assertEquals("18 months", list.get(3).label());
    Assertions.assertEquals(0, list.get(3).amount());
    Assertions.assertEquals(BigDecimal.ZERO, list.get(3).percentage());
  }

  @Test
  @DisplayName("Free growing milestones with client number filter should succeed")
  void getFreeGrowingMilestoneChartData_clientNumberFilter_shouldSucceed() {
    List<OpeningsLastYearEntity> entities = mockOpeningsEntityList();
    when(openingsLastYearRepository.findAll(SORT_BY_ID)).thenReturn(entities);

    DashboardFiltersDto filtersDto = new DashboardFiltersDto(null, null, null, null, "00011254");
    List<FreeGrowingMilestonesDto> list =
        dashboardMetricsService.getFreeGrowingMilestoneChartData(filtersDto);

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(4, list.size());
    Assertions.assertEquals(0, list.get(0).index());
    Assertions.assertEquals("0 - 5 months", list.get(0).label());
    Assertions.assertEquals(0, list.get(0).amount());
    Assertions.assertEquals(BigDecimal.ZERO, list.get(0).percentage());

    Assertions.assertEquals(1, list.get(1).index());
    Assertions.assertEquals("6 - 11 months", list.get(1).label());
    Assertions.assertEquals(0, list.get(1).amount());
    Assertions.assertEquals(BigDecimal.ZERO, list.get(1).percentage());

    Assertions.assertEquals(2, list.get(2).index());
    Assertions.assertEquals("12 - 17 months", list.get(2).label());
    Assertions.assertEquals(0, list.get(2).amount());
    Assertions.assertEquals(BigDecimal.ZERO, list.get(2).percentage());

    Assertions.assertEquals(3, list.get(3).index());
    Assertions.assertEquals("18 months", list.get(3).label());
    Assertions.assertEquals(0, list.get(3).amount());
    Assertions.assertEquals(BigDecimal.ZERO, list.get(3).percentage());
  }

  @Test
  @DisplayName("Free growing milestones with client numeric filter should succeed")
  void getFreeGrowingMilestoneChartData_clientNumericFilter_shouldSucceed() {
    List<OpeningsLastYearEntity> entities = mockOpeningsEntityList();
    when(openingsLastYearRepository.findAll(SORT_BY_ID)).thenReturn(entities);

    DashboardFiltersDto filtersDto = new DashboardFiltersDto(null, null, null, null, "12797");
    List<FreeGrowingMilestonesDto> list =
        dashboardMetricsService.getFreeGrowingMilestoneChartData(filtersDto);

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(4, list.size());
    Assertions.assertEquals(0, list.get(0).index());
    Assertions.assertEquals("0 - 5 months", list.get(0).label());
    Assertions.assertEquals(1, list.get(0).amount());
    Assertions.assertEquals(new BigDecimal("100.00"), list.get(0).percentage());

    Assertions.assertEquals(1, list.get(1).index());
    Assertions.assertEquals("6 - 11 months", list.get(1).label());
    Assertions.assertEquals(0, list.get(1).amount());
    Assertions.assertEquals(new BigDecimal("0.00"), list.get(1).percentage());

    Assertions.assertEquals(2, list.get(2).index());
    Assertions.assertEquals("12 - 17 months", list.get(2).label());
    Assertions.assertEquals(0, list.get(2).amount());
    Assertions.assertEquals(new BigDecimal("0.00"), list.get(2).percentage());

    Assertions.assertEquals(3, list.get(3).index());
    Assertions.assertEquals("18 months", list.get(3).label());
    Assertions.assertEquals(0, list.get(3).amount());
    Assertions.assertEquals(new BigDecimal("0.00"), list.get(3).percentage());
  }

  @Test
  @DisplayName("Free growing milestones with dates filter should succeed")
  void getFreeGrowingMilestoneChartData_datesFilter_shouldSucceed() {
    List<OpeningsLastYearEntity> entities = mockOpeningsEntityList();
    when(openingsLastYearRepository.findAll(SORT_BY_ID)).thenReturn(entities);

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime oneMonthBefore = now.minusMonths(1L);
    LocalDateTime oneMonthLater = now.plusMonths(1L);

    DashboardFiltersDto filtersDto =
        new DashboardFiltersDto(null, null, oneMonthBefore, oneMonthLater, null);
    List<FreeGrowingMilestonesDto> list =
        dashboardMetricsService.getFreeGrowingMilestoneChartData(filtersDto);

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(4, list.size());
    Assertions.assertEquals(0, list.get(0).index());
    Assertions.assertEquals("0 - 5 months", list.get(0).label());
    Assertions.assertEquals(1, list.get(0).amount());
    Assertions.assertEquals(new BigDecimal("100.00"), list.get(0).percentage());

    Assertions.assertEquals(1, list.get(1).index());
    Assertions.assertEquals("6 - 11 months", list.get(1).label());
    Assertions.assertEquals(0, list.get(1).amount());
    Assertions.assertEquals(new BigDecimal("0.00"), list.get(1).percentage());

    Assertions.assertEquals(2, list.get(2).index());
    Assertions.assertEquals("12 - 17 months", list.get(2).label());
    Assertions.assertEquals(0, list.get(2).amount());
    Assertions.assertEquals(new BigDecimal("0.00"), list.get(2).percentage());

    Assertions.assertEquals(3, list.get(3).index());
    Assertions.assertEquals("18 months", list.get(3).label());
    Assertions.assertEquals(0, list.get(3).amount());
    Assertions.assertEquals(new BigDecimal("0.00"), list.get(3).percentage());
  }

  @Test
  @DisplayName("My recent activities request table data test happy path should succeed")
  void getUserRecentOpeningsActions_happyPath_shouldSucceed() {
    String userId = "TEST";

    when(loggedUserService.getLoggedUserId()).thenReturn(userId);

    OpeningsActivityEntity activity = new OpeningsActivityEntity();
    activity.setOpeningId(112233L);
    // If you're here looking for more codes and descriptions, you may want to take a look on
    // the jira issue: https://apps.nrs.gov.bc.ca/int/jira/browse/SILVA-362 look for the comment
    // with the 'fire' emoji, made on 20/Mar/24 3:03 PM
    activity.setActivityTypeCode("UPD");
    activity.setActivityTypeDesc("Update");
    activity.setStatusCode("APP");
    activity.setStatusDesc("Approved");
    activity.setLastUpdated(LocalDateTime.now().minusHours(2));
    activity.setEntryUserid(userId);

    Sort sort = Sort.by("lastUpdated").descending();
    when(openingsActivityRepository.findAllByEntryUserid(userId,
        PageRequest.of(0, 5, sort)))
        .thenReturn(List.of(activity));

    List<MyRecentActionsRequestsDto> dtoList =
        dashboardMetricsService.getUserRecentOpeningsActions();

    Assertions.assertFalse(dtoList.isEmpty());
    Assertions.assertEquals(1, dtoList.size());
    Assertions.assertEquals("Update", dtoList.get(0).activityType());
    Assertions.assertEquals(112233L, dtoList.get(0).openingId());
    Assertions.assertEquals("APP", dtoList.get(0).statusCode());
    Assertions.assertEquals("Approved", dtoList.get(0).statusDescription());
    Assertions.assertEquals("2 hours ago", dtoList.get(0).lastUpdatedLabel());
  }

  @Test
  @DisplayName("My recent activities request table data test no data should succeed")
  void getUserRecentOpeningsActions_noData_shouldSucceed() {
    String userId = "TEST";

    when(loggedUserService.getLoggedUserId()).thenReturn(userId);

    Sort sort = Sort.by("lastUpdated").descending();
    when(openingsActivityRepository.findAllByEntryUserid(userId,
        PageRequest.of(0, 5, sort))).thenReturn(List.of());

    List<MyRecentActionsRequestsDto> dtoList =
        dashboardMetricsService.getUserRecentOpeningsActions();

    Assertions.assertTrue(dtoList.isEmpty());
  }
}
