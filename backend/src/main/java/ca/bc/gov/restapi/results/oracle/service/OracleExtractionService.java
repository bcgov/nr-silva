package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.dto.OracleExtractionDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardActionCodeDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningSubmissionDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOrgUnitDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardResultsAuditDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardStockingEventDto;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OracleExtractionService {

  private final OpeningRepository openingRepository;

  /**
   * Get all data from Oracle required for the dashboard metrics page.
   *
   * @return A {@link OracleExtractionDto} containing all data extracted.
   */
  public OracleExtractionDto getOpeningActivities() {
    Integer months = 24;
    int count = 0;

    // Openings main table
    List<DashboardOpeningDto> mainOpenings = openingRepository.findAllDashboardOpenings(months);
    log.info("Openings main table query count: {}", mainOpenings.size());
    for (DashboardOpeningDto openingDto : mainOpenings) {
      StringBuilder logDto = new StringBuilder("{");
      logDto.append("openingId=").append(openingDto.getOpeningId()).append(", ");
      logDto.append("openingStatusCode='").append(openingDto.getOpeningStatusCode());
      logDto.append("', entryUserId='").append(openingDto.getEntryUserId()).append("', ");
      logDto.append("entryTimestamp='").append(openingDto.getEntryTimestamp()).append("', ");
      logDto.append("updateTimestamp='").append(openingDto.getUpdateTimestamp());
      logDto.append("', adminDistrictNo=").append(openingDto.getAdminDistrictNo());
      logDto.append(", resultsSubmissionId=").append(openingDto.getResultsSubmissionId());
      logDto.append(", actionTimestamp='").append(openingDto.getActionTimestamp()).append("'}");
      log.info("DashboardOpeningDto={}", logDto.toString());

      count++;
      if (count == 3) {
        break;
      }
    }

    // Opening Submissions
    List<Long> submissionsIds =
        mainOpenings.stream().map(DashboardOpeningDto::getResultsSubmissionId).toList();
    log.info("Openings Submissions ids count: {}", submissionsIds.size());
    List<DashboardOpeningSubmissionDto> openingSubmissions =
        openingRepository.findAllDashboardOpeningSubmissions(submissionsIds);
    log.info("Openings Submissions result count: {}", openingSubmissions.size());

    count = 0;
    for (DashboardOpeningSubmissionDto submissionDto : openingSubmissions) {
      StringBuilder logDto = new StringBuilder("{");
      logDto.append("resultsSubmissionId=").append(submissionDto.getResultsSubmissionId());
      logDto.append(", clientNumber='").append(submissionDto.getClientNumber()).append("'}");
      log.info("DashboardOpeningSubmissionDto={}", logDto.toString());

      count++;
      if (count == 3) {
        break;
      }
    }

    // RESULTS Audit
    List<DashboardResultsAuditDto> resultsAudits =
        openingRepository.findAllDashboardAuditEvents(months);
    log.info("Results Audit table count: {}", resultsAudits.size());
    count = 0;
    for (DashboardResultsAuditDto resultsAudit : resultsAudits) {
      StringBuilder logDto = new StringBuilder("{");
      logDto.append("resultsAuditActionCode='").append(resultsAudit.getResultsAuditActionCode());
      logDto.append("', actionDate='").append(resultsAudit.getActionDate()).append(", ");
      logDto.append("entryTimestamp='").append(resultsAudit.getEntryTimestamp()).append("', ");
      logDto.append("entryUserid='").append(resultsAudit.getEntryUserid()).append("', ");
      logDto.append("openingId=").append(resultsAudit.getOpeningId()).append(", ");
      logDto.append("actionTimestamp=").append(resultsAudit.getActionTimestamp()).append("'}");
      log.info("DashboardResultsAuditDto={}", logDto.toString());

      count++;
      if (count == 3) {
        break;
      }
    }

    // Stocking Event History
    List<DashboardStockingEventDto> stockingEvents =
        openingRepository.findAllDashboardStockingEventHistory(months);
    log.info("Stocking Event History table count: {}", stockingEvents.size());
    count = 0;
    for (DashboardStockingEventDto stockingEvent : stockingEvents) {
      StringBuilder logDto = new StringBuilder("{");
      logDto.append("resultsAuditActionCode='").append(stockingEvent.getResultsAuditActionCode());
      logDto.append("', entryUserid='").append(stockingEvent.getEntryUserid()).append("', ");
      logDto.append("openingId=").append(stockingEvent.getOpeningId()).append(", ");
      logDto.append("entryTimestamp='").append(stockingEvent.getEntryTimestamp()).append("', ");
      logDto.append("amendEventTimestamp='").append(stockingEvent.getAmendEventTimestamp());
      logDto.append("', actionTimestamp='").append(stockingEvent.getActionTimestamp());
      logDto.append("}");
      log.info("DashboardStockingEventDto={}", logDto.toString());

      count++;
      if (count == 3) {
        break;
      }
    }

    // Org Unit - Code Table
    List<Long> orgUnitIds =
        mainOpenings.stream().map(DashboardOpeningDto::getAdminDistrictNo).toList();
    log.info("Org Unit ids count: {}", orgUnitIds.size());
    List<DashboardOrgUnitDto> orgUnits = openingRepository.findAllDashboardOrgUnits(orgUnitIds);
    log.info("Org Unit result count: {}", orgUnits.size());
    count = 0;
    for (DashboardOrgUnitDto orgUnit : orgUnits) {
      StringBuilder logDto = new StringBuilder("{");
      logDto.append("orgUnitNo=").append(orgUnit.getOrgUnitNo()).append(", ");
      logDto.append("orgUnitCode='").append(orgUnit.getOrgUnitCode()).append("', ");
      logDto.append("orgUnitName='").append(orgUnit.getOrgUnitName()).append("'}");
      log.info("DashboardOrgUnitDto={}", logDto.toString());

      count++;
      if (count == 3) {
        break;
      }
    }

    // Action Codes - Code Table
    Set<String> codesSet = new HashSet<>();
    codesSet.addAll(
        resultsAudits.stream().map(DashboardResultsAuditDto::getResultsAuditActionCode).toList());
    codesSet.addAll(
        stockingEvents.stream().map(DashboardStockingEventDto::getResultsAuditActionCode).toList());

    List<String> codes = new ArrayList<>(codesSet);
    List<DashboardActionCodeDto> actionCodes = openingRepository.findAllDashboardActionCodes(codes);
    log.info("Action Codes result count: {}", actionCodes.size());
    count = 0;
    for (DashboardActionCodeDto actionCode : actionCodes) {
      StringBuilder logDto = new StringBuilder("{");
      logDto.append("resultsAuditActionCode='").append(actionCode.getResultsAuditActionCode());
      logDto.append("', getDescription='").append(actionCode.getDescription()).append("'}");
      log.info("DashboardActionCodeDto={}", logDto.toString());

      count++;
      if (count == 3) {
        break;
      }
    }

    return new OracleExtractionDto(
        mainOpenings, openingSubmissions, resultsAudits, stockingEvents, orgUnits, actionCodes);
  }
}
