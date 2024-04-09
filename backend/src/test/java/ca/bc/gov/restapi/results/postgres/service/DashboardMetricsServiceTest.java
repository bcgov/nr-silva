package ca.bc.gov.restapi.results.postgres.service;

import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.security.LoggedUserService;
import ca.bc.gov.restapi.results.postgres.dto.DashboardFiltesDto;
import ca.bc.gov.restapi.results.postgres.dto.OpeningsPerYearDto;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsActivityRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsLastYearRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class DashboardMetricsServiceTest {

  @Mock OpeningsLastYearRepository openingsLastYearRepository;

  @Mock OpeningsActivityRepository openingsActivityRepository;

  @Mock LoggedUserService loggedUserService;

  private DashboardMetricsService dashboardMetricsService;

  private static final Sort SORT = Sort.by("entryTimestamp").ascending();

  private List<OpeningsLastYearEntity> mockOpeningsEntityList() {
    LocalDateTime entryTimestamp = LocalDateTime.now();

    OpeningsLastYearEntity entity = new OpeningsLastYearEntity();
    entity.setOpeningId(123456L);
    entity.setUserId("userId");
    entity.setEntryTimestamp(entryTimestamp);
    entity.setUpdateTimestamp(entryTimestamp);
    entity.setStatus("APP");
    entity.setOrgUnitCode("DCR");
    return List.of(entity);
  }

  @BeforeEach
  void setup() {
    dashboardMetricsService =
        new DashboardMetricsService(
            openingsLastYearRepository, openingsActivityRepository, loggedUserService);
  }

  @Test
  @DisplayName("Opening submission trends with no filters should succeed")
  void getOpeningsSubmissionTrends_noFilters_shouldSucceed() throws Exception {
    LocalDateTime now = LocalDateTime.now();
    List<OpeningsLastYearEntity> entities = mockOpeningsEntityList();
    when(openingsLastYearRepository.findAll(SORT)).thenReturn(entities);

    DashboardFiltesDto filtersDto = new DashboardFiltesDto(null, null, null, null, null);
    List<OpeningsPerYearDto> list = dashboardMetricsService.getOpeningsSubmissionTrends(filtersDto);

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
  void getOpeningsSubmissionTrends_orgUnitFilter_shouldSucceed() throws Exception {
    LocalDateTime now = LocalDateTime.now();
    List<OpeningsLastYearEntity> entities = mockOpeningsEntityList();
    when(openingsLastYearRepository.findAll(SORT)).thenReturn(entities);

    DashboardFiltesDto filtersDto = new DashboardFiltesDto("AAA", null, null, null, null);
    List<OpeningsPerYearDto> list = dashboardMetricsService.getOpeningsSubmissionTrends(filtersDto);

    String monthName = now.getMonth().name().toLowerCase();
    monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1, 3);

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(12, list.size());
    Assertions.assertEquals(now.getMonthValue(), list.get(0).month());
    Assertions.assertEquals(monthName, list.get(0).monthName());
    Assertions.assertEquals(0, list.get(0).amount());
  }

  @Test
  @DisplayName("Opening submission trends with Status filter should succeed")
  void getOpeningsSubmissionTrends_statusFilter_shouldSucceed() {
    LocalDateTime now = LocalDateTime.now();
    List<OpeningsLastYearEntity> entities = mockOpeningsEntityList();
    when(openingsLastYearRepository.findAll(SORT)).thenReturn(entities);

    DashboardFiltesDto filtersDto = new DashboardFiltesDto(null, "APP", null, null, null);
    List<OpeningsPerYearDto> list = dashboardMetricsService.getOpeningsSubmissionTrends(filtersDto);

    String monthName = now.getMonth().name().toLowerCase();
    monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1, 3);

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(12, list.size());
    Assertions.assertEquals(now.getMonthValue(), list.get(0).month());
    Assertions.assertEquals(monthName, list.get(0).monthName());
    Assertions.assertEquals(1, list.get(0).amount());
  }

  @Test
  @DisplayName("Opening submission trends with Status filter should succeed")
  void getOpeningsSubmissionTrends_datesFilter_shouldSucceed() {
    LocalDateTime now = LocalDateTime.now();
    List<OpeningsLastYearEntity> entities = mockOpeningsEntityList();
    when(openingsLastYearRepository.findAll(SORT)).thenReturn(entities);

    LocalDateTime oneMonthBefore = now.minusMonths(1L);
    LocalDateTime oneMonthLater = now.plusMonths(1L);

    DashboardFiltesDto filtersDto =
        new DashboardFiltesDto(null, null, oneMonthBefore, oneMonthLater, null);
    List<OpeningsPerYearDto> list = dashboardMetricsService.getOpeningsSubmissionTrends(filtersDto);

    String monthName = now.getMonth().name().toLowerCase();
    monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1, 3);

    Assertions.assertFalse(list.isEmpty());
    Assertions.assertEquals(12, list.size());
    Assertions.assertEquals(now.getMonthValue(), list.get(0).month());
    Assertions.assertEquals(monthName, list.get(0).monthName());
    Assertions.assertEquals(1, list.get(0).amount());
  }
}
