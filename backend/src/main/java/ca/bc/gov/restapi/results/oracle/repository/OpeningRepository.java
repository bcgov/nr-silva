package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.dto.DashboardActionCodeDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOpeningSubmissionDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardOrgUnitDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardResultsAuditDto;
import ca.bc.gov.restapi.results.oracle.dto.DashboardStockingEventDto;
import ca.bc.gov.restapi.results.oracle.dto.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.entity.SilvicultureSearchProjection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** This interface allows the service to fetch and save data into the database. */
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
      value = """
          SELECT opening_id, opening_number, category, status, cutting_permit_id, timber_mark,\s
                 cut_block_id, opening_gross_area, disturbance_start_date, forest_file_id,\s
                 org_unit_code, org_unit_name, client_number, client_location, regen_delay_date,\s
                 early_free_growing_date, late_free_growing_date, update_timestamp, entry_user_id,\s
                 submitted_to_frpa108
          FROM (
              SELECT
                  o.OPENING_ID AS opening_id,
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
                  ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMRG.LATE_OFFSET_YEARS, 0) * 12)) AS regen_delay_date,
                  ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.EARLY_OFFSET_YEARS, 0) * 12)) AS early_free_growing_date,
                  ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS, 0) * 12)) AS late_free_growing_date,
                  o.UPDATE_TIMESTAMP AS update_timestamp,
                  o.ENTRY_USERID AS entry_user_id,
                  COALESCE(sra.SILV_RELIEF_APPLICATION_ID, 0) AS submitted_to_frpa108,
                  ROW_NUMBER() OVER (PARTITION BY o.OPENING_ID ORDER BY o.UPDATE_TIMESTAMP DESC) AS rn
              FROM THE.OPENING o
              LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)
              LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)
              LEFT JOIN THE.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)
              LEFT JOIN THE.SILV_RELIEF_APPLICATION sra ON (sra.ACTIVITY_TREATMENT_UNIT_ID = o.OPENING_ID AND sra.SILV_RELIEF_APPL_STATUS_CODE = 'APP')
              LEFT JOIN THE.STOCKING_STANDARD_UNIT ssu ON (ssu.OPENING_ID = o.OPENING_ID)
              LEFT JOIN THE.STOCKING_MILESTONE smrg ON (smrg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND SMRG.SILV_MILESTONE_TYPE_CODE = 'RG')
              LEFT JOIN THE.STOCKING_MILESTONE smfg ON (smfg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND smfg.SILV_MILESTONE_TYPE_CODE = 'FG')
          )
          WHERE rn = 1 AND (
            NVL(:#{#filter.mainSearchTerm},'NOVALUE') = 'NOVALUE' OR (
                REGEXP_LIKE(:#{#filter.mainSearchTerm}, '^\\d+$')
                AND OPENING_ID = TO_NUMBER(:#{#filter.mainSearchTerm})
            ) OR (
                OPENING_NUMBER = :#{#filter.mainSearchTerm} OR
                TIMBER_MARK = :#{#filter.mainSearchTerm} OR
                FOREST_FILE_ID = :#{#filter.mainSearchTerm}
            )
            )
            AND (
                NVL(:#{#filter.orgUnit},'NOVALUE') = 'NOVALUE' OR ORG_UNIT_CODE IN (:#{#filter.orgUnit})
            )
            AND (
                NVL(:#{#filter.category},'NOVALUE') = 'NOVALUE' OR CATEGORY IN (:#{#filter.category})
            )
            AND (
                NVL(:#{#filter.statusList},'NOVALUE') = 'NOVALUE' OR STATUS IN (:#{#filter.statusList})
            )
            AND (
                NVL(:#{#filter.requestUserId},'NOVALUE') = 'NOVALUE' OR ENTRY_USER_ID = :#{#filter.requestUserId}
            )
            AND (
                NVL(:#{#filter.submittedToFrpa},'NO') = 'NO' OR (
                NVL(:#{#filter.submittedToFrpa},'NO') = 'YES' AND submitted_to_frpa108 > 0
              )
            )
            AND (
              NVL(:#{#filter.disturbanceDateStart},'NOVALUE') = 'NOVALUE' OR DISTURBANCE_START_DATE >= TO_TIMESTAMP(:#{#filter.disturbanceDateStart},'YYYY-MM-DD')
            )
            AND (
              NVL(:#{#filter.disturbanceDateEnd},'NOVALUE') = 'NOVALUE' OR DISTURBANCE_START_DATE <= TO_TIMESTAMP(:#{#filter.disturbanceDateEnd},'YYYY-MM-DD')
            )
            AND (
              NVL(:#{#filter.regenDelayDateStart},'NOVALUE') = 'NOVALUE' OR regen_delay_date > TO_TIMESTAMP(:#{#filter.regenDelayDateStart},'YYYY-MM-DD')
            )
            AND (
              NVL(:#{#filter.regenDelayDateEnd},'NOVALUE') = 'NOVALUE' OR regen_delay_date < TO_TIMESTAMP(:#{#filter.regenDelayDateEnd},'YYYY-MM-DD')
            )
            AND (
              NVL(:#{#filter.freeGrowingDateStart},'NOVALUE') = 'NOVALUE' OR early_free_growing_date > TO_TIMESTAMP(:#{#filter.freeGrowingDateStart},'YYYY-MM-DD')
            )
            AND (
              NVL(:#{#filter.freeGrowingDateEnd},'NOVALUE') = 'NOVALUE' OR late_free_growing_date < TO_TIMESTAMP(:#{#filter.freeGrowingDateEnd},'YYYY-MM-DD')
            )
            AND (
              NVL(:#{#filter.updateDateStart},'NOVALUE') = 'NOVALUE' OR UPDATE_TIMESTAMP >= TO_TIMESTAMP(:#{#filter.updateDateStart}, 'YYYY-MM-DD')
            )
            AND (
              NVL(:#{#filter.updateDateEnd},'NOVALUE') = 'NOVALUE' OR UPDATE_TIMESTAMP <= TO_TIMESTAMP(:#{#filter.updateDateEnd}, 'YYYY-MM-DD')
            )
            AND (
                NVL(:#{#filter.cuttingPermitId},'NOVALUE') = 'NOVALUE' OR CUTTING_PERMIT_ID = :#{#filter.cuttingPermitId}
            )
            AND (
                NVL(:#{#filter.cutBlockId},'NOVALUE') = 'NOVALUE' OR CUT_BLOCK_ID = :#{#filter.cutBlockId}
            )
            AND (
                NVL(:#{#filter.timberMark},'NOVALUE') = 'NOVALUE' OR TIMBER_MARK = :#{#filter.timberMark}
            )""",
      countQuery = """
          SELECT count(o.OPENING_ID) as total
             FROM THE.OPENING o
             LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)
             LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)
             LEFT JOIN THE.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)
             LEFT JOIN THE.SILV_RELIEF_APPLICATION sra ON (sra.ACTIVITY_TREATMENT_UNIT_ID = o.OPENING_ID AND sra.SILV_RELIEF_APPL_STATUS_CODE = 'APP')
             LEFT JOIN THE.STOCKING_STANDARD_UNIT ssu ON (ssu.OPENING_ID = o.OPENING_ID)
             LEFT JOIN THE.STOCKING_MILESTONE smrg ON (smrg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND SMRG.SILV_MILESTONE_TYPE_CODE = 'RG')
             LEFT JOIN THE.STOCKING_MILESTONE smfg ON (smfg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND smfg.SILV_MILESTONE_TYPE_CODE = 'FG')
         WHERE (
           NVL(:#{#filter.mainSearchTerm},'NOVALUE') = 'NOVALUE' OR (
               REGEXP_LIKE(:#{#filter.mainSearchTerm}, '^\\d+$')
               AND o.OPENING_ID = TO_NUMBER(:#{#filter.mainSearchTerm})
             ) OR (
                 o.OPENING_NUMBER = :#{#filter.mainSearchTerm} OR
                 cboa.TIMBER_MARK = :#{#filter.mainSearchTerm} OR
                 cboa.FOREST_FILE_ID = :#{#filter.mainSearchTerm}
             )
           )
           AND (
               NVL(:#{#filter.orgUnit},'NOVALUE') = 'NOVALUE' OR ou.ORG_UNIT_CODE IN (:#{#filter.orgUnit})
           )
           AND (
               NVL(:#{#filter.category},'NOVALUE') = 'NOVALUE' OR o.OPEN_CATEGORY_CODE IN (:#{#filter.category})
           )
           AND (
               NVL(:#{#filter.statusList},'NOVALUE') = 'NOVALUE' OR o.OPENING_STATUS_CODE IN (:#{#filter.statusList})
           )
           AND (
               NVL(:#{#filter.requestUserId},'NOVALUE') = 'NOVALUE' OR o.ENTRY_USERID = :#{#filter.requestUserId}
           )
           AND (
               NVL(:#{#filter.submittedToFrpa},'NO') = 'NO' OR (
               NVL(:#{#filter.submittedToFrpa},'NO') = 'YES' AND COALESCE(sra.SILV_RELIEF_APPLICATION_ID, 0) > 0
             )
           )
           AND (
             NVL(:#{#filter.disturbanceDateStart},'NOVALUE') = 'NOVALUE' OR cboa.DISTURBANCE_START_DATE >= TO_TIMESTAMP(:#{#filter.disturbanceDateStart},'YYYY-MM-DD')
           )
           AND (
             NVL(:#{#filter.disturbanceDateEnd},'NOVALUE') = 'NOVALUE' OR cboa.DISTURBANCE_START_DATE <= TO_TIMESTAMP(:#{#filter.disturbanceDateEnd},'YYYY-MM-DD')
           )
           AND (
             NVL(:#{#filter.regenDelayDateStart},'NOVALUE') = 'NOVALUE' OR ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMRG.LATE_OFFSET_YEARS, 0) * 12)) > TO_TIMESTAMP(:#{#filter.regenDelayDateStart},'YYYY-MM-DD')
           )
           AND (
             NVL(:#{#filter.regenDelayDateEnd},'NOVALUE') = 'NOVALUE' OR ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMRG.LATE_OFFSET_YEARS, 0) * 12)) < TO_TIMESTAMP(:#{#filter.regenDelayDateEnd},'YYYY-MM-DD')
           )
           AND (
             NVL(:#{#filter.freeGrowingDateStart},'NOVALUE') = 'NOVALUE' OR ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.EARLY_OFFSET_YEARS, 0) * 12)) > TO_TIMESTAMP(:#{#filter.freeGrowingDateStart},'YYYY-MM-DD')
           )
           AND (
             NVL(:#{#filter.freeGrowingDateEnd},'NOVALUE') = 'NOVALUE' OR ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS, 0) * 12)) < TO_TIMESTAMP(:#{#filter.freeGrowingDateEnd},'YYYY-MM-DD')
           )
           AND (
             NVL(:#{#filter.updateDateStart},'NOVALUE') = 'NOVALUE' OR ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS, 0) * 12)) >= TO_TIMESTAMP(:#{#filter.updateDateStart}, 'YYYY-MM-DD')
           )
           AND (
             NVL(:#{#filter.updateDateEnd},'NOVALUE') = 'NOVALUE' OR ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS, 0) * 12)) <= TO_TIMESTAMP(:#{#filter.updateDateEnd}, 'YYYY-MM-DD')
           )
           AND (
               NVL(:#{#filter.cuttingPermitId},'NOVALUE') = 'NOVALUE' OR cboa.CUTTING_PERMIT_ID = :#{#filter.cuttingPermitId}
           )
           AND (
               NVL(:#{#filter.cutBlockId},'NOVALUE') = 'NOVALUE' OR cboa.CUT_BLOCK_ID = :#{#filter.cutBlockId}
           )
           AND (
               NVL(:#{#filter.timberMark},'NOVALUE') = 'NOVALUE' OR cboa.TIMBER_MARK = :#{#filter.timberMark}
           )""",
      nativeQuery = true
  )
  Page<SilvicultureSearchProjection> searchBy(
      OpeningSearchFiltersDto filter,
      Pageable pageable
  );

  @Query(
      value = """
          SELECT opening_id, opening_number, category, status, cutting_permit_id, timber_mark,\s
                 cut_block_id, opening_gross_area, disturbance_start_date, forest_file_id,\s
                 org_unit_code, org_unit_name, client_number, client_location, regen_delay_date,\s
                 early_free_growing_date, late_free_growing_date, update_timestamp, entry_user_id,\s
                 submitted_to_frpa108
          FROM (
              SELECT
                  o.OPENING_ID AS opening_id,
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
                  ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMRG.LATE_OFFSET_YEARS, 0) * 12)) AS regen_delay_date,
                  ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.EARLY_OFFSET_YEARS, 0) * 12)) AS early_free_growing_date,
                  ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS, 0) * 12)) AS late_free_growing_date,
                  o.UPDATE_TIMESTAMP AS update_timestamp,
                  o.ENTRY_USERID AS entry_user_id,
                  COALESCE(sra.SILV_RELIEF_APPLICATION_ID, 0) AS submitted_to_frpa108,
                  ROW_NUMBER() OVER (PARTITION BY o.OPENING_ID ORDER BY o.UPDATE_TIMESTAMP DESC) AS rn
              FROM THE.OPENING o
              LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)
              LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)
              LEFT JOIN THE.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)
              LEFT JOIN THE.SILV_RELIEF_APPLICATION sra ON (sra.ACTIVITY_TREATMENT_UNIT_ID = o.OPENING_ID AND sra.SILV_RELIEF_APPL_STATUS_CODE = 'APP')
              LEFT JOIN THE.STOCKING_STANDARD_UNIT ssu ON (ssu.OPENING_ID = o.OPENING_ID)
              LEFT JOIN THE.STOCKING_MILESTONE smrg ON (smrg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND SMRG.SILV_MILESTONE_TYPE_CODE = 'RG')
              LEFT JOIN THE.STOCKING_MILESTONE smfg ON (smfg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND smfg.SILV_MILESTONE_TYPE_CODE = 'FG')
          )
          WHERE rn = 1 AND OPENING_ID IN :openingIds""",
      countQuery = """
          SELECT count(o.OPENING_ID) as total
             FROM THE.OPENING o
             LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)
             LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)
             LEFT JOIN THE.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)
             LEFT JOIN THE.SILV_RELIEF_APPLICATION sra ON (sra.ACTIVITY_TREATMENT_UNIT_ID = o.OPENING_ID AND sra.SILV_RELIEF_APPL_STATUS_CODE = 'APP')
             LEFT JOIN THE.STOCKING_STANDARD_UNIT ssu ON (ssu.OPENING_ID = o.OPENING_ID)
             LEFT JOIN THE.STOCKING_MILESTONE smrg ON (smrg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND SMRG.SILV_MILESTONE_TYPE_CODE = 'RG')
             LEFT JOIN THE.STOCKING_MILESTONE smfg ON (smfg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND smfg.SILV_MILESTONE_TYPE_CODE = 'FG')
         WHERE o.OPENING_ID IN :openingIds""",
      nativeQuery = true
  )
  Page<SilvicultureSearchProjection> searchByOpeningIds(
      List<Long> openingIds,
      Pageable pageable
  );
}
