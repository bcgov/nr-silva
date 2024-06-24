package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.dto.OracleExtractionDto;
import ca.bc.gov.restapi.results.common.dto.OracleExtractionParamsDto;
import ca.bc.gov.restapi.results.common.dto.OracleLogDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningSubmissionDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardResultsAuditDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardStockingEventDto;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntity;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntityId;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import ca.bc.gov.restapi.results.postgres.entity.OracleExtractionLogsEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsActivityRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsLastYearRepository;
import ca.bc.gov.restapi.results.postgres.repository.OracleExtractionLogsRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** This class contains methods for loading data extracted from oracle into postgres. */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardInsertionService {

  private final OpeningsLastYearRepository openingsLastYearRepository;

  private final OpeningsActivityRepository openingsActivityRepository;

  private final OracleExtractionLogsRepository oracleExtractionLogsRepository;

  private static final String INCONSISTENCY = "DEBUG mode ON! Possible data inconsistency found!";

  /**
   * Loads all data extracted from Oracle into Postgres.
   *
   * @param oracleDto A {@link OracleExtractionDto} containing all data
   */
  @Transactional(transactionManager = "postgresTransactionManager")
  public void loadDashboardData(
      OracleExtractionDto oracleDto,
      LocalDateTime startDateTime,
      OracleExtractionParamsDto params) {
    saveOpeningsLastYear(oracleDto, params.debug());
    saveOpeningsActivities(oracleDto, params.debug());
    saveLogs(oracleDto.logMessages(), startDateTime, params);
  }

  private void saveLogs(
      List<OracleLogDto> logMessages,
      LocalDateTime startDateTime,
      OracleExtractionParamsDto params) {
    long starting = startDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    long ending = Instant.now().toEpochMilli();
    long spent = ending - starting;
    String message = "Extraction finished! All Postgres tables loaded! Time spent in ms " + spent;
    log.info(message);
    logMessages.add(new OracleLogDto(message, LocalDateTime.now()));

    List<OracleExtractionLogsEntity> entities = new ArrayList<>();
    logMessages.forEach(
        log -> {
          OracleExtractionLogsEntity entity = new OracleExtractionLogsEntity();
          entity.setLogMessage(log.message());
          entity.setLoggedAt(log.eventTime());
          entity.setManuallyTriggered(params.manuallyTriggered());
          entities.add(entity);
        });

    oracleExtractionLogsRepository.deleteAll();
    oracleExtractionLogsRepository.flush();

    entities.sort(Comparator.comparing(OracleExtractionLogsEntity::getLoggedAt));
    oracleExtractionLogsRepository.saveAll(entities);
  }

  private void saveOpeningsActivities(OracleExtractionDto oracleDto, Boolean debug) {
    logAndSave(
        oracleDto.logMessages(),
        "Cleaning up opening activities (SILVA.OPENINGS_ACTIVITY on Postgres)");
    openingsActivityRepository.deleteAll();
    openingsActivityRepository.flush();

    // Map of code-descriptions
    Map<String, String> actionCodesMap = new HashMap<>();
    oracleDto
        .actionCodes()
        .forEach(
            actionCode ->
                actionCodesMap.put(
                    actionCode.getResultsAuditActionCode(), actionCode.getDescription()));

    Map<OpeningsActivityEntityId, OpeningsActivityEntity> openingActivityMap = new HashMap<>();

    // Load from: resultsAudits
    logAndSave(oracleDto.logMessages(), "Reading extracted data from the Audit table");
    Integer activityId = 0;
    for (DashboardResultsAuditDto resultsAudit : oracleDto.resultsAudits()) {
      if (Objects.isNull(resultsAudit.getOpeningId())) {
        if (Boolean.TRUE.equals(debug)) {
          logAndSave(oracleDto.logMessages(), INCONSISTENCY);

          logAndSave(
              oracleDto.logMessages(),
              "Opening ID column is null on the audit table! resultsAudit={}",
              resultsAudit.toLogString());
        }
        continue;
      }
      OpeningsActivityEntityId openingActId =
          new OpeningsActivityEntityId(++activityId, resultsAudit.getOpeningId());

      openingActivityMap.putIfAbsent(openingActId, new OpeningsActivityEntity());
      OpeningsActivityEntity activityEntity = openingActivityMap.get(openingActId);
      activityEntity.setActivityId(activityId);
      activityEntity.setOpeningId(resultsAudit.getOpeningId());

      String actionCode = resultsAudit.getResultsAuditActionCode();
      activityEntity.setActivityTypeCode(actionCode);
      activityEntity.setActivityTypeDesc(actionCodesMap.getOrDefault(actionCode, ""));
      activityEntity.setStatusCode(null); // ?? missing
      activityEntity.setStatusDesc(null); // ?? missing
      activityEntity.setLastUpdated(resultsAudit.getActionTimestamp());
      activityEntity.setEntryUserid(resultsAudit.getEntryUserid());
    }

    // Load from: stockingEvents
    logAndSave(oracleDto.logMessages(), "Reading extracted data from the Stocking Events table");
    for (DashboardStockingEventDto stockingEvent : oracleDto.stockingEvents()) {
      if (Objects.isNull(stockingEvent.getOpeningId())) {
        if (Boolean.TRUE.equals(debug)) {
          logAndSave(oracleDto.logMessages(), INCONSISTENCY);

          logAndSave(
              oracleDto.logMessages(),
              "Opening ID column is null on the stocking events table! stockingEvent={}",
              stockingEvent.toLogString());
        }
        continue;
      }
      OpeningsActivityEntityId openingActId =
          new OpeningsActivityEntityId(++activityId, stockingEvent.getOpeningId());

      openingActivityMap.putIfAbsent(openingActId, new OpeningsActivityEntity());
      OpeningsActivityEntity activityEntity = openingActivityMap.get(openingActId);
      activityEntity.setActivityId(activityId);
      activityEntity.setOpeningId(stockingEvent.getOpeningId());

      String actionCode = stockingEvent.getResultsAuditActionCode();
      activityEntity.setActivityTypeCode(actionCode);
      activityEntity.setActivityTypeDesc(actionCodesMap.getOrDefault(actionCode, ""));
      activityEntity.setStatusCode(null); // ?? missing
      activityEntity.setStatusDesc(null); // ?? missing
      activityEntity.setLastUpdated(stockingEvent.getActionTimestamp());
      activityEntity.setEntryUserid(stockingEvent.getEntryUserid());
    }

    openingsActivityRepository.saveAllAndFlush(openingActivityMap.values());

    logAndSave(
        oracleDto.logMessages(),
        "Loaded {} records into the activities table (SILVA.openings_activity on Postgres)",
        openingActivityMap.values().size());
  }

  private void saveOpeningsLastYear(OracleExtractionDto oracleDto, Boolean debug) {
    logAndSave(
        oracleDto.logMessages(),
        "Cleaning up the past openings table (SILVA.OPENINGS_LAST_YEAR on Postgres)");

    openingsLastYearRepository.deleteAll();
    openingsLastYearRepository.flush();

    // Map of org units
    Map<String, String> orgUnitsMap = new HashMap<>();
    oracleDto
        .orgUnits()
        .forEach(orgUnit -> orgUnitsMap.put(orgUnit.getOrgUnitCode(), orgUnit.getOrgUnitName()));

    // Inserting data
    Map<Long, OpeningsLastYearEntity> openingsLastYearMap = new HashMap<>();
    Map<Long, Long> openingSubmissionMap = new HashMap<>();

    // Load from: mainOpenings
    for (DashboardOpeningDto openingDto : oracleDto.mainOpenings()) {
      openingsLastYearMap.putIfAbsent(openingDto.getOpeningId(), new OpeningsLastYearEntity());

      OpeningsLastYearEntity openingEntity = openingsLastYearMap.get(openingDto.getOpeningId());
      openingEntity.setOpeningId(openingDto.getOpeningId());
      openingEntity.setUserId(openingDto.getEntryUserId());
      openingEntity.setEntryTimestamp(openingDto.getEntryTimestamp());
      openingEntity.setUpdateTimestamp(openingDto.getUpdateTimestamp());
      openingEntity.setStatus(openingDto.getOpeningStatusCode());

      String orgUnitCode = null;
      String orgUnitName = null;
      if (!Objects.isNull(openingDto.getAdminDistrictNo())) {
        orgUnitCode = openingDto.getAdminDistrictNo().toString();
        orgUnitName = orgUnitsMap.get(orgUnitCode);
      }
      openingEntity.setOrgUnitCode(orgUnitCode);
      openingEntity.setOrgUnitName(orgUnitName);
      // clientNumber: down below

      openingSubmissionMap.put(openingDto.getResultsSubmissionId(), openingDto.getOpeningId());
    }

    // Load from: openingSubmissions
    for (DashboardOpeningSubmissionDto submissionDto : oracleDto.openingSubmissions()) {
      OpeningsLastYearEntity openingEntity = null;

      Long openingId = openingSubmissionMap.get(submissionDto.getResultsSubmissionId());
      if (Objects.isNull(openingId)) {
        if (Boolean.TRUE.equals(debug)) {
          logAndSave(oracleDto.logMessages(), INCONSISTENCY);
          logAndSave(
              oracleDto.logMessages(),
              "No opening ID found for submission ID {}",
              submissionDto.getResultsSubmissionId());
        }
      } else {
        openingEntity = openingsLastYearMap.get(openingId);
      }

      if (Objects.isNull(openingEntity)) {
        if (Boolean.TRUE.equals(debug)) {
          logAndSave(oracleDto.logMessages(), INCONSISTENCY);
          logAndSave(
              oracleDto.logMessages(),
              "No opening entity for ID {} (from the openings last year table map)",
              openingId);
        }
      } else {
        // clientNumber
        openingEntity.setClientNumber(submissionDto.getClientNumber());
      }
    }

    openingsLastYearRepository.saveAllAndFlush(openingsLastYearMap.values());

    logAndSave(
        oracleDto.logMessages(),
        "Loaded {} records into the opening table (SILVA.OPENINGS_LAST_YEAR on Postgres)",
        openingsLastYearMap.values().size());
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
