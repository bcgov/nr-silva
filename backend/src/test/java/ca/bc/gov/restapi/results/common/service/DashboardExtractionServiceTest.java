package ca.bc.gov.restapi.results.common.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.OracleExtractionDto;
import ca.bc.gov.restapi.results.common.dto.OracleExtractionParamsDto;
import ca.bc.gov.restapi.results.oracle.service.OracleExtractionService;
import ca.bc.gov.restapi.results.postgres.service.DashboardInsertionService;
import java.util.ArrayList;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test | Dashboard Extraction Service")
class DashboardExtractionServiceTest {

  @Mock
  OracleExtractionService oracleExtractionService;

  @Mock
  DashboardInsertionService dashboardInsertionService;

  private DashboardExtractionService dashboardExtractionService;

  @BeforeEach
  void setup() {
    dashboardExtractionService =
        new DashboardExtractionService(oracleExtractionService, dashboardInsertionService);
  }

  @ParameterizedTest
  @MethodSource("basicValues")
  @DisplayName("extract data for the dashboard happy path should succeed")
  void extractDataForTheDashboard_happyPath_shouldSucceed(
      Integer months,
      Boolean debug,
      Boolean manuallyTriggered
  ) {
    OracleExtractionParamsDto params = new OracleExtractionParamsDto(
        24,
        false,
        false
    );
    OracleExtractionDto extractionDto =
        new OracleExtractionDto(
            null,
            null,
            null,
            null,
            null,
            null,
            new ArrayList<>()
        );

    when(oracleExtractionService.getOpeningActivities(params)).thenReturn(extractionDto);

    doNothing().when(dashboardInsertionService).loadDashboardData(any(), any(), any());

    dashboardExtractionService.extractDataForTheDashboard(months, debug, manuallyTriggered);

    Assertions.assertFalse(extractionDto.logMessages().isEmpty());
  }

  private static Stream<Arguments> basicValues() {
    return Stream.of(
        Arguments.of(24, false, false),
        Arguments.of(24, false, null),
        Arguments.of(24, null, false),
        Arguments.of(24, null, null),
        Arguments.of(null, false, false),
        Arguments.of(null, null, false),
        Arguments.of(null, null, null),
        Arguments.of(null, false, null)
    );
  }

}
