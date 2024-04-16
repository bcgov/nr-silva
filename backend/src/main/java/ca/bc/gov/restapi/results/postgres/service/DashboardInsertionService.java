package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.dto.OracleExtractionDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningSubmissionDto;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsActivityEntity;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsActivityRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningsLastYearRepository;

import java.util.ArrayList;
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

  /**
   * Loads all data extraced from Oracle into Postgres.
   *
   * @param oracleDto A {@link OracleExtractionDto} containing all data
   */
  @Transactional
  public void loadDashboardData(OracleExtractionDto oracleDto) {
    // Cleaning all tables
    openingsLastYearRepository.deleteAll();
    openingsLastYearRepository.flush();

    openingsActivityRepository.deleteAll();
    openingsActivityRepository.flush();

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
      openingEntity.setClientNumber(openingDto.getAdminDistrictNo().toString());
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

    List<OpeningsActivityEntity> activities = new ArrayList<>();
    // Load from: stockingEvents
    // Load from: resultsAudits
    // insert
    // openingId
    // activityTypeCode
    // activityTypeDesc
    // statusCode
    // statusDesc
    // lastUpdated
    // entryUserid
  }
}
