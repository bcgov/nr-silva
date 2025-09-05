package ca.bc.gov.restapi.results.oracle;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SilvaOracleQueryConstants {

  public static final String SILVICULTURE_SEARCH_SELECT =
      """
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

  public static final String SILVICULTURE_SEARCH_FROM_JOIN =
      """
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

  public static final String SILVICULTURE_SEARCH_WHERE_CLAUSE =
      """
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

  public static final String SILVICULTURE_SEARCH_CTE_SELECT =
      """
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

  public static final String SILVICULTURE_SEARCH_QUERY =
      SILVICULTURE_SEARCH_SELECT + SILVICULTURE_SEARCH_FROM_JOIN + SILVICULTURE_SEARCH_WHERE_CLAUSE;

  public static final String SILVICULTURE_SEARCH =
      "WITH silviculture_search AS ("
          + SILVICULTURE_SEARCH_QUERY
          + ")"
          + SILVICULTURE_SEARCH_CTE_SELECT
          + " FROM silviculture_search ORDER BY opening_id DESC "
          + PAGINATION;

  public static final String OPENING_TRENDS_QUERY =
      """
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

  public static final String GET_OPENING_TOMBSTONE =
      """
      SELECT
        op.opening_id,
        (LPAD(op.mapsheet_grid,3) || mapsheet_letter || ' ' || LPAD(op.mapsheet_square,3,0) || ' ' || op.mapsheet_quad || DECODE(op.mapsheet_quad, NULL, NULL, '.') || op.mapsheet_sub_quad || ' ' || op.opening_number) AS opening_number,
        op.OPENING_STATUS_CODE,
        osc.DESCRIPTION AS opening_status_name,
        ou.ORG_UNIT_NO AS org_unit_number,
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

  public static final String GET_OPENING_OVERVIEW_OPENING =
      """
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

  public static final String GET_OPENING_OVERVIEW_MILESTONE =
      """
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

  public static final String GET_COMMENTS =
      """
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
      LEFT JOIN ACTIVITY_TU_COMMENT_LINK atcl ON (sc.SILVICULTURE_COMMENT_ID = atcl.SILVICULTURE_COMMENT_ID )
      LEFT JOIN STOCKING_COMMENT_LINK scl ON (sc.SILVICULTURE_COMMENT_ID = scl.SILVICULTURE_COMMENT_ID )
      LEFT JOIN STOCKING_MILESTONE_CMT_LINK smcl ON (sc.SILVICULTURE_COMMENT_ID = smcl.SILVICULTURE_COMMENT_ID )
      LEFT JOIN SILV_PROJECT_COMMENT_LINK spcl ON (sc.SILVICULTURE_COMMENT_ID = spcl.SILVICULTURE_COMMENT_ID )
      WHERE
      ocl.OPENING_ID = :openingId
      OR atcl.ACTIVITY_TREATMENT_UNIT_ID = :atuId
      OR scl.STOCKING_STANDARD_UNIT_ID = :ssuId
      OR smcl.STOCKING_STANDARD_UNIT_ID = :ssuMId
      OR spcl.SILVICULTURE_PROJECT_ID = :projectId
      ORDER BY COMMENT_DATE DESC""";

  public static final String GET_OPENING_SS =
      """
      SELECT
      	ssu.standards_unit_id AS stocking_standard_unit,
      	ssu.STOCKING_STANDARD_UNIT_ID AS ssuid,
        ssu.STANDARDS_REGIME_ID as srid,
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
      FROM STOCKING_STANDARD_UNIT ssu
      LEFT JOIN STOCKING_ECOLOGY se ON (se.OPENING_ID = ssu.OPENING_ID AND SE.STOCKING_STANDARD_UNIT_ID = ssu.STOCKING_STANDARD_UNIT_ID)
      LEFT JOIN STANDARDS_REGIME sr ON (sr.STANDARDS_REGIME_ID = ssu.STANDARDS_REGIME_ID)
      LEFT JOIN FSP_STANDARDS_REGIME_XREF fspxref ON (fspxref.STANDARDS_REGIME_ID = ssu.STANDARDS_REGIME_ID)
      LEFT JOIN FOREST_STEWARDSHIP_PLAN fsp ON (fsp.FSP_ID = fspxref.FSP_ID AND fsp.fsp_amendment_number = fspxref.fsp_amendment_number)
      WHERE ssu.OPENING_ID = :openingId
      ORDER BY ssu.standards_unit_id""";

  public static final String GET_OPENING_SS_SPECIES =
      """
      SELECT
        sl.STOCKING_LAYER_CODE as layer_code,
      	sls.SILV_TREE_SPECIES_CODE AS species_code,
      	stsc.DESCRIPTION AS species_name,
      	sls.MIN_HEIGHT AS min_height
      FROM STOCKING_LAYER sl
      LEFT JOIN STOCKING_LAYER_SPECIES sls ON (sls.STOCKING_LAYER_ID = sl.STOCKING_LAYER_ID)
      LEFT JOIN SILV_TREE_SPECIES_CODE stsc ON (stsc.SILV_TREE_SPECIES_CODE = sls.SILV_TREE_SPECIES_CODE)
      WHERE sl.OPENING_ID = :openingId AND sls.PREFERRED_IND = :preferred AND sl.STOCKING_STANDARD_UNIT_ID = :ssuId
      ORDER BY sls.SPECIES_ORDER""";

  public static final String GET_OPENING_SS_LAYER =
      """
      SELECT
        sl.STOCKING_LAYER_CODE AS layer_code,
        slc.DESCRIPTION AS layer_name,
        sl.MIN_STOCKING_STANDARD AS min_wellspaced_trees,
        sl.MIN_PREF_STOCKING_STANDARD AS min_preferred_wellspaced_trees,
        sl.MIN_HORIZONTAL_DISTANCE AS min_horizontal_distance_wellspaced_trees,
        sl.TARGET_STOCKING AS target_wellspaced_trees,
        sl.RESIDUAL_BASAL_AREA AS min_residual_basal_area,
        sl.MIN_POST_SPACING AS min_postspacing_density,
        sl.MAX_POST_SPACING AS max_postspacing_density,
        sl.MAX_CONIFER AS max_coniferous,
        sl.HGHT_RELATIVE_TO_COMP AS height_relative_to_comp
      FROM STOCKING_LAYER sl
      LEFT JOIN STOCKING_LAYER_CODE slc ON sl.STOCKING_LAYER_CODE = slc.STOCKING_LAYER_CODE
      WHERE sl.OPENING_ID = :openingId AND sl.STOCKING_STANDARD_UNIT_ID = :ssuId
      ORDER BY sl.STOCKING_LAYER_CODE DESC""";

  public static final String GET_OPENING_SS_MILESTONES =
      """
          SELECT
            sm.STOCKING_STANDARD_UNIT_ID AS ssuid,
            MAX(CASE WHEN sm.SILV_MILESTONE_TYPE_CODE = 'PH' THEN TO_CHAR(sm.DECLARED_DATE, 'YYYY-MM-DD') END) AS post_harvest_declared_date,
            MAX(CASE WHEN sm.SILV_MILESTONE_TYPE_CODE = 'RG' THEN TO_CHAR(sm.DECLARED_DATE, 'YYYY-MM-DD') END) AS regen_declared_date,
            MAX(CASE WHEN sm.SILV_MILESTONE_TYPE_CODE = 'RG' THEN sm.LATE_OFFSET_YEARS END) AS regen_offset_years,
            MAX(CASE WHEN sm.SILV_MILESTONE_TYPE_CODE = 'RG' THEN TO_CHAR(sm.DUE_LATE_DATE, 'YYYY-MM-DD') END) AS regen_due_date,
            MAX(CASE WHEN sm.SILV_MILESTONE_TYPE_CODE = 'NR' THEN TO_CHAR(sm.DECLARED_DATE, 'YYYY-MM-DD') END) AS no_regen_declared_date,
            MAX(CASE WHEN sm.SILV_MILESTONE_TYPE_CODE = 'NR' THEN sm.LATE_OFFSET_YEARS END) AS no_regen_offset_years,
            MAX(CASE WHEN sm.SILV_MILESTONE_TYPE_CODE = 'NR' THEN TO_CHAR(sm.DUE_LATE_DATE, 'YYYY-MM-DD') END) AS no_regen_due_date,
            MAX(CASE WHEN sm.SILV_MILESTONE_TYPE_CODE = 'FG' THEN TO_CHAR(sm.DECLARED_DATE, 'YYYY-MM-DD') END) AS free_growing_declared_date,
            MAX(CASE WHEN sm.SILV_MILESTONE_TYPE_CODE = 'FG' THEN sm.LATE_OFFSET_YEARS END) AS free_growing_offset_years,
            MAX(CASE WHEN sm.SILV_MILESTONE_TYPE_CODE = 'FG' THEN TO_CHAR(sm.DUE_LATE_DATE, 'YYYY-MM-DD') END) AS free_growing_due_date,
            CASE
              WHEN MAX(CASE WHEN sm.SILV_MILESTONE_TYPE_CODE = 'NR' THEN 1 ELSE 0 END) = 1 THEN 'true'
              ELSE 'false'
            END AS no_regen_indicated,
            CASE
              WHEN MAX(CASE WHEN sm.EXTENT_FEASIBLE_DECLARED_IND = 'N' THEN 0 ELSE 1 END) = 1 THEN 'true'
              ELSE 'false'
            END AS extent_declared
          FROM THE.STOCKING_MILESTONE sm
          WHERE sm.STOCKING_STANDARD_UNIT_ID = :ssuId
          GROUP BY sm.STOCKING_STANDARD_UNIT_ID""";

  public static final String GET_OPENING_SS_NOTIFICATIONS =
      """
    SELECT
      ssu.STOCKING_STANDARD_UNIT_ID,
      ssu.STANDARDS_UNIT_ID,
      sm.SILV_MILESTONE_TYPE_CODE,
      sm.DUE_LATE_DATE,
      CASE
        WHEN sm.DUE_LATE_DATE < SYSDATE AND sm.SILV_MILESTONE_TYPE_CODE = 'FG' THEN 'ERROR'
        WHEN sm.DUE_LATE_DATE < SYSDATE AND sm.SILV_MILESTONE_TYPE_CODE = 'RG' THEN 'INFO'
        WHEN sm.DUE_LATE_DATE BETWEEN SYSDATE AND ADD_MONTHS(SYSDATE, 12) THEN 'WARNING'
      END AS NOTIFICATION_TYPE
    FROM THE.STOCKING_STANDARD_UNIT ssu
    JOIN THE.STOCKING_MILESTONE sm
      ON ssu.STOCKING_STANDARD_UNIT_ID = sm.STOCKING_STANDARD_UNIT_ID
    WHERE ssu.OPENING_ID = :openingId
      AND (sm.DECLARE_IND IS NULL OR sm.DECLARE_IND = 'N')
      AND (
        sm.DUE_LATE_DATE < SYSDATE
        OR sm.DUE_LATE_DATE BETWEEN SYSDATE AND ADD_MONTHS(SYSDATE, 12)
      )
      AND (sm.SILV_MILESTONE_TYPE_CODE = 'FG' OR sm.SILV_MILESTONE_TYPE_CODE = 'RG')
    """;

  public static final String GET_OPENING_ACTIVITIES_DISTURBANCE =
      """
      SELECT
      	atu.ACTIVITY_TREATMENT_UNIT_ID AS atu_id,
      	atu.DISTURBANCE_CODE AS disturbance_code,
      	dc.DESCRIPTION AS disturbance_name,
      	atu.SILV_SYSTEM_CODE AS system_code,
      	ssc.DESCRIPTION AS system_name,
      	atu.SILV_SYSTEM_VARIANT_CODE AS variant_code,
      	ssvc.DESCRIPTION AS variant_name,
      	atu.SILV_CUT_PHASE_CODE AS cut_phase_code,
      	scpc.DESCRIPTION AS cut_phase_name,
      	atu.TREATMENT_AMOUNT AS disturbance_area,
      	to_char(atu.UPDATE_TIMESTAMP,'YYYY-MM-DD') AS last_update,
      	to_char(atu.ATU_START_DATE,'YYYY-MM-DD') AS start_date,
      	to_char(atu.ATU_COMPLETION_DATE,'YYYY-MM-DD') AS end_date,
      	atu.ACTIVITY_LICENSEE_ID AS licensee_activity_id,
      	ffc.CLIENT_NUMBER AS disturbance_location_client,
      	ffc.CLIENT_LOCN_CODE AS disturbance_location_code,
      	cboa.FOREST_FILE_ID AS licence_number,
      	cboa.CUTTING_PERMIT_ID AS cutting_permit,
      	cboa.CUT_BLOCK_ID AS cut_block
      FROM ACTIVITY_TREATMENT_UNIT atu
      LEFT JOIN DISTURBANCE_CODE dc ON atu.DISTURBANCE_CODE = dc.DISTURBANCE_CODE
      LEFT JOIN SILV_SYSTEM_CODE ssc ON ssc.SILV_SYSTEM_CODE = atu.SILV_SYSTEM_CODE
      LEFT JOIN SILV_SYSTEM_VARIANT_CODE ssvc ON ssvc.SILV_SYSTEM_VARIANT_CODE = atu.SILV_SYSTEM_VARIANT_CODE
      LEFT JOIN SILV_CUT_PHASE_CODE scpc ON scpc.SILV_CUT_PHASE_CODE = atu.SILV_CUT_PHASE_CODE
      LEFT JOIN CUT_BLOCK_OPEN_ADMIN cboa ON cboa.CUT_BLOCK_OPEN_ADMIN_ID = atu.CUT_BLOCK_OPEN_ADMIN_ID
      LEFT JOIN FOREST_FILE_CLIENT ffc ON (ffc.FOREST_FILE_ID = cboa.FOREST_FILE_ID AND ffc.FOREST_FILE_CLIENT_TYPE_CODE = 'A')
      WHERE atu.SILV_BASE_CODE = 'DN' AND atu.OPENING_ID = :openingId""";

  public static final String GET_OPENING_ACTIVITIES_DISTURBANCE_COUNT =
      """
      SELECT count(atu.ACTIVITY_TREATMENT_UNIT_ID)
        FROM ACTIVITY_TREATMENT_UNIT atu
        WHERE atu.SILV_BASE_CODE = 'DN' AND atu.OPENING_ID = :openingId""";

  public static final String GET_OPENING_ACTIVITIES_ACTIVITIES =
      """
      SELECT
      	atu.ACTIVITY_TREATMENT_UNIT_ID AS atu_id,
      	CASE
              WHEN atu.ATU_COMPLETION_DATE IS NOT NULL THEN 'CPT'
              WHEN atu.PLANNED_DATE IS NOT NULL AND atu.ATU_COMPLETION_DATE IS NULL AND atu.PLANNED_DATE >= trunc(SYSDATE) THEN 'PLN'
              WHEN atu.PLANNED_DATE IS NOT NULL AND atu.ATU_COMPLETION_DATE IS NULL AND atu.PLANNED_DATE < trunc(SYSDATE) THEN 'OVD'
              ELSE 'INV'
          END AS status_code,
      	atu.SILV_BASE_CODE AS base_code,
      	sbc.DESCRIPTION AS base_name,
      	atu.SILV_TECHNIQUE_CODE AS tech_code,
      	stc.DESCRIPTION AS tech_name,
      	ATU.SILV_METHOD_CODE AS method_code,
      	smc.DESCRIPTION AS method_name,
      	atu.SILV_OBJECTIVE_CODE_1 AS objective1_code,
      	soc1.DESCRIPTION AS objective1_name,
      	atu.SILV_OBJECTIVE_CODE_2 AS objective2_code,
      	soc2.DESCRIPTION AS objective2_name,
      	atu.SILV_OBJECTIVE_CODE_3 AS objective3_code,
      	soc3.DESCRIPTION AS objective3_name,
      	atu.TREATMENT_AMOUNT AS area,
      	ATU.SILV_FUND_SRCE_CODE AS funding_code,
      	sfsc.DESCRIPTION AS funding_name,
      	ATU.SILVICULTURE_PROJECT_ID AS project_id,
      	to_char(atu.UPDATE_TIMESTAMP,'YYYY-MM-DD') AS last_update,
      	to_char(atu.PLANNED_DATE,'YYYY-MM-DD') AS planned_date,
      	to_char(atu.ATU_COMPLETION_DATE,'YYYY-MM-DD') AS end_date
      FROM ACTIVITY_TREATMENT_UNIT atu
      LEFT JOIN SILV_BASE_CODE sbc ON sbc.SILV_BASE_CODE = atu.SILV_BASE_CODE
      LEFT JOIN SILV_TECHNIQUE_CODE stc ON stc.SILV_TECHNIQUE_CODE = atu.SILV_TECHNIQUE_CODE
      LEFT JOIN SILV_METHOD_CODE smc ON smc.SILV_METHOD_CODE = atu.SILV_METHOD_CODE
      LEFT JOIN SILV_OBJECTIVE_CODE soc1 ON soc1.SILV_OBJECTIVE_CODE = atu.SILV_OBJECTIVE_CODE_1
      LEFT JOIN SILV_OBJECTIVE_CODE soc2 ON soc2.SILV_OBJECTIVE_CODE = atu.SILV_OBJECTIVE_CODE_2
      LEFT JOIN SILV_OBJECTIVE_CODE soc3 ON soc3.SILV_OBJECTIVE_CODE = atu.SILV_OBJECTIVE_CODE_3
      LEFT JOIN SILV_FUND_SRCE_CODE sfsc ON sfsc.SILV_FUND_SRCE_CODE = atu.SILV_FUND_SRCE_CODE
      WHERE
        atu.SILV_BASE_CODE != 'DN'
        AND atu.OPENING_ID = :openingId
        AND (
        	NVL(:mainSearchTerm,'NOVALUE') = 'NOVALUE' OR (
        		atu.SILV_BASE_CODE like '%' || :mainSearchTerm || '%'
                OR TO_CHAR(atu.ACTIVITY_TREATMENT_UNIT_ID) LIKE '%' || :mainSearchTerm || '%'
        		OR UPPER(sbc.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR atu.SILV_TECHNIQUE_CODE like '%' || :mainSearchTerm || '%'
        		OR UPPER(stc.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR ATU.SILV_METHOD_CODE like '%' || :mainSearchTerm || '%'
        		OR UPPER(smc.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR atu.SILV_OBJECTIVE_CODE_1 like '%' || :mainSearchTerm || '%'
        		OR UPPER(soc1.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR atu.SILV_OBJECTIVE_CODE_2 like '%' || :mainSearchTerm || '%'
        		OR UPPER(soc2.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR atu.SILV_OBJECTIVE_CODE_3 like '%' || :mainSearchTerm || '%'
        		OR UPPER(soc3.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR ATU.SILV_FUND_SRCE_CODE like '%' || :mainSearchTerm || '%'
        		OR UPPER(sfsc.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR UPPER(ATU.SILVICULTURE_PROJECT_ID) like '%' || :mainSearchTerm || '%'
        		OR (REGEXP_LIKE(:mainSearchTerm, '^\\d+(\\.\\d+)?$') AND atu.TREATMENT_AMOUNT = TO_NUMBER(:mainSearchTerm DEFAULT 0 ON CONVERSION ERROR,'999999.999999'))
        	)
        )""";

  public static final String GET_OPENING_ACTIVITIES_ACTIVITIES_COUNT =
      """
      SELECT count(atu.ACTIVITY_TREATMENT_UNIT_ID)
      FROM ACTIVITY_TREATMENT_UNIT atu
      LEFT JOIN SILV_BASE_CODE sbc ON sbc.SILV_BASE_CODE = atu.SILV_BASE_CODE
      LEFT JOIN SILV_TECHNIQUE_CODE stc ON stc.SILV_TECHNIQUE_CODE = atu.SILV_TECHNIQUE_CODE
      LEFT JOIN SILV_METHOD_CODE smc ON smc.SILV_METHOD_CODE = atu.SILV_METHOD_CODE
      LEFT JOIN SILV_OBJECTIVE_CODE soc1 ON soc1.SILV_OBJECTIVE_CODE = atu.SILV_OBJECTIVE_CODE_1
      LEFT JOIN SILV_OBJECTIVE_CODE soc2 ON soc2.SILV_OBJECTIVE_CODE = atu.SILV_OBJECTIVE_CODE_2
      LEFT JOIN SILV_OBJECTIVE_CODE soc3 ON soc3.SILV_OBJECTIVE_CODE = atu.SILV_OBJECTIVE_CODE_3
      LEFT JOIN SILV_FUND_SRCE_CODE sfsc ON sfsc.SILV_FUND_SRCE_CODE = atu.SILV_FUND_SRCE_CODE
      WHERE
        atu.SILV_BASE_CODE != 'DN'
        AND atu.OPENING_ID = :openingId
        AND (
        	NVL(:mainSearchTerm,'NOVALUE') = 'NOVALUE' OR (
        		atu.SILV_BASE_CODE like '%' || :mainSearchTerm || '%'
        		OR UPPER(sbc.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR atu.SILV_TECHNIQUE_CODE like '%' || :mainSearchTerm || '%'
        		OR UPPER(stc.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR ATU.SILV_METHOD_CODE like '%' || :mainSearchTerm || '%'
        		OR UPPER(smc.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR atu.SILV_OBJECTIVE_CODE_1 like '%' || :mainSearchTerm || '%'
        		OR UPPER(soc1.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR atu.SILV_OBJECTIVE_CODE_2 like '%' || :mainSearchTerm || '%'
        		OR UPPER(soc2.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR atu.SILV_OBJECTIVE_CODE_3 like '%' || :mainSearchTerm || '%'
        		OR UPPER(soc3.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR ATU.SILV_FUND_SRCE_CODE like '%' || :mainSearchTerm || '%'
        		OR UPPER(sfsc.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR UPPER(ATU.SILVICULTURE_PROJECT_ID) like '%' || :mainSearchTerm || '%'
        		OR (REGEXP_LIKE(:mainSearchTerm, '^\\d+(\\.\\d+)?$') AND atu.TREATMENT_AMOUNT = TO_NUMBER(:mainSearchTerm DEFAULT 0 ON CONVERSION ERROR,'999999.999999'))
        	)
        )""";

  public static final String GET_OPENING_ACTIVITIES_BASE =
      """
      SELECT
      	atu.ACTIVITY_LICENSEE_ID AS licensee_activity_id,
      	atu.FIA_PROJECT_ID AS intra_agency_number,
      	ffc.CLIENT_NUMBER AS activity_client,
      	ffc.CLIENT_LOCN_CODE AS activity_location,
      	atu.PLANNED_TREATMENT_AMOUNT AS planned_amount,
      	atu.TREATMENT_AMOUNT AS treated_amount,
      	atu.PLANNED_TREATMENT_COST AS planned_cost,
      	atu.ACTUAL_TREATMENT_COST AS actual_cost,
        atu.SILV_BASE_CODE AS kind,
        atu.ACT_PLANTED_NO AS total_planting
      FROM ACTIVITY_TREATMENT_UNIT atu
      LEFT JOIN CUT_BLOCK_OPEN_ADMIN cboa ON cboa.CUT_BLOCK_OPEN_ADMIN_ID = atu.CUT_BLOCK_OPEN_ADMIN_ID
      LEFT JOIN FOREST_FILE_CLIENT ffc ON (ffc.FOREST_FILE_ID = cboa.FOREST_FILE_ID AND ffc.FOREST_FILE_CLIENT_TYPE_CODE = 'A')
      WHERE
        atu.OPENING_ID = :openingId
        AND atu.ACTIVITY_TREATMENT_UNIT_ID = :atuId""";

  public static final String GET_OPENING_ACTIVITIES_SU =
      """
      SELECT
      	CASE
              WHEN atu.ATU_COMPLETION_DATE IS NOT NULL THEN atu.SURVEY_ACTUAL_NUM_PLOTS
              WHEN atu.PLANNED_DATE IS NOT NULL THEN atu.SURVEY_PLANNED_NUM_PLOTS
              ELSE 0
          END AS PLOTS_COUNT,
          atu.SURVEY_MIN_PLOTS_PER_STRATUM
      FROM ACTIVITY_TREATMENT_UNIT atu
      LEFT JOIN CUT_BLOCK_OPEN_ADMIN cboa ON cboa.CUT_BLOCK_OPEN_ADMIN_ID = atu.CUT_BLOCK_OPEN_ADMIN_ID
      LEFT JOIN FOREST_FILE_CLIENT ffc ON (ffc.FOREST_FILE_ID = cboa.FOREST_FILE_ID AND ffc.FOREST_FILE_CLIENT_TYPE_CODE = 'A')
      WHERE
        atu.SILV_BASE_CODE = 'SU'
        AND atu.OPENING_ID = :openingId
        AND atu.ACTIVITY_TREATMENT_UNIT_ID = :atuId
      ORDER BY atu.ACTIVITY_TU_SEQ_NO""";

  public static final String GET_OPENING_ACTIVITY_SPECIES =
      """
      SELECT
      	pr.SILV_TREE_SPECIES_CODE AS species_code,
      	stsc.DESCRIPTION AS species_name,
      	pr.NUMBER_PLANTED AS planted_number,
      	NVL(pr.PLANTED_NO_BEYOND_XFER_LIMIT,0) AS number_beyond_transfer_limit,
      	CASE
      		WHEN pr.CLIMATE_BASED_SEED_XFER_IND = 'Y' THEN 'true'
      		ELSE 'false'
      	END AS cbst,
      	pr.REQUEST_SKEY AS request_id,
      	NVL(pr.SEEDLOT_NUMBER,pr.VEG_LOT_ID) AS lot,
      	pr.BID_PRICE_PER_TREE AS bid_price_per_tree
      FROM PLANTING_RSLT pr
      LEFT JOIN SILV_TREE_SPECIES_CODE stsc ON stsc.SILV_TREE_SPECIES_CODE = pr.SILV_TREE_SPECIES_CODE
      WHERE pr.ACTIVITY_TREATMENT_UNIT_ID =:atuId""";

  public static final String GET_OPENING_ACTIVITY_JS =
      """
      SELECT
      	atu.INTER_TREE_TARGET_DISTANCE AS target_intertree_distance,
      	atu.INTER_TREE_VARIATION AS allowable_variation_distance,
      	atu.MAX_TREES_PER_PLOT AS allowable_tree_per_lot,
      	atu.MAX_TREES_PER_HA AS spacing_per_ha
      FROM OPENING op
      LEFT JOIN ACTIVITY_TREATMENT_UNIT atu ON atu.OPENING_ID = op.OPENING_ID
      LEFT JOIN CUT_BLOCK_OPEN_ADMIN cboa ON cboa.CUT_BLOCK_OPEN_ADMIN_ID = atu.CUT_BLOCK_OPEN_ADMIN_ID
      LEFT JOIN FOREST_FILE_CLIENT ffc ON (ffc.FOREST_FILE_ID = cboa.FOREST_FILE_ID AND ffc.FOREST_FILE_CLIENT_TYPE_CODE = 'A')
      WHERE
        atu.SILV_BASE_CODE = 'JS'
        AND op.OPENING_ID = :openingId
        AND atu.ACTIVITY_TREATMENT_UNIT_ID = :atuId
      ORDER BY atu.ACTIVITY_TU_SEQ_NO""";

  public static final String GET_OPENING_ACTIVITY_PR =
      """
      SELECT
        atu.TOTAL_STEMS_PER_HA AS total_stems_per_ha,
      	atu.STEMS_TO_PRUNE AS stems_per_ha_to_prune,
      	atu.INTER_TREE_TARGET_DISTANCE AS target_intertree_distance,
      	atu.INTER_TREE_MIN_DISTANCE AS minimum_intertree_distance,
      	atu.PRUNE_HEIGHT AS height_above_ground,
      	atu.PRUNING_MIN_CROWN_PCT AS minimum_live_crown
      FROM ACTIVITY_TREATMENT_UNIT atu
      LEFT JOIN CUT_BLOCK_OPEN_ADMIN cboa ON cboa.CUT_BLOCK_OPEN_ADMIN_ID = atu.CUT_BLOCK_OPEN_ADMIN_ID
      LEFT JOIN FOREST_FILE_CLIENT ffc ON (ffc.FOREST_FILE_ID = cboa.FOREST_FILE_ID AND ffc.FOREST_FILE_CLIENT_TYPE_CODE = 'A')
      WHERE
        atu.SILV_BASE_CODE = 'PR'
        AND atu.OPENING_ID = :openingId
        AND atu.ACTIVITY_TREATMENT_UNIT_ID = :atuId
      ORDER BY atu.ACTIVITY_TU_SEQ_NO""";

  public static final String GET_OPENING_ACTIVITY_SP =
      """
      SELECT
      	atu.TARGET_PREPARED_SPOTS
      FROM ACTIVITY_TREATMENT_UNIT atu
      WHERE
        atu.SILV_BASE_CODE = 'SP'
        AND atu.OPENING_ID = :openingId AND
        atu.ACTIVITY_TREATMENT_UNIT_ID = :atuId
      ORDER BY atu.ACTIVITY_TU_SEQ_NO""";

  public static final String GET_OPENING_TENURES =
      """
      SELECT
        cboa.CUT_BLOCK_OPEN_ADMIN_ID AS id,
        CASE WHEN NVL(cboa.OPENING_PRIME_LICENCE_IND, 'N') = 'Y' THEN 'true' ELSE 'false' END AS primary_tenure,
        cboa.FOREST_FILE_ID AS file_id,
        cboa.CUT_BLOCK_ID AS cut_block,
        cboa.CUTTING_PERMIT_ID AS cutting_permit,
        cboa.TIMBER_MARK AS timber_mark,
        cb.BLOCK_STATUS_ST AS status_code,
        bsc.DESCRIPTION AS status_name,
        cboa.PLANNED_GROSS_BLOCK_AREA AS planned_gross_area,
        cboa.PLANNED_NET_BLOCK_AREA AS planned_net_area
      FROM CUT_BLOCK_OPEN_ADMIN cboa
      LEFT JOIN CUT_BLOCK cb ON cb.CB_SKEY = cboa.CB_SKEY
      LEFT JOIN BLOCK_STATUS_CODE bsc ON bsc.BLOCK_STATUS_CODE = cb.BLOCK_STATUS_ST
      WHERE
        	cboa.OPENING_ID = :openingId
        	AND (
        		NVL(:mainSearchTerm,'NOVALUE') = 'NOVALUE' OR (
        			cboa.FOREST_FILE_ID like '%' || :mainSearchTerm || '%'
        			OR cboa.CUT_BLOCK_ID like '%' || :mainSearchTerm || '%'
        			OR cboa.CUTTING_PERMIT_ID like '%' || :mainSearchTerm || '%'
        			OR cboa.TIMBER_MARK like '%' || :mainSearchTerm || '%'
        			OR cb.BLOCK_STATUS_ST like '%' || :mainSearchTerm || '%'
        			OR UPPER(bsc.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        			OR (REGEXP_LIKE(:mainSearchTerm, '^\\d+(\\.\\d+)?$') AND cboa.PLANNED_GROSS_BLOCK_AREA = TO_NUMBER(:mainSearchTerm DEFAULT 0 ON CONVERSION ERROR,'999999.999999'))
        			OR (REGEXP_LIKE(:mainSearchTerm, '^\\d+(\\.\\d+)?$') AND cboa.PLANNED_NET_BLOCK_AREA = TO_NUMBER(:mainSearchTerm DEFAULT 0 ON CONVERSION ERROR,'999999.999999'))
        		)
        	)""";

  public static final String GET_OPENING_TENURES_COUNT =
      """
      SELECT count(1)
        FROM CUT_BLOCK_OPEN_ADMIN cboa
          LEFT JOIN CUT_BLOCK cb ON cb.CB_SKEY = cboa.CB_SKEY
          LEFT JOIN BLOCK_STATUS_CODE bsc ON bsc.BLOCK_STATUS_CODE = cb.BLOCK_STATUS_ST
        WHERE
          	cboa.OPENING_ID = :openingId
          	AND (
          		NVL(:mainSearchTerm,'NOVALUE') = 'NOVALUE' OR (
        		cboa.FOREST_FILE_ID like '%' || :mainSearchTerm || '%'
        		OR cboa.CUT_BLOCK_ID like '%' || :mainSearchTerm || '%'
        		OR cboa.CUTTING_PERMIT_ID like '%' || :mainSearchTerm || '%'
        		OR cboa.TIMBER_MARK like '%' || :mainSearchTerm || '%'
        		OR cb.BLOCK_STATUS_ST like '%' || :mainSearchTerm || '%'
        		OR UPPER(bsc.DESCRIPTION) like '%' || :mainSearchTerm || '%'
        		OR (REGEXP_LIKE(:mainSearchTerm, '^\\d+(\\.\\d+)?$') AND cboa.PLANNED_GROSS_BLOCK_AREA = TO_NUMBER(:mainSearchTerm DEFAULT 0 ON CONVERSION ERROR,'999999.999999'))
        		OR (REGEXP_LIKE(:mainSearchTerm, '^\\d+(\\.\\d+)?$') AND cboa.PLANNED_NET_BLOCK_AREA = TO_NUMBER(:mainSearchTerm DEFAULT 0 ON CONVERSION ERROR,'999999.999999'))
        	)
        )""";

  public static final String GET_OPENING_TENURE_PRIME =
      """
          SELECT
            cboa.CUT_BLOCK_OPEN_ADMIN_ID AS id,
            CASE WHEN NVL(cboa.OPENING_PRIME_LICENCE_IND, 'N') = 'Y' THEN 'true' ELSE 'false' END AS primary_tenure,
               cboa.FOREST_FILE_ID AS file_id,
               cboa.CUT_BLOCK_ID AS cut_block,
               cboa.CUTTING_PERMIT_ID AS cutting_permit,
               cboa.TIMBER_MARK AS timber_mark,
               cb.BLOCK_STATUS_ST AS status_code,
               bsc.DESCRIPTION AS status_name,
               cboa.PLANNED_GROSS_BLOCK_AREA AS planned_gross_area,
               cboa.PLANNED_NET_BLOCK_AREA AS planned_net_area
           FROM CUT_BLOCK_OPEN_ADMIN cboa
           LEFT JOIN CUT_BLOCK cb ON cb.CB_SKEY = cboa.CB_SKEY
           LEFT JOIN BLOCK_STATUS_CODE bsc ON bsc.BLOCK_STATUS_CODE = cb.BLOCK_STATUS_ST
           WHERE cboa.OPENING_ID = :openingId AND cboa.opening_prime_licence_ind = 'Y'""";

  public static final String GET_OPENING_FOREST_COVER_LIST =
      """
          SELECT
              fc.FOREST_COVER_ID AS cover_id,
              fc.SILV_POLYGON_NO AS polygon_id,
              ssu.STANDARDS_UNIT_ID AS standard_unit_id,
              fcnma.STOCKING_TYPE_CODE AS unmapped_code,
              stcnma.DESCRIPTION AS unmapped_name,
              fc.SILV_POLYGON_AREA AS gross_area,
              fc.SILV_POLYGON_NET_AREA AS net_area,
              fc.STOCKING_STATUS_CODE AS status_code,
              ssc.DESCRIPTION AS status_name,
              fc.STOCKING_TYPE_CODE AS type_code,
              stc.DESCRIPTION AS type_name,
              fcli.TOTAL_STEMS_PER_HA AS total,
              fcli.TOTAL_WELL_SPACED_STEMS_PER_HA AS inventory_total_well_spaced,
              fcli.WELL_SPACED_STEMS_PER_HA AS inventory_well_spaced,
              fcli.FREE_GROWING_STEMS_PER_HA AS inventory_free_growing,
              fcls.TOTAL_WELL_SPACED_STEMS_PER_HA AS silviculture_total_well_spaced,
              fcls.WELL_SPACED_STEMS_PER_HA AS silviculture_well_spaced,
              fcls.FREE_GROWING_STEMS_PER_HA AS silviculture_free_growing,
              fc.REFERENCE_YEAR,
              layer_summary.is_single_layer AS is_single_layer,
              fc.SILV_RESERVE_CODE AS reserve_code,
              fc.SILV_RESERVE_OBJECTIVE_CODE AS objective_code

          FROM FOREST_COVER fc

          LEFT JOIN STOCKING_STATUS_CODE ssc ON ssc.STOCKING_STATUS_CODE = fc.STOCKING_STATUS_CODE
          LEFT JOIN STOCKING_TYPE_CODE stc ON stc.STOCKING_TYPE_CODE = fc.STOCKING_TYPE_CODE
          LEFT JOIN FOREST_COVER_NON_MAPPED_AREA fcnma ON fc.FOREST_COVER_ID = fcnma.FOREST_COVER_ID
          LEFT JOIN STOCKING_TYPE_CODE stcnma ON stcnma.STOCKING_TYPE_CODE = fcnma.STOCKING_TYPE_CODE
          LEFT JOIN FOREST_COVER_LAYER fcli
              ON fcli.FOREST_COVER_LAYER_CODE = 'I' AND fcli.FOREST_COVER_ID = fc.FOREST_COVER_ID
          LEFT JOIN FOREST_COVER_LAYER fcls
              ON fcls.FOREST_COVER_LAYER_CODE = 'S' AND fcls.FOREST_COVER_ID = fc.FOREST_COVER_ID
          LEFT JOIN STOCKING_STANDARD_UNIT ssu
              ON ssu.STOCKING_STANDARD_UNIT_ID = fc.STOCKING_STANDARD_UNIT_ID

          LEFT JOIN (
              SELECT
                  fc.FOREST_COVER_ID,
                  CASE
                      WHEN layer.count_layers IS NULL OR layer.count_layers = 0 THEN 'Y'
                      WHEN layer.count_SI > 0 THEN 'Y'
                      ELSE 'N'
                  END AS is_single_layer
              FROM FOREST_COVER fc
              LEFT JOIN (
                  SELECT
                      fcl.FOREST_COVER_ID,
                      COUNT(*) AS count_layers,
                      COUNT(CASE WHEN fcl.FOREST_COVER_LAYER_CODE IN ('S', 'I') THEN 1 END) AS count_SI
                  FROM FOREST_COVER_LAYER fcl
                  GROUP BY fcl.FOREST_COVER_ID
              ) layer ON fc.FOREST_COVER_ID = layer.FOREST_COVER_ID
          ) layer_summary
              ON layer_summary.FOREST_COVER_ID = fc.FOREST_COVER_ID

          WHERE
              fc.OPENING_ID = :openingId
              AND (
                  NVL(:mainSearchTerm,'NOVALUE') = 'NOVALUE'
                  OR (
                      fc.SILV_POLYGON_NO LIKE '%' || :mainSearchTerm || '%'
                      OR ssu.STANDARDS_UNIT_ID LIKE '%' || :mainSearchTerm || '%'
                      OR UPPER(stcnma.DESCRIPTION) LIKE '%' || :mainSearchTerm || '%'
                      OR fcnma.STOCKING_TYPE_CODE LIKE '%' || :mainSearchTerm || '%'
                      OR fc.STOCKING_STATUS_CODE LIKE '%' || :mainSearchTerm || '%'
                      OR UPPER(ssc.DESCRIPTION) LIKE '%' || :mainSearchTerm || '%'
                      OR fc.STOCKING_TYPE_CODE LIKE '%' || :mainSearchTerm || '%'
                      OR UPPER(stc.DESCRIPTION) LIKE '%' || :mainSearchTerm || '%'
                      OR (REGEXP_LIKE(:mainSearchTerm, '^\\d+(\\.\\d+)?$')
                          AND fc.SILV_POLYGON_AREA = TO_NUMBER(:mainSearchTerm DEFAULT 0 ON CONVERSION ERROR, '999999.999999'))
                      OR (REGEXP_LIKE(:mainSearchTerm, '^\\d+(\\.\\d+)?$')
                          AND fc.SILV_POLYGON_NET_AREA = TO_NUMBER(:mainSearchTerm DEFAULT 0 ON CONVERSION ERROR, '999999.999999'))
                      OR (REGEXP_LIKE(:mainSearchTerm, '^\\d+(\\.\\d+)?$')
                          AND fc.REFERENCE_YEAR = TO_NUMBER(:mainSearchTerm DEFAULT 0 ON CONVERSION ERROR, '999999.999999'))
                  )
              )
           """;

  public static final String GET_OPENING_FOREST_COVER_LIST_SPECIES =
      """
      SELECT
      	fcls.TREE_SPECIES_CODE AS species_code,
      	tsc.DESCRIPTION AS species_name
      FROM FOREST_COVER_LAYER fcl
      LEFT JOIN FOREST_COVER_LAYER_SPECIES fcls ON (fcls.FOREST_COVER_ID = fcl.FOREST_COVER_ID AND fcls.FOREST_COVER_LAYER_ID = fcl.FOREST_COVER_LAYER_ID )
      LEFT JOIN TREE_SPECIES_CODE tsc ON tsc.TREE_SPECIES_CODE = fcls.TREE_SPECIES_CODE
      WHERE
      	fcl.FOREST_COVER_LAYER_CODE = :coverLayerCode AND fcl.FOREST_COVER_ID = :forestCoverId
      ORDER BY fcls.SPECIES_ORDER""";

  public static final String GET_OPENING_FOREST_COVER_POLYGON =
      """
      SELECT
      	fc.FOREST_COVER_ID,
      	fc.SILV_RESERVE_CODE AS reserve_code,
      	src.DESCRIPTION AS reserve_name,
      	fc.SILV_RESERVE_OBJECTIVE_CODE AS objective_code,
      	sroc.DESCRIPTION AS objective_name,
      	fc.SITE_CLASS_CODE AS site_class_code,
      	scc.DESCRIPTION AS site_class_name,
      	fc.SITE_INDEX AS site_index,
      	fc.SITE_INDEX_SOURCE_CODE AS site_index_source_code,
      	sisc.DESCRIPTION AS site_index_source_name,
      	fc.TREE_COVER_PATTERN_CODE AS tree_cover_pattern_code,
      	tcpc.DESCRIPTION AS tree_cover_pattern_name,
      	fc.REENTRY_YEAR
      FROM FOREST_COVER fc
      LEFT JOIN SILV_RESERVE_CODE src ON src.SILV_RESERVE_CODE = fc.SILV_RESERVE_CODE
      LEFT JOIN SILV_RESERVE_OBJECTIVE_CODE sroc ON sroc.SILV_RESERVE_OBJECTIVE_CODE = fc.SILV_RESERVE_OBJECTIVE_CODE
      LEFT JOIN SITE_CLASS_CODE scc ON scc.SITE_CLASS_CODE = fc.SITE_CLASS_CODE
      LEFT JOIN SITE_INDEX_SOURCE_CODE sisc ON sisc.SITE_INDEX_SOURCE_CODE = fc.SITE_INDEX_SOURCE_CODE
      LEFT JOIN TREE_COVER_PATTERN_CODE tcpc ON tcpc.TREE_COVER_PATTERN_CODE = fc.TREE_COVER_PATTERN_CODE
      WHERE
      	fc.FOREST_COVER_ID = :forestCoverId""";

  public static final String GET_OPENING_FOREST_COVER_UNMAPPED =
      """
      SELECT
      	fcnma.NON_MAPPED_AREA_ID AS unmapped_area_id,
      	fcnma.NON_MAPPED_AREA AS area,
      	fcnma.STOCKING_STATUS_CODE AS stocking_status_code,
      	ssc.DESCRIPTION AS stocking_status_name,
      	fcnma.STOCKING_TYPE_CODE AS stocking_type_code,
      	stc.DESCRIPTION AS stocking_type_name
      FROM FOREST_COVER_NON_MAPPED_AREA fcnma
      LEFT JOIN STOCKING_STATUS_CODE ssc ON ssc.STOCKING_STATUS_CODE = fcnma.STOCKING_STATUS_CODE
      LEFT JOIN STOCKING_TYPE_CODE stc ON stc.STOCKING_TYPE_CODE = fcnma.STOCKING_TYPE_CODE
      WHERE
      	fcnma.FOREST_COVER_ID = :forestCoverId""";

  public static final String GET_OPENING_FOREST_COVER_LAYER =
      """
      SELECT
      	fcl.FOREST_COVER_LAYER_ID AS layer_id,
        fcl.FOREST_COVER_LAYER_CODE AS layer_code,
      	fclc.DESCRIPTION AS layer_name,
      	fcl.CROWN_CLOSURE_PCT AS crown_closure,
      	fcl.BASAL_AREA AS basal_area_st,
      	fcl.TOTAL_STEMS_PER_HA AS total_stems,
      	fcl.TOTAL_WELL_SPACED_STEMS_PER_HA AS total_well_spaced,
      	fcl.WELL_SPACED_STEMS_PER_HA AS well_spaced,
      	fcl.FREE_GROWING_STEMS_PER_HA AS free_growing
      FROM FOREST_COVER_LAYER fcl
      LEFT JOIN FOREST_COVER_LAYER_CODE fclc ON fclc.FOREST_COVER_LAYER_CODE = fcl.FOREST_COVER_LAYER_CODE
      WHERE
      	fcl.FOREST_COVER_ID = :forestCoverId""";

  public static final String GET_OPENING_FOREST_COVER_DETAILS_SPECIES =
      """
      SELECT
      	fcls.TREE_SPECIES_CODE AS species_code,
      	tsc.DESCRIPTION AS species_name,
      	fcls.TREE_SPECIES_PCT AS species_percent,
      	fcls.AVG_AGE AS average_age,
      	fcls.AVG_HEIGHT AS average_height
      FROM FOREST_COVER_LAYER_SPECIES fcls
      LEFT JOIN TREE_SPECIES_CODE tsc ON tsc.TREE_SPECIES_CODE = fcls.TREE_SPECIES_CODE
      WHERE
      	fcls.FOREST_COVER_LAYER_ID = :forestCoverLayerId
      ORDER BY fcls.SPECIES_ORDER""";

  public static final String GET_OPENING_FOREST_COVER_DAMAGE =
      """
      SELECT
      	fr.SILV_DAMAGE_AGENT_CODE AS damage_agent_code,
      	sdac.DESCRIPTION AS damage_agent_name,
      	fr.INCIDENCE_PCT AS forest_health_incidence,
      	fr.INCIDENCE_AREA AS incidence_area
      FROM FORHEALTH_RSLT fr
      LEFT JOIN SILV_DAMAGE_AGENT_CODE sdac ON sdac.SILV_DAMAGE_AGENT_CODE = fr.SILV_DAMAGE_AGENT_CODE
      WHERE
      	fr.FOREST_COVER_LAYER_ID = :forestCoverLayerId""";

  public static final String GET_OPENING_ATTACHMENT_LIST =
      """
          SELECT
            OPENING_ATTACHMENT_FILE_ID AS openingAttachmentFileId,
            OPENING_ID AS openingId,
            ATTACHMENT_NAME AS attachmentName,
            ATTACHMENT_DESCRIPTION AS attachmentDescription,
            MIME_TYPE_CODE AS mimeTypeCode,
            ENTRY_USERID AS entryUserId,
            ENTRY_TIMESTAMP AS entryTimestamp,
            UPDATE_USERID AS updateUserId,
            UPDATE_TIMESTAMP AS updateTimestamp,
            REVISION_COUNT AS revisionCount,
            RAWTOHEX(OPENING_ATTACHMENT_GUID) AS attachmentGuid
          FROM THE.OPENING_ATTACHMENT
          WHERE OPENING_ID = :openingId
          """;

  public static final String GET_OPENING_HISTORY_AUDIT_LIST =
      """
      SELECT
        rae.OPENING_ID AS opening_id,
        rae.RESULTS_AUDIT_EVENT_ID AS audit_event_id,
        rae.STANDARDS_REGIME_ID AS regime_id,
        rae.SILVICULTURE_PROJECT_ID AS project_id,
        rae.RESULTS_AUDIT_ACTION_CODE AS action_code,
        TO_CHAR(rae.ACTION_DATE, 'YYYY-MM-DD HH24:MI:SS') AS action_timestamp,
        rae.DESCRIPTION AS description,
        rae.USER_ID AS user_id,
        CASE
            WHEN rae.EMAIL_SENT_IND = 'Y' THEN 'true'
            ELSE 'false'
        END AS is_email_sent,
        rae.XML_SUBMISSION_ID AS xml_submission_id,
        rae.OPENING_AMENDMENT_NUMBER AS opening_amendment_number,
        rae.ENTRY_USERID AS entry_user_id,
        TO_CHAR(rae.ENTRY_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS') AS entry_timestamp
      FROM RESULTS_AUDIT_EVENT rae
      WHERE rae.OPENING_ID = :openingId
      """;

  public static final String GET_OPENING_HISTORY_AUDIT_DETAIL_LIST =
      """
      SELECT
        rad.RESULTS_AUDIT_EVENT_ID AS audit_event_id,
        rad.RESULTS_AUDIT_DETAIL_ID AS audit_detail_id,
        rad.BUSINESS_IDENTIFIER AS business_identifier,
        rad.TABLE_NAME AS table_name,
        rad.COLUMN_NAME AS column_name,
        rad.OLD_VALUE AS old_value,
        rad.NEW_VALUE AS new_value,
        rad.ENTRY_USERID AS entry_user_id,
        TO_CHAR(rad.ENTRY_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS') AS entry_timestamp
      FROM RESULTS_AUDIT_DETAIL rad
      WHERE rad.RESULTS_AUDIT_EVENT_ID = :auditEventId
      """;

  public static final String GET_OPENING_STANDARD_UNIT_HISTORY_LIST =
      """
      SELECT
        ssua.STOCKING_EVENT_HISTORY_ID,
        MAX(seh.OPENING_AMENDMENT_NUMBER) AS amendment_number,
        TO_CHAR(MAX(seh.ENTRY_TIMESTAMP), 'YYYY-MM-DD"T"HH24:MI:SS') AS event_timestamp,
        COUNT(ssua.STOCKING_EVENT_HISTORY_ID) AS su_count,
        SUM(ssua.NET_AREA) AS total_nar,
        MAX(seh.RESULTS_AUDIT_ACTION_CODE) AS audit_action_code,
        MAX(raac.DESCRIPTION) AS audit_action_description,
        MAX(seh.RESULTS_SUBMISSION_ID) AS esf_submission_id,
        MAX(seh.ENTRY_USERID) AS submitted_by_user_id,
        MAX(oah.APP_ENT_BY_USERID) AS approved_by_user_id
      FROM STOCKING_EVENT_HISTORY seh
      LEFT JOIN STOCKING_STANDARD_UNIT_ARCHIVE ssua
        ON ssua.STOCKING_EVENT_HISTORY_ID = seh.STOCKING_EVENT_HISTORY_ID
      LEFT JOIN OPENING_AMENDMENT_HISTORY oah
        ON seh.OPENING_ID = oah.OPENING_ID
        AND seh.OPENING_AMENDMENT_NUMBER = oah.OPENING_AMENDMENT_NUMBER
      LEFT JOIN RESULTS_AUDIT_ACTION_CODE raac
        ON seh.RESULTS_AUDIT_ACTION_CODE = raac.RESULTS_AUDIT_ACTION_CODE
      WHERE seh.OPENING_ID = :openingId
      GROUP BY ssua.STOCKING_EVENT_HISTORY_ID
      ORDER BY MAX(seh.AMEND_EVENT_TIMESTAMP) DESC
      """;

  public static final String GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_WITH_COMPARISON_LIST =
      """
      WITH ordered_seh AS (
        SELECT
          seh.STOCKING_EVENT_HISTORY_ID,
          seh.ENTRY_TIMESTAMP,
          ROW_NUMBER() OVER (ORDER BY seh.ENTRY_TIMESTAMP DESC) AS rn
        FROM STOCKING_EVENT_HISTORY seh
        WHERE seh.OPENING_ID = :openingId
      ),
      current_event AS (
        SELECT * FROM ordered_seh WHERE STOCKING_EVENT_HISTORY_ID = :historyId
      ),
      previous_event AS (
        SELECT * FROM ordered_seh WHERE rn = (SELECT rn FROM current_event) + 1
      ),
      -- current and previous SU
      current_su AS (
        SELECT * FROM STOCKING_STANDARD_UNIT_ARCHIVE
        WHERE STOCKING_EVENT_HISTORY_ID = (SELECT STOCKING_EVENT_HISTORY_ID FROM current_event)
      ),
      previous_su AS (
        SELECT * FROM STOCKING_STANDARD_UNIT_ARCHIVE
        WHERE STOCKING_EVENT_HISTORY_ID = (SELECT STOCKING_EVENT_HISTORY_ID FROM previous_event)
      ),
      -- current and previous ECO
      current_ec AS (
        SELECT * FROM STOCKING_ECOLOGY_ARCHIVE
        WHERE STOCKING_EVENT_HISTORY_ID = (SELECT STOCKING_EVENT_HISTORY_ID FROM current_event)
      ),
      previous_ec AS (
        SELECT * FROM STOCKING_ECOLOGY_ARCHIVE
        WHERE STOCKING_EVENT_HISTORY_ID = (SELECT STOCKING_EVENT_HISTORY_ID FROM previous_event)
      ),
      all_units AS (
        SELECT DISTINCT
            COALESCE(curr.STOCKING_STANDARD_UNIT_ID, prev.STOCKING_STANDARD_UNIT_ID) AS stocking_standard_unit_id,
            COALESCE(curr.STANDARDS_UNIT_ID, prev.STANDARDS_UNIT_ID) AS standards_unit_id,
            curr.STOCKING_EVENT_HISTORY_ID AS stocking_event_history_id
        FROM current_su curr
        FULL OUTER JOIN previous_su prev ON curr.STOCKING_STANDARD_UNIT_ID = prev.STOCKING_STANDARD_UNIT_ID
      )
      SELECT
        a.stocking_event_history_id,
        a.stocking_standard_unit_id,
        a.standards_unit_id,
        -- SU diffs
        prev_su.STANDARDS_REGIME_ID AS old_regime_id,
        curr_su.STANDARDS_REGIME_ID AS new_regime_id,
        prev_su.NET_AREA AS old_net_area,
        curr_su.NET_AREA AS new_net_area,
        prev_su.MAX_ALLOW_SOIL_DISTURBANCE_PCT AS old_max_soil_disturbance,
        curr_su.MAX_ALLOW_SOIL_DISTURBANCE_PCT AS new_max_soil_disturbance,
        CASE
            WHEN prev_su.VARIANCE_IND = 'Y' THEN 'true' ELSE 'false'
        END AS old_variance_indicator,
        CASE
            WHEN curr_su.VARIANCE_IND = 'Y' THEN 'true' ELSE 'false'
        END AS new_variance_indicator,
        CASE
            WHEN prev_su.REGEN_OBLIGATION_IND = 'Y' THEN 'true' ELSE 'false'
        END AS old_regen_obligation_indicator,
        CASE
            WHEN curr_su.REGEN_OBLIGATION_IND = 'Y' THEN 'true' ELSE 'false'
        END AS new_regen_obligation_indicator,
        prev_su.NO_REGEN_EARLY_OFFSET_YRS AS old_no_regen_early_offset_years,
        curr_su.NO_REGEN_EARLY_OFFSET_YRS AS new_no_regen_early_offset_years,
        prev_su.NO_REGEN_LATE_OFFSET_YRS AS old_no_regen_late_offset_years,
        curr_su.NO_REGEN_LATE_OFFSET_YRS AS new_no_regen_late_offset_years,
        prev_su.REGEN_DELAY_OFFSET_YRS AS old_regen_offset_years,
        curr_su.REGEN_DELAY_OFFSET_YRS AS new_regen_offset_years,
        prev_su.FREE_GROWING_EARLY_OFFSET_YRS AS old_free_growing_early_offset_years,
        curr_su.FREE_GROWING_EARLY_OFFSET_YRS AS new_free_growing_early_offset_years,
        prev_su.FREE_GROWING_LATE_OFFSET_YRS AS old_free_growing_late_offset_years,
        curr_su.FREE_GROWING_LATE_OFFSET_YRS AS new_free_growing_late_offset_years,
        prev_su.AMENDMENT_RATIONALE_COMMENT AS old_amendment_comment,
        curr_su.AMENDMENT_RATIONALE_COMMENT AS new_amendment_comment,
        -- ECOLOGY diffs
        prev_ec.BGC_ZONE_CODE AS old_bgc_zone,
        curr_ec.BGC_ZONE_CODE AS new_bgc_zone,
        prev_ec.BGC_SUBZONE_CODE AS old_bgc_subzone,
        curr_ec.BGC_SUBZONE_CODE AS new_bgc_subzone,
        prev_ec.BGC_VARIANT AS old_bgc_variant,
        curr_ec.BGC_VARIANT AS new_bgc_variant,
        prev_ec.BGC_PHASE AS old_bgc_phase,
        curr_ec.BGC_PHASE AS new_bgc_phase,
        prev_ec.BEC_SITE_SERIES AS old_bec_site_series,
        curr_ec.BEC_SITE_SERIES AS new_bec_site_series,
        prev_ec.BEC_SITE_TYPE AS old_bec_site_type,
        curr_ec.BEC_SITE_TYPE AS new_bec_site_type,
        prev_ec.BEC_SERAL AS old_bec_seral,
        curr_ec.BEC_SERAL AS new_bec_seral
      FROM all_units a
      LEFT JOIN current_su curr_su ON curr_su.STOCKING_STANDARD_UNIT_ID = a.STOCKING_STANDARD_UNIT_ID
      LEFT JOIN previous_su prev_su ON prev_su.STOCKING_STANDARD_UNIT_ID = a.STOCKING_STANDARD_UNIT_ID
      LEFT JOIN current_ec curr_ec ON curr_ec.STOCKING_STANDARD_UNIT_ID = a.STOCKING_STANDARD_UNIT_ID
      LEFT JOIN previous_ec prev_ec ON prev_ec.STOCKING_STANDARD_UNIT_ID = a.STOCKING_STANDARD_UNIT_ID
      """;

  public static final String GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_LAYERS_WITH_COMPARISON =
      """
      WITH ordered_seh AS (
        SELECT
          seh.STOCKING_EVENT_HISTORY_ID,
          seh.ENTRY_TIMESTAMP,
          ROW_NUMBER() OVER (ORDER BY seh.ENTRY_TIMESTAMP DESC) AS rn
        FROM THE.STOCKING_EVENT_HISTORY seh
        WHERE seh.OPENING_ID = :openingId
      ),
      current_event AS (
        SELECT * FROM ordered_seh WHERE STOCKING_EVENT_HISTORY_ID = :historyId
      ),
      previous_event AS (
        SELECT * FROM ordered_seh WHERE rn = (SELECT rn FROM current_event) + 1
      ),
      curr_layer AS (
        SELECT * FROM STOCKING_LAYER_ARCHIVE
        WHERE STOCKING_EVENT_HISTORY_ID = (SELECT STOCKING_EVENT_HISTORY_ID FROM current_event)
      ),
      prev_layer AS (
        SELECT * FROM STOCKING_LAYER_ARCHIVE
        WHERE STOCKING_EVENT_HISTORY_ID = (SELECT STOCKING_EVENT_HISTORY_ID FROM previous_event)
      ),
      layer_keys AS (
        SELECT DISTINCT
          COALESCE(c.STOCKING_STANDARD_UNIT_ID, p.STOCKING_STANDARD_UNIT_ID) AS ssu_id,
          COALESCE(c.STOCKING_LAYER_ID, p.STOCKING_LAYER_ID) AS stocking_layer_id
        FROM curr_layer c
        FULL OUTER JOIN prev_layer p ON
          c.STOCKING_STANDARD_UNIT_ID = p.STOCKING_STANDARD_UNIT_ID AND
          c.STOCKING_LAYER_ID = p.STOCKING_LAYER_ID
      )
      SELECT
        (SELECT STOCKING_EVENT_HISTORY_ID FROM current_event) AS stocking_event_history_id,
        k.ssu_id,
        p.STOCKING_LAYER_ID AS old_layer_id,
        c.STOCKING_LAYER_ID AS new_layer_id,
        p.STOCKING_LAYER_CODE AS old_stocking_layer_code,
        c.STOCKING_LAYER_CODE AS new_stocking_layer_code,
        slcp.DESCRIPTION AS old_stocking_layer_description,
        slcc.DESCRIPTION AS new_stocking_layer_description,
        p.MIN_HORIZONTAL_DISTANCE AS old_min_horizontal_distance,
        c.MIN_HORIZONTAL_DISTANCE AS new_min_horizontal_distance,
        p.MIN_PREF_STOCKING_STANDARD AS old_min_perf_stocking_standard,
        c.MIN_PREF_STOCKING_STANDARD AS new_min_perf_stocking_standard,
        p.MIN_STOCKING_STANDARD AS old_min_stocking_standard,
        c.MIN_STOCKING_STANDARD AS new_min_stocking_standard,
        p.MIN_POST_SPACING AS old_min_post_spacing,
        c.MIN_POST_SPACING AS new_min_post_spacing,
        p.RESIDUAL_BASAL_AREA AS old_residual_basal_area,
        c.RESIDUAL_BASAL_AREA AS new_residual_basal_area,
        p.TARGET_STOCKING AS old_target_well_spaced_trees,
        c.TARGET_STOCKING AS new_target_well_spaced_trees,
        p.HGHT_RELATIVE_TO_COMP AS old_height_relative_to_comp,
        c.HGHT_RELATIVE_TO_COMP AS new_height_relative_to_comp,
        p.MAX_CONIFER AS old_max_conifer,
        c.MAX_CONIFER AS new_max_conifer,
        p.MAX_POST_SPACING AS old_max_post_spacing,
        c.MAX_POST_SPACING AS new_max_post_spacing
      FROM layer_keys k
      LEFT JOIN curr_layer c ON c.STOCKING_STANDARD_UNIT_ID = k.ssu_id AND c.STOCKING_LAYER_ID = k.stocking_layer_id
      LEFT JOIN prev_layer p ON p.STOCKING_STANDARD_UNIT_ID = k.ssu_id AND p.STOCKING_LAYER_ID = k.stocking_layer_id
      LEFT JOIN STOCKING_LAYER_CODE slcc ON c.STOCKING_LAYER_CODE = slcc.STOCKING_LAYER_CODE
      LEFT JOIN STOCKING_LAYER_CODE slcp ON p.STOCKING_LAYER_CODE = slcp.STOCKING_LAYER_CODE
      """;

  public static final String GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_SPECIES_WITH_COMPARISON =
      """
      WITH ordered_seh AS (
          SELECT
              seh.STOCKING_EVENT_HISTORY_ID AS seh_id,
              seh.ENTRY_TIMESTAMP,
              ROW_NUMBER() OVER (ORDER BY seh.ENTRY_TIMESTAMP DESC) AS rn
          FROM THE.STOCKING_EVENT_HISTORY seh
          WHERE seh.OPENING_ID = :openingId
      ),
      current_event AS (
          SELECT * FROM ordered_seh WHERE seh_id = :historyId
      ),
      previous_event AS (
          SELECT * FROM ordered_seh WHERE rn = (SELECT rn FROM current_event) + 1
      ),
      curr_species AS (
          SELECT * FROM STOCKING_LAYER_SPECIES_ARCHIVE
          WHERE STOCKING_EVENT_HISTORY_ID = (SELECT seh_id FROM current_event)
      ),
      prev_species AS (
          SELECT * FROM STOCKING_LAYER_SPECIES_ARCHIVE
          WHERE STOCKING_EVENT_HISTORY_ID = (SELECT seh_id FROM previous_event)
      ),
      all_keys AS (
          SELECT DISTINCT
              COALESCE(c.STOCKING_STANDARD_UNIT_ID, p.STOCKING_STANDARD_UNIT_ID) AS ssu_id,
              COALESCE(c.STOCKING_LAYER_ID, p.STOCKING_LAYER_ID) AS stocking_layer_id,
              COALESCE(c.SILV_TREE_SPECIES_CODE, p.SILV_TREE_SPECIES_CODE) AS species_code
          FROM curr_species c
          FULL OUTER JOIN prev_species p
            ON c.STOCKING_STANDARD_UNIT_ID = p.STOCKING_STANDARD_UNIT_ID
           AND c.STOCKING_LAYER_ID = p.STOCKING_LAYER_ID
           AND c.SILV_TREE_SPECIES_CODE = p.SILV_TREE_SPECIES_CODE
      )
      SELECT
          k.ssu_id,
          p.STOCKING_LAYER_ID AS old_stocking_layer_id,
          c.STOCKING_LAYER_ID AS new_stocking_layer_id,
          slap.STOCKING_LAYER_CODE AS old_layer_code,
          slac.STOCKING_LAYER_CODE AS new_layer_code,
          p.SILV_TREE_SPECIES_CODE AS old_species_code,
          c.SILV_TREE_SPECIES_CODE AS new_species_code,
          stscp.DESCRIPTION AS old_species_description,
          stscc.DESCRIPTION AS new_species_description,
          CASE
            WHEN p.PREFERRED_IND = 'Y' THEN 'true' ELSE 'false'
          END AS old_preferred_ind,
          CASE
            WHEN c.PREFERRED_IND = 'Y' THEN 'true' ELSE 'false'
          END AS new_preferred_ind,
          p.MIN_HEIGHT AS old_min_height,
          c.MIN_HEIGHT AS new_min_height
      FROM all_keys k
      LEFT JOIN curr_species c
        ON c.STOCKING_STANDARD_UNIT_ID = k.ssu_id
       AND c.STOCKING_LAYER_ID = k.stocking_layer_id
       AND c.SILV_TREE_SPECIES_CODE = k.species_code
      LEFT JOIN prev_species p
        ON p.STOCKING_STANDARD_UNIT_ID = k.ssu_id
       AND p.STOCKING_LAYER_ID = k.stocking_layer_id
       AND p.SILV_TREE_SPECIES_CODE = k.species_code
      LEFT JOIN SILV_TREE_SPECIES_CODE stscc ON stscc.SILV_TREE_SPECIES_CODE = c.SILV_TREE_SPECIES_CODE
      LEFT JOIN SILV_TREE_SPECIES_CODE stscp ON stscp.SILV_TREE_SPECIES_CODE = p.SILV_TREE_SPECIES_CODE
      LEFT JOIN STOCKING_LAYER_ARCHIVE slac
        ON slac.STOCKING_LAYER_ID = c.STOCKING_LAYER_ID
        AND slac.STOCKING_EVENT_HISTORY_ID = c.STOCKING_EVENT_HISTORY_ID
      LEFT JOIN STOCKING_LAYER_ARCHIVE slap
        ON slap.STOCKING_LAYER_ID = p.STOCKING_LAYER_ID
        AND slap.STOCKING_EVENT_HISTORY_ID = p.STOCKING_EVENT_HISTORY_ID
      """;

  public static final String GET_OPENING_FOREST_COVER_HISTORY_OVERVIEW_LIST =
      """
      WITH fca_deduped AS (
          SELECT
              MAX(OPENING_ID) AS opening_id,
              TRUNC(UPDATE_TIMESTAMP) AS update_timestamp
          FROM (
              SELECT fca.*,
                     ROW_NUMBER() OVER (
                         PARTITION BY fca.OPENING_ID, TRUNC(fca.UPDATE_TIMESTAMP)
                         ORDER BY TRUNC(fca.UPDATE_TIMESTAMP) DESC
                     ) AS rn
              FROM THE.FOREST_COVER_ARCHIVE fca
              WHERE fca.OPENING_ID = :openingId
          )
          WHERE rn = 1
          GROUP BY TRUNC(UPDATE_TIMESTAMP)
      )
      SELECT
          ols.OPENING_ID,
          TO_CHAR(ols.OPENING_LAND_STATUS_DATE, 'YYYY-MM-DD"T"HH24:MI:SS') AS fc_date,
          (ols.NP_FOR_AREA + ols.NP_NAT_AREA + ols.NP_UNN_AREA) AS np,
          (ols.NSR_NPL_AREA + ols.NSR_PL_AREA + ols.NSR_NAT_AREA) AS nsr,
          (ols.SR_ART_AREA + ols.SR_NAT_AREA) AS imm,
          (ols.MAT_AREA + ols.NP_NAT_AREA + ols.NC_BR_AREA) AS other,
          (
              ols.NP_FOR_AREA + ols.NP_NAT_AREA + ols.NP_UNN_AREA +
              ols.NSR_NPL_AREA + ols.NSR_PL_AREA + ols.NSR_NAT_AREA +
              ols.SR_ART_AREA + ols.SR_NAT_AREA +
              ols.MAT_AREA + ols.NP_NAT_AREA + ols.NC_BR_AREA
          ) AS TOTAL,
          CASE
              WHEN ols.OPENING_LAND_STATUS_DATE = (
                  SELECT MAX(ols2.OPENING_LAND_STATUS_DATE)
                  FROM THE.OPENING_LAND_STATUS ols2
                  WHERE ols2.OPENING_ID = ols.OPENING_ID
              )
              THEN 'true'
              WHEN fca.OPENING_ID IS NOT NULL
                   AND TRUNC(fca.UPDATE_TIMESTAMP) = TRUNC(ols.OPENING_LAND_STATUS_DATE)
              THEN 'true'
              ELSE 'false'
          END AS HAS_DETAILS,
          CASE
              WHEN ols.OPENING_LAND_STATUS_DATE = (
                  SELECT MAX(ols2.OPENING_LAND_STATUS_DATE)
                  FROM THE.OPENING_LAND_STATUS ols2
                  WHERE ols2.OPENING_ID = ols.OPENING_ID
              )
              THEN 'true'
              ELSE 'false'
          END AS IS_CURRENT_HISTORY
      FROM THE.OPENING_LAND_STATUS ols
      LEFT JOIN fca_deduped fca
          ON ols.OPENING_ID = fca.OPENING_ID
          AND TRUNC(ols.OPENING_LAND_STATUS_DATE) = TRUNC(fca.UPDATE_TIMESTAMP)
      WHERE ols.OPENING_ID = :openingId
      ORDER BY ols.OPENING_LAND_STATUS_DATE DESC
      """;

  public static final String GET_OPENING_FOREST_COVER_HISTORY_LIST =
      """
      SELECT
        fc.FOREST_COVER_ID AS cover_id,
        TO_CHAR(fc.ARCHIVE_DATE, 'YYYY-MM-DD') AS archive_date,
        fc.SILV_POLYGON_NO AS polygon_id,
        ssu.STANDARDS_UNIT_ID AS standard_unit_id,
        fcnma.STOCKING_TYPE_CODE AS unmapped_code,
        stcnma.DESCRIPTION AS unmapped_name,
        fc.SILV_POLYGON_AREA AS gross_area,
        fc.SILV_POLYGON_NET_AREA AS net_area,
        fc.STOCKING_STATUS_CODE AS status_code,
        ssc.DESCRIPTION AS status_name,
        fc.STOCKING_TYPE_CODE AS type_code,
        stc.DESCRIPTION AS type_name,
        fcli.TOTAL_STEMS_PER_HA AS total,
        fcli.TOTAL_WELL_SPACED_STEMS_PER_HA AS inventory_total_well_spaced,
        fcli.WELL_SPACED_STEMS_PER_HA AS inventory_well_spaced,
        fcli.FREE_GROWING_STEMS_PER_HA AS inventory_free_growing,
        fcls.TOTAL_WELL_SPACED_STEMS_PER_HA AS silviculture_total_well_spaced,
        fcls.WELL_SPACED_STEMS_PER_HA AS silviculture_well_spaced,
        fcls.FREE_GROWING_STEMS_PER_HA AS silviculture_free_growing,
        fc.REFERENCE_YEAR,
        layer_summary.is_single_layer AS is_single_layer,
        fc.SILV_RESERVE_CODE AS reserve_code,
        fc.SILV_RESERVE_OBJECTIVE_CODE AS objective_code
      FROM FOREST_COVER_ARCHIVE fc
      LEFT JOIN STOCKING_STATUS_CODE ssc ON ssc.STOCKING_STATUS_CODE = fc.STOCKING_STATUS_CODE
      LEFT JOIN STOCKING_TYPE_CODE stc ON stc.STOCKING_TYPE_CODE = fc.STOCKING_TYPE_CODE
      LEFT JOIN FOREST_COVER_NON_MAPPED_ARC fcnma ON fc.FOREST_COVER_ID = fcnma.FOREST_COVER_ID
      LEFT JOIN STOCKING_TYPE_CODE stcnma ON stcnma.STOCKING_TYPE_CODE = fcnma.STOCKING_TYPE_CODE
      LEFT JOIN FOREST_COVER_LAYER_ARCHIVE fcli
        ON fcli.FOREST_COVER_LAYER_CODE = 'I' AND fcli.FOREST_COVER_ID = fc.FOREST_COVER_ID
      LEFT JOIN FOREST_COVER_LAYER_ARCHIVE fcls
        ON fcls.FOREST_COVER_LAYER_CODE = 'S' AND fcls.FOREST_COVER_ID = fc.FOREST_COVER_ID
      LEFT JOIN STOCKING_STANDARD_UNIT_ARCHIVE ssu
        ON ssu.STOCKING_STANDARD_UNIT_ID = fc.STOCKING_STANDARD_UNIT_ID
      LEFT JOIN (
        SELECT
          fc.FOREST_COVER_ID,
          CASE
            WHEN layer.count_layers IS NULL OR layer.count_layers = 0 THEN 'Y'
            WHEN layer.count_SI > 0 THEN 'Y'
            ELSE 'N'
          END AS is_single_layer
        FROM FOREST_COVER_ARCHIVE fc
        LEFT JOIN (
          SELECT
              fcl.FOREST_COVER_ID,
              COUNT(*) AS count_layers,
              COUNT(CASE WHEN fcl.FOREST_COVER_LAYER_CODE IN ('S', 'I') THEN 1 END) AS count_SI
          FROM FOREST_COVER_LAYER_ARCHIVE fcl
          GROUP BY fcl.FOREST_COVER_ID
        ) layer ON fc.FOREST_COVER_ID = layer.FOREST_COVER_ID
      ) layer_summary
        ON layer_summary.FOREST_COVER_ID = fc.FOREST_COVER_ID
      WHERE
        fc.OPENING_ID = :openingId
        AND TRUNC(fc.UPDATE_TIMESTAMP) = TO_DATE(:updateDate, 'YYYY-MM-DD')
      """;

  public static final String GET_OPENING_FOREST_COVER_HISTORY_LIST_SPECIES =
      """
      SELECT
        fcls.TREE_SPECIES_CODE AS species_code,
        tsc.DESCRIPTION AS species_name
      FROM FOREST_COVER_LAYER_ARCHIVE fcl
      LEFT JOIN FOREST_COVER_LAYER_SPECIES_ARC fcls ON (fcls.FOREST_COVER_ID = fcl.FOREST_COVER_ID AND fcls.FOREST_COVER_LAYER_ID = fcl.FOREST_COVER_LAYER_ID )
      LEFT JOIN TREE_SPECIES_CODE tsc ON tsc.TREE_SPECIES_CODE = fcls.TREE_SPECIES_CODE
      WHERE
        fcl.FOREST_COVER_LAYER_CODE = :coverLayerCode
        AND fcl.FOREST_COVER_ID = :forestCoverId
        AND TRUNC(fcl.ARCHIVE_DATE) = TO_DATE(:archiveDate, 'YYYY-MM-DD')
      ORDER BY fcls.SPECIES_ORDER
      """;

  public static final String GET_OPENING_FOREST_COVER_HISTORY_POLYGON =
      """
      SELECT
        fc.FOREST_COVER_ID,
        fc.SILV_RESERVE_CODE AS reserve_code,
        src.DESCRIPTION AS reserve_name,
        fc.SILV_RESERVE_OBJECTIVE_CODE AS objective_code,
        sroc.DESCRIPTION AS objective_name,
        fc.SITE_CLASS_CODE AS site_class_code,
        scc.DESCRIPTION AS site_class_name,
        fc.SITE_INDEX AS site_index,
        fc.SITE_INDEX_SOURCE_CODE AS site_index_source_code,
        sisc.DESCRIPTION AS site_index_source_name,
        fc.TREE_COVER_PATTERN_CODE AS tree_cover_pattern_code,
        tcpc.DESCRIPTION AS tree_cover_pattern_name,
        fc.REENTRY_YEAR
      FROM FOREST_COVER_ARCHIVE fc
      LEFT JOIN SILV_RESERVE_CODE src ON src.SILV_RESERVE_CODE = fc.SILV_RESERVE_CODE
      LEFT JOIN SILV_RESERVE_OBJECTIVE_CODE sroc ON sroc.SILV_RESERVE_OBJECTIVE_CODE = fc.SILV_RESERVE_OBJECTIVE_CODE
      LEFT JOIN SITE_CLASS_CODE scc ON scc.SITE_CLASS_CODE = fc.SITE_CLASS_CODE
      LEFT JOIN SITE_INDEX_SOURCE_CODE sisc ON sisc.SITE_INDEX_SOURCE_CODE = fc.SITE_INDEX_SOURCE_CODE
      LEFT JOIN TREE_COVER_PATTERN_CODE tcpc ON tcpc.TREE_COVER_PATTERN_CODE = fc.TREE_COVER_PATTERN_CODE
      WHERE
        fc.FOREST_COVER_ID = :forestCoverId
        AND TRUNC(fc.ARCHIVE_DATE) = TO_DATE(:archiveDate, 'YYYY-MM-DD')
      """;

  public static final String GET_OPENING_FOREST_COVER_HISTORY_UNMAPPED =
      """
      SELECT
        fcnma.NON_MAPPED_AREA_ID AS unmapped_area_id,
        fcnma.NON_MAPPED_AREA AS area,
        fcnma.STOCKING_STATUS_CODE AS stocking_status_code,
        ssc.DESCRIPTION AS stocking_status_name,
        fcnma.STOCKING_TYPE_CODE AS stocking_type_code,
        stc.DESCRIPTION AS stocking_type_name
      FROM FOREST_COVER_NON_MAPPED_ARC fcnma
      LEFT JOIN STOCKING_STATUS_CODE ssc ON ssc.STOCKING_STATUS_CODE = fcnma.STOCKING_STATUS_CODE
      LEFT JOIN STOCKING_TYPE_CODE stc ON stc.STOCKING_TYPE_CODE = fcnma.STOCKING_TYPE_CODE
      WHERE
        fcnma.FOREST_COVER_ID = :forestCoverId
        AND TRUNC(fcnma.ARCHIVE_DATE) = TO_DATE(:archiveDate, 'YYYY-MM-DD')
      """;

  public static final String GET_OPENING_FOREST_COVER_HISTORY_LAYER =
      """
      SELECT
        fcl.FOREST_COVER_LAYER_ID AS layer_id,
        fcl.FOREST_COVER_LAYER_CODE AS layer_code,
        fclc.DESCRIPTION AS layer_name,
        fcl.CROWN_CLOSURE_PCT AS crown_closure,
        fcl.BASAL_AREA AS basal_area_st,
        fcl.TOTAL_STEMS_PER_HA AS total_stems,
        fcl.TOTAL_WELL_SPACED_STEMS_PER_HA AS total_well_spaced,
        fcl.WELL_SPACED_STEMS_PER_HA AS well_spaced,
        fcl.FREE_GROWING_STEMS_PER_HA AS free_growing
      FROM FOREST_COVER_LAYER_ARCHIVE fcl
      LEFT JOIN FOREST_COVER_LAYER_CODE fclc ON fclc.FOREST_COVER_LAYER_CODE = fcl.FOREST_COVER_LAYER_CODE
      WHERE
        fcl.FOREST_COVER_ID = :forestCoverId
        AND TRUNC(fcl.ARCHIVE_DATE) = TO_DATE(:archiveDate, 'YYYY-MM-DD')
      """;

  public static final String GET_OPENING_FOREST_COVER_HISTORY_DETAILS_SPECIES =
      """
      SELECT
        fcls.TREE_SPECIES_CODE AS species_code,
        tsc.DESCRIPTION AS species_name,
        fcls.TREE_SPECIES_PCT AS species_percent,
        fcls.AVG_AGE AS average_age,
        fcls.AVG_HEIGHT AS average_height
      FROM FOREST_COVER_LAYER_SPECIES_ARC fcls
      LEFT JOIN TREE_SPECIES_CODE tsc ON tsc.TREE_SPECIES_CODE = fcls.TREE_SPECIES_CODE
      WHERE
        fcls.FOREST_COVER_LAYER_ID = :forestCoverLayerId
        AND TRUNC(fcls.ARCHIVE_DATE) = TO_DATE(:archiveDate, 'YYYY-MM-DD')
      ORDER BY fcls.SPECIES_ORDER
      """;

  public static final String GET_OPENING_FOREST_COVER_HISTORY_DAMAGE =
      """
      SELECT
        fr.SILV_DAMAGE_AGENT_CODE AS damage_agent_code,
        sdac.DESCRIPTION AS damage_agent_name,
        fr.INCIDENCE_PCT AS forest_health_incidence,
        fr.INCIDENCE_AREA AS incidence_area
      FROM FORHEALTH_RSLT_ARCHIVE fr
      LEFT JOIN SILV_DAMAGE_AGENT_CODE sdac ON sdac.SILV_DAMAGE_AGENT_CODE = fr.SILV_DAMAGE_AGENT_CODE
      WHERE
        fr.FOREST_COVER_LAYER_ID = :forestCoverLayerId
        AND TRUNC(fr.ARCHIVE_DATE) = TO_DATE(:archiveDate, 'YYYY-MM-DD')
      """;
}
