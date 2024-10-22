package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.dto.DashboardActionCodeDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningSubmissionDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOrgUnitDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardResultsAuditDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardStockingEventDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningSearchProjection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * This interface allows the service to fetch and save data into the database.
 */
public interface OpeningRepository extends JpaRepository<OpeningEntity, Long> {

  Page<OpeningEntity> findAllByEntryUserId(String entryUserId, Pageable pageable);

  @Query(
      value =
          """
              SELECT op.OPENING_ID AS openingId
                ,op.OPENING_STATUS_CODE AS openingStatusCode
                ,op.ENTRY_USERID AS entryUserId
                ,op.ENTRY_TIMESTAMP AS entryTimestamp
                ,op.UPDATE_TIMESTAMP AS updateTimestamp
                ,op.ADMIN_DISTRICT_NO AS adminDistrictNo
                ,op.RESULTS_SUBMISSION_ID AS resultsSubmissionId
                ,GREATEST(op.ENTRY_TIMESTAMP,op.UPDATE_TIMESTAMP) AS actionTimestamp
              FROM THE.OPENING op
              WHERE op.ENTRY_TIMESTAMP >= ADD_MONTHS(SYSDATE, - ?1)
                OR op.UPDATE_TIMESTAMP >= ADD_MONTHS(SYSDATE, - ?1)
              ORDER BY actionTimestamp DESC
              """,
      nativeQuery = true)
  List<DashboardOpeningDto> findAllDashboardOpenings(Integer months);

  @Query(
      value =
          """
              SELECT res.RESULTS_SUBMISSION_ID AS resultsSubmissionId
                ,res.CLIENT_NUMBER AS clientNumber
              FROM THE.RESULTS_ELECTRONIC_SUBMISSION res
              WHERE res.RESULTS_SUBMISSION_ID IN (?1)
              """,
      nativeQuery = true)
  List<DashboardOpeningSubmissionDto> findAllDashboardOpeningSubmissions(
      List<Long> submissionIdList);

  @Query(
      value =
          """
                SELECT ra.RESULTS_AUDIT_ACTION_CODE AS resultsAuditActionCode
                  ,ra.ACTION_DATE AS actionDate
                  ,ra.ENTRY_TIMESTAMP AS entryTimestamp
                  ,ra.ENTRY_USERID AS entryUserid
                  ,ra.OPENING_ID AS openingId
                  ,GREATEST(ra.ENTRY_TIMESTAMP,ra.ACTION_DATE) AS actionTimestamp
                FROM THE.RESULTS_AUDIT_EVENT ra
                WHERE ra.ENTRY_TIMESTAMP >= ADD_MONTHS(SYSDATE, - ?1)
                  OR ra.ACTION_DATE >= ADD_MONTHS(SYSDATE, - ?1)
                ORDER BY actionTimestamp DESC
              """,
      nativeQuery = true)
  List<DashboardResultsAuditDto> findAllDashboardAuditEvents(Integer months);

  @Query(
      value =
          """
                SELECT seh.RESULTS_AUDIT_ACTION_CODE AS resultsAuditActionCode
                  ,seh.ENTRY_USERID AS entryUserid
                  ,seh.OPENING_ID AS openingId
                  ,seh.ENTRY_TIMESTAMP AS entryTimestamp
                  ,seh.AMEND_EVENT_TIMESTAMP AS amendEventTimestamp
                  ,GREATEST(seh.ENTRY_TIMESTAMP,seh.AMEND_EVENT_TIMESTAMP) AS actionTimestamp
                FROM THE.STOCKING_EVENT_HISTORY seh
                WHERE seh.ENTRY_TIMESTAMP >= ADD_MONTHS(SYSDATE, - ?1)
                  OR seh.AMEND_EVENT_TIMESTAMP  >= ADD_MONTHS(SYSDATE, - ?1)
                ORDER BY actionTimestamp DESC
              """,
      nativeQuery = true)
  List<DashboardStockingEventDto> findAllDashboardStockingEventHistory(Integer months);

  @Query(
      value =
          """
                SELECT ou.ORG_UNIT_NO AS orgUnitNo
                  ,ou.ORG_UNIT_CODE AS orgUnitCode
                  ,ou.ORG_UNIT_NAME AS orgUnitName
                FROM THE.ORG_UNIT ou
                WHERE ou.ORG_UNIT_NO IN (?1)
              """,
      nativeQuery = true)
  List<DashboardOrgUnitDto> findAllDashboardOrgUnits(List<Long> orgUnitIds);

  @Query(
      value =
          """
                SELECT raac.RESULTS_AUDIT_ACTION_CODE AS resultsAuditActionCode
                  ,raac.DESCRIPTION AS description
                FROM THE.RESULTS_AUDIT_ACTION_CODE raac
                WHERE raac.RESULTS_AUDIT_ACTION_CODE IN (?1)
              """,
      nativeQuery = true)
  List<DashboardActionCodeDto> findAllDashboardActionCodes(List<String> codes);

  @Query(
      nativeQuery = true,
      value = """
           SELECT o.OPENING_ID AS opening_id,
              o.OPENING_NUMBER AS opening_number,
              o.OPEN_CATEGORY_CODE AS category,
              o.OPENING_STATUS_CODE AS status,
              cboa.CUTTING_PERMIT_ID AS cutting_permit_id,
              cboa.TIMBER_MARK AS timber_mark,
              cboa.CUT_BLOCK_ID AS cut_block_id,
              cboa.OPENING_GROSS_AREA AS opening_gross_area,
              cboa.DISTURBANCE_START_DATE AS disturbance_start_date,
              cboa.FOREST_FILE_ID AS forest_file_id,
              ou.ORG_UNIT_CODE AS org_unit_code,
              ou.ORG_UNIT_NAME AS org_unit_name,
              res.CLIENT_NUMBER AS client_number,
              res.CLIENT_LOCN_CODE AS client_location,
              ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMRG.LATE_OFFSET_YEARS,0)*12)) AS regen_delay_date,
              ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.EARLY_OFFSET_YEARS,0)*12)) AS early_free_growing_date,
              ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS,0)*12)) AS late_free_growing_date,
              o.UPDATE_TIMESTAMP AS update_timestamp,
              o.ENTRY_USERID AS entry_user_id,
              COALESCE(sra.SILV_RELIEF_APPLICATION_ID, 0) AS submitted_to_frpa108
           FROM THE.OPENING o
           LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)
           LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)
           LEFT JOIN the.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)
           LEFT JOIN THE.CLIENT_ACRONYM ca ON (ca.CLIENT_NUMBER = res.CLIENT_NUMBER)
           LEFT JOIN THE.ACTIVITY_TREATMENT_UNIT atu ON (atu.OPENING_ID = o.OPENING_ID)
           LEFT JOIN THE.SILV_RELIEF_APPLICATION sra ON (sra.ACTIVITY_TREATMENT_UNIT_ID = atu.ACTIVITY_TREATMENT_UNIT_ID AND sra.SILV_RELIEF_APPL_STATUS_CODE = 'APP')
           LEFT JOIN THE.STOCKING_STANDARD_UNIT ssu ON (ssu.OPENING_ID = o.OPENING_ID)
           LEFT JOIN THE.STOCKING_MILESTONE smrg ON (smrg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND SMRG.SILV_MILESTONE_TYPE_CODE = 'RG')
           LEFT JOIN THE.STOCKING_MILESTONE smfg ON (smfg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND smfg.SILV_MILESTONE_TYPE_CODE = 'FG')
           WHERE (
               :#{#filters.mainSearchTerm} IS NULL OR (
                   REGEXP_LIKE(:#{#filters.mainSearchTerm}, '^\\d+$')
                   AND o.OPENING_ID = TO_NUMBER(:#{#filters.mainSearchTerm})
               ) OR (
                   o.OPENING_NUMBER = :#{#filters.mainSearchTerm} OR
                   cboa.TIMBER_MARK = :#{#filters.mainSearchTerm} OR
                   cboa.FOREST_FILE_ID = :#{#filters.mainSearchTerm}
               )
           )
           AND (
               :#{#filters.orgUnit} IS NULL OR ou.ORG_UNIT_CODE = :#{#filters.orgUnit}
           )
           AND (
               :#{#filters.category} IS NULL OR o.OPEN_CATEGORY_CODE = :#{#filters.category}
           )
           AND (
               :#{#filters.statusList} IS NULL OR o.OPENING_STATUS_CODE IN (:#{#filters.statusList})
           )
           AND (
               :#{#filters.requestUserId} IS NULL OR o.ENTRY_USERID = :#{#filters.requestUserId}
           )
           AND (
               :#{#filters.submittedToFrpa} IS NULL OR (
                   (:#{#filters.submittedToFrpa} = TRUE AND sra.SILV_RELIEF_APPLICATION_ID IS NOT NULL) OR
                   (:#{#filters.submittedToFrpa} = FALSE AND sra.SILV_RELIEF_APPLICATION_ID IS NULL)
               )
           )
           AND (
             :#{#filters.disturbanceDateStart} IS NULL OR cboa.DISTURBANCE_START_DATE >= :#{#filters.disturbanceDateStart}
           )
           AND (
             :#{#filters.disturbanceDateEnd} IS NULL OR cboa.DISTURBANCE_START_DATE <= :#{#filters.disturbanceDateEnd}
           )
           AND (
             :#{#filters.regenDelayDateStart} IS NULL OR
             ADD_MONTHS(cboa.DISTURBANCE_START_DATE, COALESCE(SMRG.LATE_OFFSET_YEARS,0)*12) > :#{#filters.regenDelayDateStart}
           )
           AND (
             :#{#filters.regenDelayDateEnd} IS NULL OR
             ADD_MONTHS(cboa.DISTURBANCE_START_DATE, COALESCE(SMRG.LATE_OFFSET_YEARS,0)*12) < :#{#filters.regenDelayDateEnd}
           )
           AND (
             :#{#filters.freeGrowingDateStart} IS NULL OR
             ADD_MONTHS(cboa.DISTURBANCE_START_DATE, COALESCE(SMFG.EARLY_OFFSET_YEARS,0)*12) > :#{#filters.freeGrowingDateStart}
           )
           AND (
             :#{#filters.freeGrowingDateEnd} IS NULL OR
             ADD_MONTHS(cboa.DISTURBANCE_START_DATE, COALESCE(SMFG.LATE_OFFSET_YEARS,0)*12) < :#{#filters.freeGrowingDateEnd}
           )
           AND (
             :#{#filters.updateDateStart} IS NULL OR o.UPDATE_TIMESTAMP >= :#{#filters.updateDateStart}
           )
           AND (
             :#{#filters.updateDateEnd} IS NULL OR o.UPDATE_TIMESTAMP <= :#{#filters.updateDateEnd}
           )
           AND (
               :#{#filters.cuttingPermitId} IS NULL OR cboa.CUTTING_PERMIT_ID = :#{#filters.cuttingPermitId}
           )
           AND (
               :#{#filters.cutBlockId} IS NULL OR cboa.CUT_BLOCK_ID = :#{#filters.cutBlockId}
           )
           AND (
               :#{#filters.timberMark} IS NULL OR cboa.TIMBER_MARK = :#{#filters.timberMark}
           )
           GROUP BY o.OPENING_ID
            ,o.OPENING_NUMBER
            ,o.OPEN_CATEGORY_CODE
            ,o.OPENING_STATUS_CODE
            ,cboa.CUTTING_PERMIT_ID
            ,cboa.TIMBER_MARK
            ,cboa.CUT_BLOCK_ID
            ,cboa.OPENING_GROSS_AREA
            ,cboa.DISTURBANCE_START_DATE
            ,cboa.FOREST_FILE_ID
            ,ou.ORG_UNIT_CODE
            ,ou.ORG_UNIT_NAME
            ,res.CLIENT_NUMBER
            ,res.CLIENT_LOCN_CODE
            ,ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMRG.LATE_OFFSET_YEARS, 0) * 12))
            ,ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.EARLY_OFFSET_YEARS, 0) * 12))
            ,ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS, 0) * 12))
            ,o.UPDATE_TIMESTAMP
            ,o.ENTRY_USERID
            ,COALESCE(sra.SILV_RELIEF_APPLICATION_ID, 0)
            ORDER BY o.OPENING_ID DESC
            OFFSET :offset ROWS FETCH NEXT :size ROWS ONLY
          """
  )
  List<OpeningSearchProjection> findOpenings(OpeningSearchFiltersDto filters, long offset, int size);
}
