package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningSubmissionDto;
import ca.bc.gov.restapi.results.oracle.repository.OpeningRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OracleExtractionService {

  private final OpeningRepository openingRepository;

  public void getOpeningActivities() {
    Integer months = 24;
    int count = 0;

    // Openings main table
    List<DashboardOpeningDto> openings = openingRepository.findAllDashboardOpenings(months);
    log.info("Openings main table query count: {}", openings.size());
    for (DashboardOpeningDto openingDto : openings) {
      StringBuilder openingString = new StringBuilder("{");
      openingString.append("OPENING_ID=").append(openingDto.getOpeningId()).append(",");
      openingString
          .append("OPENING_STATUS_CODE='")
          .append(openingDto.getOpeningStatusCode())
          .append("',");
      openingString.append("ENTRY_USERID='").append(openingDto.getEntryUserId()).append("',");
      openingString.append("ENTRY_TIMESTAMP='").append(openingDto.getEntryTimestamp()).append("',");
      openingString.append("UPDATE_TIMESTAMP='").append(openingDto.getUpdateTimestamp()).append("',");
      openingString.append("ADMIN_DISTRICT_NO=").append(openingDto.getAdminDistrictNo()).append(",");
      openingString
          .append("RESULTS_SUBMISSION_ID=")
          .append(openingDto.getResultsSubmissionId())
          .append(",");
      openingString.append("ACTION_TIMESTAMP='").append(openingDto.getActionTimestamp()).append("'}");

      log.info("openingDto: {}", openingString.toString());
      count++;
      if (count == 3) {
        break;
      }
    }

    // Opening Submissions
    List<Long> submissionsIds =
        openings.stream().map(DashboardOpeningDto::getResultsSubmissionId).toList();
    log.info("Openings Submissions ids count: {}", submissionsIds.size());
    List<DashboardOpeningSubmissionDto> submissions =
        openingRepository.findAllDashboardOpeningSubmissions(submissionsIds);
    log.info("Openings Submissions result count: {}", submissions.size());

    count = 0;
    for (DashboardOpeningSubmissionDto submissionDto : submissions) {
      StringBuilder submissionString = new StringBuilder("{");
      submissionString
          .append("RESULTS_SUBMISSION_ID=")
          .append(submissionDto.getResultsSubmissionId())
          .append(",");
      submissionString.append("CLIENT_NUMBER='").append(submissionDto.getClientNumber()).append("'}");

      log.info("submissionDto: {}", submissionString.toString());
      count++;
      if (count == 3) {
        break;
      }
    }

    /*
    // RESULTS Audit
    List<DashboardResultsAuditDto> audits = openingRepository.findAllDashboardAuditEvents(months);
    log.info("Results Audit table count: {}", audits.size());
    count = 0;
    for (DashboardResultsAuditDto resultsAudit : audits) {
      log.info("resultsAudit: {}", resultsAudit.toString());
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
      log.info("stockingEvent: {}", stockingEvent.toString());
      count++;
      if (count == 3) {
        break;
      }
    }

    // Org Unit - Code Table
    List<Long> orgUnitIds = openings.stream().map(DashboardOpeningDto::ADMIN_DISTRICT_NO).toList();
    log.info("Org Unit ids count: {}", orgUnitIds.size());
    List<DashboardOrgUnitDto> orgUnits = openingRepository.findAllDashboardOrgUnits(orgUnitIds);
    log.info("Org Unit result count: {}", orgUnits.size());
    count = 0;
    for (DashboardOrgUnitDto orgUnit : orgUnits) {
      log.info("orgUnit: {}", orgUnit.toString());
      count++;
      if (count == 3) {
        break;
      }
    }

    // Action Codes - Code Table
    Set<String> codesSet = new HashSet<>();
    codesSet.addAll(audits.stream().map(DashboardResultsAuditDto::resultsAuditActionCode).toList());
    codesSet.addAll(
        stockingEvents.stream().map(DashboardStockingEventDto::resultsAuditActionCode).toList());

    List<String> codes = new ArrayList<>(codesSet);
    List<DashboardActionCodeDto> actionCodes = openingRepository.findAllDashboardActionCodes(codes);
    log.info("Action Codes result count: {}", actionCodes.size());
    count = 0;
    for (DashboardActionCodeDto actionCode : actionCodes) {
      log.info("actionCode: {}", actionCode.toString());
      count++;
      if (count == 3) {
        break;
      }
    }
    */
  }
}
