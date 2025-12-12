
SET search_path = silva,public;
BEGIN;
ALTER TABLE activity_treatment_unit DISABLE TRIGGER USER;
ALTER TABLE activity_tu_comment_link DISABLE TRIGGER USER;
ALTER TABLE age_class_code DISABLE TRIGGER USER;
ALTER TABLE block_status_code DISABLE TRIGGER USER;
ALTER TABLE cascade_split_code DISABLE TRIGGER USER;
ALTER TABLE client_location DISABLE TRIGGER USER;
ALTER TABLE corp_capture_method DISABLE TRIGGER USER;
ALTER TABLE crew_contract_hire_code DISABLE TRIGGER USER;
ALTER TABLE crown_lands_region_code DISABLE TRIGGER USER;
ALTER TABLE cut_block DISABLE TRIGGER USER;
ALTER TABLE cut_block_client DISABLE TRIGGER USER;
ALTER TABLE cut_block_client_type_code DISABLE TRIGGER USER;
ALTER TABLE cut_block_open_admin DISABLE TRIGGER USER;
ALTER TABLE cut_regulation_code DISABLE TRIGGER USER;
ALTER TABLE data_source_code DISABLE TRIGGER USER;
ALTER TABLE disturbance_code DISABLE TRIGGER USER;
ALTER TABLE feature_classes DISABLE TRIGGER USER;
ALTER TABLE file_client_type_code DISABLE TRIGGER USER;
ALTER TABLE file_type_code DISABLE TRIGGER USER;
ALTER TABLE fire_harvesting_reason_code DISABLE TRIGGER USER;
ALTER TABLE forest_client DISABLE TRIGGER USER;
ALTER TABLE forest_cover DISABLE TRIGGER USER;
ALTER TABLE forest_cover_archive DISABLE TRIGGER USER;
ALTER TABLE forest_cover_archive_geometry DISABLE TRIGGER USER;
ALTER TABLE forest_cover_geometry DISABLE TRIGGER USER;
ALTER TABLE forest_cover_layer DISABLE TRIGGER USER;
ALTER TABLE forest_cover_layer_archive DISABLE TRIGGER USER;
ALTER TABLE forest_cover_layer_code DISABLE TRIGGER USER;
ALTER TABLE forest_cover_layer_species DISABLE TRIGGER USER;
ALTER TABLE forest_cover_layer_species_arc DISABLE TRIGGER USER;
ALTER TABLE forest_cover_non_mapped_arc DISABLE TRIGGER USER;
ALTER TABLE forest_cover_non_mapped_area DISABLE TRIGGER USER;
ALTER TABLE forest_file_client DISABLE TRIGGER USER;
ALTER TABLE forest_mgmt_unit DISABLE TRIGGER USER;
ALTER TABLE forest_stewardship_plan DISABLE TRIGGER USER;
ALTER TABLE forhealth_rslt DISABLE TRIGGER USER;
ALTER TABLE forhealth_rslt_archive DISABLE TRIGGER USER;
ALTER TABLE for_client_link DISABLE TRIGGER USER;
ALTER TABLE fsp_standards_regime_xref DISABLE TRIGGER USER;
ALTER TABLE fsp_status_code DISABLE TRIGGER USER;
ALTER TABLE harvesting_authority DISABLE TRIGGER USER;
ALTER TABLE harvest_auth_extend_reas_code DISABLE TRIGGER USER;
ALTER TABLE harvest_auth_status_code DISABLE TRIGGER USER;
ALTER TABLE harvest_sale DISABLE TRIGGER USER;
ALTER TABLE harvest_type_code DISABLE TRIGGER USER;
ALTER TABLE hauling_authority DISABLE TRIGGER USER;
ALTER TABLE height_class_code DISABLE TRIGGER USER;
ALTER TABLE licence_to_cut_code DISABLE TRIGGER USER;
ALTER TABLE marking_instrument_code DISABLE TRIGGER USER;
ALTER TABLE marking_method_code DISABLE TRIGGER USER;
ALTER TABLE mark_extension_reason_code DISABLE TRIGGER USER;
ALTER TABLE mgmt_unit_type_code DISABLE TRIGGER USER;
ALTER TABLE mime_type_code DISABLE TRIGGER USER;
ALTER TABLE opening DISABLE TRIGGER USER;
ALTER TABLE opening_amendment_history DISABLE TRIGGER USER;
ALTER TABLE opening_attachment DISABLE TRIGGER USER;
ALTER TABLE opening_comment_link DISABLE TRIGGER USER;
ALTER TABLE opening_geometry DISABLE TRIGGER USER;
ALTER TABLE opening_land_status DISABLE TRIGGER USER;
ALTER TABLE opening_status_code DISABLE TRIGGER USER;
ALTER TABLE open_category_code DISABLE TRIGGER USER;
ALTER TABLE open_viewable_category DISABLE TRIGGER USER;
ALTER TABLE org_unit DISABLE TRIGGER USER;
ALTER TABLE payment_method_code DISABLE TRIGGER USER;
ALTER TABLE planting_rslt DISABLE TRIGGER USER;
ALTER TABLE prov_forest_use DISABLE TRIGGER USER;
ALTER TABLE quota_type_code DISABLE TRIGGER USER;
ALTER TABLE reforest_declare_type_code DISABLE TRIGGER USER;
ALTER TABLE request_item DISABLE TRIGGER USER;
ALTER TABLE results_audit_action_code DISABLE TRIGGER USER;
ALTER TABLE results_audit_detail DISABLE TRIGGER USER;
ALTER TABLE results_audit_event DISABLE TRIGGER USER;
ALTER TABLE results_electronic_submission DISABLE TRIGGER USER;
ALTER TABLE sale_method_code DISABLE TRIGGER USER;
ALTER TABLE sale_type_code DISABLE TRIGGER USER;
ALTER TABLE salvage_type_code DISABLE TRIGGER USER;
ALTER TABLE sb_category_code DISABLE TRIGGER USER;
ALTER TABLE silviculture_comment DISABLE TRIGGER USER;
ALTER TABLE silviculture_project DISABLE TRIGGER USER;
ALTER TABLE silv_activity_measurement_code DISABLE TRIGGER USER;
ALTER TABLE silv_admin_zone DISABLE TRIGGER USER;
ALTER TABLE silv_base_code DISABLE TRIGGER USER;
ALTER TABLE silv_comment_source_code DISABLE TRIGGER USER;
ALTER TABLE silv_comment_type_code DISABLE TRIGGER USER;
ALTER TABLE silv_comment_xref DISABLE TRIGGER USER;
ALTER TABLE silv_cut_phase_code DISABLE TRIGGER USER;
ALTER TABLE silv_damage_agent_code DISABLE TRIGGER USER;
ALTER TABLE silv_fund_srce_code DISABLE TRIGGER USER;
ALTER TABLE silv_method_code DISABLE TRIGGER USER;
ALTER TABLE silv_milestone_type_code DISABLE TRIGGER USER;
ALTER TABLE silv_objective_code DISABLE TRIGGER USER;
ALTER TABLE silv_project_comment_link DISABLE TRIGGER USER;
ALTER TABLE silv_project_status_code DISABLE TRIGGER USER;
ALTER TABLE silv_relief_application DISABLE TRIGGER USER;
ALTER TABLE silv_relief_appl_status_code DISABLE TRIGGER USER;
ALTER TABLE silv_reserve_code DISABLE TRIGGER USER;
ALTER TABLE silv_reserve_objective_code DISABLE TRIGGER USER;
ALTER TABLE silv_statute_code DISABLE TRIGGER USER;
ALTER TABLE silv_system_code DISABLE TRIGGER USER;
ALTER TABLE silv_system_variant_code DISABLE TRIGGER USER;
ALTER TABLE silv_technique_code DISABLE TRIGGER USER;
ALTER TABLE silv_tree_species_code DISABLE TRIGGER USER;
ALTER TABLE site_class_code DISABLE TRIGGER USER;
ALTER TABLE site_index_source_code DISABLE TRIGGER USER;
ALTER TABLE standards_regime DISABLE TRIGGER USER;
ALTER TABLE standards_regime_status_code DISABLE TRIGGER USER;
ALTER TABLE stocking_class_code DISABLE TRIGGER USER;
ALTER TABLE stocking_comment_link DISABLE TRIGGER USER;
ALTER TABLE stocking_ecology DISABLE TRIGGER USER;
ALTER TABLE stocking_ecology_archive DISABLE TRIGGER USER;
ALTER TABLE stocking_event_history DISABLE TRIGGER USER;
ALTER TABLE stocking_layer DISABLE TRIGGER USER;
ALTER TABLE stocking_layer_archive DISABLE TRIGGER USER;
ALTER TABLE stocking_layer_code DISABLE TRIGGER USER;
ALTER TABLE stocking_layer_species DISABLE TRIGGER USER;
ALTER TABLE stocking_layer_species_archive DISABLE TRIGGER USER;
ALTER TABLE stocking_milestone DISABLE TRIGGER USER;
ALTER TABLE stocking_milestone_cmt_link DISABLE TRIGGER USER;
ALTER TABLE stocking_standard_unit DISABLE TRIGGER USER;
ALTER TABLE stocking_standard_unit_amd DISABLE TRIGGER USER;
ALTER TABLE stocking_standard_unit_archive DISABLE TRIGGER USER;
ALTER TABLE stocking_status_code DISABLE TRIGGER USER;
ALTER TABLE stocking_type_code DISABLE TRIGGER USER;
ALTER TABLE storage_files DISABLE TRIGGER USER;
ALTER TABLE tenure_file_status_code DISABLE TRIGGER USER;
ALTER TABLE timber_mark DISABLE TRIGGER USER;
ALTER TABLE tree_cover_pattern_code DISABLE TRIGGER USER;
ALTER TABLE tree_size_unit_code DISABLE TRIGGER USER;
ALTER TABLE tree_species_code DISABLE TRIGGER USER;
ALTER TABLE tsb_number_code DISABLE TRIGGER USER;
ALTER TABLE unit_bid_code DISABLE TRIGGER USER;

\i './data/ACTIVITY_TREATMENT_UNIT_data.sql'
\i './data/ACTIVITY_TU_COMMENT_LINK_data.sql'
\i './data/AGE_CLASS_CODE_data.sql'
\i './data/BLOCK_STATUS_CODE_data.sql'
\i './data/CASCADE_SPLIT_CODE_data.sql'
\i './data/CLIENT_LOCATION_data.sql'
\i './data/CORP_CAPTURE_METHOD_data.sql'
\i './data/CREW_CONTRACT_HIRE_CODE_data.sql'
\i './data/CROWN_LANDS_REGION_CODE_data.sql'
\i './data/CUT_BLOCK_data.sql'
\i './data/CUT_BLOCK_CLIENT_data.sql'
\i './data/CUT_BLOCK_CLIENT_TYPE_CODE_data.sql'
\i './data/CUT_BLOCK_OPEN_ADMIN_data.sql'
\i './data/CUT_REGULATION_CODE_data.sql'
\i './data/DATA_SOURCE_CODE_data.sql'
\i './data/DISTURBANCE_CODE_data.sql'
\i './data/FEATURE_CLASSES_data.sql'
\i './data/FILE_CLIENT_TYPE_CODE_data.sql'
\i './data/FILE_TYPE_CODE_data.sql'
\i './data/FIRE_HARVESTING_REASON_CODE_data.sql'
\i './data/FOREST_CLIENT_data.sql'
\i './data/FOREST_COVER_data.sql'
\i './data/FOREST_COVER_ARCHIVE_data.sql'
\i './data/FOREST_COVER_ARCHIVE_GEOMETRY_data.sql'
\i './data/FOREST_COVER_GEOMETRY_data.sql'
\i './data/FOREST_COVER_LAYER_data.sql'
\i './data/FOREST_COVER_LAYER_ARCHIVE_data.sql'
\i './data/FOREST_COVER_LAYER_CODE_data.sql'
\i './data/FOREST_COVER_LAYER_SPECIES_data.sql'
\i './data/FOREST_COVER_LAYER_SPECIES_ARC_data.sql'
\i './data/FOREST_COVER_NON_MAPPED_ARC_data.sql'
\i './data/FOREST_COVER_NON_MAPPED_AREA_data.sql'
\i './data/FOREST_FILE_CLIENT_data.sql'
\i './data/FOREST_MGMT_UNIT_data.sql'
\i './data/FOREST_STEWARDSHIP_PLAN_data.sql'
\i './data/FORHEALTH_RSLT_data.sql'
\i './data/FORHEALTH_RSLT_ARCHIVE_data.sql'
\i './data/FOR_CLIENT_LINK_data.sql'
\i './data/FSP_STANDARDS_REGIME_XREF_data.sql'
\i './data/FSP_STATUS_CODE_data.sql'
\i './data/HARVESTING_AUTHORITY_data.sql'
\i './data/HARVEST_AUTH_EXTEND_REAS_CODE_data.sql'
\i './data/HARVEST_AUTH_STATUS_CODE_data.sql'
\i './data/HARVEST_SALE_data.sql'
\i './data/HARVEST_TYPE_CODE_data.sql'
\i './data/HAULING_AUTHORITY_data.sql'
\i './data/HEIGHT_CLASS_CODE_data.sql'
\i './data/LICENCE_TO_CUT_CODE_data.sql'
\i './data/MARKING_INSTRUMENT_CODE_data.sql'
\i './data/MARKING_METHOD_CODE_data.sql'
\i './data/MARK_EXTENSION_REASON_CODE_data.sql'
\i './data/MGMT_UNIT_TYPE_CODE_data.sql'
\i './data/MIME_TYPE_CODE_data.sql'
\i './data/OPENING_data.sql'
\i './data/OPENING_AMENDMENT_HISTORY_data.sql'
\i './data/OPENING_ATTACHMENT_data.sql'
\i './data/OPENING_COMMENT_LINK_data.sql'
\i './data/OPENING_GEOMETRY_data.sql'
\i './data/OPENING_LAND_STATUS_data.sql'
\i './data/OPENING_STATUS_CODE_data.sql'
\i './data/OPEN_CATEGORY_CODE_data.sql'
\i './data/OPEN_VIEWABLE_CATEGORY_data.sql'
\i './data/ORG_UNIT_data.sql'
\i './data/PAYMENT_METHOD_CODE_data.sql'
\i './data/PLANTING_RSLT_data.sql'
\i './data/PROV_FOREST_USE_data.sql'
\i './data/QUOTA_TYPE_CODE_data.sql'
\i './data/REFOREST_DECLARE_TYPE_CODE_data.sql'
\i './data/REQUEST_ITEM_data.sql'
\i './data/RESULTS_AUDIT_ACTION_CODE_data.sql'
\i './data/RESULTS_AUDIT_DETAIL_data.sql'
\i './data/RESULTS_AUDIT_EVENT_data.sql'
\i './data/RESULTS_ELECTRONIC_SUBMISSION_data.sql'
\i './data/SALE_METHOD_CODE_data.sql'
\i './data/SALE_TYPE_CODE_data.sql'
\i './data/SALVAGE_TYPE_CODE_data.sql'
\i './data/SB_CATEGORY_CODE_data.sql'
\i './data/SILVICULTURE_COMMENT_data.sql'
\i './data/SILVICULTURE_PROJECT_data.sql'
\i './data/SILV_ACTIVITY_MEASUREMENT_CODE_data.sql'
\i './data/SILV_ADMIN_ZONE_data.sql'
\i './data/SILV_BASE_CODE_data.sql'
\i './data/SILV_COMMENT_SOURCE_CODE_data.sql'
\i './data/SILV_COMMENT_TYPE_CODE_data.sql'
\i './data/SILV_COMMENT_XREF_data.sql'
\i './data/SILV_CUT_PHASE_CODE_data.sql'
\i './data/SILV_DAMAGE_AGENT_CODE_data.sql'
\i './data/SILV_FUND_SRCE_CODE_data.sql'
\i './data/SILV_METHOD_CODE_data.sql'
\i './data/SILV_MILESTONE_TYPE_CODE_data.sql'
\i './data/SILV_OBJECTIVE_CODE_data.sql'
\i './data/SILV_PROJECT_COMMENT_LINK_data.sql'
\i './data/SILV_PROJECT_STATUS_CODE_data.sql'
\i './data/SILV_RELIEF_APPLICATION_data.sql'
\i './data/SILV_RELIEF_APPL_STATUS_CODE_data.sql'
\i './data/SILV_RESERVE_CODE_data.sql'
\i './data/SILV_RESERVE_OBJECTIVE_CODE_data.sql'
\i './data/SILV_STATUTE_CODE_data.sql'
\i './data/SILV_SYSTEM_CODE_data.sql'
\i './data/SILV_SYSTEM_VARIANT_CODE_data.sql'
\i './data/SILV_TECHNIQUE_CODE_data.sql'
\i './data/SILV_TREE_SPECIES_CODE_data.sql'
\i './data/SITE_CLASS_CODE_data.sql'
\i './data/SITE_INDEX_SOURCE_CODE_data.sql'
\i './data/STANDARDS_REGIME_data.sql'
\i './data/STANDARDS_REGIME_STATUS_CODE_data.sql'
\i './data/STOCKING_CLASS_CODE_data.sql'
\i './data/STOCKING_COMMENT_LINK_data.sql'
\i './data/STOCKING_ECOLOGY_data.sql'
\i './data/STOCKING_ECOLOGY_ARCHIVE_data.sql'
\i './data/STOCKING_EVENT_HISTORY_data.sql'
\i './data/STOCKING_LAYER_data.sql'
\i './data/STOCKING_LAYER_ARCHIVE_data.sql'
\i './data/STOCKING_LAYER_CODE_data.sql'
\i './data/STOCKING_LAYER_SPECIES_data.sql'
\i './data/STOCKING_LAYER_SPECIES_ARCHIVE_data.sql'
\i './data/STOCKING_MILESTONE_data.sql'
\i './data/STOCKING_MILESTONE_CMT_LINK_data.sql'
\i './data/STOCKING_STANDARD_UNIT_data.sql'
\i './data/STOCKING_STANDARD_UNIT_AMD_data.sql'
\i './data/STOCKING_STANDARD_UNIT_ARCHIVE_data.sql'
\i './data/STOCKING_STATUS_CODE_data.sql'
\i './data/STOCKING_TYPE_CODE_data.sql'
\i './data/STORAGE_FILES_data.sql'
\i './data/TENURE_FILE_STATUS_CODE_data.sql'
\i './data/TIMBER_MARK_data.sql'
\i './data/TREE_COVER_PATTERN_CODE_data.sql'
\i './data/TREE_SIZE_UNIT_CODE_data.sql'
\i './data/TREE_SPECIES_CODE_data.sql'
\i './data/TSB_NUMBER_CODE_data.sql'
\i './data/UNIT_BID_CODE_data.sql'

COMMIT;

