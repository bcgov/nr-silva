package ca.bc.gov.restapi.results.oracle.service;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** This class contains methods for extracting data from Oracle THE schema. */
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
  public OracleExtractionDto getOpeningActivities(OracleExtractionParamsDto params) {
    List<OracleLogDto> logMessages = new ArrayList<>();

    // Openings main table
    logAndSave(
        logMessages,
        "Querying Openings (on THE.OPENING main table) from the last {} months",
        params.months());
    List<DashboardOpeningDto> mainOpenings =
        openingRepository.findAllDashboardOpenings(params.months());
    logAndSave(logMessages, "{} record(s) found (on THE.OPENING main table)", mainOpenings.size());

    if (Boolean.TRUE.equals(params.debug())) {
      logAndSave(logMessages, "DEBUG mode ON! Logging each found record on THE.OPENING");
      for (DashboardOpeningDto openingDto : mainOpenings) {
        logAndSave(logMessages, "DashboardOpeningDto={}", openingDto.toLogString());
      }
    }

    // Opening Submissions
    List<Long> submissionsIds =
        mainOpenings.stream()
            .filter(x -> !Objects.isNull(x.getResultsSubmissionId()))
            .map(DashboardOpeningDto::getResultsSubmissionId)
            .toList();

    if (Boolean.TRUE.equals(params.debug())) {
      logAndSave(logMessages, "DEBUG mode ON! Logging all Submission IDs for the next query");

      if (submissionsIds.size() > 800) {
        int perPage = 800;
        int size = (submissionsIds.size() / perPage) + 1;
        boolean stop = false;
        for (int i = 0; i < size; i++) {
          int start = i * perPage;
          int end = i * perPage + perPage;

          if (end > submissionsIds.size() - 1) {
            end = submissionsIds.size() - 1;
          }

          List<Long> slice = submissionsIds.subList(start, end);
          if (slice.isEmpty() || stop) {
            break;
          }

          logAndSave(logMessages, "IDs: {}", slice);
        }
      } else {
        logAndSave(logMessages, "IDs: {}", submissionsIds);
      }
    }

    logAndSave(
        logMessages,
        "Querying Submissions (on THE.RESULTS_ELECTRONIC_SUBMISSION table) using {} Opening IDs"
            + " found in the previous query",
        submissionsIds.size());

    List<DashboardOpeningSubmissionDto> openingSubmissions =
        openingRepository.findAllDashboardOpeningSubmissions(submissionsIds);
    logAndSave(
        logMessages,
        "{} record(s) found (on THE.RESULTS_ELECTRONIC_SUBMISSION table)",
        openingSubmissions.size());

    if (Boolean.TRUE.equals(params.debug())) {
      logAndSave(
          logMessages,
          "DEBUG mode ON! Logging each found record on THE.RESULTS_ELECTRONIC_SUBMISSION");
      for (DashboardOpeningSubmissionDto submissionDto : openingSubmissions) {
        logAndSave(logMessages, "DashboardOpeningSubmissionDto={}", submissionDto.toLogString());
      }
    }

    // RESULTS Audit
    logAndSave(
        logMessages,
        "Querying Audit Events (on THE.RESULTS_AUDIT_EVENT table) from the last {} months",
        params.months());
    List<DashboardResultsAuditDto> resultsAudits =
        openingRepository.findAllDashboardAuditEvents(params.months());
    logAndSave(
        logMessages, "{} record(s) found (on THE.RESULTS_AUDIT_EVENT table)", resultsAudits.size());

    if (Boolean.TRUE.equals(params.debug())) {
      logAndSave(
          logMessages, "DEBUG mode ON! Logging each found record on THE.RESULTS_AUDIT_EVENT");
      for (DashboardResultsAuditDto resultsAudit : resultsAudits) {
        logAndSave(logMessages, "DashboardResultsAuditDto={}", resultsAudit.toLogString());
      }
    }

    // Stocking Event History
    logAndSave(
        logMessages,
        "Querying Stocking Events (on THE.STOCKING_EVENT_HISTORY table) from the last {} months",
        params.months());
    List<DashboardStockingEventDto> stockingEvents =
        openingRepository.findAllDashboardStockingEventHistory(params.months());
    logAndSave(
        logMessages,
        "{} record(s) found (on THE.STOCKING_EVENT_HISTORY table)",
        stockingEvents.size());

    if (Boolean.TRUE.equals(params.debug())) {
      logAndSave(
          logMessages, "DEBUG mode ON! Logging each found record on THE.STOCKING_EVENT_HISTORY");
      for (DashboardStockingEventDto stockingEvent : stockingEvents) {
        logAndSave(logMessages, "DashboardStockingEventDto={}", stockingEvent.toLogString());
      }
    }

    // Org Unit - Code Table
    List<Long> orgUnitIds =
        mainOpenings.stream().map(DashboardOpeningDto::getAdminDistrictNo).toList();
    logAndSave(
        logMessages,
        "Gathered {} district(s) (Org Units) found on the Openings",
        orgUnitIds.size());

    if (Boolean.TRUE.equals(params.debug())) {
      logAndSave(logMessages, "DEBUG mode ON! Logging all Org Unit Codes for the next query");

      if (orgUnitIds.size() > 800) {
        int perPage = 800;
        int size = (orgUnitIds.size() / perPage) + 1;
        boolean stop = false;
        for (int i = 0; i < size; i++) {
          int start = i * perPage;
          int end = i * perPage + perPage;

          if (end > orgUnitIds.size() - 1) {
            end = orgUnitIds.size() - 1;
          }

          List<Long> slice = orgUnitIds.subList(start, end);
          if (slice.isEmpty() || stop) {
            break;
          }

          logAndSave(logMessages, "IDs: {}", slice);
        }
      } else {
        logAndSave(logMessages, "IDs: {}", orgUnitIds);
      }
    }

    logAndSave(
        logMessages,
        "Querying Org Units (on THE.ORG_UNIT table) using {} Codes found in the previous query",
        orgUnitIds.size());
    List<DashboardOrgUnitDto> orgUnits = openingRepository.findAllDashboardOrgUnits(orgUnitIds);
    logAndSave(logMessages, "{} record(s) found (on THE.ORG_UNIT table)", orgUnits.size());

    if (Boolean.TRUE.equals(params.debug())) {
      logAndSave(logMessages, "DEBUG mode ON! Logging all Org Units found");

      for (DashboardOrgUnitDto orgUnit : orgUnits) {
        logAndSave(logMessages, "DashboardOrgUnitDto={}", orgUnit.toLogString());
      }
    }

    // Action Codes - Code Table
    Set<String> codesSet = new HashSet<>();
    codesSet.addAll(
        resultsAudits.stream().map(DashboardResultsAuditDto::getResultsAuditActionCode).toList());
    codesSet.addAll(
        stockingEvents.stream().map(DashboardStockingEventDto::getResultsAuditActionCode).toList());

    logAndSave(
        logMessages,
        "Gathered {} unique action codes found on both Results Audit and Stocking Events",
        codesSet.size());

    if (Boolean.TRUE.equals(params.debug())) {
      logAndSave(logMessages, "DEBUG mode ON! Logging all Action Codes for the next query");
      logAndSave(logMessages, "Codes: {}", codesSet);
    }

    logAndSave(
        logMessages,
        "Querying Action Codes (on THE.RESULTS_AUDIT_ACTION_CODE table) using {} Codes found in the"
            + " previous query",
        codesSet.size());

    List<String> codes = new ArrayList<>(codesSet);
    List<DashboardActionCodeDto> actionCodes = openingRepository.findAllDashboardActionCodes(codes);
    logAndSave(
        logMessages,
        "{} record(s) found (on THE.RESULTS_AUDIT_ACTION_CODE table)",
        actionCodes.size());

    if (Boolean.TRUE.equals(params.debug())) {
      logAndSave(logMessages, "DEBUG mode ON! Logging all Action Codes found");

      for (DashboardActionCodeDto actionCode : actionCodes) {
        logAndSave(logMessages, "DashboardActionCodeDto={}", actionCode.toLogString());
      }
    }

    return new OracleExtractionDto(
        mainOpenings,
        openingSubmissions,
        resultsAudits,
        stockingEvents,
        orgUnits,
        actionCodes,
        logMessages);
  }

  private void logAndSave(List<OracleLogDto> logMessages, String message, Object... params) {
    log.info(message, params);

    if (params.length > 0) {
      for (int i = 0, len = params.length; i < len; i++) {
        message = message.replaceFirst("\\{\\}", String.valueOf(params[i]));
      }
    }
    logMessages.add(new OracleLogDto(message, LocalDateTime.now()));
  }
}
