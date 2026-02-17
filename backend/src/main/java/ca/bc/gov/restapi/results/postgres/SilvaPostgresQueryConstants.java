package ca.bc.gov.restapi.results.postgres;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SilvaPostgresQueryConstants {

  public static final String SILVICULTURE_SEARCH_SELECT =
	"""
	SELECT DISTINCT op.opening_id
		,cboa.forest_file_id AS forest_file_id
		,TRIM(cboa.cutting_permit_id) AS cutting_permit_id
		,cboa.timber_mark AS timber_mark
		,cboa.cut_block_id AS cut_block_id
		,TRIM(LPAD(op.mapsheet_grid,3) || mapsheet_letter || ' ' || LPAD(op.mapsheet_square,3,'0') || ' ' || op.mapsheet_quad || CASE WHEN op.mapsheet_quad IS NULL THEN NULL ELSE '.' END || op.mapsheet_sub_quad || ' ' || op.opening_number) AS mapsheep_opening_id
		,op.open_category_code AS category
		,op.opening_status_code AS status
		,op.licensee_opening_id AS licensee_opening_id
		,cboa.opening_gross_area as opening_gross_area
		,cboa.disturbance_gross_area as disturbance_gross_area
		,to_char(cboa.disturbance_start_date,'YYYY-MM-DD') as disturbance_start_date
		,ou.org_unit_code as org_unit_code
		,ou.org_unit_name as org_unit_name
		,ffc.client_number as client_number
		,ffc.client_locn_code as client_location
		,to_char(smrg.due_late_date, 'YYYY-MM-DD') as regen_delay_date
		,to_char(smfg.due_early_date, 'YYYY-MM-DD') AS early_free_growing_date
		,to_char(smfg.due_late_date, 'YYYY-MM-DD') AS late_free_growing_date
		,op.update_timestamp as update_timestamp
		,op.entry_userid as entry_user_id
		,MAX(COALESCE(sra.silv_relief_application_id, 0)) OVER() as submitted_to_frpa108
		,op.opening_number AS opening_number
	""";

  public static final String SILVICULTURE_SEARCH_FROM_JOIN =
	"""
	FROM silva.opening op
		LEFT JOIN cut_block_open_admin cboa ON (op.opening_id = cboa.opening_id AND cboa.opening_prime_licence_ind = 'Y') -- ideally, always have a matching entry for opening_id, sometimes multiples, but NOT ALL op have cboa
		LEFT JOIN org_unit ou ON (op.admin_district_no = ou.org_unit_no)  -- always have a matching entry for org_unit_no
		LEFT JOIN activity_treatment_unit atu ON (op.opening_id = atu.opening_id)
		LEFT JOIN stocking_milestone smrg ON (smrg.stocking_standard_unit_id = (SELECT ssu.stocking_standard_unit_id FROM stocking_standard_unit ssu WHERE op.opening_id = ssu.opening_id LIMIT 1) AND smrg.silv_milestone_type_code = 'RG')
		LEFT JOIN stocking_milestone smfg ON (smfg.stocking_standard_unit_id = (SELECT ssu.stocking_standard_unit_id FROM stocking_standard_unit ssu WHERE op.opening_id = ssu.opening_id LIMIT 1) AND smrg.silv_milestone_type_code = 'FG')
		LEFT JOIN silv_relief_application sra ON (atu.activity_treatment_unit_id = sra.activity_treatment_unit_id and sra.silv_relief_appl_status_code = 'APP') -- This is ours
		LEFT JOIN forest_file_client ffc ON (cboa.forest_file_id = ffc.forest_file_id AND ffc.forest_file_client_type_code = 'A')
		LEFT JOIN cut_block_client cbcr ON (cbcr.cut_block_client_type_code = 'R' AND cbcr.cb_skey = cboa.cb_skey)
		LEFT JOIN cut_block_client cbco ON (cbcr.cut_block_client_type_code = 'O' AND cbcr.cb_skey = cboa.cb_skey)
  """;

  public static final String SILVICULTURE_SEARCH_WHERE_CLAUSE =
	  """
	  WHERE
		  (
			COALESCE(:#{#filter.mainSearchTerm},'NOVALUE') = 'NOVALUE' OR (
			  (:#{#filter.mainSearchTerm} ~ '^\\d+$')
				  AND op.opening_id = CAST(:#{#filter.mainSearchTerm} AS bigint)
			  ) OR (
				  op.opening_number = :#{#filter.mainSearchTerm} OR
				  cboa.timber_mark = :#{#filter.mainSearchTerm} OR
				  cboa.forest_file_id = :#{#filter.mainSearchTerm}
			  )
			 OR
			 (
			  TRIM((LPAD(op.mapsheet_grid,3) || mapsheet_letter || ' ' || LPAD(op.mapsheet_square,3,'0') || ' ' || op.mapsheet_quad || CASE WHEN op.mapsheet_quad IS NULL THEN NULL ELSE '.' END || op.mapsheet_sub_quad || ' ' || op.opening_number)) = TRIM(:#{#filter.mainSearchTerm})
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
			  COALESCE(:#{#filter.requestUserId},'NOVALUE') = 'NOVALUE' OR op.entry_userid = :#{#filter.requestUserId}
		  )
		  AND (
			  COALESCE(:#{#filter.submittedToFrpa},'NO') = 'NO' OR (
			  COALESCE(:#{#filter.submittedToFrpa},'NO') = 'YES' AND COALESCE(sra.silv_relief_application_id, 0) > 0
			)
		  )
		  AND (
			(
				COALESCE(:#{#filter.disturbanceDateStart},'NOVALUE') = 'NOVALUE' AND COALESCE(:#{#filter.disturbanceDateEnd},'NOVALUE') = 'NOVALUE'
		  )
			OR
			(
			  cboa.disturbance_start_date IS NOT NULL AND
			  cboa.disturbance_start_date between TO_DATE(:#{#filter.disturbanceDateStart},'YYYY-MM-DD') AND TO_DATE(:#{#filter.disturbanceDateEnd},'YYYY-MM-DD')
		  )
		  )
		  AND (
			(
				COALESCE(:#{#filter.regenDelayDateStart},'NOVALUE') = 'NOVALUE' AND COALESCE(:#{#filter.regenDelayDateEnd},'NOVALUE') = 'NOVALUE'
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
				COALESCE(:#{#filter.freeGrowingDateStart},'NOVALUE') = 'NOVALUE' AND COALESCE(:#{#filter.freeGrowingDateEnd},'NOVALUE') = 'NOVALUE'
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
				COALESCE(:#{#filter.updateDateStart},'NOVALUE') = 'NOVALUE' AND COALESCE(:#{#filter.updateDateEnd},'NOVALUE') = 'NOVALUE'
		  )
			OR
			(
			  op.update_timestamp IS NOT NULL AND
			  op.update_timestamp between TO_DATE(:#{#filter.updateDateStart},'YYYY-MM-DD') AND TO_DATE(:#{#filter.updateDateEnd},'YYYY-MM-DD')
		  )
		  )
		  AND (
			  COALESCE(:#{#filter.cuttingPermitId},'NOVALUE') = 'NOVALUE' OR cboa.cutting_permit_id = :#{#filter.cuttingPermitId}
		  )
		  AND (
			  COALESCE(:#{#filter.cutBlockId},'NOVALUE') = 'NOVALUE' OR cboa.cut_block_id = :#{#filter.cutBlockId}
		  )
		  AND (
			  COALESCE(:#{#filter.timberMark},'NOVALUE') = 'NOVALUE' OR cboa.timber_mark = :#{#filter.timberMark}
		  )
		  AND (
			  COALESCE(:#{#filter.clientLocationCode},'NOVALUE') = 'NOVALUE' OR COALESCE(cbcr.client_locn_code,cbco.client_locn_code,ffc.client_locn_code) = :#{#filter.clientLocationCode}
		  )
		  AND (
			  COALESCE(:#{#filter.clientNumber},'NOVALUE') = 'NOVALUE' OR COALESCE(cbcr.client_number,cbco.client_number,ffc.client_number) = :#{#filter.clientNumber}
		  )
		  AND (
			 0 in (:openingIds) OR op.opening_id IN (:openingIds)
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
		  licensee_opening_id,
		  opening_gross_area,
		  disturbance_gross_area,
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

  public static final String PAGINATION = "OFFSET :page LIMIT :size";

	public static final String SILVICULTURE_SEARCH_EXACT_WHERE_CLAUSE =
			"""
            WHERE
                (
                    COALESCE(:#{#filter.openingId}, 0) = 0 OR op.OPENING_ID = :#{#filter.openingId}
                )
                AND (
                    'NOVALUE' in (:#{#filter.categories}) OR op.open_category_code IN (:#{#filter.categories})
                )
                AND (
                    'NOVALUE' in (:#{#filter.openingStatuses}) OR op.opening_status_code IN (:#{#filter.openingStatuses})
                )
                AND (
                  COALESCE(:#{#filter.licenseeOpeningId},'NOVALUE') = 'NOVALUE' OR op.LICENSEE_OPENING_ID = :#{#filter.licenseeOpeningId}
                )
                AND (
                    COALESCE(:#{#filter.licenseNumber},'NOVALUE') = 'NOVALUE' OR cboa.FOREST_FILE_ID = :#{#filter.licenseNumber}
                )
                AND (
                    COALESCE(:#{#filter.cutBlockId},'NOVALUE') = 'NOVALUE' OR cboa.cut_block_id = :#{#filter.cutBlockId}
                )
                AND (
                    COALESCE(:#{#filter.cuttingPermitId},'NOVALUE') = 'NOVALUE' OR cboa.cutting_permit_id = :#{#filter.cuttingPermitId}
                )
                AND (
                    COALESCE(:#{#filter.timberMark},'NOVALUE') = 'NOVALUE' OR cboa.timber_mark = :#{#filter.timberMark}
                )
                AND (
                    'NOVALUE' in (:#{#filter.orgUnits}) OR ou.org_unit_code IN (:#{#filter.orgUnits})
                )
                AND (
                    'NOVALUE' in (:#{#filter.clientNumbers}) OR ffc.client_number IN (:#{#filter.clientNumbers})
                )
                AND (
                    COALESCE(:#{#filter.requestUserId},'NOVALUE') = 'NOVALUE' OR op.ENTRY_USERID = :#{#filter.requestUserId}
                )
                AND (
                    COALESCE(:#{#filter.submittedToFrpa},'NO') = 'NO' OR (
                    COALESCE(:#{#filter.submittedToFrpa},'NO') = 'YES' AND COALESCE(sra.silv_relief_application_id, 0) > 0
                  )
                )
                AND (
                  (
                    COALESCE(:#{#filter.updateDateStart},'NOVALUE') = 'NOVALUE' AND COALESCE(:#{#filter.updateDateEnd},'NOVALUE') = 'NOVALUE'
                  )
                  OR
                  (
                    COALESCE(:#{#filter.updateDateStart},'NOVALUE') <> 'NOVALUE' AND COALESCE(:#{#filter.updateDateEnd},'NOVALUE') = 'NOVALUE' AND
                    op.UPDATE_TIMESTAMP >= TO_TIMESTAMP(:#{#filter.updateDateStart} || ' 00:00:00','YYYY-MM-DD HH24:MI:SS')
                  )
                  OR
                  (
                    COALESCE(:#{#filter.updateDateStart},'NOVALUE') = 'NOVALUE' AND COALESCE(:#{#filter.updateDateEnd},'NOVALUE') <> 'NOVALUE' AND
                    op.UPDATE_TIMESTAMP <= TO_TIMESTAMP(:#{#filter.updateDateEnd} || ' 23:59:59','YYYY-MM-DD HH24:MI:SS')
                  )
                  OR
                  (
                    COALESCE(:#{#filter.updateDateStart},'NOVALUE') <> 'NOVALUE' AND COALESCE(:#{#filter.updateDateEnd},'NOVALUE') <> 'NOVALUE' AND
                    op.UPDATE_TIMESTAMP BETWEEN TO_TIMESTAMP(:#{#filter.updateDateStart} || ' 00:00:00','YYYY-MM-DD HH24:MI:SS')
                    AND TO_TIMESTAMP(:#{#filter.updateDateEnd} || ' 23:59:59','YYYY-MM-DD HH24:MI:SS')
                  )
                )
                AND (
                    COALESCE(:#{#filter.mapsheetGrid},'NOVALUE') = 'NOVALUE' OR CAST(op.mapsheet_grid AS NUMERIC) = CAST(:#{#filter.mapsheetGrid} AS NUMERIC)
                )
                AND (
                    COALESCE(:#{#filter.mapsheetLetter},'NOVALUE') = 'NOVALUE' OR op.mapsheet_letter = :#{#filter.mapsheetLetter}
                )
                AND (
                    COALESCE(:#{#filter.mapsheetSquare},'NOVALUE') = 'NOVALUE' OR op.mapsheet_square = :#{#filter.mapsheetSquare}
                )
                AND (
                    COALESCE(:#{#filter.mapsheetQuad},'NOVALUE') = 'NOVALUE' OR op.mapsheet_quad = :#{#filter.mapsheetQuad}
                )
                AND (
                    COALESCE(:#{#filter.mapsheetSubQuad},'NOVALUE') = 'NOVALUE' OR op.mapsheet_sub_quad = :#{#filter.mapsheetSubQuad}
                )
                AND (
                    COALESCE(:#{#filter.openingNumber},'NOVALUE') = 'NOVALUE' OR TRIM(op.opening_number) = :#{#filter.openingNumber}
                )
                AND (
                   0 in (:openingIds) OR op.OPENING_ID IN (:openingIds)
                )
            """;

	public static final String SILVICULTURE_SEARCH_EXACT =
			"WITH silviculture_search AS ("
					+ SILVICULTURE_SEARCH_SELECT
					+ SILVICULTURE_SEARCH_FROM_JOIN
					+ SILVICULTURE_SEARCH_EXACT_WHERE_CLAUSE
					+ ")"
					+ SILVICULTURE_SEARCH_CTE_SELECT
					+ " FROM silviculture_search ORDER BY opening_id DESC "
					+ PAGINATION;


  public static final String OPENING_TRENDS_QUERY =
	  """
	  SELECT
		  EXTRACT(YEAR FROM o.update_timestamp) AS year,
		  EXTRACT(MONTH FROM o.update_timestamp) AS month,
		  o.opening_status_code AS status,
		  COUNT(*) AS count
	  FROM silva.opening o
	  LEFT JOIN silva.org_unit ou ON (ou.org_unit_no = o.admin_district_no)
	  WHERE
		  o.update_timestamp BETWEEN TO_TIMESTAMP(:startDate || ' 00:00:00','YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP(:endDate || ' 23:59:59','YYYY-MM-DD HH24:MI:SS')
		  AND ('NOVALUE' IN (:statusList) OR o.opening_status_code IN (:statusList))
		  AND ('NOVALUE' IN (:orgUnitList) OR ou.org_unit_code IN (:orgUnitList))
	  GROUP BY
		  EXTRACT(YEAR FROM o.update_timestamp),
		  EXTRACT(MONTH FROM o.update_timestamp),
		  o.opening_status_code
	  ORDER BY year, month""";

  public static final String GET_OPENING_TOMBSTONE =
	"""
	SELECT
		op.opening_id,
		(LPAD(op.mapsheet_grid,3) || mapsheet_letter || ' ' || LPAD(op.mapsheet_square,3,'0') || ' ' || op.mapsheet_quad || CASE WHEN op.mapsheet_quad IS NULL THEN NULL ELSE '.' END || op.mapsheet_sub_quad || ' ' || op.opening_number) AS opening_number,
		op.opening_status_code,
		osc.description AS opening_status_name,
		ou.org_unit_no AS org_unit_number,
		ou.org_unit_code,
		ou.org_unit_name,
		op.open_category_code,
		occ.description AS open_category_name,
		ffc.client_number AS client, -- load details FROM FCApi
		cboa.forest_file_id AS file_id,
		cboa.cut_block_id,
		cboa.cutting_permit_id,
		cboa.timber_mark,
		op.max_allow_permnt_access_pct AS  max_allowed_access, --max allowed permanent access FROM inquiry
		cboa.opening_gross_area,
		op.entry_userid AS created_by,
		to_char(op.entry_timestamp,'YYYY-MM-DD') AS created_on,
		op.update_timestamp AS last_updated_on, -- needs TO be ON any of the related date
		to_char(cboa.disturbance_start_date,'YYYY-MM-DD') AS disturbance_start_date
	FROM opening op
	LEFT JOIN org_unit ou ON ou.org_unit_no = op.admin_district_no
	LEFT JOIN cut_block_open_admin cboa ON (cboa.opening_id = op.opening_id AND cboa.opening_prime_licence_ind = 'Y')
	LEFT JOIN forest_file_client ffc ON (cboa.forest_file_id = ffc.forest_file_id AND ffc.forest_file_client_type_code = 'A')
	LEFT JOIN open_category_code occ ON (occ.open_category_code = op.open_category_code)
	LEFT JOIN opening_status_code osc ON osc.opening_status_code = op.opening_status_code
	WHERE op.opening_id = :openingId
	""";

	public static final String GET_OPENING_OVERVIEW_OPENING =
			"""
			SELECT
					op.licensee_opening_id,
					pfu.file_type_code AS tenure_type_code,
					ftc.description AS tenure_type_name,
					pfu.mgmt_unit_type AS management_unit_type_code,
					mutc.description AS management_unit_type_name,
					pfu.mgmt_unit_id AS management_unit_id,
					outsb.org_unit_code AS timber_sale_office_code,
					outsb.org_unit_name AS timber_sale_office_name
			FROM opening op
			LEFT JOIN opening_status_code osc ON osc.opening_status_code = op.opening_status_code
			LEFT JOIN cut_block_open_admin cboa ON (cboa.opening_id = op.opening_id AND cboa.opening_prime_licence_ind = 'Y')
			LEFT JOIN prov_forest_use pfu ON (cboa.forest_file_id = pfu.forest_file_id)
			LEFT JOIN org_unit outsb ON (outsb.org_unit_no = (SELECT hs.bcts_org_unit FROM silva.harvest_sale hs WHERE cboa.forest_file_id = hs.forest_file_id LIMIT 1))
			LEFT JOIN file_type_code ftc ON (pfu.file_type_code = ftc.file_type_code)
			LEFT JOIN mgmt_unit_type_code mutc ON (pfu.mgmt_unit_type = mutc.mgmt_unit_type_code)
			WHERE op.opening_id = :openingId""";

	public static final String GET_OPENING_OVERVIEW_MILESTONE =
			"""
			SELECT
				ssu.standards_unit_id,
					to_char(smph.declared_date,'YYYY-MM-DD') AS post_harverst_declared_date,
					to_char(smrg.declared_date,'YYYY-MM-DD') AS regen_declared_date,
					smrg.late_offset_years AS  regen_offset_years,
					to_char(smrg.due_late_date,'YYYY-MM-DD') AS regen_due_date,
					to_char(smfg.declared_date,'YYYY-MM-DD') AS free_growing_declared_date,
					smfg.late_offset_years AS free_growing_offset_years,
					to_char(smfg.due_late_date,'YYYY-MM-DD') AS free_growing_due_date
			FROM opening op
			LEFT JOIN stocking_standard_unit ssu ON ssu.opening_id = op.opening_id
			LEFT JOIN silva.stocking_milestone smrg ON (smrg.stocking_standard_unit_id = ssu.stocking_standard_unit_id AND smrg.silv_milestone_type_code = 'RG')
			LEFT JOIN silva.stocking_milestone smfg ON (smfg.stocking_standard_unit_id = ssu.stocking_standard_unit_id AND smfg.silv_milestone_type_code = 'FG')
			LEFT JOIN silva.stocking_milestone smph ON (smph.stocking_standard_unit_id = ssu.stocking_standard_unit_id AND smph.silv_milestone_type_code = 'PH')
			WHERE op.opening_id = :openingId
			ORDER BY ssu.entry_timestamp
			LIMIT 1""";

	public static final String GET_COMMENTS =
			"""
			SELECT
				sc.silv_comment_source_code as comment_source_code,
				scsc.description AS comment_source_name,
				sc.silv_comment_type_code as comment_type_code,
				sctc.description AS comment_type_name,
				sc.comment_text
			FROM silviculture_comment sc
			LEFT JOIN silv_comment_source_code scsc ON (sc.silv_comment_source_code = scsc.silv_comment_source_code )
			LEFT JOIN silv_comment_type_code sctc ON (sc.silv_comment_type_code = sctc.silv_comment_type_code )
			LEFT JOIN opening_comment_link ocl ON (sc.silviculture_comment_id = ocl.silviculture_comment_id )
			LEFT JOIN activity_tu_comment_link atcl ON (sc.silviculture_comment_id = atcl.silviculture_comment_id )
			LEFT JOIN stocking_comment_link scl ON (sc.silviculture_comment_id = scl.silviculture_comment_id )
			LEFT JOIN stocking_milestone_cmt_link smcl ON (sc.silviculture_comment_id = smcl.silviculture_comment_id )
			LEFT JOIN silv_project_comment_link spcl ON (sc.silviculture_comment_id = spcl.silviculture_comment_id )
			WHERE
			ocl.opening_id = :openingId
			OR atcl.activity_treatment_unit_id = :atuId
			OR scl.stocking_standard_unit_id = :ssuId
			OR smcl.stocking_standard_unit_id = :ssuMId
			OR spcl.silviculture_project_id = :projectId
			ORDER BY comment_date DESC""";

	public static final String GET_OPENING_SS =
			"""
			SELECT
				ssu.standards_unit_id AS stocking_standard_unit,
				ssu.stocking_standard_unit_id AS ssuid,
				ssu.standards_regime_id as srid,
				CASE WHEN COALESCE(sr.mof_default_standard_ind, 'N') = 'Y' THEN 'true' ELSE 'false' END AS default_mof,
				CASE WHEN COALESCE(ssu.stocking_standard_unit_id, 0) = 0 THEN 'true' ELSE 'false' END AS manual_entry,
				fsp.fsp_id,
				ssu.net_area,
				ssu.max_allow_soil_disturbance_pct AS soil_disturbance_percent,
				se.bgc_zone_code AS bec_zone_code,
				se.bgc_subzone_code AS bec_subzone_code,
				se.bgc_variant AS bec_variant,
				se.bgc_phase AS bec_phase,
				se.bec_site_series AS bec_site_series,
				se.bec_site_type AS bec_site_type,
				se.bec_seral AS bec_seral,
				sr.regen_delay_offset_yrs AS regen_delay,
				sr.free_growing_late_offset_yrs AS free_growing_late,
				sr.free_growing_early_offset_yrs AS free_growing_early,
				sr.additional_standards
			FROM stocking_standard_unit ssu
			LEFT JOIN stocking_ecology se ON (se.opening_id = ssu.opening_id AND se.stocking_standard_unit_id = ssu.stocking_standard_unit_id)
			LEFT JOIN standards_regime sr ON (sr.standards_regime_id = ssu.standards_regime_id)
			LEFT JOIN fsp_standards_regime_xref fspxref ON (fspxref.standards_regime_id = ssu.standards_regime_id)
			LEFT JOIN forest_stewardship_plan fsp ON (fsp.fsp_id = fspxref.fsp_id AND fsp.fsp_amendment_number = fspxref.fsp_amendment_number)
			WHERE ssu.opening_id = :openingId
			ORDER BY ssu.standards_unit_id""";

	public static final String GET_OPENING_SS_SPECIES =
			"""
			SELECT
				sl.stocking_layer_code as layer_code,
				sls.silv_tree_species_code AS species_code,
				stsc.description AS species_name,
				sls.min_height AS min_height
			FROM stocking_layer sl
			LEFT JOIN stocking_layer_species sls ON (sls.stocking_layer_id = sl.stocking_layer_id)
			LEFT JOIN silv_tree_species_code stsc ON (stsc.silv_tree_species_code = sls.silv_tree_species_code)
			WHERE sl.opening_id = :openingId AND sls.preferred_ind = :preferred AND sl.stocking_standard_unit_id = :ssuId
			ORDER BY sls.species_order""";

	public static final String GET_OPENING_SS_LAYER =
			"""
			SELECT
				sl.stocking_layer_code AS layer_code,
				slc.description AS layer_name,
				sl.min_stocking_standard AS min_wellspaced_trees,
				sl.min_pref_stocking_standard AS min_preferred_wellspaced_trees,
				sl.min_horizontal_distance AS min_horizontal_distance_wellspaced_trees,
				sl.target_stocking AS target_wellspaced_trees,
				sl.residual_basal_area AS min_residual_basal_area,
				sl.min_post_spacing AS min_postspacing_density,
				sl.max_post_spacing AS max_postspacing_density,
				sl.max_conifer AS max_coniferous,
				sl.hght_relative_to_comp AS height_relative_to_comp
			FROM stocking_layer sl
			LEFT JOIN stocking_layer_code slc ON sl.stocking_layer_code = slc.stocking_layer_code
			WHERE sl.opening_id = :openingId AND sl.stocking_standard_unit_id = :ssuId
			ORDER BY sl.stocking_layer_code DESC""";

	public static final String GET_OPENING_SS_MILESTONES =
			"""
					SELECT
						sm.stocking_standard_unit_id AS ssuid,
						MAX(CASE WHEN sm.silv_milestone_type_code = 'PH' THEN TO_CHAR(sm.declared_date, 'YYYY-MM-DD') END) AS post_harvest_declared_date,
						MAX(CASE WHEN sm.silv_milestone_type_code = 'RG' THEN TO_CHAR(sm.declared_date, 'YYYY-MM-DD') END) AS regen_declared_date,
						MAX(CASE WHEN sm.silv_milestone_type_code = 'RG' THEN sm.late_offset_years END) AS regen_offset_years,
						MAX(CASE WHEN sm.silv_milestone_type_code = 'RG' THEN TO_CHAR(sm.due_late_date, 'YYYY-MM-DD') END) AS regen_due_date,
						MAX(CASE WHEN sm.silv_milestone_type_code = 'NR' THEN TO_CHAR(sm.declared_date, 'YYYY-MM-DD') END) AS no_regen_declared_date,
						MAX(CASE WHEN sm.silv_milestone_type_code = 'NR' THEN sm.late_offset_years END) AS no_regen_offset_years,
						MAX(CASE WHEN sm.silv_milestone_type_code = 'NR' THEN TO_CHAR(sm.due_late_date, 'YYYY-MM-DD') END) AS no_regen_due_date,
						MAX(CASE WHEN sm.silv_milestone_type_code = 'FG' THEN TO_CHAR(sm.declared_date, 'YYYY-MM-DD') END) AS free_growing_declared_date,
						MAX(CASE WHEN sm.silv_milestone_type_code = 'FG' THEN sm.late_offset_years END) AS free_growing_offset_years,
						MAX(CASE WHEN sm.silv_milestone_type_code = 'FG' THEN TO_CHAR(sm.due_late_date, 'YYYY-MM-DD') END) AS free_growing_due_date,
						CASE
							WHEN MAX(CASE WHEN sm.silv_milestone_type_code = 'NR' THEN 1 ELSE 0 END) = 1 THEN 'true'
							ELSE 'false'
						END AS no_regen_indicated,
						CASE
							WHEN MAX(CASE WHEN sm.extent_feasible_declared_ind = 'N' THEN 0 ELSE 1 END) = 1 THEN 'true'
							ELSE 'false'
						END AS extent_declared
					FROM silva.stocking_milestone sm
					WHERE sm.stocking_standard_unit_id = :ssuId
					GROUP BY sm.stocking_standard_unit_id""";

	public static final String GET_OPENING_SS_NOTIFICATIONS =
			"""
		SELECT
			ssu.stocking_standard_unit_id,
			ssu.standards_unit_id,
			sm.silv_milestone_type_code,
			sm.due_late_date,
			CASE
				WHEN sm.due_late_date < CURRENT_TIMESTAMP AND sm.silv_milestone_type_code = 'FG' THEN 'ERROR'
				WHEN sm.due_late_date < CURRENT_TIMESTAMP AND sm.silv_milestone_type_code = 'RG' THEN 'INFO'
				WHEN sm.due_late_date BETWEEN CURRENT_TIMESTAMP AND (CURRENT_TIMESTAMP + INTERVAL '12 months') THEN 'WARNING'
			END AS notification_type
		FROM silva.stocking_standard_unit ssu
		JOIN silva.stocking_milestone sm
			ON ssu.stocking_standard_unit_id = sm.stocking_standard_unit_id
		WHERE ssu.opening_id = :openingId
			AND (sm.declare_ind IS NULL OR sm.declare_ind = 'N')
			AND (
				sm.due_late_date < CURRENT_TIMESTAMP
				OR sm.due_late_date BETWEEN CURRENT_TIMESTAMP AND (CURRENT_TIMESTAMP + INTERVAL '12 months')
			)
			AND (sm.silv_milestone_type_code = 'FG' OR sm.silv_milestone_type_code = 'RG')""";

		public static final String GET_OPENING_ACTIVITIES_DISTURBANCE =
				"""
				SELECT
					atu.activity_treatment_unit_id AS atu_id,
					atu.disturbance_code AS disturbance_code,
					dc.description AS disturbance_name,
					atu.silv_system_code AS system_code,
					ssc.description AS system_name,
					atu.silv_system_variant_code AS variant_code,
					ssvc.description AS variant_name,
					atu.silv_cut_phase_code AS cut_phase_code,
					scpc.description AS cut_phase_name,
					atu.treatment_amount AS disturbance_area,
					to_char(atu.update_timestamp,'YYYY-MM-DD') AS last_update,
					to_char(atu.atu_start_date,'YYYY-MM-DD') AS start_date,
					to_char(atu.atu_completion_date,'YYYY-MM-DD') AS end_date,
					atu.activity_licensee_id AS licensee_activity_id,
					ffc.client_number AS disturbance_location_client,
					ffc.client_locn_code AS disturbance_location_code,
					cboa.forest_file_id AS licence_number,
					cboa.cutting_permit_id AS cutting_permit,
					cboa.cut_block_id AS cut_block
				FROM activity_treatment_unit atu
				LEFT JOIN disturbance_code dc ON atu.disturbance_code = dc.disturbance_code
				LEFT JOIN silv_system_code ssc ON ssc.silv_system_code = atu.silv_system_code
				LEFT JOIN silv_system_variant_code ssvc ON ssvc.silv_system_variant_code = atu.silv_system_variant_code
				LEFT JOIN silv_cut_phase_code scpc ON scpc.silv_cut_phase_code = atu.silv_cut_phase_code
				LEFT JOIN cut_block_open_admin cboa ON cboa.cut_block_open_admin_id = atu.cut_block_open_admin_id
				LEFT JOIN forest_file_client ffc ON (ffc.forest_file_id = cboa.forest_file_id AND ffc.forest_file_client_type_code = 'A')
				WHERE atu.silv_base_code = 'DN' AND atu.opening_id = :openingId""";

		public static final String GET_OPENING_ACTIVITIES_DISTURBANCE_COUNT =
				"""
				SELECT count(atu.activity_treatment_unit_id)
					FROM activity_treatment_unit atu
					WHERE atu.silv_base_code = 'DN' AND atu.opening_id = :openingId""";

		public static final String GET_OPENING_ACTIVITIES_ACTIVITIES =
				"""
				SELECT
					atu.activity_treatment_unit_id AS atu_id,
					CASE
								WHEN atu.atu_completion_date IS NOT NULL THEN 'CPT'
								ELSE 'PLN'
						END AS status_code,
					atu.silv_base_code AS base_code,
					sbc.description AS base_name,
					CASE WHEN atu.atu_completion_date IS NOT NULL THEN atu.silv_technique_code ELSE atu.plan_silv_technique_code END AS tech_code,
					stc.description AS tech_name,
					CASE WHEN atu.atu_completion_date IS NOT NULL THEN atu.silv_method_code ELSE atu.plan_silv_method_code END AS method_code,
					smc.description AS method_name,
					CASE WHEN atu.atu_completion_date IS NOT NULL THEN atu.silv_objective_code_1 ELSE atu.plan_silv_objective_code_1 END AS objective1_code,
					soc1.description AS objective1_name,
					CASE WHEN atu.atu_completion_date IS NOT NULL THEN atu.silv_objective_code_2 ELSE atu.plan_silv_objective_code_2 END AS objective2_code,
					soc2.description AS objective2_name,
					CASE WHEN atu.atu_completion_date IS NOT NULL THEN atu.silv_objective_code_3 ELSE atu.plan_silv_objective_code_3 END AS objective3_code,
					soc3.description AS objective3_name,
					atu.treatment_amount AS area,
					atu.silv_fund_srce_code AS funding_code,
					sfsc.description AS funding_name,
					atu.silviculture_project_id AS project_id,
					to_char(atu.update_timestamp,'YYYY-MM-DD') AS last_update,
					to_char(atu.planned_date,'YYYY-MM-DD') AS planned_date,
					to_char(atu.atu_completion_date,'YYYY-MM-DD') AS end_date
				FROM activity_treatment_unit atu
				LEFT JOIN silv_base_code sbc ON sbc.silv_base_code = atu.silv_base_code
				LEFT JOIN silv_technique_code stc ON stc.silv_technique_code = CASE WHEN atu.atu_completion_date IS NOT NULL THEN atu.silv_technique_code ELSE atu.plan_silv_technique_code END
				LEFT JOIN silv_method_code smc ON smc.silv_method_code = CASE WHEN atu.atu_completion_date IS NOT NULL THEN atu.silv_method_code ELSE atu.plan_silv_method_code END
				LEFT JOIN silv_objective_code soc1 ON soc1.silv_objective_code = CASE WHEN atu.atu_completion_date IS NOT NULL THEN atu.silv_objective_code_1 ELSE atu.plan_silv_objective_code_1 END
				LEFT JOIN silv_objective_code soc2 ON soc2.silv_objective_code = CASE WHEN atu.atu_completion_date IS NOT NULL THEN atu.silv_objective_code_2 ELSE atu.plan_silv_objective_code_2 END
				LEFT JOIN silv_objective_code soc3 ON soc3.silv_objective_code = CASE WHEN atu.atu_completion_date IS NOT NULL THEN atu.silv_objective_code_3 ELSE atu.plan_silv_objective_code_3 END
				LEFT JOIN silv_fund_srce_code sfsc ON sfsc.silv_fund_srce_code = atu.silv_fund_srce_code
				WHERE
					atu.silv_base_code != 'DN'
					AND atu.opening_id = :openingId
					AND (
					 COALESCE(:mainSearchTerm,'NOVALUE') = 'NOVALUE' OR (
						 atu.silv_base_code like '%' || :mainSearchTerm || '%'
						 OR CAST(atu.activity_treatment_unit_id AS text) LIKE '%' || :mainSearchTerm || '%'
						 OR upper(sbc.description) like '%' || :mainSearchTerm || '%'
						 OR atu.silv_technique_code like '%' || :mainSearchTerm || '%'
						 OR atu.plan_silv_technique_code like '%' || :mainSearchTerm || '%'
						 OR upper(stc.description) like '%' || :mainSearchTerm || '%'
						 OR atu.silv_method_code like '%' || :mainSearchTerm || '%'
						 OR atu.plan_silv_method_code like '%' || :mainSearchTerm || '%'
						 OR upper(smc.description) like '%' || :mainSearchTerm || '%'
						 OR atu.silv_objective_code_1 like '%' || :mainSearchTerm || '%'
						 OR atu.plan_silv_objective_code_1 like '%' || :mainSearchTerm || '%'
						 OR upper(soc1.description) like '%' || :mainSearchTerm || '%'
						 OR atu.silv_objective_code_2 like '%' || :mainSearchTerm || '%'
						 OR atu.plan_silv_objective_code_2 like '%' || :mainSearchTerm || '%'
						 OR upper(soc2.description) like '%' || :mainSearchTerm || '%'
						 OR atu.silv_objective_code_3 like '%' || :mainSearchTerm || '%'
						 OR atu.plan_silv_objective_code_3 like '%' || :mainSearchTerm || '%'
						 OR upper(soc3.description) like '%' || :mainSearchTerm || '%'
						 OR atu.silv_fund_srce_code like '%' || :mainSearchTerm || '%'
						 OR upper(sfsc.description) like '%' || :mainSearchTerm || '%'
						 OR CAST(atu.silviculture_project_id AS text) like '%' || :mainSearchTerm || '%'
						 OR (:mainSearchTerm ~ '^\\d+(\\.\\d+)?$' AND atu.treatment_amount = CAST(:mainSearchTerm AS numeric))
					 )
					)""";

		public static final String GET_OPENING_ACTIVITIES_ACTIVITIES_COUNT =
				"""
				SELECT COUNT(atu.activity_treatment_unit_id)
				FROM (
					SELECT
						activity_treatment_unit_id,
						opening_id,
						silv_base_code,
						atu_completion_date,
						CASE WHEN atu_completion_date IS NOT NULL THEN 'CPT' ELSE 'PLN' END AS status_code,
						CASE WHEN atu_completion_date IS NOT NULL THEN silv_technique_code ELSE plan_silv_technique_code END AS tech_code_unified,
						CASE WHEN atu_completion_date IS NOT NULL THEN silv_method_code ELSE plan_silv_method_code END AS method_code_unified,
						CASE WHEN atu_completion_date IS NOT NULL THEN silv_objective_code_1 ELSE plan_silv_objective_code_1 END AS objective1_code_unified,
						CASE WHEN atu_completion_date IS NOT NULL THEN silv_objective_code_2 ELSE plan_silv_objective_code_2 END AS objective2_code_unified,
						CASE WHEN atu_completion_date IS NOT NULL THEN silv_objective_code_3 ELSE plan_silv_objective_code_3 END AS objective3_code_unified,
						silv_technique_code,
						plan_silv_technique_code,
						silv_method_code,
						plan_silv_method_code,
						silv_objective_code_1,
						plan_silv_objective_code_1,
						silv_objective_code_2,
						plan_silv_objective_code_2,
						silv_objective_code_3,
						plan_silv_objective_code_3,
						silv_fund_srce_code,
						silviculture_project_id,
						treatment_amount
					FROM activity_treatment_unit
				) atu
				LEFT JOIN silv_base_code sbc ON sbc.silv_base_code = atu.silv_base_code
				LEFT JOIN silv_technique_code stc ON stc.silv_technique_code = atu.tech_code_unified
				LEFT JOIN silv_method_code smc ON smc.silv_method_code = atu.method_code_unified
				LEFT JOIN silv_objective_code soc1 ON soc1.silv_objective_code = atu.objective1_code_unified
				LEFT JOIN silv_objective_code soc2 ON soc2.silv_objective_code = atu.objective2_code_unified
				LEFT JOIN silv_objective_code soc3 ON soc3.silv_objective_code = atu.objective3_code_unified
				LEFT JOIN silv_fund_srce_code sfsc ON sfsc.silv_fund_srce_code = atu.silv_fund_srce_code
				WHERE
					atu.silv_base_code != 'DN'
					AND atu.opening_id = :openingId
					AND (
						COALESCE(:mainSearchTerm,'NOVALUE') = 'NOVALUE' OR (
							atu.silv_base_code LIKE '%' || :mainSearchTerm || '%'
							OR upper(sbc.description) LIKE '%' || :mainSearchTerm || '%'
							OR atu.silv_technique_code LIKE '%' || :mainSearchTerm || '%'
							OR CAST(atu.activity_treatment_unit_id AS text) LIKE '%' || :mainSearchTerm || '%'
							OR atu.plan_silv_technique_code LIKE '%' || :mainSearchTerm || '%'
							OR upper(stc.description) LIKE '%' || :mainSearchTerm || '%'
							OR atu.silv_method_code LIKE '%' || :mainSearchTerm || '%'
							OR atu.plan_silv_method_code LIKE '%' || :mainSearchTerm || '%'
							OR upper(smc.description) LIKE '%' || :mainSearchTerm || '%'
							OR atu.silv_objective_code_1 LIKE '%' || :mainSearchTerm || '%'
							OR atu.plan_silv_objective_code_1 LIKE '%' || :mainSearchTerm || '%'
							OR upper(soc1.description) LIKE '%' || :mainSearchTerm || '%'
							OR atu.silv_objective_code_2 LIKE '%' || :mainSearchTerm || '%'
							OR atu.plan_silv_objective_code_2 LIKE '%' || :mainSearchTerm || '%'
							OR upper(soc2.description) LIKE '%' || :mainSearchTerm || '%'
							OR atu.silv_objective_code_3 LIKE '%' || :mainSearchTerm || '%'
							OR atu.plan_silv_objective_code_3 LIKE '%' || :mainSearchTerm || '%'
							OR upper(soc3.description) LIKE '%' || :mainSearchTerm || '%'
							OR atu.silv_fund_srce_code LIKE '%' || :mainSearchTerm || '%'
							OR upper(sfsc.description) LIKE '%' || :mainSearchTerm || '%'
							OR CAST(atu.silviculture_project_id AS text) LIKE '%' || :mainSearchTerm || '%'
							OR (:mainSearchTerm ~ '^\\d+(\\.\\d+)?$' AND atu.treatment_amount = CAST(:mainSearchTerm AS numeric))
						)
					)""";

		public static final String GET_OPENING_ACTIVITIES_BASE =
				"""
				SELECT
					atu.activity_licensee_id AS licensee_activity_id,
					atu.fia_project_id AS intra_agency_number,
					ffc.client_number AS activity_client,
					ffc.client_locn_code AS activity_location,
					atu.planned_treatment_amount AS planned_amount,
					atu.treatment_amount AS treated_amount,
					atu.planned_treatment_cost AS planned_cost,
					atu.actual_treatment_cost AS actual_cost,
					atu.silv_base_code AS kind,
					atu.act_planted_no AS total_planting
				FROM activity_treatment_unit atu
				LEFT JOIN cut_block_open_admin cboa ON cboa.cut_block_open_admin_id = atu.cut_block_open_admin_id
				LEFT JOIN forest_file_client ffc ON (ffc.forest_file_id = cboa.forest_file_id AND ffc.forest_file_client_type_code = 'A')
				WHERE
					atu.opening_id = :openingId
					AND atu.activity_treatment_unit_id = :atuId""";

		public static final String GET_OPENING_ACTIVITIES_SU =
				"""
				SELECT
					CASE
								WHEN atu.atu_completion_date IS NOT NULL THEN atu.survey_actual_num_plots
								WHEN atu.planned_date IS NOT NULL THEN atu.survey_planned_num_plots
								ELSE 0
						END AS plots_count,
						atu.survey_min_plots_per_stratum
				FROM activity_treatment_unit atu
				LEFT JOIN cut_block_open_admin cboa ON cboa.cut_block_open_admin_id = atu.cut_block_open_admin_id
				LEFT JOIN forest_file_client ffc ON (ffc.forest_file_id = cboa.forest_file_id AND ffc.forest_file_client_type_code = 'A')
				WHERE
					atu.silv_base_code = 'SU'
					AND atu.opening_id = :openingId
					AND atu.activity_treatment_unit_id = :atuId
				ORDER BY atu.activity_tu_seq_no""";

		public static final String GET_OPENING_ACTIVITY_SPECIES =
				"""
				SELECT
					pr.silv_tree_species_code AS species_code,
					stsc.description AS species_name,
					pr.number_planted AS planted_number,
					COALESCE(pr.planted_no_beyond_xfer_limit,0) AS number_beyond_transfer_limit,
					CASE
						WHEN pr.climate_based_seed_xfer_ind = 'Y' THEN 'true'
						ELSE 'false'
					END AS cbst,
					pr.request_skey AS request_id,
					COALESCE(pr.seedlot_number,pr.veg_lot_id) AS lot,
					pr.bid_price_per_tree AS bid_price_per_tree
				FROM planting_rslt pr
				LEFT JOIN silv_tree_species_code stsc ON stsc.silv_tree_species_code = pr.silv_tree_species_code
				WHERE pr.activity_treatment_unit_id =:atuId""";

		public static final String GET_OPENING_ACTIVITY_JS =
				"""
				SELECT
					atu.inter_tree_target_distance AS target_intertree_distance,
					atu.inter_tree_variation AS allowable_variation_distance,
					atu.max_trees_per_plot AS allowable_tree_per_lot,
					atu.max_trees_per_ha AS spacing_per_ha
				FROM opening op
				LEFT JOIN activity_treatment_unit atu ON atu.opening_id = op.opening_id
				LEFT JOIN cut_block_open_admin cboa ON cboa.cut_block_open_admin_id = atu.cut_block_open_admin_id
				LEFT JOIN forest_file_client ffc ON (ffc.forest_file_id = cboa.forest_file_id AND ffc.forest_file_client_type_code = 'A')
				WHERE
					atu.silv_base_code = 'JS'
					AND op.opening_id = :openingId
					AND atu.activity_treatment_unit_id = :atuId
				ORDER BY atu.activity_tu_seq_no""";

		public static final String GET_OPENING_ACTIVITY_PR =
				"""
				SELECT
					atu.total_stems_per_ha AS total_stems_per_ha,
					atu.stems_to_prune AS stems_per_ha_to_prune,
					atu.inter_tree_target_distance AS target_intertree_distance,
					atu.inter_tree_min_distance AS minimum_intertree_distance,
					atu.prune_height AS height_above_ground,
					atu.pruning_min_crown_pct AS minimum_live_crown
				FROM activity_treatment_unit atu
				LEFT JOIN cut_block_open_admin cboa ON cboa.cut_block_open_admin_id = atu.cut_block_open_admin_id
				LEFT JOIN forest_file_client ffc ON (ffc.forest_file_id = cboa.forest_file_id AND ffc.forest_file_client_type_code = 'A')
				WHERE
					atu.silv_base_code = 'PR'
					AND atu.opening_id = :openingId
					AND atu.activity_treatment_unit_id = :atuId
				ORDER BY atu.activity_tu_seq_no""";

		public static final String GET_OPENING_ACTIVITY_SP =
				"""
				SELECT
					atu.target_prepared_spots
				FROM activity_treatment_unit atu
				WHERE
					atu.silv_base_code = 'SP'
					AND atu.opening_id = :openingId AND
					atu.activity_treatment_unit_id = :atuId
				ORDER BY atu.activity_tu_seq_no""";

		public static final String GET_OPENING_TENURES =
				"""
				SELECT
					cboa.cut_block_open_admin_id AS id,
					CASE WHEN COALESCE(cboa.opening_prime_licence_ind, 'N') = 'Y' THEN 'true' ELSE 'false' END AS primary_tenure,
					cboa.forest_file_id AS file_id,
					cboa.cut_block_id AS cut_block,
					cboa.cutting_permit_id AS cutting_permit,
					cboa.timber_mark AS timber_mark,
					cb.block_status_st AS status_code,
					bsc.description AS status_name,
					cboa.planned_gross_block_area AS planned_gross_area,
					cboa.planned_net_block_area AS planned_net_area
				FROM cut_block_open_admin cboa
				LEFT JOIN cut_block cb ON cb.cb_skey = cboa.cb_skey
				LEFT JOIN block_status_code bsc ON bsc.block_status_code = cb.block_status_st
				WHERE
						cboa.opening_id = :openingId
						AND (
							COALESCE(:mainSearchTerm,'NOVALUE') = 'NOVALUE' OR (
								cboa.forest_file_id like '%' || :mainSearchTerm || '%'
								OR cboa.cut_block_id like '%' || :mainSearchTerm || '%'
								OR cboa.cutting_permit_id like '%' || :mainSearchTerm || '%'
								OR cboa.timber_mark like '%' || :mainSearchTerm || '%'
								OR cb.block_status_st like '%' || :mainSearchTerm || '%'
								OR upper(bsc.description) like '%' || :mainSearchTerm || '%'
								OR (:mainSearchTerm ~ '^\\d+(\\.\\d+)?$' AND cboa.planned_gross_block_area = CAST(:mainSearchTerm AS numeric))
								OR (:mainSearchTerm ~ '^\\d+(\\.\\d+)?$' AND cboa.planned_net_block_area = CAST(:mainSearchTerm AS numeric))
							)
						)""";

		public static final String GET_OPENING_TENURES_COUNT =
				"""
				SELECT count(1)
					FROM cut_block_open_admin cboa
						LEFT JOIN cut_block cb ON cb.cb_skey = cboa.cb_skey
						LEFT JOIN block_status_code bsc ON bsc.block_status_code = cb.block_status_st
					WHERE
							cboa.opening_id = :openingId
							AND (
								COALESCE(:mainSearchTerm,'NOVALUE') = 'NOVALUE' OR (
					 		cboa.forest_file_id like '%' || :mainSearchTerm || '%'
					 		OR cboa.cut_block_id like '%' || :mainSearchTerm || '%'
					 		OR cboa.cutting_permit_id like '%' || :mainSearchTerm || '%'
					 		OR cboa.timber_mark like '%' || :mainSearchTerm || '%'
					 		OR cb.block_status_st like '%' || :mainSearchTerm || '%'
					 		OR upper(bsc.description) like '%' || :mainSearchTerm || '%'
					 		OR (:mainSearchTerm ~ '^\\d+(\\.\\d+)?$' AND cboa.planned_gross_block_area = CAST(:mainSearchTerm AS numeric))
					 		OR (:mainSearchTerm ~ '^\\d+(\\.\\d+)?$' AND cboa.planned_net_block_area = CAST(:mainSearchTerm AS numeric))
					 	)
					)""";

		public static final String GET_OPENING_TENURE_PRIME =
				"""
						SELECT
							cboa.cut_block_open_admin_id AS id,
							CASE WHEN COALESCE(cboa.opening_prime_licence_ind, 'N') = 'Y' THEN 'true' ELSE 'false' END AS primary_tenure,
								 cboa.forest_file_id AS file_id,
								 cboa.cut_block_id AS cut_block,
								 cboa.cutting_permit_id AS cutting_permit,
								 cboa.timber_mark AS timber_mark,
								 cb.block_status_st AS status_code,
								 bsc.description AS status_name,
								 cboa.planned_gross_block_area AS planned_gross_area,
								 cboa.planned_net_block_area AS planned_net_area
						 FROM cut_block_open_admin cboa
						 LEFT JOIN cut_block cb ON cb.cb_skey = cboa.cb_skey
						 LEFT JOIN block_status_code bsc ON bsc.block_status_code = cb.block_status_st
						 WHERE cboa.opening_id = :openingId AND cboa.opening_prime_licence_ind = 'Y'""";

		public static final String GET_OPENING_FOREST_COVER_LIST =
				"""
						SELECT
								fc.forest_cover_id AS cover_id,
								fc.silv_polygon_no AS polygon_id,
								ssu.standards_unit_id AS standard_unit_id,
								fcnma.stocking_type_code AS unmapped_code,
								stcnma.description AS unmapped_name,
								fc.silv_polygon_area AS gross_area,
								fc.silv_polygon_net_area AS net_area,
								fc.stocking_status_code AS status_code,
								ssc.description AS status_name,
								fc.stocking_type_code AS type_code,
								stc.description AS type_name,
								fcli.total_stems_per_ha AS total,
								fcli.total_well_spaced_stems_per_ha AS inventory_total_well_spaced,
								fcli.well_spaced_stems_per_ha AS inventory_well_spaced,
								fcli.free_growing_stems_per_ha AS inventory_free_growing,
								fcls.total_well_spaced_stems_per_ha AS silviculture_total_well_spaced,
								fcls.well_spaced_stems_per_ha AS silviculture_well_spaced,
								fcls.free_growing_stems_per_ha AS silviculture_free_growing,
								fc.reference_year,
								layer_summary.is_single_layer AS is_single_layer,
								fc.silv_reserve_code AS reserve_code,
								fc.silv_reserve_objective_code AS objective_code

						FROM forest_cover fc

						LEFT JOIN stocking_status_code ssc ON ssc.stocking_status_code = fc.stocking_status_code
						LEFT JOIN stocking_type_code stc ON stc.stocking_type_code = fc.stocking_type_code
						LEFT JOIN forest_cover_non_mapped_area fcnma ON fc.forest_cover_id = fcnma.forest_cover_id
						LEFT JOIN stocking_type_code stcnma ON stcnma.stocking_type_code = fcnma.stocking_type_code
						LEFT JOIN forest_cover_layer fcli
								ON fcli.forest_cover_layer_code = 'I' AND fcli.forest_cover_id = fc.forest_cover_id
						LEFT JOIN forest_cover_layer fcls
								ON fcls.forest_cover_layer_code = 'S' AND fcls.forest_cover_id = fc.forest_cover_id
						LEFT JOIN stocking_standard_unit ssu
								ON ssu.stocking_standard_unit_id = fc.stocking_standard_unit_id

						LEFT JOIN (
								SELECT
										fc.forest_cover_id,
										CASE
												WHEN layer.count_layers IS NULL OR layer.count_layers = 0 THEN 'Y'
												WHEN layer.count_SI > 0 THEN 'Y'
												ELSE 'N'
										END AS is_single_layer
								FROM forest_cover fc
								LEFT JOIN (
										SELECT
												fcl.forest_cover_id,
												COUNT(*) AS count_layers,
												COUNT(CASE WHEN fcl.forest_cover_layer_code IN ('S', 'I') THEN 1 END) AS count_SI
										FROM forest_cover_layer fcl
										GROUP BY fcl.forest_cover_id
								) layer ON fc.forest_cover_id = layer.forest_cover_id
						) layer_summary
								ON layer_summary.forest_cover_id = fc.forest_cover_id

						WHERE
								fc.opening_id = :openingId
								AND (
										COALESCE(:mainSearchTerm,'NOVALUE') = 'NOVALUE'
										OR (
												fc.silv_polygon_no LIKE '%' || :mainSearchTerm || '%'
												OR ssu.standards_unit_id LIKE '%' || :mainSearchTerm || '%'
												OR upper(stcnma.description) LIKE '%' || :mainSearchTerm || '%'
												OR fcnma.stocking_type_code LIKE '%' || :mainSearchTerm || '%'
												OR fc.stocking_status_code LIKE '%' || :mainSearchTerm || '%'
												OR upper(ssc.description) LIKE '%' || :mainSearchTerm || '%'
												OR fc.stocking_type_code LIKE '%' || :mainSearchTerm || '%'
												OR upper(stc.description) LIKE '%' || :mainSearchTerm || '%'
												OR (:mainSearchTerm ~ '^\\d+(\\.\\d+)?$' AND fc.silv_polygon_area = CAST(:mainSearchTerm AS numeric))
												OR (:mainSearchTerm ~ '^\\d+(\\.\\d+)?$' AND fc.silv_polygon_net_area = CAST(:mainSearchTerm AS numeric))
												OR (:mainSearchTerm ~ '^\\d+(\\.\\d+)?$' AND fc.reference_year = CAST(:mainSearchTerm AS numeric))
										)
								)
						 """;

		public static final String GET_OPENING_FOREST_COVER_LIST_SPECIES =
				"""
				SELECT
					fcls.tree_species_code AS species_code,
					tsc.description AS species_name
				FROM forest_cover_layer fcl
				LEFT JOIN forest_cover_layer_species fcls ON (fcls.forest_cover_id = fcl.forest_cover_id AND fcls.forest_cover_layer_id = fcl.forest_cover_layer_id )
				LEFT JOIN tree_species_code tsc ON tsc.tree_species_code = fcls.tree_species_code
				WHERE
					fcl.forest_cover_layer_code = :coverLayerCode AND fcl.forest_cover_id = :forestCoverId
				ORDER BY fcls.species_order""";

		public static final String GET_OPENING_FOREST_COVER_POLYGON =
				"""
				SELECT
					fc.forest_cover_id,
					fc.silv_reserve_code AS reserve_code,
					src.description AS reserve_name,
					fc.silv_reserve_objective_code AS objective_code,
					sroc.description AS objective_name,
					fc.site_class_code AS site_class_code,
					scc.description AS site_class_name,
					fc.site_index AS site_index,
					fc.site_index_source_code AS site_index_source_code,
					sisc.description AS site_index_source_name,
					fc.tree_cover_pattern_code AS tree_cover_pattern_code,
					tcpc.description AS tree_cover_pattern_name,
					fc.reentry_year
				FROM forest_cover fc
				LEFT JOIN silv_reserve_code src ON src.silv_reserve_code = fc.silv_reserve_code
				LEFT JOIN silv_reserve_objective_code sroc ON sroc.silv_reserve_objective_code = fc.silv_reserve_objective_code
				LEFT JOIN site_class_code scc ON scc.site_class_code = fc.site_class_code
				LEFT JOIN site_index_source_code sisc ON sisc.site_index_source_code = fc.site_index_source_code
				LEFT JOIN tree_cover_pattern_code tcpc ON tcpc.tree_cover_pattern_code = fc.tree_cover_pattern_code
				WHERE
					fc.forest_cover_id = :forestCoverId""";

		public static final String GET_OPENING_FOREST_COVER_UNMAPPED =
				"""
				SELECT
					fcnma.non_mapped_area_id AS unmapped_area_id,
					fcnma.non_mapped_area AS area,
					fcnma.stocking_status_code AS stocking_status_code,
					ssc.description AS stocking_status_name,
					fcnma.stocking_type_code AS stocking_type_code,
					stc.description AS stocking_type_name
				FROM forest_cover_non_mapped_area fcnma
				LEFT JOIN stocking_status_code ssc ON ssc.stocking_status_code = fcnma.stocking_status_code
				LEFT JOIN stocking_type_code stc ON stc.stocking_type_code = fcnma.stocking_type_code
				WHERE
					fcnma.forest_cover_id = :forestCoverId""";

		public static final String GET_OPENING_FOREST_COVER_LAYER =
				"""
				SELECT
					fcl.forest_cover_layer_id AS layer_id,
					fcl.forest_cover_layer_code AS layer_code,
					fclc.description AS layer_name,
					fcl.crown_closure_pct AS crown_closure,
					fcl.basal_area AS basal_area_st,
					fcl.total_stems_per_ha AS total_stems,
					fcl.total_well_spaced_stems_per_ha AS total_well_spaced,
					fcl.well_spaced_stems_per_ha AS well_spaced,
					fcl.free_growing_stems_per_ha AS free_growing
				FROM forest_cover_layer fcl
				LEFT JOIN forest_cover_layer_code fclc ON fclc.forest_cover_layer_code = fcl.forest_cover_layer_code
				WHERE
					fcl.forest_cover_id = :forestCoverId""";

		public static final String GET_OPENING_FOREST_COVER_DETAILS_SPECIES =
				"""
				SELECT
					fcls.tree_species_code AS species_code,
					tsc.description AS species_name,
					fcls.tree_species_pct AS species_percent,
					fcls.avg_age AS average_age,
					fcls.avg_height AS average_height
				FROM forest_cover_layer_species fcls
				LEFT JOIN tree_species_code tsc ON tsc.tree_species_code = fcls.tree_species_code
				WHERE
					fcls.forest_cover_layer_id = :forestCoverLayerId
				ORDER BY fcls.species_order""";

		public static final String GET_OPENING_FOREST_COVER_DAMAGE =
				"""
				SELECT
					fr.silv_damage_agent_code AS damage_agent_code,
					sdac.description AS damage_agent_name,
					fr.incidence_pct AS forest_health_incidence,
					fr.incidence_area AS incidence_area
				FROM forhealth_rslt fr
				LEFT JOIN silv_damage_agent_code sdac ON sdac.silv_damage_agent_code = fr.silv_damage_agent_code
				WHERE
					fr.forest_cover_layer_id = :forestCoverLayerId""";

		public static final String GET_OPENING_ATTACHMENT_LIST =
				"""
						SELECT
							opening_attachment_file_id AS openingAttachmentFileId,
							opening_id AS openingId,
							attachment_name AS attachmentName,
							attachment_description AS attachmentDescription,
							mime_type_code AS mimeTypeCode,
							entry_userid AS entryUserId,
							entry_timestamp AS entryTimestamp,
							update_userid AS updateUserId,
							update_timestamp AS updateTimestamp,
							revision_count AS revisionCount,
							REPLACE(opening_attachment_guid::text, '-', '') AS attachmentGuid
						FROM silva.opening_attachment
						WHERE opening_id = :openingId
						""";

		public static final String GET_OPENING_HISTORY_AUDIT_LIST =
				"""
				SELECT
					rae.opening_id AS opening_id,
					rae.results_audit_event_id AS audit_event_id,
					rae.standards_regime_id AS regime_id,
					rae.silviculture_project_id AS project_id,
					rae.results_audit_action_code AS action_code,
					TO_CHAR(rae.action_date, 'YYYY-MM-DD HH24:MI:SS') AS action_timestamp,
					rae.description AS description,
					rae.user_id AS user_id,
					CASE
							WHEN rae.email_sent_ind = 'Y' THEN 'true'
							ELSE 'false'
					END AS is_email_sent,
					rae.xml_submission_id AS xml_submission_id,
					rae.opening_amendment_number AS opening_amendment_number,
					rae.entry_userid AS entry_user_id,
					TO_CHAR(rae.entry_timestamp, 'YYYY-MM-DD HH24:MI:SS') AS entry_timestamp
				FROM results_audit_event rae
				WHERE rae.opening_id = :openingId
				""";

		public static final String GET_OPENING_HISTORY_AUDIT_DETAIL_LIST =
				"""
				SELECT
					rad.results_audit_event_id AS audit_event_id,
					rad.results_audit_detail_id AS audit_detail_id,
					rad.business_identifier AS business_identifier,
					rad.table_name AS table_name,
					rad.column_name AS column_name,
					rad.old_value AS old_value,
					rad.new_value AS new_value,
					rad.entry_userid AS entry_user_id,
					TO_CHAR(rad.entry_timestamp, 'YYYY-MM-DD HH24:MI:SS') AS entry_timestamp
				FROM results_audit_detail rad
				WHERE rad.results_audit_event_id = :auditEventId
				""";

		public static final String GET_OPENING_STANDARD_UNIT_HISTORY_LIST =
				"""
				SELECT
					ssua.stocking_event_history_id,
					MAX(seh.opening_amendment_number) AS amendment_number,
					TO_CHAR(MAX(seh.entry_timestamp), 'YYYY-MM-DD"T"HH24:MI:SS') AS event_timestamp,
					COUNT(ssua.stocking_event_history_id) AS su_count,
					SUM(ssua.net_area) AS total_nar,
					MAX(seh.results_audit_action_code) AS audit_action_code,
					MAX(raac.description) AS audit_action_description,
					MAX(seh.results_submission_id) AS esf_submission_id,
					MAX(seh.entry_userid) AS submitted_by_user_id,
					MAX(oah.app_ent_by_userid) AS approved_by_user_id
				FROM stocking_event_history seh
				LEFT JOIN stocking_standard_unit_archive ssua
					ON ssua.stocking_event_history_id = seh.stocking_event_history_id
				LEFT JOIN opening_amendment_history oah
					ON seh.opening_id = oah.opening_id
					AND seh.opening_amendment_number = oah.opening_amendment_number
				LEFT JOIN results_audit_action_code raac
					ON seh.results_audit_action_code = raac.results_audit_action_code
				WHERE seh.opening_id = :openingId
				GROUP BY ssua.stocking_event_history_id
				ORDER BY MAX(seh.amend_event_timestamp) DESC
				""";

		public static final String GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_WITH_COMPARISON_LIST =
				"""
				with ordered_seh AS (
					SELECT
						seh.stocking_event_history_id,
						seh.entry_timestamp,
						ROW_NUMBER() OVER (ORDER BY seh.entry_timestamp DESC) AS rn
					FROM stocking_event_history seh
					WHERE seh.opening_id = :openingId
				),
				current_event AS (
					SELECT * FROM ordered_seh WHERE stocking_event_history_id = :historyId
				),
				previous_event AS (
					SELECT * FROM ordered_seh WHERE rn = (SELECT rn FROM current_event) + 1
				),
				current_su AS (
					SELECT * FROM stocking_standard_unit_archive
					WHERE stocking_event_history_id = (SELECT stocking_event_history_id FROM current_event)
				),
				previous_su AS (
					SELECT * FROM stocking_standard_unit_archive
					WHERE stocking_event_history_id = (SELECT stocking_event_history_id FROM previous_event)
				),
				current_ec AS (
					SELECT * FROM stocking_ecology_archive
					WHERE stocking_event_history_id = (SELECT stocking_event_history_id FROM current_event)
				),
				previous_ec AS (
					SELECT * FROM stocking_ecology_archive
					WHERE stocking_event_history_id = (SELECT stocking_event_history_id FROM previous_event)
				),
				all_units AS (
					SELECT DISTINCT
							COALESCE(curr.stocking_standard_unit_id, prev.stocking_standard_unit_id) AS stocking_standard_unit_id,
							COALESCE(curr.standards_unit_id, prev.standards_unit_id) AS standards_unit_id,
							curr.stocking_event_history_id AS stocking_event_history_id
					FROM current_su curr
					full OUTER JOIN previous_su prev ON curr.stocking_standard_unit_id = prev.stocking_standard_unit_id
				)
				SELECT
					a.stocking_event_history_id,
					a.stocking_standard_unit_id,
					a.standards_unit_id,
					prev_su.standards_regime_id AS old_regime_id,
					curr_su.standards_regime_id AS new_regime_id,
					prev_su.net_area AS old_net_area,
					curr_su.net_area AS new_net_area,
					prev_su.max_allow_soil_disturbance_pct AS old_max_soil_disturbance,
					curr_su.max_allow_soil_disturbance_pct AS new_max_soil_disturbance,
					CASE
							WHEN prev_su.variance_ind = 'Y' THEN 'true' ELSE 'false'
					END AS old_variance_indicator,
					CASE
							WHEN curr_su.variance_ind = 'Y' THEN 'true' ELSE 'false'
					END AS new_variance_indicator,
					CASE
							WHEN prev_su.regen_obligation_ind = 'Y' THEN 'true' ELSE 'false'
					END AS old_regen_obligation_indicator,
					CASE
							WHEN curr_su.regen_obligation_ind = 'Y' THEN 'true' ELSE 'false'
					END AS new_regen_obligation_indicator,
					prev_su.no_regen_early_offset_yrs AS old_no_regen_early_offset_years,
					curr_su.no_regen_early_offset_yrs AS new_no_regen_early_offset_years,
					prev_su.no_regen_late_offset_yrs AS old_no_regen_late_offset_years,
					curr_su.no_regen_late_offset_yrs AS new_no_regen_late_offset_years,
					prev_su.regen_delay_offset_yrs AS old_regen_offset_years,
					curr_su.regen_delay_offset_yrs AS new_regen_offset_years,
					prev_su.free_growing_early_offset_yrs AS old_free_growing_early_offset_years,
					curr_su.free_growing_early_offset_yrs AS new_free_growing_early_offset_years,
					prev_su.free_growing_late_offset_yrs AS old_free_growing_late_offset_years,
					curr_su.free_growing_late_offset_yrs AS new_free_growing_late_offset_years,
					prev_su.amendment_rationale_comment AS old_amendment_comment,
					curr_su.amendment_rationale_comment AS new_amendment_comment,
					prev_ec.bgc_zone_code AS old_bgc_zone,
					curr_ec.bgc_zone_code AS new_bgc_zone,
					prev_ec.bgc_subzone_code AS old_bgc_subzone,
					curr_ec.bgc_subzone_code AS new_bgc_subzone,
					prev_ec.bgc_variant AS old_bgc_variant,
					curr_ec.bgc_variant AS new_bgc_variant,
					prev_ec.bgc_phase AS old_bgc_phase,
					curr_ec.bgc_phase AS new_bgc_phase,
					prev_ec.bec_site_series AS old_bec_site_series,
					curr_ec.bec_site_series AS new_bec_site_series,
					prev_ec.bec_site_type AS old_bec_site_type,
					curr_ec.bec_site_type AS new_bec_site_type,
					prev_ec.bec_seral AS old_bec_seral,
					curr_ec.bec_seral AS new_bec_seral
				FROM all_units a
				LEFT JOIN current_su curr_su ON curr_su.stocking_standard_unit_id = a.stocking_standard_unit_id
				LEFT JOIN previous_su prev_su ON prev_su.stocking_standard_unit_id = a.stocking_standard_unit_id
				LEFT JOIN current_ec curr_ec ON curr_ec.stocking_standard_unit_id = a.stocking_standard_unit_id
				LEFT JOIN previous_ec prev_ec ON prev_ec.stocking_standard_unit_id = a.stocking_standard_unit_id
				""";

		public static final String GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_LAYERS_WITH_COMPARISON =
				"""
				with ordered_seh AS (
					SELECT
						seh.stocking_event_history_id,
						seh.entry_timestamp,
						ROW_NUMBER() OVER (ORDER BY seh.entry_timestamp DESC) AS rn
					FROM silva.stocking_event_history seh
					WHERE seh.opening_id = :openingId
				),
				current_event AS (
					SELECT * FROM ordered_seh WHERE stocking_event_history_id = :historyId
				),
				previous_event AS (
					SELECT * FROM ordered_seh WHERE rn = (SELECT rn FROM current_event) + 1
				),
				curr_layer AS (
					SELECT * FROM stocking_layer_archive
					WHERE stocking_event_history_id = (SELECT stocking_event_history_id FROM current_event)
				),
				prev_layer AS (
					SELECT * FROM stocking_layer_archive
					WHERE stocking_event_history_id = (SELECT stocking_event_history_id FROM previous_event)
				),
				layer_keys AS (
					SELECT DISTINCT
						COALESCE(c.stocking_standard_unit_id, p.stocking_standard_unit_id) AS ssu_id,
						COALESCE(c.stocking_layer_id, p.stocking_layer_id) AS stocking_layer_id
					FROM curr_layer c
					full OUTER JOIN prev_layer p ON
						c.stocking_standard_unit_id = p.stocking_standard_unit_id AND
						c.stocking_layer_id = p.stocking_layer_id
				)
				SELECT
					(SELECT stocking_event_history_id FROM current_event) AS stocking_event_history_id,
					k.ssu_id,
					p.stocking_layer_id AS old_layer_id,
					c.stocking_layer_id AS new_layer_id,
					p.stocking_layer_code AS old_stocking_layer_code,
					c.stocking_layer_code AS new_stocking_layer_code,
					slcp.description AS old_stocking_layer_description,
					slcc.description AS new_stocking_layer_description,
					p.min_horizontal_distance AS old_min_horizontal_distance,
					c.min_horizontal_distance AS new_min_horizontal_distance,
					p.min_pref_stocking_standard AS old_min_perf_stocking_standard,
					c.min_pref_stocking_standard AS new_min_perf_stocking_standard,
					p.min_stocking_standard AS old_min_stocking_standard,
					c.min_stocking_standard AS new_min_stocking_standard,
					p.min_post_spacing AS old_min_post_spacing,
					c.min_post_spacing AS new_min_post_spacing,
					p.residual_basal_area AS old_residual_basal_area,
					c.residual_basal_area AS new_residual_basal_area,
					p.target_stocking AS old_target_well_spaced_trees,
					c.target_stocking AS new_target_well_spaced_trees,
					p.hght_relative_to_comp AS old_height_relative_to_comp,
					c.hght_relative_to_comp AS new_height_relative_to_comp,
					p.max_conifer AS old_max_conifer,
					c.max_conifer AS new_max_conifer,
					p.max_post_spacing AS old_max_post_spacing,
					c.max_post_spacing AS new_max_post_spacing
				FROM layer_keys k
				LEFT JOIN curr_layer c ON c.stocking_standard_unit_id = k.ssu_id AND c.stocking_layer_id = k.stocking_layer_id
				LEFT JOIN prev_layer p ON p.stocking_standard_unit_id = k.ssu_id AND p.stocking_layer_id = k.stocking_layer_id
				LEFT JOIN stocking_layer_code slcc ON c.stocking_layer_code = slcc.stocking_layer_code
				LEFT JOIN stocking_layer_code slcp ON p.stocking_layer_code = slcp.stocking_layer_code
				""";

		public static final String GET_OPENING_STANDARD_UNIT_HISTORY_DETAIL_SPECIES_WITH_COMPARISON =
				"""
				with ordered_seh AS (
						SELECT
								seh.stocking_event_history_id AS seh_id,
								seh.entry_timestamp,
								ROW_NUMBER() OVER (ORDER BY seh.entry_timestamp DESC) AS rn
						FROM silva.stocking_event_history seh
						WHERE seh.opening_id = :openingId
				),
				current_event AS (
						SELECT * FROM ordered_seh WHERE seh_id = :historyId
				),
				previous_event AS (
						SELECT * FROM ordered_seh WHERE rn = (SELECT rn FROM current_event) + 1
				),
				curr_species AS (
						SELECT * FROM silva.stocking_layer_species_archive
						WHERE stocking_event_history_id = (SELECT seh_id FROM current_event)
				),
				prev_species AS (
						SELECT * FROM silva.stocking_layer_species_archive
						WHERE stocking_event_history_id = (SELECT seh_id FROM previous_event)
				),
				all_keys AS (
						SELECT DISTINCT
								COALESCE(c.stocking_standard_unit_id, p.stocking_standard_unit_id) AS ssu_id,
								COALESCE(c.stocking_layer_id, p.stocking_layer_id) AS stocking_layer_id,
								COALESCE(c.silv_tree_species_code, p.silv_tree_species_code) AS species_code
						FROM curr_species c
						full OUTER JOIN prev_species p
							ON c.stocking_standard_unit_id = p.stocking_standard_unit_id
						 AND c.stocking_layer_id = p.stocking_layer_id
						 AND c.silv_tree_species_code = p.silv_tree_species_code
				)
				SELECT
						k.ssu_id,
						p.stocking_layer_id AS old_stocking_layer_id,
						c.stocking_layer_id AS new_stocking_layer_id,
						slap.stocking_layer_code AS old_layer_code,
						slac.stocking_layer_code AS new_layer_code,
						p.silv_tree_species_code AS old_species_code,
						c.silv_tree_species_code AS new_species_code,
						stscp.description AS old_species_description,
						stscc.description AS new_species_description,
						CASE
							WHEN p.preferred_ind = 'Y' THEN 'true' ELSE 'false'
						END AS old_preferred_ind,
						CASE
							WHEN c.preferred_ind = 'Y' THEN 'true' ELSE 'false'
						END AS new_preferred_ind,
						p.min_height AS old_min_height,
						c.min_height AS new_min_height
				FROM all_keys k
				LEFT JOIN curr_species c
					ON c.stocking_standard_unit_id = k.ssu_id
				 AND c.stocking_layer_id = k.stocking_layer_id
				 AND c.silv_tree_species_code = k.species_code
				LEFT JOIN prev_species p
					ON p.stocking_standard_unit_id = k.ssu_id
				 AND p.stocking_layer_id = k.stocking_layer_id
				 AND p.silv_tree_species_code = k.species_code
				LEFT JOIN silv_tree_species_code stscc ON stscc.silv_tree_species_code = c.silv_tree_species_code
				LEFT JOIN silv_tree_species_code stscp ON stscp.silv_tree_species_code = p.silv_tree_species_code
				LEFT JOIN stocking_layer_archive slac
					ON slac.stocking_layer_id = c.stocking_layer_id
					AND slac.stocking_event_history_id = c.stocking_event_history_id
				LEFT JOIN stocking_layer_archive slap
					ON slap.stocking_layer_id = p.stocking_layer_id
					AND slap.stocking_event_history_id = p.stocking_event_history_id
				""";

		public static final String GET_OPENING_SS_ARCHIVE =
				"""
				SELECT
						ssu.standards_unit_id AS stocking_standard_unit,
						ssu.stocking_standard_unit_id AS ssuid,
					ssu.standards_regime_id as srid,
						CASE WHEN COALESCE(sr.mof_default_standard_ind, 'N') = 'Y' THEN 'true' ELSE 'false' END AS default_mof,
						CASE WHEN COALESCE(ssu.stocking_standard_unit_id, 0) = 0 THEN 'true' ELSE 'false' END AS manual_entry,
						fsp.fsp_id,
						ssu.net_area,
						ssu.max_allow_soil_disturbance_pct AS soil_disturbance_percent,
						se.bgc_zone_code AS bec_zone_code,
						se.bgc_subzone_code AS bec_subzone_code,
						se.bgc_variant AS bec_variant,
						se.bgc_phase AS bec_phase,
						se.bec_site_series AS bec_site_series,
						se.bec_site_type AS bec_site_type,
						se.bec_seral AS bec_seral,
						ssu.regen_delay_offset_yrs AS regen_delay,
						ssu.free_growing_late_offset_yrs AS free_growing_late,
						ssu.free_growing_early_offset_yrs AS free_growing_early,
						sr.additional_standards,
						ssu.amendment_rationale_comment AS amendment_comment
				FROM stocking_standard_unit_archive ssu
				LEFT JOIN stocking_ecology_archive se ON (se.opening_id = ssu.opening_id AND se.stocking_standard_unit_id = ssu.stocking_standard_unit_id AND se.stocking_event_history_id = ssu.stocking_event_history_id)
				LEFT JOIN standards_regime sr ON (sr.standards_regime_id = ssu.standards_regime_id)
				LEFT JOIN fsp_standards_regime_xref fspxref ON (fspxref.standards_regime_id = ssu.standards_regime_id)
				LEFT JOIN forest_stewardship_plan fsp ON (fsp.fsp_id = fspxref.fsp_id AND fsp.fsp_amendment_number = fspxref.fsp_amendment_number)
				WHERE ssu.opening_id = :openingId
				AND ssu.stocking_event_history_id = :eventHistoryId
				ORDER BY ssu.standards_unit_id
				""";

		public static final String GET_OPENING_SS_SPECIES_ARCHIVE =
				"""
				SELECT
					sl.stocking_layer_code as layer_code,
						sls.silv_tree_species_code AS species_code,
						stsc.description AS species_name,
						sls.min_height AS min_height
				FROM stocking_layer_archive sl
				LEFT JOIN stocking_layer_species_archive sls ON (sls.stocking_layer_id = sl.stocking_layer_id AND sls.stocking_event_history_id = sl.stocking_event_history_id)
				LEFT JOIN silv_tree_species_code stsc ON (stsc.silv_tree_species_code = sls.silv_tree_species_code)
				WHERE sl.opening_id = :openingId AND sls.preferred_ind = :preferred AND sl.stocking_standard_unit_id = :ssuId AND sl.stocking_event_history_id = :eventHistoryId
				ORDER BY sls.species_order
				""";

		public static final String GET_OPENING_SS_LAYER_ARCHIVE =
				"""
				SELECT
					sl.stocking_layer_code AS layer_code,
					slc.description AS layer_name,
					sl.min_stocking_standard AS min_wellspaced_trees,
					sl.min_pref_stocking_standard AS min_preferred_wellspaced_trees,
					sl.min_horizontal_distance AS min_horizontal_distance_wellspaced_trees,
					sl.target_stocking AS target_wellspaced_trees,
					sl.residual_basal_area AS min_residual_basal_area,
					sl.min_post_spacing AS min_postspacing_density,
					sl.max_post_spacing AS max_postspacing_density,
					sl.max_conifer AS max_coniferous,
					sl.hght_relative_to_comp AS height_relative_to_comp
				FROM stocking_layer_archive sl
				LEFT JOIN stocking_layer_code slc ON sl.stocking_layer_code = slc.stocking_layer_code
				WHERE sl.opening_id = :openingId AND sl.stocking_standard_unit_id = :ssuId AND sl.stocking_event_history_id = :eventHistoryId
				""";

		public static final String GET_OPENING_FOREST_COVER_HISTORY_OVERVIEW_LIST =
				"""
				with fca_deduped AS (
						SELECT
								MAX(opening_id) AS opening_id,
								DATE(update_timestamp) AS update_timestamp
						FROM (
								SELECT fca.*,
											 ROW_NUMBER() OVER (
													 PARTITION BY fca.opening_id, DATE(fca.update_timestamp)
													 ORDER BY DATE(fca.update_timestamp) DESC
											 ) AS rn
								FROM silva.forest_cover_archive fca
								WHERE fca.opening_id = :openingId
						) t
						WHERE rn = 1
						GROUP BY DATE(update_timestamp)
				)
				SELECT
						ols.opening_id,
						TO_CHAR(ols.opening_land_status_date, 'YYYY-MM-DD"T"HH24:MI:SS') AS fc_date,
						(ols.np_for_area + ols.np_nat_area + ols.np_unn_area) AS np,
						(ols.nsr_npl_area + ols.nsr_pl_area + ols.nsr_nat_area) AS nsr,
						(ols.sr_art_area + ols.sr_nat_area) AS imm,
						(ols.mat_area + ols.np_nat_area + ols.nc_br_area) AS other,
						(
								ols.np_for_area + ols.np_nat_area + ols.np_unn_area +
								ols.nsr_npl_area + ols.nsr_pl_area + ols.nsr_nat_area +
								ols.sr_art_area + ols.sr_nat_area +
								ols.mat_area + ols.np_nat_area + ols.nc_br_area
						) AS total,
						CASE
								WHEN ols.opening_land_status_date = (
										SELECT MAX(ols2.opening_land_status_date)
										FROM silva.opening_land_status ols2
										WHERE ols2.opening_id = ols.opening_id
								)
								THEN 'true'
								WHEN fca.opening_id IS NOT NULL
										 AND DATE(fca.update_timestamp) = DATE(ols.opening_land_status_date)
								THEN 'true'
								ELSE 'false'
						END AS has_details,
						CASE
								WHEN ols.opening_land_status_date = (
										SELECT MAX(ols2.opening_land_status_date)
										FROM silva.opening_land_status ols2
										WHERE ols2.opening_id = ols.opening_id
								)
								THEN 'true'
								ELSE 'false'
						END AS is_current,
						CASE
								WHEN ols.opening_land_status_date = (
										SELECT MIN(ols2.opening_land_status_date)
										FROM silva.opening_land_status ols2
										WHERE ols2.opening_id = ols.opening_id
								)
								THEN 'true'
								ELSE 'false'
						END AS is_oldest
				FROM silva.opening_land_status ols
				LEFT JOIN fca_deduped fca
						ON ols.opening_id = fca.opening_id
						AND DATE(ols.opening_land_status_date) = DATE(fca.update_timestamp)
				WHERE ols.opening_id = :openingId
				ORDER BY ols.opening_land_status_date DESC
				""";

		public static final String GET_OPENING_FOREST_COVER_HISTORY_LIST =
				"""
				SELECT
					fc.forest_cover_id AS cover_id,
					TO_CHAR(fc.archive_date, 'YYYY-MM-DD') AS archive_date,
					fc.silv_polygon_no AS polygon_id,
					ssu.standards_unit_id AS standard_unit_id,
					fcnma.stocking_type_code AS unmapped_code,
					stcnma.description AS unmapped_name,
					fc.silv_polygon_area AS gross_area,
					fc.silv_polygon_net_area AS net_area,
					fc.stocking_status_code AS status_code,
					ssc.description AS status_name,
					fc.stocking_type_code AS type_code,
					stc.description AS type_name,
					fcli.total_stems_per_ha AS total,
					fcli.total_well_spaced_stems_per_ha AS inventory_total_well_spaced,
					fcli.well_spaced_stems_per_ha AS inventory_well_spaced,
					fcli.free_growing_stems_per_ha AS inventory_free_growing,
					fcls.total_well_spaced_stems_per_ha AS silviculture_total_well_spaced,
					fcls.well_spaced_stems_per_ha AS silviculture_well_spaced,
					fcls.free_growing_stems_per_ha AS silviculture_free_growing,
					fc.reference_year,
					layer_summary.is_single_layer AS is_single_layer,
					fc.silv_reserve_code AS reserve_code,
					fc.silv_reserve_objective_code AS objective_code
				FROM forest_cover_archive fc
				LEFT JOIN stocking_status_code ssc ON ssc.stocking_status_code = fc.stocking_status_code
				LEFT JOIN stocking_type_code stc ON stc.stocking_type_code = fc.stocking_type_code
				LEFT JOIN forest_cover_non_mapped_arc fcnma ON fc.forest_cover_id = fcnma.forest_cover_id
				LEFT JOIN stocking_type_code stcnma ON stcnma.stocking_type_code = fcnma.stocking_type_code
				LEFT JOIN forest_cover_layer_archive fcli
					ON fcli.forest_cover_layer_code = 'I' AND fcli.forest_cover_id = fc.forest_cover_id
				LEFT JOIN forest_cover_layer_archive fcls
					ON fcls.forest_cover_layer_code = 'S' AND fcls.forest_cover_id = fc.forest_cover_id
				LEFT JOIN stocking_standard_unit_archive ssu
					ON ssu.stocking_standard_unit_id = fc.stocking_standard_unit_id
				LEFT JOIN (
					SELECT
						fc.forest_cover_id,
						CASE
							WHEN layer.count_layers IS NULL OR layer.count_layers = 0 THEN 'Y'
							WHEN layer.count_SI > 0 THEN 'Y'
							ELSE 'N'
						END AS is_single_layer
					FROM forest_cover_archive fc
					LEFT JOIN (
						SELECT
								fcl.forest_cover_id,
								COUNT(*) AS count_layers,
								COUNT(CASE WHEN fcl.forest_cover_layer_code IN ('S', 'I') THEN 1 END) AS count_SI
						FROM forest_cover_layer_archive fcl
						GROUP BY fcl.forest_cover_id
					) layer ON fc.forest_cover_id = layer.forest_cover_id
				) layer_summary
					ON layer_summary.forest_cover_id = fc.forest_cover_id
				WHERE
					fc.opening_id = :openingId
					AND DATE(fc.update_timestamp) = TO_DATE(:updateDate, 'YYYY-MM-DD')
					AND (
								COALESCE(:mainSearchTerm,'NOVALUE') = 'NOVALUE'
								OR (
										fc.silv_polygon_no LIKE '%' || :mainSearchTerm || '%'
										OR ssu.standards_unit_id LIKE '%' || :mainSearchTerm || '%'
										OR upper(stcnma.description) LIKE '%' || :mainSearchTerm || '%'
										OR fcnma.stocking_type_code LIKE '%' || :mainSearchTerm || '%'
										OR fc.stocking_status_code LIKE '%' || :mainSearchTerm || '%'
										OR upper(ssc.description) LIKE '%' || :mainSearchTerm || '%'
										OR fc.stocking_type_code LIKE '%' || :mainSearchTerm || '%'
										OR upper(stc.description) LIKE '%' || :mainSearchTerm || '%'
										OR (:mainSearchTerm ~ '^\\d+(\\.\\d+)?$' AND fc.silv_polygon_area = CAST(:mainSearchTerm AS numeric))
										OR (:mainSearchTerm ~ '^\\d+(\\.\\d+)?$' AND fc.silv_polygon_net_area = CAST(:mainSearchTerm AS numeric))
										OR (:mainSearchTerm ~ '^\\d+(\\.\\d+)?$' AND fc.reference_year = CAST(:mainSearchTerm AS numeric))
								)
					)
				""";

		public static final String GET_OPENING_FOREST_COVER_HISTORY_LIST_SPECIES =
				"""
				SELECT
					fcls.tree_species_code AS species_code,
					tsc.description AS species_name
				FROM forest_cover_layer_archive fcl
				LEFT JOIN forest_cover_layer_species_arc fcls ON (fcls.forest_cover_id = fcl.forest_cover_id AND fcls.forest_cover_layer_id = fcl.forest_cover_layer_id )
				LEFT JOIN tree_species_code tsc ON tsc.tree_species_code = fcls.tree_species_code
				WHERE
					fcl.forest_cover_layer_code = :coverLayerCode
					AND fcl.forest_cover_id = :forestCoverId
					AND DATE(fcl.archive_date) = TO_DATE(:archiveDate, 'YYYY-MM-DD')
				ORDER BY fcls.species_order
				""";

		public static final String GET_OPENING_FOREST_COVER_HISTORY_POLYGON =
				"""
				SELECT
					fc.forest_cover_id,
					fc.silv_reserve_code AS reserve_code,
					src.description AS reserve_name,
					fc.silv_reserve_objective_code AS objective_code,
					sroc.description AS objective_name,
					fc.site_class_code AS site_class_code,
					scc.description AS site_class_name,
					fc.site_index AS site_index,
					fc.site_index_source_code AS site_index_source_code,
					sisc.description AS site_index_source_name,
					fc.tree_cover_pattern_code AS tree_cover_pattern_code,
					tcpc.description AS tree_cover_pattern_name,
					fc.reentry_year
				FROM forest_cover_archive fc
				LEFT JOIN silv_reserve_code src ON src.silv_reserve_code = fc.silv_reserve_code
				LEFT JOIN silv_reserve_objective_code sroc ON sroc.silv_reserve_objective_code = fc.silv_reserve_objective_code
				LEFT JOIN site_class_code scc ON scc.site_class_code = fc.site_class_code
				LEFT JOIN site_index_source_code sisc ON sisc.site_index_source_code = fc.site_index_source_code
				LEFT JOIN tree_cover_pattern_code tcpc ON tcpc.tree_cover_pattern_code = fc.tree_cover_pattern_code
				WHERE
					fc.forest_cover_id = :forestCoverId
					AND DATE(fc.archive_date) = TO_DATE(:archiveDate, 'YYYY-MM-DD')
				""";

		public static final String GET_OPENING_FOREST_COVER_HISTORY_UNMAPPED =
				"""
				SELECT
					fcnma.non_mapped_area_id AS unmapped_area_id,
					fcnma.non_mapped_area AS area,
					fcnma.stocking_status_code AS stocking_status_code,
					ssc.description AS stocking_status_name,
					fcnma.stocking_type_code AS stocking_type_code,
					stc.description AS stocking_type_name
				FROM forest_cover_non_mapped_arc fcnma
				LEFT JOIN stocking_status_code ssc ON ssc.stocking_status_code = fcnma.stocking_status_code
				LEFT JOIN stocking_type_code stc ON stc.stocking_type_code = fcnma.stocking_type_code
				WHERE
					fcnma.forest_cover_id = :forestCoverId
					AND DATE(fcnma.archive_date) = TO_DATE(:archiveDate, 'YYYY-MM-DD')
				""";

		public static final String GET_OPENING_FOREST_COVER_HISTORY_LAYER =
				"""
				SELECT
					fcl.forest_cover_layer_id AS layer_id,
					fcl.forest_cover_layer_code AS layer_code,
					fclc.description AS layer_name,
					fcl.crown_closure_pct AS crown_closure,
					fcl.basal_area AS basal_area_st,
					fcl.total_stems_per_ha AS total_stems,
					fcl.total_well_spaced_stems_per_ha AS total_well_spaced,
					fcl.well_spaced_stems_per_ha AS well_spaced,
					fcl.free_growing_stems_per_ha AS free_growing
				FROM forest_cover_layer_archive fcl
				LEFT JOIN forest_cover_layer_code fclc ON fclc.forest_cover_layer_code = fcl.forest_cover_layer_code
				WHERE
					fcl.forest_cover_id = :forestCoverId
					AND DATE(fcl.archive_date) = TO_DATE(:archiveDate, 'YYYY-MM-DD')
				""";

		public static final String GET_OPENING_FOREST_COVER_HISTORY_DETAILS_SPECIES =
				"""
				SELECT
					fcls.tree_species_code AS species_code,
					tsc.description AS species_name,
					fcls.tree_species_pct AS species_percent,
					fcls.avg_age AS average_age,
					fcls.avg_height AS average_height
				FROM forest_cover_layer_species_arc fcls
				LEFT JOIN tree_species_code tsc ON tsc.tree_species_code = fcls.tree_species_code
				WHERE
					fcls.forest_cover_layer_id = :forestCoverLayerId
					AND DATE(fcls.archive_date) = TO_DATE(:archiveDate, 'YYYY-MM-DD')
				ORDER BY fcls.species_order
				""";

		public static final String GET_OPENING_FOREST_COVER_HISTORY_DAMAGE =
				"""
				SELECT
					fr.silv_damage_agent_code AS damage_agent_code,
					sdac.description AS damage_agent_name,
					fr.incidence_pct AS forest_health_incidence,
					fr.incidence_area AS incidence_area
				FROM forhealth_rslt_archive fr
				LEFT JOIN silv_damage_agent_code sdac ON sdac.silv_damage_agent_code = fr.silv_damage_agent_code
				WHERE
					fr.forest_cover_layer_id = :forestCoverLayerId
					AND DATE(fr.archive_date) = TO_DATE(:archiveDate, 'YYYY-MM-DD')
				""";

	public static final String SILVICULTURE_SEARCH_BY_OPENING_IDS =
			"WITH silviculture_search AS ("
					+ SILVICULTURE_SEARCH_SELECT
					+ SILVICULTURE_SEARCH_FROM_JOIN
					+ "WHERE op.opening_id IN (:openingIds)"
					+ ")"
					+ SILVICULTURE_SEARCH_CTE_SELECT
					+ " FROM silviculture_search ORDER BY opening_id DESC "
					+ PAGINATION;


	public static final String ACTIVITY_SEARCH = """
			WITH activity_search AS (
				SELECT
					atu.activity_treatment_unit_id AS activityId,
					sbc.silv_base_code AS baseCode,
					sbc.description AS baseDescription,
					stc.silv_technique_code AS techniqueCode,
					stc.description AS techniqueDescription,
					smc.silv_method_code AS methodCode,
					smc.description AS methodDescription,
					CASE WHEN atu.atu_completion_date IS NOT NULL THEN 1 ELSE 0 END AS isComplete,
					sfsc.silv_fund_srce_code AS fundingSourceCode,
					sfsc.description AS fundingSourceDescription,
					cboa.forest_file_id AS fileId,
					cboa.cut_block_id AS cutBlock,
					atu.opening_id AS openingId,
					cboa.cutting_permit_id AS cuttingPermit,
					atu.treatment_amount AS treatmentAmountArea,
					atu.activity_licensee_id AS intraAgencyNumber,
					occ.open_category_code AS openingCategoryCode,
					occ.description AS openingCategoryDescription,
					ou.org_unit_code AS orgUnitCode,
					ou.org_unit_name AS orgUnitDescription,
					ffc.client_number AS openingClientCode,
					COUNT(*) OVER () AS totalCount,
					atu.update_timestamp AS updateTimestamp
				FROM activity_treatment_unit atu
				LEFT JOIN opening op ON atu.opening_id = op.opening_id
				LEFT JOIN org_unit ou ON atu.org_unit_no = ou.org_unit_no
				LEFT JOIN cut_block_open_admin cboa ON op.opening_id = cboa.opening_id AND cboa.cut_block_open_admin_id = (
					SELECT MAX(cut_block_open_admin_id) FROM cut_block_open_admin cboa2
					WHERE cboa2.opening_id = op.opening_id
				)
				LEFT JOIN silv_base_code sbc ON atu.silv_base_code = sbc.silv_base_code
				LEFT JOIN silv_technique_code stc ON atu.silv_technique_code = stc.silv_technique_code
				LEFT JOIN silv_method_code smc ON atu.silv_method_code = smc.silv_method_code
				LEFT JOIN silv_fund_srce_code sfsc ON atu.silv_fund_srce_code = sfsc.silv_fund_srce_code
				LEFT JOIN open_category_code occ ON op.open_category_code = occ.open_category_code
				LEFT JOIN forest_file_client ffc ON (cboa.forest_file_id = ffc.forest_file_id AND ffc.forest_file_client_type_code = 'A')
				WHERE
					(
						'NOVALUE' IN (:#{#filter.bases}) OR atu.silv_base_code IN (:#{#filter.bases})
					)
					AND (
						'NOVALUE' IN (:#{#filter.techniques}) OR atu.silv_technique_code IN (:#{#filter.techniques})
					)
					AND (
						'NOVALUE' IN (:#{#filter.methods}) OR atu.silv_method_code IN (:#{#filter.methods})
					)
					AND (
						CASE WHEN CAST(:#{#filter.isComplete} AS boolean) = true THEN CASE WHEN atu.atu_completion_date IS NOT NULL THEN true ELSE false END ELSE true END
					)
					AND (
						'NOVALUE' IN (:#{#filter.objectives})
						OR atu.silv_objective_code_1 IN (:#{#filter.objectives})
						OR atu.silv_objective_code_2 IN (:#{#filter.objectives})
						OR atu.silv_objective_code_3 IN (:#{#filter.objectives})
					)
					AND (
						'NOVALUE' IN (:#{#filter.fundingSources}) OR atu.silv_fund_srce_code IN (:#{#filter.fundingSources})
					)
					AND (
						'NOVALUE' IN (:#{#filter.orgUnits}) OR ou.org_unit_code IN (:#{#filter.orgUnits})
					)
					AND (
						'NOVALUE' IN (:#{#filter.openingCategories}) OR op.open_category_code IN (:#{#filter.openingCategories})
					)
					AND (
						COALESCE(CAST(:#{#filter.fileId} AS text),'NOVALUE') = 'NOVALUE' OR cboa.forest_file_id = CAST(:#{#filter.fileId} AS text)
					)
					AND (
						'NOVALUE' IN (:#{#filter.clientNumbers}) OR ffc.client_number IN (:#{#filter.clientNumbers})
					)
					AND (
						'NOVALUE' IN (:#{#filter.openingStatuses}) OR op.opening_status_code IN (:#{#filter.openingStatuses})
					)
					AND (
						(
							COALESCE(CAST(:#{#filter.updateDateStart} AS text),'NOVALUE') = 'NOVALUE' AND COALESCE(CAST(:#{#filter.updateDateEnd} AS text),'NOVALUE') = 'NOVALUE'
						)
						OR (
							atu.update_timestamp IS NOT NULL
							AND (
								(
									COALESCE(CAST(:#{#filter.updateDateStart} AS text),'NOVALUE') != 'NOVALUE'
									AND atu.update_timestamp >= TO_DATE(CAST(:#{#filter.updateDateStart} AS text),'YYYY-MM-DD')
								)
								OR COALESCE(CAST(:#{#filter.updateDateStart} AS text),'NOVALUE') = 'NOVALUE'
							)
							AND (
								(
									COALESCE(CAST(:#{#filter.updateDateEnd} AS text),'NOVALUE') != 'NOVALUE'
									AND atu.update_timestamp < TO_DATE(CAST(:#{#filter.updateDateEnd} AS text),'YYYY-MM-DD') + INTERVAL '1 day'
								)
								OR COALESCE(CAST(:#{#filter.updateDateEnd} AS text),'NOVALUE') = 'NOVALUE'
							)
						)
					)
			)
			SELECT
				activityId,
				baseCode,
				baseDescription,
				techniqueCode,
				techniqueDescription,
				methodCode,
				methodDescription,
				isComplete,
				fundingSourceCode,
				fundingSourceDescription,
				fileId,
				cutBlock,
				openingId,
				cuttingPermit,
				treatmentAmountArea,
				intraAgencyNumber,
				openingCategoryCode,
				openingCategoryDescription,
				orgUnitCode,
				orgUnitDescription,
				openingClientCode,
				totalCount,
				updateTimestamp
			FROM activity_search
			ORDER BY activityId DESC
			""" + PAGINATION;
	}

