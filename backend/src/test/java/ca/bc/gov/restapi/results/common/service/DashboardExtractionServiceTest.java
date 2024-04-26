package ca.bc.gov.restapi.results.common.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.OracleExtractionDto;
import ca.bc.gov.restapi.results.common.dto.OracleExtractionParamsDto;
import ca.bc.gov.restapi.results.oracle.service.OracleExtractionService;
import ca.bc.gov.restapi.results.postgres.service.DashboardInsertionService;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardExtractionServiceTest {

  @Mock OracleExtractionService oracleExtractionService;

  @Mock DashboardInsertionService dashboardInsertionService;

  private DashboardExtractionService dashboardExtractionService;

  @BeforeEach
  void setup() {
    dashboardExtractionService =
        new DashboardExtractionService(oracleExtractionService, dashboardInsertionService);
  }

  @Test
  @DisplayName("extract data for the dashboard happy path should succeed")
  void extractDataForTheDashboard_happyPath_shouldSucceed() {
    OracleExtractionParamsDto params = new OracleExtractionParamsDto(24, false, false);
    OracleExtractionDto extractionDto =
        new OracleExtractionDto(null, null, null, null, null, null, new ArrayList<>());

    when(oracleExtractionService.getOpeningActivities(params)).thenReturn(extractionDto);

    doNothing().when(dashboardInsertionService).loadDashboardData(any(), any(), any());

    dashboardExtractionService.extractDataForTheDashboard(null, null, null);

    Assertions.assertFalse(extractionDto.logMessages().isEmpty());
  }
}
