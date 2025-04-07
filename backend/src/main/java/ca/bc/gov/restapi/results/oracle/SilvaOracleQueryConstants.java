package ca.bc.gov.restapi.results.oracle;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SilvaOracleQueryConstants {

  public static final String SILVICULTURE_SEARCH_QUERY = """
      SELECT DISTINCT op.opening_id
          ,MAX(prime.forest_file_id) AS forest_file_id
          ,MAX(prime.cutting_permit_id) AS cutting_permit_id
          ,MAX(prime.timber_mark) AS timber_mark
          ,MAX(prime.cut_block_id) AS cut_block_id
          ,MAX((LPAD(op.mapsheet_grid,3) || mapsheet_letter || ' ' || LPAD(op.mapsheet_square,3,0) || ' ' || op.mapsheet_quad || DECODE(op.mapsheet_quad, NULL, NULL, '.') || op.mapsheet_sub_quad || ' ' || op.opening_number)) AS mapsheep_opening_id
          ,MAX(op.open_category_code) AS category
          ,MAX(op.opening_status_code) AS status
          ,MAX(prime.opening_gross_area) as opening_gross_area -- cboa and prime are the same
          ,MAX(to_char(prime.disturbance_start_date,'YYYY-MM-DD')) as disturbance_start_date
          ,MAX(ou.org_unit_code) as org_unit_code
          ,MAX(ou.org_unit_name) as org_unit_name
          ,MAX(ffc.client_number) as client_number
          ,MAX(ffc.client_locn_code) as client_location
          ,MAX(CASE WHEN sm.silv_milestone_type_code = 'RG' THEN to_char(sm.due_late_date, 'YYYY-MM-DD') ELSE NULL END) as regen_delay_date
          ,MAX(CASE WHEN sm.silv_milestone_type_code = 'FG' THEN to_char(sm.due_early_date, 'YYYY-MM-DD') ELSE NULL END) AS early_free_growing_date
          ,MAX(CASE WHEN sm.silv_milestone_type_code = 'FG' THEN to_char(sm.due_late_date, 'YYYY-MM-DD') ELSE NULL END) AS late_free_growing_date
          ,MAX(op.UPDATE_TIMESTAMP) as update_timestamp
          ,MAX(op.ENTRY_USERID) as entry_user_id
          ,MAX(COALESCE(sra.silv_relief_application_id, 0)) as submitted_to_frpa108
          ,MAX(op.opening_number) AS opening_number
      FROM opening op
        FULL OUTER JOIN cut_block_open_admin prime ON (prime.opening_id = op.opening_id AND prime.opening_prime_licence_ind = 'Y')-- DEFAULT
        FULL OUTER JOIN cut_block_open_admin cboa ON (cboa.opening_id = op.opening_id)-- Date is disturbance or client number, user client number, forest file, cutting permit, timber mark, cutblock, block status
        FULL OUTER JOIN stocking_standard_unit ssu ON (op.opening_id = ssu.opening_id) -- Regen OR FREE Growing date
        FULL OUTER JOIN stocking_milestone sm ON (ssu.stocking_standard_unit_id = sm.stocking_standard_unit_id)
        FULL OUTER JOIN org_unit ou ON (ou.org_unit_no = op.admin_district_no) -- This is ours
        FULL OUTER JOIN activity_treatment_unit atu ON (op.opening_id = atu.opening_id) -- This is ours
        FULL OUTER JOIN silv_relief_application sra ON (sra.activity_treatment_unit_id = atu.activity_treatment_unit_id and sra.silv_relief_appl_status_code = 'APP') -- This is ours
        FULL OUTER JOIN forest_file_client ffc ON (ffc.forest_file_id = prime.forest_file_id AND ffc.forest_file_client_type_code = 'A')
        FULL OUTER JOIN cut_block_client cbcr ON (cbcr.cut_block_client_type_code = 'R' AND cbcr.cb_skey = prime.cb_skey)
        FULL OUTER JOIN cut_block_client cbco ON (cbcr.cut_block_client_type_code = 'O' AND cbcr.cb_skey = prime.cb_skey)
        FULL OUTER JOIN cut_block cb ON (cb.cb_skey = cboa.cb_skey)
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
              sm.due_late_date IS NOT NULL AND
              sm.silv_milestone_type_code = 'RG' AND
              sm.due_late_date between TO_TIMESTAMP(:#{#filter.regenDelayDateStart},'YYYY-MM-DD') AND TO_TIMESTAMP(:#{#filter.regenDelayDateEnd},'YYYY-MM-DD')
          )
          )
          AND (
            (
                NVL(:#{#filter.freeGrowingDateStart},'NOVALUE') = 'NOVALUE' AND NVL(:#{#filter.freeGrowingDateEnd},'NOVALUE') = 'NOVALUE'
          )
            OR
            (
              sm.due_early_date IS NOT NULL AND
              sm.due_late_date IS NOT NULL AND
              sm.silv_milestone_type_code = 'FG' AND
              (
                sm.due_early_date between TO_TIMESTAMP(:#{#filter.freeGrowingDateStart},'YYYY-MM-DD') AND TO_TIMESTAMP(:#{#filter.freeGrowingDateEnd},'YYYY-MM-DD')
                OR
                sm.due_late_date between TO_TIMESTAMP(:#{#filter.freeGrowingDateStart},'YYYY-MM-DD') AND TO_TIMESTAMP(:#{#filter.freeGrowingDateEnd},'YYYY-MM-DD')
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
              op.update_timestamp between TO_DATE(:#{#filter.updateDateStart},'YYYY-MM-DD HH24:MI:SS') AND TO_DATE(:#{#filter.updateDateEnd},'YYYY-MM-DD HH24:MI:SS')
          )
          )
          AND (
              NVL(:#{#filter.cuttingPermitId},'NOVALUE') = 'NOVALUE' OR prime.cutting_permit_id = :#{#filter.cuttingPermitId}
          )
          AND (
              NVL(:#{#filter.cutBlockId},'NOVALUE') = 'NOVALUE' OR prime.cut_block_id = :#{#filter.cutBlockId}
          )
          AND (
              NVL(:#{#filter.timberMark},'NOVALUE') = 'NOVALUE' OR prime.timber_mark = :#{#filter.timberMark}
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
          GROUP BY op.opening_id
          ORDER BY mapsheep_opening_id
""";
  // The new line here is just to keep the pagination params on a new line

  public static final String SILVICULTURE_SEARCH_COUNT_QUERY = "SELECT count(opening_id) FROM ( " + SILVICULTURE_SEARCH_QUERY + " )";

  public static final String OPENING_TRENDS_QUERY = """
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
      ORDER BY year, month""";


  public static final String GET_OPENING_TOMBSTONE = """
      SELECT
        op.opening_id,
        (LPAD(op.mapsheet_grid,3) || mapsheet_letter || ' ' || LPAD(op.mapsheet_square,3,0) || ' ' || op.mapsheet_quad || DECODE(op.mapsheet_quad, NULL, NULL, '.') || op.mapsheet_sub_quad || ' ' || op.opening_number) AS opening_number,
        op.OPENING_STATUS_CODE AS opening_status,
        ou.ORG_UNIT_CODE,
        ou.ORG_UNIT_NAME,
        op.OPEN_CATEGORY_CODE as open_category,
        ffc.CLIENT_NUMBER AS client, -- load details FROM FCApi
        cboa.FOREST_FILE_ID AS file_id,
        cboa.CUT_BLOCK_ID,
        cboa.CUTTING_PERMIT_ID,
        cboa.TIMBER_MARK,
        op.max_allow_permnt_access_pct AS  max_allowed_access, --max allowed permanent ACCESS FROM inquiry
        cboa.OPENING_GROSS_AREA,
        op.ENTRY_USERID AS created_by,
        to_char(op.ENTRY_TIMESTAMP,'YYYY-MM-DD') AS created_on,
        op.UPDATE_TIMESTAMP AS last_updated_on, -- needs TO be ON ANY OF the related date
        to_char(cboa.DISTURBANCE_START_DATE,'YYYY-MM-DD') AS disturbance_start_date
      FROM OPENING op
      LEFT JOIN ORG_UNIT ou ON ou.ORG_UNIT_NO = op.ADMIN_DISTRICT_NO
      LEFT JOIN CUT_BLOCK_OPEN_ADMIN cboa ON cboa.OPENING_ID = op.OPENING_ID
      LEFT JOIN FOREST_FILE_CLIENT ffc ON (cboa.forest_file_id = ffc.forest_file_id AND ffc.forest_file_client_type_code = 'A')
      WHERE op.OPENING_ID = :openingId""";

  public static final String GET_OPENING_OVERVIEW_OPENING = """
      SELECT
          op.LICENSEE_OPENING_ID,
          pfu.FILE_TYPE_CODE AS tenure_type_code,
          ftc.DESCRIPTION AS tenure_type_name,
          pfu.MGMT_UNIT_TYPE AS management_unit_type_code,
          mutc.DESCRIPTION AS management_unit_type_name,
          pfu.MGMT_UNIT_ID AS management_unit_id,
          outsb.ORG_UNIT_CODE AS timber_sale_office_code,
          outsb.ORG_UNIT_NAME AS timber_sale_office_name
      FROM OPENING op
      LEFT JOIN OPENING_STATUS_CODE osc ON osc.OPENING_STATUS_CODE = op.OPENING_STATUS_CODE
      LEFT JOIN CUT_BLOCK_OPEN_ADMIN cboa ON cboa.OPENING_ID = op.OPENING_ID
      LEFT JOIN PROV_FOREST_USE pfu ON (cboa.FOREST_FILE_ID = pfu.FOREST_FILE_ID)
      LEFT JOIN ORG_UNIT outsb ON (outsb.ORG_UNIT_NO = (SELECT hs.BCTS_ORG_UNIT FROM THE.HARVEST_SALE hs WHERE cboa.FOREST_FILE_ID = hs.FOREST_FILE_ID FETCH FIRST 1 ROWS ONLY))
      LEFT JOIN FILE_TYPE_CODE ftc ON (pfu.FILE_TYPE_CODE = ftc.FILE_TYPE_CODE)
      LEFT JOIN MGMT_UNIT_TYPE_CODE mutc ON (pfu.MGMT_UNIT_TYPE = mutc.MGMT_UNIT_TYPE_CODE)
      WHERE op.OPENING_ID = :openingId""";

  public static final String GET_OPENING_OVERVIEW_MILESTONE = """
      SELECT
      	ssu.standards_unit_id,
          to_char(smph.DECLARED_DATE,'YYYY-MM-DD') AS post_harverst_declared_date,
          to_char(smrg.DECLARED_DATE,'YYYY-MM-DD') AS regen_declared_date,
          smrg.LATE_OFFSET_YEARS AS  regen_offset_years,
          to_char(smrg.DUE_LATE_DATE,'YYYY-MM-DD') AS regen_due_date,
          to_char(smfg.DECLARED_DATE,'YYYY-MM-DD') AS free_growing_declared_date,
          smfg.LATE_OFFSET_YEARS AS free_growing_offset_years,
          to_char(smfg.DUE_LATE_DATE,'YYYY-MM-DD') AS free_growing_due_date
      FROM OPENING op
      LEFT JOIN STOCKING_STANDARD_UNIT ssu ON ssu.OPENING_ID = op.OPENING_ID
      LEFT JOIN THE.STOCKING_MILESTONE smrg ON (smrg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND SMRG.SILV_MILESTONE_TYPE_CODE = 'RG')
      LEFT JOIN THE.STOCKING_MILESTONE smfg ON (smfg.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND smfg.SILV_MILESTONE_TYPE_CODE = 'FG')
      LEFT JOIN THE.STOCKING_MILESTONE smph ON (smph.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID AND smph.SILV_MILESTONE_TYPE_CODE = 'PH')
      WHERE op.OPENING_ID = :openingId
      ORDER BY ssu.ENTRY_TIMESTAMP
      FETCH FIRST ROW ONLY""";

  public static final String GET_COMMENTS = """
      SELECT
        sc.SILV_COMMENT_SOURCE_CODE as comment_source_code,
        scsc.DESCRIPTION AS comment_source_name,
        sc.SILV_COMMENT_TYPE_CODE as comment_type_code,
        sctc.DESCRIPTION AS comment_type_name,
        sc.COMMENT_TEXT
      FROM SILVICULTURE_COMMENT sc
      LEFT JOIN SILV_COMMENT_SOURCE_CODE scsc ON (sc.SILV_COMMENT_SOURCE_CODE = scsc.SILV_COMMENT_SOURCE_CODE )
      LEFT JOIN SILV_COMMENT_TYPE_CODE sctc ON (sc.SILV_COMMENT_TYPE_CODE = sctc.SILV_COMMENT_TYPE_CODE )
      -- This joins down below are the linkage between the data and the comment
      LEFT JOIN OPENING_COMMENT_LINK ocl ON (sc.SILVICULTURE_COMMENT_ID = ocl.SILVICULTURE_COMMENT_ID )
      --LEFT JOIN ACTIVITY_TU_COMMENT_LINK atcl ON (sc.SILVICULTURE_COMMENT_ID = atcl.SILVICULTURE_COMMENT_ID )
      --LEFT JOIN STOCKING_COMMENT_LINK scl ON (sc.SILVICULTURE_COMMENT_ID = scl.SILVICULTURE_COMMENT_ID )
      --LEFT JOIN STOCKING_MILESTONE_CMT_LINK smcl ON (sc.SILVICULTURE_COMMENT_ID = smcl.SILVICULTURE_COMMENT_ID )
      --LEFT JOIN SILV_PROJECT_COMMENT_LINK spcl ON (sc.SILVICULTURE_COMMENT_ID = spcl.SILVICULTURE_COMMENT_ID )
      WHERE
      ocl.OPENING_ID = :openingId
      --OR atcl.ACTIVITY_TREATMENT_UNIT_ID = :atuId
      --OR scl.STOCKING_STANDARD_UNIT_ID = :ssuId
      --OR (smcl.STOCKING_STANDARD_UNIT_ID = :ssuId AND smcl.SILV_MILESTONE_TYPE_CODE = :smtc)
      --OR spcl.SILVICULTURE_PROJECT_ID = :projectId
      ORDER BY COMMENT_DATE DESC""";
}
