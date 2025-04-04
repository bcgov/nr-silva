package ca.bc.gov.restapi.results.oracle;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SilvaOracleQueryConstants {

  public static final String SILVICULTURE_SEARCH_SELECT = """
      SELECT DISTINCT op.opening_id
        ,cboa.forest_file_id AS forest_file_id
        ,cboa.cutting_permit_id AS cutting_permit_id
        ,cboa.timber_mark AS timber_mark
        ,cboa.cut_block_id AS cut_block_id
        ,(LPAD(op.mapsheet_grid,3) || mapsheet_letter || ' ' || LPAD(op.mapsheet_square,3,0) || ' ' || op.mapsheet_quad || DECODE(op.mapsheet_quad, NULL, NULL, '.') || op.mapsheet_sub_quad || ' ' || op.opening_number) AS mapsheep_opening_id
        ,op.open_category_code AS category
        ,op.opening_status_code AS status
        ,cboa.opening_gross_area as opening_gross_area
        ,to_char(cboa.disturbance_start_date,'YYYY-MM-DD') as disturbance_start_date
        ,ou.org_unit_code as org_unit_code
        ,ou.org_unit_name as org_unit_name
        ,ffc.client_number as client_number
        ,ffc.client_locn_code as client_location
        ,to_char(smrg.due_late_date, 'YYYY-MM-DD') as regen_delay_date
        ,to_char(smfg.due_early_date, 'YYYY-MM-DD') AS early_free_growing_date
        ,to_char(smfg.due_late_date, 'YYYY-MM-DD') AS late_free_growing_date
        ,op.UPDATE_TIMESTAMP as update_timestamp
        ,op.ENTRY_USERID as entry_user_id
        ,MAX(COALESCE(sra.silv_relief_application_id, 0)) OVER() as submitted_to_frpa108
        ,op.opening_number AS opening_number
      """;

  public static final String SILVICULTURE_SEARCH_FROM_JOIN = """
      FROM opening op
        LEFT JOIN cut_block_open_admin cboa ON (op.opening_id = cboa.opening_id AND cboa.opening_prime_licence_ind = 'Y') -- ideally, ALWAYS have a matching entry FOR opening_id, sometimes multiples, but NOT ALL op have cboa
        LEFT JOIN org_unit ou ON (op.admin_district_no = ou.org_unit_no)  -- ALWAYS have a matching entry FOR org_unit_no
        LEFT JOIN activity_treatment_unit atu ON (op.opening_id = atu.opening_id)
        LEFT JOIN stocking_milestone smrg ON (smrg.stocking_standard_unit_id = (SELECT ssu.stocking_standard_unit_id FROM stocking_standard_unit ssu WHERE op.opening_id = ssu.opening_id FETCH FIRST 1 ROWS ONLY) AND smrg.SILV_MILESTONE_TYPE_CODE = 'RG')
        LEFT JOIN stocking_milestone smfg ON (smfg.stocking_standard_unit_id = (SELECT ssu.stocking_standard_unit_id FROM stocking_standard_unit ssu WHERE op.opening_id = ssu.opening_id FETCH FIRST 1 ROWS ONLY) AND smrg.SILV_MILESTONE_TYPE_CODE = 'FG')
        LEFT JOIN silv_relief_application sra ON (atu.activity_treatment_unit_id = sra.activity_treatment_unit_id and sra.silv_relief_appl_status_code = 'APP') -- This is ours
        LEFT JOIN forest_file_client ffc ON (cboa.forest_file_id = ffc.forest_file_id AND ffc.forest_file_client_type_code = 'A')
        LEFT JOIN cut_block_client cbcr ON (cbcr.cut_block_client_type_code = 'R' AND cbcr.cb_skey = cboa.cb_skey)
        LEFT JOIN cut_block_client cbco ON (cbcr.cut_block_client_type_code = 'O' AND cbcr.cb_skey = cboa.cb_skey)
      """;

  public static final String SILVICULTURE_SEARCH_WHERE_CLAUSE = """
      WHERE
          (
            NVL(:#{#filter.mainSearchTerm},'NOVALUE') = 'NOVALUE' OR (
              REGEXP_LIKE(:#{#filter.mainSearchTerm}, '^\\d+$')
                  AND op.OPENING_ID = TO_NUMBER(:#{#filter.mainSearchTerm})
              ) OR (
                  op.OPENING_NUMBER = :#{#filter.mainSearchTerm} OR
                  cboa.TIMBER_MARK = :#{#filter.mainSearchTerm} OR
                  cboa.FOREST_FILE_ID = :#{#filter.mainSearchTerm}
              )
          )
          AND (
              'NOVALUE' in (:#{#filter.orgUnit}) OR ou.org_unit_code IN (:#{#filter.orgUnit})
          )
          AND (
              'NOVALUE' in (:#{#filter.category}) OR op.open_category_code IN (:#{#filter.category})
          )
          AND (
              'NOVALUE' in (:#{#filter.statusList}) OR op.opening_status_code IN (:#{#filter.statusList})
          )
          AND (
              NVL(:#{#filter.requestUserId},'NOVALUE') = 'NOVALUE' OR op.ENTRY_USERID = :#{#filter.requestUserId}
          )
          AND (
              NVL(:#{#filter.submittedToFrpa},'NO') = 'NO' OR (
              NVL(:#{#filter.submittedToFrpa},'NO') = 'YES' AND COALESCE(sra.silv_relief_application_id, 0) > 0
            )
          )
          AND (
            (
                NVL(:#{#filter.disturbanceDateStart},'NOVALUE') = 'NOVALUE' AND NVL(:#{#filter.disturbanceDateEnd},'NOVALUE') = 'NOVALUE'
          )
            OR
            (
              cboa.disturbance_start_date IS NOT NULL AND
              cboa.disturbance_start_date between TO_TIMESTAMP(:#{#filter.disturbanceDateStart},'YYYY-MM-DD') AND TO_TIMESTAMP(:#{#filter.disturbanceDateEnd},'YYYY-MM-DD')
          )
          )
          AND (
            (
                NVL(:#{#filter.regenDelayDateStart},'NOVALUE') = 'NOVALUE' AND NVL(:#{#filter.regenDelayDateEnd},'NOVALUE') = 'NOVALUE'
          )
            OR
            (
              smrg.due_late_date IS NOT NULL AND
              smrg.silv_milestone_type_code = 'RG' AND
              smrg.due_late_date between TO_TIMESTAMP(:#{#filter.regenDelayDateStart},'YYYY-MM-DD') AND TO_TIMESTAMP(:#{#filter.regenDelayDateEnd},'YYYY-MM-DD')
          )
          )
          AND (
            (
                NVL(:#{#filter.freeGrowingDateStart},'NOVALUE') = 'NOVALUE' AND NVL(:#{#filter.freeGrowingDateEnd},'NOVALUE') = 'NOVALUE'
          )
            OR
            (
              smfg.due_early_date IS NOT NULL AND
              smfg.due_late_date IS NOT NULL AND
              smfg.silv_milestone_type_code = 'FG' AND
              (
                smfg.due_early_date between TO_TIMESTAMP(:#{#filter.freeGrowingDateStart},'YYYY-MM-DD') AND TO_TIMESTAMP(:#{#filter.freeGrowingDateEnd},'YYYY-MM-DD')
                OR
                smfg.due_late_date between TO_TIMESTAMP(:#{#filter.freeGrowingDateStart},'YYYY-MM-DD') AND TO_TIMESTAMP(:#{#filter.freeGrowingDateEnd},'YYYY-MM-DD')
              )
          )
          )
          AND (
            (
                NVL(:#{#filter.updateDateStart},'NOVALUE') = 'NOVALUE' AND NVL(:#{#filter.updateDateEnd},'NOVALUE') = 'NOVALUE'
          )
            OR
            (
              op.update_timestamp IS NOT NULL AND
              op.update_timestamp between TO_DATE(:#{#filter.updateDateStart},'YYYY-MM-DD') AND TO_DATE(:#{#filter.updateDateEnd},'YYYY-MM-DD')
          )
          )
          AND (
              NVL(:#{#filter.cuttingPermitId},'NOVALUE') = 'NOVALUE' OR cboa.cutting_permit_id = :#{#filter.cuttingPermitId}
          )
          AND (
              NVL(:#{#filter.cutBlockId},'NOVALUE') = 'NOVALUE' OR cboa.cut_block_id = :#{#filter.cutBlockId}
          )
          AND (
              NVL(:#{#filter.timberMark},'NOVALUE') = 'NOVALUE' OR cboa.timber_mark = :#{#filter.timberMark}
          )
          AND (
              NVL(:#{#filter.clientLocationCode},'NOVALUE') = 'NOVALUE' OR COALESCE(cbcr.client_locn_code,cbco.client_locn_code,ffc.client_locn_code) = :#{#filter.clientLocationCode}
          )
          AND (
              NVL(:#{#filter.clientNumber},'NOVALUE') = 'NOVALUE' OR COALESCE(cbcr.client_number,cbco.client_number,ffc.client_number) = :#{#filter.clientNumber}
          )
          AND (
             0 in (:openingIds) OR op.OPENING_ID IN (:openingIds)
          )
      """;

  public static final String SILVICULTURE_SEARCH_CTE_SELECT = """
      SELECT
          opening_id,
          COUNT(*) OVER () AS total_count,
          forest_file_id,
          cutting_permit_id,
          timber_mark,
          cut_block_id,
          mapsheep_opening_id,
          category,
          status,
          opening_gross_area,
          disturbance_start_date,
          org_unit_code,
          org_unit_name,
          client_number,
          client_location,
          regen_delay_date,
          early_free_growing_date,
          late_free_growing_date,
          update_timestamp,
          entry_user_id,
          submitted_to_frpa108,
          opening_number
      """;

  public static final String PAGINATION = "OFFSET :page ROWS FETCH NEXT :size ROWS ONLY";

  public static final String SILVICULTURE_SEARCH_QUERY = SILVICULTURE_SEARCH_SELECT + SILVICULTURE_SEARCH_FROM_JOIN + SILVICULTURE_SEARCH_WHERE_CLAUSE;

  public static final String SILVICULTURE_SEARCH = "WITH silviculture_search AS ("+ SILVICULTURE_SEARCH_QUERY +")"+ SILVICULTURE_SEARCH_CTE_SELECT+" FROM silviculture_search ORDER BY opening_id DESC "+PAGINATION;

  public static final String OPENING_TRENDS_QUERY = """
      SELECT
          EXTRACT(YEAR FROM o.UPDATE_TIMESTAMP) AS year,
          EXTRACT(MONTH FROM o.UPDATE_TIMESTAMP) AS month,
          o.OPENING_STATUS_CODE AS status,
          COUNT(*) AS count
      FROM THE.OPENING o
      LEFT JOIN THE.ORG_UNIT ou ON (ou.ORG_UNIT_NO = o.ADMIN_DISTRICT_NO)
      WHERE
          o.UPDATE_TIMESTAMP BETWEEN TO_TIMESTAMP(:startDate || ' 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP(:endDate || ' 23:59:59','YYYY-MM-DD HH24:MI:SS')
          AND ('NOVALUE' IN (:statusList) OR o.OPENING_STATUS_CODE IN (:statusList))
          AND ('NOVALUE' IN (:orgUnitList) OR ou.ORG_UNIT_CODE IN (:orgUnitList))
      GROUP BY
          EXTRACT(YEAR FROM o.UPDATE_TIMESTAMP),
          EXTRACT(MONTH FROM o.UPDATE_TIMESTAMP),
          o.OPENING_STATUS_CODE
      ORDER BY year, month""";
}
