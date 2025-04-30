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
        ,TRIM(LPAD(op.mapsheet_grid,3) || mapsheet_letter || ' ' || LPAD(op.mapsheet_square,3,0) || ' ' || op.mapsheet_quad || DECODE(op.mapsheet_quad, NULL, NULL, '.') || op.mapsheet_sub_quad || ' ' || op.opening_number) AS mapsheep_opening_id
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
             OR
             (
              TRIM((LPAD(op.mapsheet_grid,3) || mapsheet_letter || ' ' || LPAD(op.mapsheet_square,3,0) || ' ' || op.mapsheet_quad || DECODE(op.mapsheet_quad, NULL, NULL, '.') || op.mapsheet_sub_quad || ' ' || op.opening_number)) = TRIM(:#{#filter.mainSearchTerm})
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
              cboa.disturbance_start_date between TO_DATE(:#{#filter.disturbanceDateStart},'YYYY-MM-DD') AND TO_DATE(:#{#filter.disturbanceDateEnd},'YYYY-MM-DD')
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
              smrg.due_late_date between TO_DATE(:#{#filter.regenDelayDateStart},'YYYY-MM-DD') AND TO_DATE(:#{#filter.regenDelayDateEnd},'YYYY-MM-DD')
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
                smfg.due_early_date between TO_DATE(:#{#filter.freeGrowingDateStart},'YYYY-MM-DD') AND TO_DATE(:#{#filter.freeGrowingDateEnd},'YYYY-MM-DD')
                OR
                smfg.due_late_date between TO_DATE(:#{#filter.freeGrowingDateStart},'YYYY-MM-DD') AND TO_DATE(:#{#filter.freeGrowingDateEnd},'YYYY-MM-DD')
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


  public static final String GET_OPENING_TOMBSTONE = """
      SELECT
        op.opening_id,
        (LPAD(op.mapsheet_grid,3) || mapsheet_letter || ' ' || LPAD(op.mapsheet_square,3,0) || ' ' || op.mapsheet_quad || DECODE(op.mapsheet_quad, NULL, NULL, '.') || op.mapsheet_sub_quad || ' ' || op.opening_number) AS opening_number,
        op.OPENING_STATUS_CODE,
        osc.DESCRIPTION AS opening_status_name,
        ou.ORG_UNIT_CODE,
        ou.ORG_UNIT_NAME,
        op.OPEN_CATEGORY_CODE,
        occ.DESCRIPTION AS open_category_name,
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
      LEFT JOIN CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = op.OPENING_ID AND cboa.opening_prime_licence_ind = 'Y')
      LEFT JOIN FOREST_FILE_CLIENT ffc ON (cboa.forest_file_id = ffc.forest_file_id AND ffc.forest_file_client_type_code = 'A')
      LEFT JOIN OPEN_CATEGORY_CODE occ ON (occ.OPEN_CATEGORY_CODE = op.OPEN_CATEGORY_CODE)
      LEFT JOIN OPENING_STATUS_CODE osc ON osc.OPENING_STATUS_CODE = op.OPENING_STATUS_CODE
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
      LEFT JOIN CUT_BLOCK_OPEN_ADMIN cboa ON (cboa.OPENING_ID = op.OPENING_ID AND cboa.opening_prime_licence_ind = 'Y')
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

  public static final String GET_OPENING_SS = """
      SELECT
      	ssu.standards_unit_id AS stocking_standard_unit,
      	ssu.STOCKING_STANDARD_UNIT_ID AS ssid,
      	CASE WHEN NVL(sr.mof_default_standard_ind, 'N') = 'Y' THEN 'true' ELSE 'false' END AS default_mof,
      	CASE WHEN NVL(ssu.STOCKING_STANDARD_UNIT_ID, 0) = 0 THEN 'true' ELSE 'false' END AS manual_entry,
      	fsp.fsp_id,
      	ssu.net_area,
      	ssu.MAX_ALLOW_SOIL_DISTURBANCE_PCT AS soil_disturbance_percent,
      	se.BGC_ZONE_CODE AS bec_zone_code,
      	se.BGC_SUBZONE_CODE AS bec_subzone_code,
      	se.BGC_VARIANT AS bec_variant,
      	se.BGC_PHASE AS bec_phase,
      	se.BEC_SITE_SERIES AS bec_site_series,
      	se.BEC_SITE_TYPE AS bec_site_type,
      	se.BEC_SERAL AS bec_seral,
      	sr.REGEN_DELAY_OFFSET_YRS AS regen_delay,
      	sr.FREE_GROWING_LATE_OFFSET_YRS AS free_growing_late,
      	sr.FREE_GROWING_EARLY_OFFSET_YRS AS free_growing_early,
      	sr.ADDITIONAL_STANDARDS
      FROM OPENING op
      LEFT JOIN STOCKING_STANDARD_UNIT ssu ON ssu.OPENING_ID = op.OPENING_ID
      LEFT JOIN STOCKING_ECOLOGY se ON (se.OPENING_ID = op.OPENING_ID AND SE.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID)
      LEFT JOIN STANDARDS_REGIME sr ON (sr.STANDARDS_REGIME_ID = ssu.STANDARDS_REGIME_ID)
      LEFT JOIN FSP_STANDARDS_REGIME_XREF fspxref ON (fspxref.STANDARDS_REGIME_ID = ssu.STANDARDS_REGIME_ID)
      LEFT JOIN FOREST_STEWARDSHIP_PLAN fsp ON (fsp.FSP_ID = fspxref.FSP_ID AND fsp.fsp_amendment_number = fspxref.fsp_amendment_number)
      WHERE op.OPENING_ID = :openingId
      ORDER BY ssu.standards_unit_id""";

  public static final String GET_OPENING_SS_SPECIES = """
      SELECT
      	sls.SILV_TREE_SPECIES_CODE AS species_code,
      	stsc.DESCRIPTION AS species_name,
      	sls.MIN_HEIGHT AS min_height
      FROM OPENING op
      LEFT JOIN STOCKING_LAYER sl ON (sl.OPENING_ID = op.OPENING_ID)
      LEFT JOIN STOCKING_LAYER_SPECIES sls ON (sls.STOCKING_LAYER_ID = sl.STOCKING_LAYER_ID)
      LEFT JOIN SILV_TREE_SPECIES_CODE stsc ON (stsc.SILV_TREE_SPECIES_CODE = sls.SILV_TREE_SPECIES_CODE)
      WHERE op.OPENING_ID = :openingId AND sls.PREFERRED_IND = :preferred AND sl.STOCKING_STANDARD_UNIT_ID = :ssuId
      ORDER BY sls.SPECIES_ORDER""";

  public static final String GET_OPENING_SS_LAYER = """
      SELECT
      	sl.MIN_STOCKING_STANDARD AS min_wellspaced_trees,
      	sl.MIN_PREF_STOCKING_STANDARD AS min_preferred_wellspaced_trees,
      	sl.MIN_HORIZONTAL_DISTANCE AS min_horizontal_distance_wellspaced_trees,
      	sl.TARGET_STOCKING AS target_wellspaced_trees,
      	sl.RESIDUAL_BASAL_AREA AS min_residual_basal_area,
      	sl.MIN_POST_SPACING AS min_postspacing_density,
      	sl.MAX_POST_SPACING AS max_postspacing_density,
      	sl.MAX_CONIFER AS max_coniferous,
      	sl.HGHT_RELATIVE_TO_COMP AS height_relative_to_comp
      FROM OPENING op
      LEFT JOIN STOCKING_LAYER sl ON (sl.OPENING_ID = op.OPENING_ID)
      WHERE op.OPENING_ID = :openingId AND sl.STOCKING_STANDARD_UNIT_ID = :ssuId""";
}
