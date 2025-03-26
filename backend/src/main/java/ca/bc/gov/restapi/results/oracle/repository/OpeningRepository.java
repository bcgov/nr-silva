package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.oracle.dto.opening.OpeningSearchFiltersDto;
import ca.bc.gov.restapi.results.oracle.entity.OpeningEntity;
import ca.bc.gov.restapi.results.oracle.entity.OpeningTrendsProjection;
import ca.bc.gov.restapi.results.oracle.entity.SilvicultureSearchProjection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * This interface allows the service to fetch and save data into the database.
 */
@Repository
public interface OpeningRepository extends JpaRepository<OpeningEntity, Long> {

  @Query(
      value = """
          SELECT opening_id, opening_number, category, status, cutting_permit_id, timber_mark,
                 cut_block_id, opening_gross_area, disturbance_start_date, forest_file_id,
                 org_unit_code, org_unit_name, client_number, client_location, regen_delay_date,
                 early_free_growing_date, late_free_growing_date, update_timestamp, entry_user_id,
                 submitted_to_frpa108
          FROM (
              SELECT
                o.OPENING_ID AS opening_id,
                MAX(o.OPENING_NUMBER) AS opening_number,
                MAX(o.OPEN_CATEGORY_CODE) AS category,
                MAX(o.OPENING_STATUS_CODE) AS status,
                MAX(cboa.CUTTING_PERMIT_ID) AS cutting_permit_id,
                MAX(cboa.TIMBER_MARK) AS timber_mark,
                MAX(cboa.CUT_BLOCK_ID) AS cut_block_id,
                MAX(cboa.OPENING_GROSS_AREA) AS opening_gross_area,
                MAX(cboa.DISTURBANCE_START_DATE) AS disturbance_start_date,
                MAX(cboa.FOREST_FILE_ID) AS forest_file_id,
                MAX(ou.ORG_UNIT_CODE) AS org_unit_code,
                MAX(ou.ORG_UNIT_NAME) AS org_unit_name,
                MAX(res.CLIENT_NUMBER) AS client_number,
                MAX(res.CLIENT_LOCN_CODE) AS client_location,
                MAX(ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMRG.LATE_OFFSET_YEARS, 0) * 12))) AS regen_delay_date,
                MAX(ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.EARLY_OFFSET_YEARS, 0) * 12))) AS early_free_growing_date,
                MAX(ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS, 0) * 12))) AS late_free_growing_date,
                MAX(o.UPDATE_TIMESTAMP) AS update_timestamp,
                MAX(o.ENTRY_USERID) AS entry_user_id,
                MAX(COALESCE(sra.SILV_RELIEF_APPLICATION_ID, 0)) AS submitted_to_frpa108
              FROM THE.OPENING o
              LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)
              LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)
              LEFT JOIN THE.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)
              LEFT JOIN THE.ACTIVITY_TREATMENT_UNIT atu ON atu.OPENING_ID = o.OPENING_ID
              LEFT JOIN THE.SILV_RELIEF_APPLICATION sra ON (sra.ACTIVITY_TREATMENT_UNIT_ID = atu.ACTIVITY_TREATMENT_UNIT_ID AND sra.SILV_RELIEF_APPL_STATUS_CODE = 'APP')
              LEFT JOIN THE.STOCKING_STANDARD_UNIT ssu ON (ssu.OPENING_ID = o.OPENING_ID)
              LEFT JOIN THE.STOCKING_MILESTONE smrg ON (smrg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND SMRG.SILV_MILESTONE_TYPE_CODE = 'RG')
              LEFT JOIN THE.STOCKING_MILESTONE smfg ON (smfg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND smfg.SILV_MILESTONE_TYPE_CODE = 'FG')
              GROUP BY o.OPENING_ID
          )
          WHERE (
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
                'NOVALUE' in (:#{#filter.orgUnit}) OR ORG_UNIT_CODE IN (:#{#filter.orgUnit})
            )
            AND (
                'NOVALUE' in (:#{#filter.category}) OR CATEGORY IN (:#{#filter.category})
            )
            AND (
                'NOVALUE' in (:#{#filter.statusList}) OR STATUS IN (:#{#filter.statusList})
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
            )
            AND (
                NVL(:#{#filter.clientLocationCode},'NOVALUE') = 'NOVALUE' OR client_location = :#{#filter.clientLocationCode}
            )
            AND (
               NVL(:#{#filter.clientNumber},'NOVALUE') = 'NOVALUE' OR client_number = :#{#filter.clientNumber}
            )""",
      countQuery = """
          SELECT count(opening_id) as total
             FROM (
              SELECT
                o.OPENING_ID AS opening_id,
                MAX(o.OPENING_NUMBER) AS opening_number,
                MAX(o.OPEN_CATEGORY_CODE) AS category,
                MAX(o.OPENING_STATUS_CODE) AS status,
                MAX(cboa.CUTTING_PERMIT_ID) AS cutting_permit_id,
                MAX(cboa.TIMBER_MARK) AS timber_mark,
                MAX(cboa.CUT_BLOCK_ID) AS cut_block_id,
                MAX(cboa.OPENING_GROSS_AREA) AS opening_gross_area,
                MAX(cboa.DISTURBANCE_START_DATE) AS disturbance_start_date,
                MAX(cboa.FOREST_FILE_ID) AS forest_file_id,
                MAX(ou.ORG_UNIT_CODE) AS org_unit_code,
                MAX(ou.ORG_UNIT_NAME) AS org_unit_name,
                MAX(res.CLIENT_NUMBER) AS client_number,
                MAX(res.CLIENT_LOCN_CODE) AS client_location,
                MAX(ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMRG.LATE_OFFSET_YEARS, 0) * 12))) AS regen_delay_date,
                MAX(ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.EARLY_OFFSET_YEARS, 0) * 12))) AS early_free_growing_date,
                MAX(ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS, 0) * 12))) AS late_free_growing_date,
                MAX(o.UPDATE_TIMESTAMP) AS update_timestamp,
                MAX(o.ENTRY_USERID) AS entry_user_id,
                MAX(COALESCE(sra.SILV_RELIEF_APPLICATION_ID, 0)) AS submitted_to_frpa108
              FROM THE.OPENING o
              LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)
              LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)
              LEFT JOIN THE.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)
              LEFT JOIN THE.ACTIVITY_TREATMENT_UNIT atu ON atu.OPENING_ID = o.OPENING_ID
              LEFT JOIN THE.SILV_RELIEF_APPLICATION sra ON (sra.ACTIVITY_TREATMENT_UNIT_ID = atu.ACTIVITY_TREATMENT_UNIT_ID AND sra.SILV_RELIEF_APPL_STATUS_CODE = 'APP')
              LEFT JOIN THE.STOCKING_STANDARD_UNIT ssu ON (ssu.OPENING_ID = o.OPENING_ID)
              LEFT JOIN THE.STOCKING_MILESTONE smrg ON (smrg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND SMRG.SILV_MILESTONE_TYPE_CODE = 'RG')
              LEFT JOIN THE.STOCKING_MILESTONE smfg ON (smfg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND smfg.SILV_MILESTONE_TYPE_CODE = 'FG')
            WHERE (
             NVL(:#{#filter.mainSearchTerm},'NOVALUE') = 'NOVALUE' OR (
                 REGEXP_LIKE(:#{#filter.mainSearchTerm}, '^\\d+$')
                 AND o.OPENING_ID = TO_NUMBER(:#{#filter.mainSearchTerm})
               ) OR (
                   o.OPENING_NUMBER = :#{#filter.mainSearchTerm} OR
                   cboa.FOREST_FILE_ID = :#{#filter.mainSearchTerm}
               )
             )
             AND (
                 'NOVALUE' in (:#{#filter.orgUnit}) OR ou.ORG_UNIT_CODE IN (:#{#filter.orgUnit})
             )
             AND (
                 'NOVALUE' in (:#{#filter.category}) OR o.OPEN_CATEGORY_CODE IN (:#{#filter.category})
             )
             AND (
                 'NOVALUE' in (:#{#filter.statusList}) OR o.OPENING_STATUS_CODE IN (:#{#filter.statusList})
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
             )
             AND (
                 NVL(:#{#filter.clientLocationCode},'NOVALUE') = 'NOVALUE' OR res.CLIENT_LOCN_CODE = :#{#filter.clientLocationCode}
             )
             AND (
                 NVL(:#{#filter.clientNumber},'NOVALUE') = 'NOVALUE' OR res.CLIENT_NUMBER = :#{#filter.clientNumber}
             )
            GROUP BY o.OPENING_ID
          )""",
      nativeQuery = true
  )
  Page<SilvicultureSearchProjection> searchBy(
      OpeningSearchFiltersDto filter,
      Pageable pageable
  );

  @Query(
      value = """
          SELECT opening_id, opening_number, category, status, cutting_permit_id, timber_mark,
                 cut_block_id, opening_gross_area, disturbance_start_date, forest_file_id,
                 org_unit_code, org_unit_name, client_number, client_location, regen_delay_date,
                 early_free_growing_date, late_free_growing_date, update_timestamp, entry_user_id,
                 submitted_to_frpa108
          FROM (
              SELECT
                o.OPENING_ID AS opening_id,
                MAX(o.OPENING_NUMBER) AS opening_number,
                MAX(o.OPEN_CATEGORY_CODE) AS category,
                MAX(o.OPENING_STATUS_CODE) AS status,
                MAX(cboa.CUTTING_PERMIT_ID) AS cutting_permit_id,
                MAX(cboa.TIMBER_MARK) AS timber_mark,
                MAX(cboa.CUT_BLOCK_ID) AS cut_block_id,
                MAX(cboa.OPENING_GROSS_AREA) AS opening_gross_area,
                MAX(cboa.DISTURBANCE_START_DATE) AS disturbance_start_date,
                MAX(cboa.FOREST_FILE_ID) AS forest_file_id,
                MAX(ou.ORG_UNIT_CODE) AS org_unit_code,
                MAX(ou.ORG_UNIT_NAME) AS org_unit_name,
                MAX(res.CLIENT_NUMBER) AS client_number,
                MAX(res.CLIENT_LOCN_CODE) AS client_location,
                MAX(ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMRG.LATE_OFFSET_YEARS, 0) * 12))) AS regen_delay_date,
                MAX(ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.EARLY_OFFSET_YEARS, 0) * 12))) AS early_free_growing_date,
                MAX(ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS, 0) * 12))) AS late_free_growing_date,
                MAX(o.UPDATE_TIMESTAMP) AS update_timestamp,
                MAX(o.ENTRY_USERID) AS entry_user_id,
                MAX(COALESCE(sra.SILV_RELIEF_APPLICATION_ID, 0)) AS submitted_to_frpa108
              FROM THE.OPENING o
              LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)
              LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)
              LEFT JOIN THE.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)
              LEFT JOIN THE.ACTIVITY_TREATMENT_UNIT atu ON atu.OPENING_ID = o.OPENING_ID
              LEFT JOIN THE.SILV_RELIEF_APPLICATION sra ON (sra.ACTIVITY_TREATMENT_UNIT_ID = atu.ACTIVITY_TREATMENT_UNIT_ID AND sra.SILV_RELIEF_APPL_STATUS_CODE = 'APP')
              LEFT JOIN THE.STOCKING_STANDARD_UNIT ssu ON (ssu.OPENING_ID = o.OPENING_ID)
              LEFT JOIN THE.STOCKING_MILESTONE smrg ON (smrg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND SMRG.SILV_MILESTONE_TYPE_CODE = 'RG')
              LEFT JOIN THE.STOCKING_MILESTONE smfg ON (smfg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND smfg.SILV_MILESTONE_TYPE_CODE = 'FG')
              GROUP BY o.OPENING_ID
          )
          WHERE opening_id IN :openingIds""",
      countQuery = """
          SELECT count(opening_id) as total
          FROM (
              SELECT
                o.OPENING_ID AS opening_id,
                MAX(o.OPENING_NUMBER) AS opening_number,
                MAX(o.OPEN_CATEGORY_CODE) AS category,
                MAX(o.OPENING_STATUS_CODE) AS status,
                MAX(cboa.CUTTING_PERMIT_ID) AS cutting_permit_id,
                MAX(cboa.TIMBER_MARK) AS timber_mark,
                MAX(cboa.CUT_BLOCK_ID) AS cut_block_id,
                MAX(cboa.OPENING_GROSS_AREA) AS opening_gross_area,
                MAX(cboa.DISTURBANCE_START_DATE) AS disturbance_start_date,
                MAX(cboa.FOREST_FILE_ID) AS forest_file_id,
                MAX(ou.ORG_UNIT_CODE) AS org_unit_code,
                MAX(ou.ORG_UNIT_NAME) AS org_unit_name,
                MAX(res.CLIENT_NUMBER) AS client_number,
                MAX(res.CLIENT_LOCN_CODE) AS client_location,
                MAX(ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMRG.LATE_OFFSET_YEARS, 0) * 12))) AS regen_delay_date,
                MAX(ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.EARLY_OFFSET_YEARS, 0) * 12))) AS early_free_growing_date,
                MAX(ADD_MONTHS(cboa.DISTURBANCE_START_DATE, (COALESCE(SMFG.LATE_OFFSET_YEARS, 0) * 12))) AS late_free_growing_date,
                MAX(o.UPDATE_TIMESTAMP) AS update_timestamp,
                MAX(o.ENTRY_USERID) AS entry_user_id,
                MAX(COALESCE(sra.SILV_RELIEF_APPLICATION_ID, 0)) AS submitted_to_frpa108
              FROM THE.OPENING o
              LEFT JOIN THE.CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = o.OPENING_ID)
              LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)
              LEFT JOIN THE.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)
              LEFT JOIN THE.ACTIVITY_TREATMENT_UNIT atu ON atu.OPENING_ID = o.OPENING_ID
              LEFT JOIN THE.SILV_RELIEF_APPLICATION sra ON (sra.ACTIVITY_TREATMENT_UNIT_ID = atu.ACTIVITY_TREATMENT_UNIT_ID AND sra.SILV_RELIEF_APPL_STATUS_CODE = 'APP')
              LEFT JOIN THE.STOCKING_STANDARD_UNIT ssu ON (ssu.OPENING_ID = o.OPENING_ID)
              LEFT JOIN THE.STOCKING_MILESTONE smrg ON (smrg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND SMRG.SILV_MILESTONE_TYPE_CODE = 'RG')
              LEFT JOIN THE.STOCKING_MILESTONE smfg ON (smfg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND smfg.SILV_MILESTONE_TYPE_CODE = 'FG')
              WHERE o.OPENING_ID IN :openingIds
              GROUP BY o.OPENING_ID
          )""",
      nativeQuery = true
  )
  Page<SilvicultureSearchProjection> searchByOpeningIds(
      List<Long> openingIds,
      Pageable pageable
  );

  @Query(
      nativeQuery = true,
      value = """
          SELECT
              EXTRACT(YEAR FROM GREATEST(o.ENTRY_TIMESTAMP,o.UPDATE_TIMESTAMP)) AS year,
              EXTRACT(MONTH FROM GREATEST(o.ENTRY_TIMESTAMP,o.UPDATE_TIMESTAMP)) AS month,
              o.OPENING_STATUS_CODE AS status,
              COUNT(*) AS count
          FROM THE.OPENING o
          LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)
          LEFT JOIN THE.RESULTS_ELECTRONIC_SUBMISSION res ON (res.RESULTS_SUBMISSION_ID = o.RESULTS_SUBMISSION_ID)
          WHERE
              (
                  o.ENTRY_TIMESTAMP BETWEEN TO_TIMESTAMP(:startDate, 'YYYY-MM-DD')\s
                  AND TO_TIMESTAMP(:endDate, 'YYYY-MM-DD')
                  OR o.UPDATE_TIMESTAMP BETWEEN TO_TIMESTAMP(:startDate, 'YYYY-MM-DD')\s
                  AND TO_TIMESTAMP(:endDate, 'YYYY-MM-DD')
              )
              AND ('NOVALUE' IN (:statusList) OR o.OPENING_STATUS_CODE IN (:statusList))
              AND ('NOVALUE' IN (:orgUnitList) OR ou.ORG_UNIT_CODE IN (:orgUnitList))
          GROUP BY
              EXTRACT(YEAR FROM GREATEST(o.ENTRY_TIMESTAMP,o.UPDATE_TIMESTAMP)),
              EXTRACT(MONTH FROM GREATEST(o.ENTRY_TIMESTAMP,o.UPDATE_TIMESTAMP)),
              o.OPENING_STATUS_CODE
          ORDER BY year, month""")
  List<OpeningTrendsProjection> getOpeningTrends(
      String startDate,
      String endDate,
      List<String> statusList,
      List<String> orgUnitList
  );
}
