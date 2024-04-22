package ca.bc.gov.restapi.results.postgres.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.OracleExtractionDto;
import ca.bc.gov.restapi.results.common.dto.OracleExtractionParamsDto;
import ca.bc.gov.restapi.results.common.dto.OracleLogDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardActionCodeDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningSubmissionDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOrgUnitDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardResultsAuditDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardStockingEventDto;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsActivityRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsLastYearRepository;
import ca.bc.gov.restapi.results.postgres.repository.OracleExtractionLogsRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardInsertionServiceTest {

  @Mock OpeningsLastYearRepository openingsLastYearRepository;

  @Mock OpeningsActivityRepository openingsActivityRepository;

  @Mock OracleExtractionLogsRepository oracleExtractionLogsRepository;

  private DashboardInsertionService dashboardInsertionService;

  private static final Long OPENING_ID = 112233L;

  private static final String TEST_USER = "USER";

  private static final Long ADMIN_DISTRICT_NO = 112L;

  private static final String AUDIT_ACTION_CODE = "UPD";

  private DashboardOpeningDto mockDashboardOpeningDto() {
    return new DashboardOpeningDto() {

      @Override
      public Long getOpeningId() {
        return OPENING_ID;
      }

      @Override
      public String getOpeningStatusCode() {
        return "APP";
      }

      @Override
      public String getEntryUserId() {
        return TEST_USER;
      }

      @Override
      public LocalDateTime getEntryTimestamp() {
        return LocalDateTime.now().minusMonths(1L);
      }

      @Override
      public LocalDateTime getUpdateTimestamp() {
        return LocalDateTime.now().minusDays(3);
      }

      @Override
      public Long getAdminDistrictNo() {
        return ADMIN_DISTRICT_NO;
      }

      @Override
      public Long getResultsSubmissionId() {
        return 22L;
      }

      @Override
      public LocalDateTime getActionTimestamp() {
        return LocalDateTime.now().minusDays(3);
      }
    };
  }

  private DashboardOpeningSubmissionDto mockDashboardOpeningSubmissionDto() {
    return new DashboardOpeningSubmissionDto() {

      @Override
      public Long getResultsSubmissionId() {
        return 22L;
      }

      @Override
      public String getClientNumber() {
        return "0012797";
      }
    };
  }

  private DashboardResultsAuditDto mockDashboardResultsAuditDto() {
    return new DashboardResultsAuditDto() {

      @Override
      public String getResultsAuditActionCode() {
        return AUDIT_ACTION_CODE;
      }

      @Override
      public LocalDateTime getActionDate() {
        return LocalDateTime.now().minusDays(1L);
      }

      @Override
      public LocalDateTime getEntryTimestamp() {
        return LocalDateTime.now().minusDays(20L);
      }

      @Override
      public String getEntryUserid() {
        return TEST_USER;
      }

      @Override
      public Long getOpeningId() {
        return OPENING_ID;
      }

      @Override
      public LocalDateTime getActionTimestamp() {
        return LocalDateTime.now().minusDays(1L);
      }
    };
  }

  private DashboardStockingEventDto mockDashboardStockingEventDto() {
    return new DashboardStockingEventDto() {

      @Override
      public String getResultsAuditActionCode() {
        return AUDIT_ACTION_CODE;
      }

      @Override
      public String getEntryUserid() {
        return TEST_USER;
      }

      @Override
      public Long getOpeningId() {
        return OPENING_ID;
      }

      @Override
      public LocalDateTime getEntryTimestamp() {
        return LocalDateTime.now().minusDays(20L);
      }

      @Override
      public LocalDateTime getAmendEventTimestamp() {
        return LocalDateTime.now().minusDays(10L);
      }

      @Override
      public LocalDateTime getActionTimestamp() {
        return LocalDateTime.now().minusDays(10L);
      }
    };
  }

  private DashboardOrgUnitDto mockDashboardOrgUnitDto() {
    return new DashboardOrgUnitDto() {

      @Override
      public Long getOrgUnitNo() {
        return ADMIN_DISTRICT_NO;
      }

      @Override
      public String getOrgUnitCode() {
        return "DCR";
      }

      @Override
      public String getOrgUnitName() {
        return "DCR Name";
      }
    };
  }

  private DashboardActionCodeDto mockDashboardActionCodeDto() {
    return new DashboardActionCodeDto() {

      @Override
      public String getResultsAuditActionCode() {
        return AUDIT_ACTION_CODE;
      }

      @Override
      public String getDescription() {
        return AUDIT_ACTION_CODE;
      }
    };
  }

  @BeforeEach
  void setup() {
    dashboardInsertionService =
        new DashboardInsertionService(
            openingsLastYearRepository, openingsActivityRepository, oracleExtractionLogsRepository);
  }

  @Test
  @DisplayName("Load dashboard data happy path should succeed")
  void loadDashboardData_happyPath_shouldSucceed() {
    // openings last year
    doNothing().when(openingsLastYearRepository).deleteAll();
    doNothing().when(openingsLastYearRepository).flush();
    when(openingsLastYearRepository.saveAllAndFlush(any())).thenReturn(List.of());

    // opening activities
    doNothing().when(openingsActivityRepository).deleteAll();
    doNothing().when(openingsActivityRepository).flush();
    when(openingsActivityRepository.saveAllAndFlush(any())).thenReturn(List.of());

    LocalDateTime startDateTime = LocalDateTime.now();
    OracleExtractionParamsDto paramsDto = new OracleExtractionParamsDto(24, true, false);

    // Params
    List<DashboardOpeningDto> mainOpenings = List.of(mockDashboardOpeningDto());
    List<DashboardOpeningSubmissionDto> openingSubmissions =
        List.of(mockDashboardOpeningSubmissionDto());
    List<DashboardResultsAuditDto> resultsAudits = List.of(mockDashboardResultsAuditDto());
    List<DashboardStockingEventDto> stockingEvents = List.of(mockDashboardStockingEventDto());
    List<DashboardOrgUnitDto> orgUnits = List.of(mockDashboardOrgUnitDto());
    List<DashboardActionCodeDto> actionCodes = List.of(mockDashboardActionCodeDto());
    List<OracleLogDto> logMessages = new ArrayList<>();
    logMessages.add(new OracleLogDto("Hey", LocalDateTime.now()));
    OracleExtractionDto extractionDto =
        new OracleExtractionDto(
            mainOpenings,
            openingSubmissions,
            resultsAudits,
            stockingEvents,
            orgUnits,
            actionCodes,
            logMessages);

    dashboardInsertionService.loadDashboardData(extractionDto, startDateTime, paramsDto);

    Assertions.assertFalse(extractionDto.logMessages().isEmpty());
  }
}
