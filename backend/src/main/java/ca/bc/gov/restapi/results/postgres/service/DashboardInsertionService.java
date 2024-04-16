package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.dto.OracleExtractionDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningSubmissionDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardResultsAuditDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardStockingEventDto;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntity;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntityId;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsActivityRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsLastYearRepository;
import java.util.HashMap;
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

  /**
   * Loads all data extraced from Oracle into Postgres.
   *
   * @param oracleDto A {@link OracleExtractionDto} containing all data
   */
  @Transactional(transactionManager = "postgresTransactionManager")
  public void loadDashboardData(OracleExtractionDto oracleDto) {
    saveOpeningsLastYear(oracleDto);
    saveOpeningsActivities(oracleDto);
  }

  private void saveOpeningsActivities(OracleExtractionDto oracleDto) {
    // Cleaning table
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
    Integer activityId = 0;
    for (DashboardResultsAuditDto resultsAudit : oracleDto.resultsAudits()) {
      if (Objects.isNull(resultsAudit.getOpeningId())) {
        log.warn("Opening ID not found on audit! resultsAudit={}", resultsAudit.toLogString());
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
    for (DashboardStockingEventDto stockingEvent : oracleDto.stockingEvents()) {
      if (Objects.isNull(stockingEvent.getOpeningId())) {
        log.warn("Opening ID not found on stocking! stockingEvent={}", stockingEvent.toLogString());
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

    log.info(
        "Inserting {} records into openingsActivityRepository", openingActivityMap.values().size());
    openingsActivityRepository.saveAllAndFlush(openingActivityMap.values());
  }

  private void saveOpeningsLastYear(OracleExtractionDto oracleDto) {
    // Cleaning table
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
      Long openingId = openingSubmissionMap.get(submissionDto.getResultsSubmissionId());
      if (Objects.isNull(openingId)) {
        log.info("No opening ID for submission ID {}", submissionDto.getResultsSubmissionId());
        continue;
      }

      OpeningsLastYearEntity openingEntity = openingsLastYearMap.get(openingId);
      if (Objects.isNull(openingId)) {
        log.info("No opening for ID {}", openingId);
        continue;
      }

      // clientNumber
      openingEntity.setClientNumber(submissionDto.getClientNumber());
    }

    log.info(
        "Inserting {} records into openingsLastYearRepository",
        openingsLastYearMap.values().size());
    openingsLastYearRepository.saveAllAndFlush(openingsLastYearMap.values());
  }
}
