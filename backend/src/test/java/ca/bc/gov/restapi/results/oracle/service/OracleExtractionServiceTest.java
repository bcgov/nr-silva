package ca.bc.gov.restapi.results.oracle.service;

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
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//TODO: Review in the future
@ExtendWith(SpringExtension.class)
class OracleExtractionServiceTest {

  @Mock OpeningRepository openingRepository;

  private OracleExtractionService oracleExtractionService;

  private static final Long OPENING_ID = 112233L;

  private static final String TEST_USER = "USER";

  private static final Long ADMIN_DISTRICT_NO = 112L;

  private static final String AUDIT_ACTION_CODE = "UPD";

  @BeforeEach
  void setup() {
    oracleExtractionService = new OracleExtractionService(openingRepository);
  }

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

  @Test
  @DisplayName("Get opening activities happy path should succeed")
  void getOpeningActivities_happyPath_shouldSucceed() {
    Integer months = 24;

    DashboardOpeningDto openingDto = mockDashboardOpeningDto();
    when(openingRepository.findAllDashboardOpenings(months)).thenReturn(List.of(openingDto));

    DashboardOpeningSubmissionDto submissionDto = mockDashboardOpeningSubmissionDto();
    when(openingRepository.findAllDashboardOpeningSubmissions(List.of(22L)))
        .thenReturn(List.of(submissionDto));

    DashboardResultsAuditDto auditDto = mockDashboardResultsAuditDto();
    when(openingRepository.findAllDashboardAuditEvents(months)).thenReturn(List.of(auditDto));

    DashboardStockingEventDto stockingEventDto = mockDashboardStockingEventDto();
    when(openingRepository.findAllDashboardStockingEventHistory(months))
        .thenReturn(List.of(stockingEventDto));

    DashboardOrgUnitDto orgUnitDto = mockDashboardOrgUnitDto();
    when(openingRepository.findAllDashboardOrgUnits(List.of(ADMIN_DISTRICT_NO)))
        .thenReturn(List.of(orgUnitDto));

    DashboardActionCodeDto actionCodeDto = mockDashboardActionCodeDto();
    when(openingRepository.findAllDashboardActionCodes(List.of(AUDIT_ACTION_CODE)))
        .thenReturn(List.of(actionCodeDto));

    OracleExtractionParamsDto paramsDto = new OracleExtractionParamsDto(months, true, false);
    OracleExtractionDto extractionDto = oracleExtractionService.getOpeningActivities(paramsDto);

    Assertions.assertNotNull(extractionDto);
    Assertions.assertEquals(1, extractionDto.mainOpenings().size());
    DashboardOpeningDto openingDto2 = extractionDto.mainOpenings().get(0);
    Assertions.assertEquals(OPENING_ID, openingDto2.getOpeningId());
    Assertions.assertEquals("APP", openingDto2.getOpeningStatusCode());
    Assertions.assertEquals(TEST_USER, openingDto2.getEntryUserId());
    Assertions.assertEquals(ADMIN_DISTRICT_NO, openingDto2.getAdminDistrictNo());
    Assertions.assertEquals(22L, openingDto2.getResultsSubmissionId());
    Assertions.assertFalse(openingDto2.toLogString().isBlank());

    Assertions.assertEquals(1, extractionDto.openingSubmissions().size());
    DashboardOpeningSubmissionDto submissionDto2 = extractionDto.openingSubmissions().get(0);
    Assertions.assertEquals(22L, submissionDto2.getResultsSubmissionId());
    Assertions.assertEquals("0012797", submissionDto2.getClientNumber());
    Assertions.assertFalse(submissionDto2.toLogString().isBlank());

    Assertions.assertEquals(1, extractionDto.resultsAudits().size());
    DashboardResultsAuditDto auditDto2 = extractionDto.resultsAudits().get(0);
    Assertions.assertEquals(AUDIT_ACTION_CODE, auditDto2.getResultsAuditActionCode());
    Assertions.assertEquals(TEST_USER, auditDto2.getEntryUserid());
    Assertions.assertEquals(OPENING_ID, auditDto2.getOpeningId());
    Assertions.assertFalse(auditDto2.toLogString().isBlank());

    Assertions.assertEquals(1, extractionDto.stockingEvents().size());
    DashboardStockingEventDto stockingEventDto2 = extractionDto.stockingEvents().get(0);
    Assertions.assertEquals(AUDIT_ACTION_CODE, stockingEventDto2.getResultsAuditActionCode());
    Assertions.assertEquals(AUDIT_ACTION_CODE, stockingEventDto2.getResultsAuditActionCode());
    Assertions.assertEquals(OPENING_ID, stockingEventDto2.getOpeningId());
    Assertions.assertFalse(stockingEventDto2.toLogString().isBlank());

    Assertions.assertEquals(1, extractionDto.orgUnits().size());
    DashboardOrgUnitDto orgUnitDto2 = extractionDto.orgUnits().get(0);
    Assertions.assertEquals(ADMIN_DISTRICT_NO, orgUnitDto2.getOrgUnitNo());
    Assertions.assertEquals("DCR", orgUnitDto2.getOrgUnitCode());
    Assertions.assertEquals("DCR Name", orgUnitDto2.getOrgUnitName());
    Assertions.assertFalse(orgUnitDto2.toLogString().isBlank());

    Assertions.assertEquals(1, extractionDto.actionCodes().size());
    DashboardActionCodeDto actionCodeDto2 = extractionDto.actionCodes().get(0);
    Assertions.assertEquals(AUDIT_ACTION_CODE, actionCodeDto2.getResultsAuditActionCode());
    Assertions.assertEquals(AUDIT_ACTION_CODE, actionCodeDto2.getDescription());
    Assertions.assertFalse(actionCodeDto2.toLogString().isBlank());

    Assertions.assertFalse(extractionDto.logMessages().isEmpty());
    OracleLogDto logDto = extractionDto.logMessages().get(0);
    String message = "Querying Openings (on THE.OPENING main table) from the last 24 months";
    Assertions.assertEquals(message, logDto.message());
    Assertions.assertNotNull(logDto.eventTime());
  }
}
