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
      log.info("DashboardOpeningDto={}", openingDto.toLogString());

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
      log.info("DashboardOpeningSubmissionDto={}", submissionDto.toLogString());

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
      log.info("DashboardResultsAuditDto={}", resultsAudit.toLogString());

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
      log.info("DashboardStockingEventDto={}", stockingEvent.toLogString());

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
      log.info("DashboardOrgUnitDto={}", orgUnit.toLogString());

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
      log.info("DashboardActionCodeDto={}", actionCode.toLogString());

      count++;
      if (count == 3) {
        break;
      }
    }

    return new OracleExtractionDto(
        mainOpenings, openingSubmissions, resultsAudits, stockingEvents, orgUnits, actionCodes);
  }
}
