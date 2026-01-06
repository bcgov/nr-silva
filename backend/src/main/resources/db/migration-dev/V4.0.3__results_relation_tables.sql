-- silva.activity_treatment_unit foreign keys

ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_cboa_fk FOREIGN KEY (cut_block_open_admin_id) REFERENCES silva.cut_block_open_admin(cut_block_open_admin_id);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_dc_fk FOREIGN KEY (disturbance_code) REFERENCES silva.disturbance_code(disturbance_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_proj_fk FOREIGN KEY (silviculture_project_id) REFERENCES silva.silviculture_project(silviculture_project_id);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_res_fk FOREIGN KEY (results_submission_id) REFERENCES silva.results_electronic_submission(results_submission_id);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_sam_fk FOREIGN KEY (silv_activity_measurement_code) REFERENCES silva.silv_activity_measurement_code(silv_activity_measurement_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_sbc_fk FOREIGN KEY (silv_base_code) REFERENCES silva.silv_base_code(silv_base_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_scpc_fk FOREIGN KEY (silv_cut_phase_code) REFERENCES silva.silv_cut_phase_code(silv_cut_phase_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_sfsc_fk FOREIGN KEY (silv_fund_srce_code) REFERENCES silva.silv_fund_srce_code(silv_fund_srce_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_sfsc_plnd_fk FOREIGN KEY (plan_silv_fund_srce_code) REFERENCES silva.silv_fund_srce_code(silv_fund_srce_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_smc_fk FOREIGN KEY (silv_method_code) REFERENCES silva.silv_method_code(silv_method_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_smc_plnd_fk FOREIGN KEY (plan_silv_method_code) REFERENCES silva.silv_method_code(silv_method_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_soc1_fk FOREIGN KEY (silv_objective_code_1) REFERENCES silva.silv_objective_code(silv_objective_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_soc2_fk FOREIGN KEY (silv_objective_code_2) REFERENCES silva.silv_objective_code(silv_objective_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_soc3_fk FOREIGN KEY (silv_objective_code_3) REFERENCES silva.silv_objective_code(silv_objective_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_soc_plnd1_fk FOREIGN KEY (plan_silv_objective_code_1) REFERENCES silva.silv_objective_code(silv_objective_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_soc_plnd2_fk FOREIGN KEY (plan_silv_objective_code_2) REFERENCES silva.silv_objective_code(silv_objective_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_soc_plnd3_fk FOREIGN KEY (plan_silv_objective_code_3) REFERENCES silva.silv_objective_code(silv_objective_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_ssc_fk FOREIGN KEY (silv_system_code) REFERENCES silva.silv_system_code(silv_system_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_ssvc_fk FOREIGN KEY (silv_system_variant_code) REFERENCES silva.silv_system_variant_code(silv_system_variant_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_stc_fk FOREIGN KEY (silv_technique_code) REFERENCES silva.silv_technique_code(silv_technique_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_stc_plnd_fk FOREIGN KEY (plan_silv_technique_code) REFERENCES silva.silv_technique_code(silv_technique_code);
ALTER TABLE silva.activity_treatment_unit ADD CONSTRAINT atu_stsc_fk FOREIGN KEY (silv_tree_species_code) REFERENCES silva.silv_tree_species_code(silv_tree_species_code);


-- silva.activity_tu_comment_link foreign keys

ALTER TABLE silva.activity_tu_comment_link ADD CONSTRAINT acl_atu_fk FOREIGN KEY (activity_treatment_unit_id) REFERENCES silva.activity_treatment_unit(activity_treatment_unit_id);
ALTER TABLE silva.activity_tu_comment_link ADD CONSTRAINT acl_silvc_fk FOREIGN KEY (silviculture_comment_id) REFERENCES silva.silviculture_comment(silviculture_comment_id);


-- silva.cut_block foreign keys

ALTER TABLE silva.cut_block ADD CONSTRAINT cb_crc_fk FOREIGN KEY (cut_regulation_code) REFERENCES silva.cut_regulation_code(cut_regulation_code);
ALTER TABLE silva.cut_block ADD CONSTRAINT cb_haa_fk FOREIGN KEY (timber_mark) REFERENCES silva.hauling_authority(timber_mark);
ALTER TABLE silva.cut_block ADD CONSTRAINT cb_hva_fk FOREIGN KEY (hva_skey) REFERENCES silva.harvesting_authority(hva_skey);
ALTER TABLE silva.cut_block ADD CONSTRAINT cb_rdtc_fk FOREIGN KEY (reforest_declare_type_code) REFERENCES silva.reforest_declare_type_code(reforest_declare_type_code);
ALTER TABLE silva.cut_block ADD CONSTRAINT cblk_bscd_fk FOREIGN KEY (block_status_st) REFERENCES silva.block_status_code(block_status_code);
ALTER TABLE silva.cut_block ADD CONSTRAINT cblk_fhrc_fk FOREIGN KEY (fire_harvesting_reason_code) REFERENCES silva.fire_harvesting_reason_code(fire_harvesting_reason_code);
ALTER TABLE silva.cut_block ADD CONSTRAINT cblk_tm_fk FOREIGN KEY (timber_mark) REFERENCES silva.timber_mark(timber_mark);


-- silva.cut_block_client foreign keys

ALTER TABLE silva.cut_block_client ADD CONSTRAINT cbc_cb_fk FOREIGN KEY (cb_skey) REFERENCES silva.cut_block(cb_skey);
ALTER TABLE silva.cut_block_client ADD CONSTRAINT cbc_cbctc_fk FOREIGN KEY (cut_block_client_type_code) REFERENCES silva.cut_block_client_type_code(cut_block_client_type_code);


-- silva.cut_block_open_admin foreign keys

ALTER TABLE silva.cut_block_open_admin ADD CONSTRAINT cboa_cb_fk FOREIGN KEY (cb_skey) REFERENCES silva.cut_block(cb_skey);
ALTER TABLE silva.cut_block_open_admin ADD CONSTRAINT cboa_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);


-- silva.for_client_link foreign keys

ALTER TABLE silva.for_client_link ADD CONSTRAINT fcl_fctc_fk FOREIGN KEY (file_client_type) REFERENCES silva.file_client_type_code(file_client_type_code);
ALTER TABLE silva.for_client_link ADD CONSTRAINT fcl_pfu_fk FOREIGN KEY (forest_file_id) REFERENCES silva.prov_forest_use(forest_file_id);


-- silva.forest_cover foreign keys

ALTER TABLE silva.forest_cover ADD CONSTRAINT fc_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);
ALTER TABLE silva.forest_cover ADD CONSTRAINT fc_res_fk FOREIGN KEY (results_submission_id) REFERENCES silva.results_electronic_submission(results_submission_id);
ALTER TABLE silva.forest_cover ADD CONSTRAINT fc_scc1_fk FOREIGN KEY (stocking_class_code) REFERENCES silva.stocking_class_code(stocking_class_code);
ALTER TABLE silva.forest_cover ADD CONSTRAINT fc_scc2_fk FOREIGN KEY (site_class_code) REFERENCES silva.site_class_code(site_class_code);
ALTER TABLE silva.forest_cover ADD CONSTRAINT fc_sisc_fk FOREIGN KEY (site_index_source_code) REFERENCES silva.site_index_source_code(site_index_source_code);
ALTER TABLE silva.forest_cover ADD CONSTRAINT fc_src_fk FOREIGN KEY (silv_reserve_code) REFERENCES silva.silv_reserve_code(silv_reserve_code);
ALTER TABLE silva.forest_cover ADD CONSTRAINT fc_sroc_fk FOREIGN KEY (silv_reserve_objective_code) REFERENCES silva.silv_reserve_objective_code(silv_reserve_objective_code);
ALTER TABLE silva.forest_cover ADD CONSTRAINT fc_ssc6_fk FOREIGN KEY (stocking_status_code) REFERENCES silva.stocking_status_code(stocking_status_code);
ALTER TABLE silva.forest_cover ADD CONSTRAINT fc_ssu_fk FOREIGN KEY (stocking_standard_unit_id) REFERENCES silva.stocking_standard_unit(stocking_standard_unit_id);
ALTER TABLE silva.forest_cover ADD CONSTRAINT fc_stc3_fk FOREIGN KEY (stocking_type_code) REFERENCES silva.stocking_type_code(stocking_type_code);
ALTER TABLE silva.forest_cover ADD CONSTRAINT fc_tcpc_fk FOREIGN KEY (tree_cover_pattern_code) REFERENCES silva.tree_cover_pattern_code(tree_cover_pattern_code);
ALTER TABLE silva.forest_cover ADD CONSTRAINT fc_tsc_fk FOREIGN KEY (tree_species_code) REFERENCES silva.tree_species_code(tree_species_code);


-- silva.forest_cover_archive foreign keys

ALTER TABLE silva.forest_cover_archive ADD CONSTRAINT fca_scc2_fk FOREIGN KEY (site_class_code) REFERENCES silva.site_class_code(site_class_code);
ALTER TABLE silva.forest_cover_archive ADD CONSTRAINT fca_sisc_fk FOREIGN KEY (site_index_source_code) REFERENCES silva.site_index_source_code(site_index_source_code);
ALTER TABLE silva.forest_cover_archive ADD CONSTRAINT fca_src_fk FOREIGN KEY (silv_reserve_code) REFERENCES silva.silv_reserve_code(silv_reserve_code);
ALTER TABLE silva.forest_cover_archive ADD CONSTRAINT fca_sroc_fk FOREIGN KEY (silv_reserve_objective_code) REFERENCES silva.silv_reserve_objective_code(silv_reserve_objective_code);
ALTER TABLE silva.forest_cover_archive ADD CONSTRAINT fca_ssc6_fk FOREIGN KEY (stocking_status_code) REFERENCES silva.stocking_status_code(stocking_status_code);
ALTER TABLE silva.forest_cover_archive ADD CONSTRAINT fca_stc3_fk FOREIGN KEY (stocking_type_code) REFERENCES silva.stocking_type_code(stocking_type_code);
ALTER TABLE silva.forest_cover_archive ADD CONSTRAINT fca_tcpc_fk FOREIGN KEY (tree_cover_pattern_code) REFERENCES silva.tree_cover_pattern_code(tree_cover_pattern_code);
ALTER TABLE silva.forest_cover_archive ADD CONSTRAINT fca_tsc_fk FOREIGN KEY (tree_species_code) REFERENCES silva.tree_species_code(tree_species_code);


-- silva.forest_cover_archive_geometry foreign keys

ALTER TABLE silva.forest_cover_archive_geometry ADD CONSTRAINT fcag_cmc_fk FOREIGN KEY (capture_method_code) REFERENCES silva.corp_capture_method(capture_method_code);
ALTER TABLE silva.forest_cover_archive_geometry ADD CONSTRAINT fcag_corpfcc_fk FOREIGN KEY (feature_class_skey) REFERENCES silva.feature_classes(feature_class_skey);
ALTER TABLE silva.forest_cover_archive_geometry ADD CONSTRAINT fcag_dsc_fk FOREIGN KEY (data_source_code) REFERENCES silva.data_source_code(data_source_code);
ALTER TABLE silva.forest_cover_archive_geometry ADD CONSTRAINT fcag_fca_fk FOREIGN KEY (forest_cover_id,archive_date) REFERENCES silva.forest_cover_archive(forest_cover_id,archive_date);


-- silva.forest_cover_geometry foreign keys

ALTER TABLE silva.forest_cover_geometry ADD CONSTRAINT fcg_cmc_fk FOREIGN KEY (capture_method_code) REFERENCES silva.corp_capture_method(capture_method_code);
ALTER TABLE silva.forest_cover_geometry ADD CONSTRAINT fcg_corpfcc_fk FOREIGN KEY (feature_class_skey) REFERENCES silva.feature_classes(feature_class_skey);
ALTER TABLE silva.forest_cover_geometry ADD CONSTRAINT fcg_dsc_fk FOREIGN KEY (data_source_code) REFERENCES silva.data_source_code(data_source_code);
ALTER TABLE silva.forest_cover_geometry ADD CONSTRAINT fcg_fc_fk FOREIGN KEY (forest_cover_id) REFERENCES silva.forest_cover(forest_cover_id);


-- silva.forest_cover_layer foreign keys

ALTER TABLE silva.forest_cover_layer ADD CONSTRAINT fclr_fc3_fk FOREIGN KEY (forest_cover_id) REFERENCES silva.forest_cover(forest_cover_id);
ALTER TABLE silva.forest_cover_layer ADD CONSTRAINT fclr_fclc_fk FOREIGN KEY (forest_cover_layer_code) REFERENCES silva.forest_cover_layer_code(forest_cover_layer_code);
ALTER TABLE silva.forest_cover_layer ADD CONSTRAINT fclr_ssc6_fk FOREIGN KEY (stocking_status_code) REFERENCES silva.stocking_status_code(stocking_status_code);
ALTER TABLE silva.forest_cover_layer ADD CONSTRAINT fclr_stc3_fk FOREIGN KEY (stocking_type_code) REFERENCES silva.stocking_type_code(stocking_type_code);


-- silva.forest_cover_layer_archive foreign keys

ALTER TABLE silva.forest_cover_layer_archive ADD CONSTRAINT fcla_fca_fk FOREIGN KEY (forest_cover_id,archive_date) REFERENCES silva.forest_cover_archive(forest_cover_id,archive_date);
ALTER TABLE silva.forest_cover_layer_archive ADD CONSTRAINT fcla_fclc_fk FOREIGN KEY (forest_cover_layer_code) REFERENCES silva.forest_cover_layer_code(forest_cover_layer_code);
ALTER TABLE silva.forest_cover_layer_archive ADD CONSTRAINT fcla_ssc6_fk FOREIGN KEY (stocking_status_code) REFERENCES silva.stocking_status_code(stocking_status_code);
ALTER TABLE silva.forest_cover_layer_archive ADD CONSTRAINT fcla_stc3_fk FOREIGN KEY (stocking_type_code) REFERENCES silva.stocking_type_code(stocking_type_code);


-- silva.forest_cover_layer_species foreign keys

ALTER TABLE silva.forest_cover_layer_species ADD CONSTRAINT fcls_fclr_fk FOREIGN KEY (forest_cover_id,forest_cover_layer_id) REFERENCES silva.forest_cover_layer(forest_cover_id,forest_cover_layer_id);
ALTER TABLE silva.forest_cover_layer_species ADD CONSTRAINT fcls_tsc_fk FOREIGN KEY (tree_species_code) REFERENCES silva.tree_species_code(tree_species_code);


-- silva.forest_cover_layer_species_arc foreign keys

ALTER TABLE silva.forest_cover_layer_species_arc ADD CONSTRAINT fclsa_fcla_fk FOREIGN KEY (forest_cover_id,archive_date,forest_cover_layer_id) REFERENCES silva.forest_cover_layer_archive(forest_cover_id,archive_date,forest_cover_layer_id);
ALTER TABLE silva.forest_cover_layer_species_arc ADD CONSTRAINT fclsa_tsc_fk FOREIGN KEY (tree_species_code) REFERENCES silva.tree_species_code(tree_species_code);


-- silva.forest_cover_non_mapped_arc foreign keys

ALTER TABLE silva.forest_cover_non_mapped_arc ADD CONSTRAINT fcnma1_fca_fk FOREIGN KEY (forest_cover_id,archive_date) REFERENCES silva.forest_cover_archive(forest_cover_id,archive_date);
ALTER TABLE silva.forest_cover_non_mapped_arc ADD CONSTRAINT fcnma1_ssc6_fk FOREIGN KEY (stocking_status_code) REFERENCES silva.stocking_status_code(stocking_status_code);
ALTER TABLE silva.forest_cover_non_mapped_arc ADD CONSTRAINT fcnma1_stc3_fk FOREIGN KEY (stocking_type_code) REFERENCES silva.stocking_type_code(stocking_type_code);


-- silva.forest_cover_non_mapped_area foreign keys

ALTER TABLE silva.forest_cover_non_mapped_area ADD CONSTRAINT fcnma_fc_fk FOREIGN KEY (forest_cover_id) REFERENCES silva.forest_cover(forest_cover_id);
ALTER TABLE silva.forest_cover_non_mapped_area ADD CONSTRAINT fcnma_ssc6_fk FOREIGN KEY (stocking_status_code) REFERENCES silva.stocking_status_code(stocking_status_code);
ALTER TABLE silva.forest_cover_non_mapped_area ADD CONSTRAINT fcnma_stc3_fk FOREIGN KEY (stocking_type_code) REFERENCES silva.stocking_type_code(stocking_type_code);


-- silva.forest_file_client foreign keys

ALTER TABLE silva.forest_file_client ADD CONSTRAINT ffc_ffctc_fk FOREIGN KEY (forest_file_client_type_code) REFERENCES silva.file_client_type_code(file_client_type_code);
ALTER TABLE silva.forest_file_client ADD CONSTRAINT ffc_pfu_fk FOREIGN KEY (forest_file_id) REFERENCES silva.prov_forest_use(forest_file_id);


-- silva.forest_mgmt_unit foreign keys

ALTER TABLE silva.forest_mgmt_unit ADD CONSTRAINT fmu_mutc_fk FOREIGN KEY (mgmt_unit_type_code) REFERENCES silva.mgmt_unit_type_code(mgmt_unit_type_code);


-- silva.forest_stewardship_plan foreign keys

ALTER TABLE silva.forest_stewardship_plan ADD CONSTRAINT fsp_fspsc_fk FOREIGN KEY (fsp_status_code) REFERENCES silva.fsp_status_code(fsp_status_code);


-- silva.forhealth_rslt foreign keys

ALTER TABLE silva.forhealth_rslt ADD CONSTRAINT fr_fclr_fk FOREIGN KEY (forest_cover_id,forest_cover_layer_id) REFERENCES silva.forest_cover_layer(forest_cover_id,forest_cover_layer_id);
ALTER TABLE silva.forhealth_rslt ADD CONSTRAINT fr_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);
ALTER TABLE silva.forhealth_rslt ADD CONSTRAINT fr_sdac_fk FOREIGN KEY (silv_damage_agent_code) REFERENCES silva.silv_damage_agent_code(silv_damage_agent_code);


-- silva.forhealth_rslt_archive foreign keys

ALTER TABLE silva.forhealth_rslt_archive ADD CONSTRAINT fra_fcla_fk FOREIGN KEY (forest_cover_id,forest_cover_archive_date,forest_cover_layer_id) REFERENCES silva.forest_cover_layer_archive(forest_cover_id,archive_date,forest_cover_layer_id);
ALTER TABLE silva.forhealth_rslt_archive ADD CONSTRAINT fra_sdac_fk FOREIGN KEY (silv_damage_agent_code) REFERENCES silva.silv_damage_agent_code(silv_damage_agent_code);


-- silva.fsp_standards_regime_xref foreign keys

ALTER TABLE silva.fsp_standards_regime_xref ADD CONSTRAINT fspsrx_fsp_fk FOREIGN KEY (fsp_id,fsp_amendment_number) REFERENCES silva.forest_stewardship_plan(fsp_id,fsp_amendment_number);
ALTER TABLE silva.fsp_standards_regime_xref ADD CONSTRAINT fspsrx_sr_fk FOREIGN KEY (standards_regime_id) REFERENCES silva.standards_regime(standards_regime_id);


-- silva.harvesting_authority foreign keys

ALTER TABLE silva.harvesting_authority ADD CONSTRAINT hva_clrc_fk FOREIGN KEY (crown_lands_region_code) REFERENCES silva.crown_lands_region_code(crown_lands_region_code);
ALTER TABLE silva.harvesting_authority ADD CONSTRAINT hva_csc_fk FOREIGN KEY (cascade_split_code) REFERENCES silva.cascade_split_code(cascade_split_code);
ALTER TABLE silva.harvesting_authority ADD CONSTRAINT hva_haerc_fk FOREIGN KEY (harvest_auth_extend_reas_code) REFERENCES silva.harvest_auth_extend_reas_code(harvest_auth_extend_reas_code);
ALTER TABLE silva.harvesting_authority ADD CONSTRAINT hva_hasc_fk FOREIGN KEY (harvest_auth_status_code) REFERENCES silva.harvest_auth_status_code(harvest_auth_status_code);
ALTER TABLE silva.harvesting_authority ADD CONSTRAINT hva_htc_fk FOREIGN KEY (harvest_type_code) REFERENCES silva.harvest_type_code(harvest_type_code);
ALTER TABLE silva.harvesting_authority ADD CONSTRAINT hva_ltccd_fk FOREIGN KEY (licence_to_cut_code) REFERENCES silva.licence_to_cut_code(licence_to_cut_code);
ALTER TABLE silva.harvesting_authority ADD CONSTRAINT hva_mutc_fk FOREIGN KEY (mgmt_unit_type_code) REFERENCES silva.mgmt_unit_type_code(mgmt_unit_type_code);
ALTER TABLE silva.harvesting_authority ADD CONSTRAINT hva_pfu_fk FOREIGN KEY (forest_file_id) REFERENCES silva.prov_forest_use(forest_file_id);
ALTER TABLE silva.harvesting_authority ADD CONSTRAINT hva_qtc_fk FOREIGN KEY (quota_type_code) REFERENCES silva.quota_type_code(quota_type_code);
ALTER TABLE silva.harvesting_authority ADD CONSTRAINT hva_salvtcd_fk FOREIGN KEY (salvage_type_code) REFERENCES silva.salvage_type_code(salvage_type_code);


-- silva.hauling_authority foreign keys

ALTER TABLE silva.hauling_authority ADD CONSTRAINT haa_micd_fk FOREIGN KEY (marking_instrument_code) REFERENCES silva.marking_instrument_code(marking_instrument_code);
ALTER TABLE silva.hauling_authority ADD CONSTRAINT haa_mmcd_fk FOREIGN KEY (marking_method_code) REFERENCES silva.marking_method_code(marking_method_code);
ALTER TABLE silva.hauling_authority ADD CONSTRAINT haa_pfu_fk FOREIGN KEY (forest_file_id) REFERENCES silva.prov_forest_use(forest_file_id);


-- silva.open_viewable_category foreign keys

ALTER TABLE silva.open_viewable_category ADD CONSTRAINT ovc_occ_fk FOREIGN KEY (open_category_code) REFERENCES silva.open_category_code(open_category_code);
ALTER TABLE silva.open_viewable_category ADD CONSTRAINT ovc_osc_fk FOREIGN KEY (opening_status_code) REFERENCES silva.opening_status_code(opening_status_code);


-- silva.opening foreign keys

ALTER TABLE silva.opening ADD CONSTRAINT o_acc_fk FOREIGN KEY (prev_age_class_code) REFERENCES silva.age_class_code(age_class_code);
ALTER TABLE silva.opening ADD CONSTRAINT o_hcc_fk FOREIGN KEY (prev_height_class_code) REFERENCES silva.height_class_code(height_class_code);
ALTER TABLE silva.opening ADD CONSTRAINT o_occ_fk FOREIGN KEY (open_category_code) REFERENCES silva.open_category_code(open_category_code);
ALTER TABLE silva.opening ADD CONSTRAINT o_osc_fk FOREIGN KEY (opening_status_code) REFERENCES silva.opening_status_code(opening_status_code);
ALTER TABLE silva.opening ADD CONSTRAINT o_res_fk FOREIGN KEY (results_submission_id) REFERENCES silva.results_electronic_submission(results_submission_id);
ALTER TABLE silva.opening ADD CONSTRAINT o_saz_fk FOREIGN KEY (org_unit_no,dist_admin_zone) REFERENCES silva.silv_admin_zone(org_unit_no,dist_admin_zone);
ALTER TABLE silva.opening ADD CONSTRAINT o_scc1_fk FOREIGN KEY (prev_stocking_class_code) REFERENCES silva.stocking_class_code(stocking_class_code);
ALTER TABLE silva.opening ADD CONSTRAINT o_scc2_fk FOREIGN KEY (prev_site_class_code) REFERENCES silva.site_class_code(site_class_code);
ALTER TABLE silva.opening ADD CONSTRAINT o_sisc_fk FOREIGN KEY (prev_site_index_source_code) REFERENCES silva.site_index_source_code(site_index_source_code);
ALTER TABLE silva.opening ADD CONSTRAINT o_ssc6_fk FOREIGN KEY (prev_stocking_status_code) REFERENCES silva.stocking_status_code(stocking_status_code);
ALTER TABLE silva.opening ADD CONSTRAINT o_tnc1_fk FOREIGN KEY (tsb_number_code) REFERENCES silva.tsb_number_code(tsb_number_code);
ALTER TABLE silva.opening ADD CONSTRAINT o_tsc1_fk FOREIGN KEY (prev_tree_spp1_code) REFERENCES silva.tree_species_code(tree_species_code);
ALTER TABLE silva.opening ADD CONSTRAINT o_tsc2_fk FOREIGN KEY (prev_tree_spp2_code) REFERENCES silva.tree_species_code(tree_species_code);


-- silva.opening_amendment_history foreign keys

ALTER TABLE silva.opening_amendment_history ADD CONSTRAINT oah_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);


-- silva.opening_attachment foreign keys

ALTER TABLE silva.opening_attachment ADD CONSTRAINT oa_mtc_fk FOREIGN KEY (mime_type_code) REFERENCES silva.mime_type_code(mime_type_code);
ALTER TABLE silva.opening_attachment ADD CONSTRAINT oa_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);


-- silva.opening_comment_link foreign keys

ALTER TABLE silva.opening_comment_link ADD CONSTRAINT ocl_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);
ALTER TABLE silva.opening_comment_link ADD CONSTRAINT ocl_silvc_fk FOREIGN KEY (silviculture_comment_id) REFERENCES silva.silviculture_comment(silviculture_comment_id);


-- silva.opening_geometry foreign keys

ALTER TABLE silva.opening_geometry ADD CONSTRAINT og_cmc_fk FOREIGN KEY (capture_method_code) REFERENCES silva.corp_capture_method(capture_method_code);
ALTER TABLE silva.opening_geometry ADD CONSTRAINT og_corpfcc_fk FOREIGN KEY (feature_class_skey) REFERENCES silva.feature_classes(feature_class_skey);
ALTER TABLE silva.opening_geometry ADD CONSTRAINT og_dsc_fk FOREIGN KEY (data_source_code) REFERENCES silva.data_source_code(data_source_code);
ALTER TABLE silva.opening_geometry ADD CONSTRAINT og_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);


-- silva.opening_land_status foreign keys

ALTER TABLE silva.opening_land_status ADD CONSTRAINT ols_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);


-- silva.planting_rslt foreign keys

ALTER TABLE silva.planting_rslt ADD CONSTRAINT pr_atu_fk FOREIGN KEY (activity_treatment_unit_id) REFERENCES silva.activity_treatment_unit(activity_treatment_unit_id);
ALTER TABLE silva.planting_rslt ADD CONSTRAINT pr_sparri_fk FOREIGN KEY (request_skey,item_id) REFERENCES silva.request_item(request_skey,item_id);
ALTER TABLE silva.planting_rslt ADD CONSTRAINT pr_stsc_fk FOREIGN KEY (silv_tree_species_code) REFERENCES silva.silv_tree_species_code(silv_tree_species_code);


-- silva.prov_forest_use foreign keys

ALTER TABLE silva.prov_forest_use ADD CONSTRAINT pfu_fmu_fk FOREIGN KEY (mgmt_unit_id,mgmt_unit_type) REFERENCES silva.forest_mgmt_unit(mgmt_unit_id,mgmt_unit_type_code);
ALTER TABLE silva.prov_forest_use ADD CONSTRAINT pfu_ftcd_fk FOREIGN KEY (file_type_code) REFERENCES silva.file_type_code(file_type_code);
ALTER TABLE silva.prov_forest_use ADD CONSTRAINT pfu_tfscd_fk FOREIGN KEY (file_status_st) REFERENCES silva.tenure_file_status_code(tenure_file_status_code);


-- silva.results_audit_detail foreign keys

ALTER TABLE silva.results_audit_detail ADD CONSTRAINT rad_rae_fk FOREIGN KEY (results_audit_event_id) REFERENCES silva.results_audit_event(results_audit_event_id);


-- silva.results_audit_event foreign keys

ALTER TABLE silva.results_audit_event ADD CONSTRAINT rae_raac_fk FOREIGN KEY (results_audit_action_code) REFERENCES silva.results_audit_action_code(results_audit_action_code);


-- silva.silv_comment_xref foreign keys

ALTER TABLE silva.silv_comment_xref ADD CONSTRAINT scx_scsc_fk FOREIGN KEY (silv_comment_source_code) REFERENCES silva.silv_comment_source_code(silv_comment_source_code);
ALTER TABLE silva.silv_comment_xref ADD CONSTRAINT scx_sctc_fk FOREIGN KEY (silv_comment_type_code) REFERENCES silva.silv_comment_type_code(silv_comment_type_code);


-- silva.silv_project_comment_link foreign keys

ALTER TABLE silva.silv_project_comment_link ADD CONSTRAINT spcl_proj_fk FOREIGN KEY (silviculture_project_id) REFERENCES silva.silviculture_project(silviculture_project_id);
ALTER TABLE silva.silv_project_comment_link ADD CONSTRAINT spcl_silvc_fk FOREIGN KEY (silviculture_comment_id) REFERENCES silva.silviculture_comment(silviculture_comment_id);


-- silva.silv_relief_application foreign keys

ALTER TABLE silva.silv_relief_application ADD CONSTRAINT sra_atu_fk FOREIGN KEY (activity_treatment_unit_id) REFERENCES silva.activity_treatment_unit(activity_treatment_unit_id);
ALTER TABLE silva.silv_relief_application ADD CONSTRAINT sra_oah_fk FOREIGN KEY (amendment_opening_id,opening_amendment_number) REFERENCES silva.opening_amendment_history(opening_id,opening_amendment_number);
ALTER TABLE silva.silv_relief_application ADD CONSTRAINT sra_srascd_fk FOREIGN KEY (silv_relief_appl_status_code) REFERENCES silva.silv_relief_appl_status_code(silv_relief_appl_status_code);


-- silva.silviculture_comment foreign keys

ALTER TABLE silva.silviculture_comment ADD CONSTRAINT silvc_scx_fk FOREIGN KEY (silv_comment_type_code,silv_comment_source_code) REFERENCES silva.silv_comment_xref(silv_comment_type_code,silv_comment_source_code);


-- silva.silviculture_project foreign keys

ALTER TABLE silva.silviculture_project ADD CONSTRAINT proj_cchcd_fk FOREIGN KEY (crew_contract_hire_code) REFERENCES silva.crew_contract_hire_code(crew_contract_hire_code);
ALTER TABLE silva.silviculture_project ADD CONSTRAINT proj_pscd_fk FOREIGN KEY (silv_project_status_code) REFERENCES silva.silv_project_status_code(silv_project_status_code);
ALTER TABLE silva.silviculture_project ADD CONSTRAINT proj_saz_fk FOREIGN KEY (org_unit_no,dist_admin_zone) REFERENCES silva.silv_admin_zone(org_unit_no,dist_admin_zone);
ALTER TABLE silva.silviculture_project ADD CONSTRAINT proj_sbc_fk FOREIGN KEY (silv_base_code) REFERENCES silva.silv_base_code(silv_base_code);
ALTER TABLE silva.silviculture_project ADD CONSTRAINT proj_ubcd_fk FOREIGN KEY (unit_bid_code) REFERENCES silva.unit_bid_code(unit_bid_code);


-- silva.standards_regime foreign keys

ALTER TABLE silva.standards_regime ADD CONSTRAINT sr_srsc_fk FOREIGN KEY (standards_regime_status_code) REFERENCES silva.standards_regime_status_code(standards_regime_status_code);
ALTER TABLE silva.standards_regime ADD CONSTRAINT sr_sstcd_fk FOREIGN KEY (silv_statute_code) REFERENCES silva.silv_statute_code(silv_statute_code);


-- silva.stocking_comment_link foreign keys

ALTER TABLE silva.stocking_comment_link ADD CONSTRAINT scl_silvc_fk FOREIGN KEY (silviculture_comment_id) REFERENCES silva.silviculture_comment(silviculture_comment_id);
ALTER TABLE silva.stocking_comment_link ADD CONSTRAINT scl_ssu_fk FOREIGN KEY (stocking_standard_unit_id) REFERENCES silva.stocking_standard_unit(stocking_standard_unit_id);


-- silva.stocking_ecology foreign keys

ALTER TABLE silva.stocking_ecology ADD CONSTRAINT se_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);
ALTER TABLE silva.stocking_ecology ADD CONSTRAINT se_ssu_fk FOREIGN KEY (stocking_standard_unit_id) REFERENCES silva.stocking_standard_unit(stocking_standard_unit_id);


-- silva.stocking_ecology_archive foreign keys

ALTER TABLE silva.stocking_ecology_archive ADD CONSTRAINT searc_ssuarc_fk FOREIGN KEY (stocking_event_history_id,stocking_standard_unit_id) REFERENCES silva.stocking_standard_unit_archive(stocking_event_history_id,stocking_standard_unit_id);


-- silva.stocking_event_history foreign keys

ALTER TABLE silva.stocking_event_history ADD CONSTRAINT seh_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);
ALTER TABLE silva.stocking_event_history ADD CONSTRAINT seh_oah_fk FOREIGN KEY (opening_amendment_id,opening_amendment_number) REFERENCES silva.opening_amendment_history(opening_id,opening_amendment_number);
ALTER TABLE silva.stocking_event_history ADD CONSTRAINT seh_raac_fk FOREIGN KEY (results_audit_action_code) REFERENCES silva.results_audit_action_code(results_audit_action_code);


-- silva.stocking_layer foreign keys

ALTER TABLE silva.stocking_layer ADD CONSTRAINT sl_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);
ALTER TABLE silva.stocking_layer ADD CONSTRAINT sl_slc_fk FOREIGN KEY (stocking_layer_code) REFERENCES silva.stocking_layer_code(stocking_layer_code);
ALTER TABLE silva.stocking_layer ADD CONSTRAINT sl_ssu_fk FOREIGN KEY (stocking_standard_unit_id) REFERENCES silva.stocking_standard_unit(stocking_standard_unit_id);
ALTER TABLE silva.stocking_layer ADD CONSTRAINT sl_tsuc_fk FOREIGN KEY (tree_size_unit_code) REFERENCES silva.tree_size_unit_code(tree_size_unit_code);


-- silva.stocking_layer_archive foreign keys

ALTER TABLE silva.stocking_layer_archive ADD CONSTRAINT slarc_slc_fk FOREIGN KEY (stocking_layer_code) REFERENCES silva.stocking_layer_code(stocking_layer_code);
ALTER TABLE silva.stocking_layer_archive ADD CONSTRAINT slarc_ssuarc_fk FOREIGN KEY (stocking_event_history_id,stocking_standard_unit_id) REFERENCES silva.stocking_standard_unit_archive(stocking_event_history_id,stocking_standard_unit_id);
ALTER TABLE silva.stocking_layer_archive ADD CONSTRAINT slarc_tsuc_fk FOREIGN KEY (tree_size_unit_code) REFERENCES silva.tree_size_unit_code(tree_size_unit_code);


-- silva.stocking_layer_species foreign keys

ALTER TABLE silva.stocking_layer_species ADD CONSTRAINT sls_sl_fk FOREIGN KEY (stocking_layer_id) REFERENCES silva.stocking_layer(stocking_layer_id);
ALTER TABLE silva.stocking_layer_species ADD CONSTRAINT sls_stsc_fk FOREIGN KEY (silv_tree_species_code) REFERENCES silva.silv_tree_species_code(silv_tree_species_code);


-- silva.stocking_layer_species_archive foreign keys

ALTER TABLE silva.stocking_layer_species_archive ADD CONSTRAINT slsarc_slarc_fk FOREIGN KEY (stocking_layer_id,stocking_event_history_id,stocking_standard_unit_id) REFERENCES silva.stocking_layer_archive(stocking_layer_id,stocking_event_history_id,stocking_standard_unit_id);
ALTER TABLE silva.stocking_layer_species_archive ADD CONSTRAINT slsarc_ssc1_fk FOREIGN KEY (silv_tree_species_code) REFERENCES silva.silv_tree_species_code(silv_tree_species_code);


-- silva.stocking_milestone foreign keys

ALTER TABLE silva.stocking_milestone ADD CONSTRAINT sm_res_fk FOREIGN KEY (results_submission_id) REFERENCES silva.results_electronic_submission(results_submission_id);
ALTER TABLE silva.stocking_milestone ADD CONSTRAINT sm_smtc_fk FOREIGN KEY (silv_milestone_type_code) REFERENCES silva.silv_milestone_type_code(silv_milestone_type_code);
ALTER TABLE silva.stocking_milestone ADD CONSTRAINT sm_ssu_fk FOREIGN KEY (stocking_standard_unit_id) REFERENCES silva.stocking_standard_unit(stocking_standard_unit_id);


-- silva.stocking_milestone_cmt_link foreign keys

ALTER TABLE silva.stocking_milestone_cmt_link ADD CONSTRAINT smcl_sc_fk FOREIGN KEY (silviculture_comment_id) REFERENCES silva.silviculture_comment(silviculture_comment_id);
ALTER TABLE silva.stocking_milestone_cmt_link ADD CONSTRAINT smcl_sm_fk FOREIGN KEY (stocking_standard_unit_id,silv_milestone_type_code) REFERENCES silva.stocking_milestone(stocking_standard_unit_id,silv_milestone_type_code);


-- silva.stocking_standard_unit foreign keys

ALTER TABLE silva.stocking_standard_unit ADD CONSTRAINT ssu_o_fk FOREIGN KEY (opening_id) REFERENCES silva.opening(opening_id);
ALTER TABLE silva.stocking_standard_unit ADD CONSTRAINT ssu_sr_fk FOREIGN KEY (standards_regime_id) REFERENCES silva.standards_regime(standards_regime_id);


-- silva.stocking_standard_unit_archive foreign keys

ALTER TABLE silva.stocking_standard_unit_archive ADD CONSTRAINT ssuarc_seh_fk FOREIGN KEY (stocking_event_history_id) REFERENCES silva.stocking_event_history(stocking_event_history_id);


-- silva.timber_mark foreign keys

ALTER TABLE silva.timber_mark ADD CONSTRAINT tm_cascd_fk FOREIGN KEY (cascade_split_code) REFERENCES silva.cascade_split_code(cascade_split_code);
ALTER TABLE silva.timber_mark ADD CONSTRAINT tm_clrc_fk FOREIGN KEY (lands_region) REFERENCES silva.crown_lands_region_code(crown_lands_region_code);
ALTER TABLE silva.timber_mark ADD CONSTRAINT tm_mercd_fk FOREIGN KEY (mark_extend_rsn_cd) REFERENCES silva.mark_extension_reason_code(mark_extension_reason_code);
ALTER TABLE silva.timber_mark ADD CONSTRAINT tm_micd_fk FOREIGN KEY (markng_instrmnt_cd) REFERENCES silva.marking_instrument_code(marking_instrument_code);
ALTER TABLE silva.timber_mark ADD CONSTRAINT tm_mmcd_fk FOREIGN KEY (marking_method_cd) REFERENCES silva.marking_method_code(marking_method_code);
ALTER TABLE silva.timber_mark ADD CONSTRAINT tm_pfu_fk FOREIGN KEY (forest_file_id) REFERENCES silva.prov_forest_use(forest_file_id);
ALTER TABLE silva.timber_mark ADD CONSTRAINT tm_qtcd_fk FOREIGN KEY (quota_type_code) REFERENCES silva.quota_type_code(quota_type_code);
ALTER TABLE silva.timber_mark ADD CONSTRAINT tm_salvtcd_fk FOREIGN KEY (salvage_type_code) REFERENCES silva.salvage_type_code(salvage_type_code);
ALTER TABLE silva.timber_mark ADD CONSTRAINT tm_tscd_fk FOREIGN KEY (mark_status_st) REFERENCES silva.tenure_file_status_code(tenure_file_status_code);
