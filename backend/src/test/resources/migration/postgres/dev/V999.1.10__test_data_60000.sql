INSERT INTO silva.results_electronic_submission (results_submission_id, submission_timestamp, submitted_by, client_number, client_locn_code, org_unit_no, user_filename, user_reference, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (129231,TIMESTAMP'2005-11-16 00:00:00','BCEID\DSCOTT',NULL,NULL,2,'original file name',NULL,'BCEID\DSCOTT',TIMESTAMP'2005-11-16 07:28:17','BCEID\DSCOTT',TIMESTAMP'2005-11-16 07:28:17',1),
	 (279646,TIMESTAMP'2006-11-27 00:00:00','BCEID\DSCOTT',NULL,NULL,2,'145-1AmendmentUpdate.xml',NULL,'BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:38:07','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:38:07',1),
	 (1106043,TIMESTAMP'2012-04-16 00:00:00','BCEID\DSCOTT','00010006','00',2,'145-1FormCFreegrowing.xml',NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:30','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:30',1);

INSERT INTO silva.opening (opening_id, geo_district_no, admin_district_no, mapsheet_grid, mapsheet_letter, mapsheet_square, mapsheet_quad, mapsheet_sub_quad, opening_number, opening_locn_name, open_category_code, licensee_opening_id, tsb_number_code, opening_status_code, org_unit_no, dist_admin_zone, max_allow_permnt_access_pct, prev_age_class_code, prev_site_index, prev_site_index_source_code, prev_height_class_code, prev_site_class_code, prev_stocking_class_code, prev_stocking_status_code, prev_tree_spp1_code, prev_tree_spp2_code, app_ent_by_userid, approve_date, amendment_ind, results_submission_id, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (60000,12,2,' 93','P','016','0','0','  73','MURRAY RIVER','FTML','418385','41B','FG',NULL,NULL,7,'7',17,'H','3',NULL,'1','MAT','PLI','SW','LKSTRAND',TIMESTAMP'1997-06-12 00:00:00','Y',279646,'MLSIS',TIMESTAMP'1999-10-15 12:31:18','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',18);

INSERT INTO silva.timber_mark (timber_mark, forest_file_id, cutting_permit_id, forest_district, geographic_distrct, cascade_split_code, quota_type_code, deciduous_ind, catastrophic_ind, crown_granted_ind, cruise_based_ind, certificate, hdbs_timber_mark, vm_timber_mark, tenure_term, bcaa_folio_number, activated_userid, amended_userid, district_admn_zone, granted_acqrd_date, lands_region, crown_granted_acq_desc, mark_status_st, mark_status_date, mark_amend_date, mark_appl_date, mark_cancel_date, mark_extend_date, mark_extend_rsn_cd, mark_extend_count, mark_issue_date, mark_expiry_date, markng_instrmnt_cd, marking_method_cd, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count, small_patch_salvage_ind, salvage_type_code) VALUES
	 ('FA9145','A13840','145',2,12,'E','A','N','N','N','N',NULL,NULL,'FA9145',24,NULL,NULL,NULL,'DEAD',NULL,'8',NULL,'HC',TIMESTAMP'2000-03-09 00:00:00',NULL,NULL,NULL,NULL,NULL,0,TIMESTAMP'1997-06-15 00:00:00',TIMESTAMP'1999-06-14 00:00:00','P','4','TECUTLER',TIMESTAMP'1996-12-06 08:20:54','BHENNEY',TIMESTAMP'2003-10-24 14:36:10',1,'N',NULL);

INSERT INTO silva.cut_block (cb_skey, hva_skey, forest_file_id, cutting_permit_id, timber_mark, cut_block_id, sp_exempt_ind, block_status_date, cut_block_description, cut_regulation_code, block_status_st, reforest_declare_type_code, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp, is_waste_assessment_required, cut_block_guid, fire_harvesting_reason_code, is_under_partition_order, reported_fire_date) VALUES
	 (88061,99592,'A13840','145','FA9145','1','Y',TIMESTAMP'1999-06-08 00:00:00',NULL,NULL,'S',NULL,1,'FTA_CONV',TIMESTAMP'2003-11-15 10:48:04','FTA 4.0 DATA CONVERSION',TIMESTAMP'2007-06-02 11:15:37','U','B9B0909F2719645AE05332B3228EA5A5',NULL,NULL,NULL);

INSERT INTO silva.cut_block_open_admin (cut_block_open_admin_id, forest_file_id, timber_mark, cut_block_id, cutting_permit_id, disturbance_gross_area, disturbance_start_date, disturbance_end_date, opening_id, opening_gross_area, planned_harvest_date, planned_gross_block_area, planned_net_block_area, opening_prime_licence_ind, cb_skey, revision_count, entry_userid, entry_timestamp, update_userid, update_timestamp) VALUES
	 (8021,'A13840','FA9145','1','145',49.3,TIMESTAMP'1998-06-01 00:00:00',TIMESTAMP'1998-08-01 00:00:00',60000,47.6,TIMESTAMP'1998-06-24 00:00:00',49.3,39,'Y',88061,4,'RESULTS_CONV',TIMESTAMP'9999-12-31 00:00:00','FTA 4.0 DATA CONVERSION',TIMESTAMP'2007-06-02 11:17:23');

INSERT INTO silva.activity_treatment_unit (activity_treatment_unit_id, activity_tu_seq_no, org_unit_no, opening_id, silviculture_project_id, project_unit_id, silv_base_code, silv_technique_code, silv_method_code, silv_objective_code_1, silv_objective_code_2, silv_objective_code_3, silv_fund_srce_code, silv_activity_measurement_code, activity_licensee_id, treatment_amount, actual_treatment_cost, act_planted_no, plan_silv_technique_code, plan_silv_method_code, plan_silv_fund_srce_code, planned_date, planned_treatment_amount, planned_treatment_cost, plan_silv_objective_code_1, plan_silv_objective_code_2, plan_silv_objective_code_3, target_prepared_spots, pruning_min_crown_pct, prune_height, stems_to_prune, min_acceptable_density, total_stems_per_ha, inter_tree_target_distance, inter_tree_variation, inter_tree_min_distance, max_trees_per_plot, max_trees_per_ha, survey_planned_num_plots, survey_actual_num_plots, survey_min_plots_per_stratum, silv_tree_species_code, atu_start_date, atu_completion_date, results_ind, unit_bid_price, fia_project_id, results_submission_id, disturbance_code, silv_system_code, silv_system_variant_code, silv_cut_phase_code, cut_block_open_admin_id, disturbance_completed_ind, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (533908,1,2,60000,NULL,NULL,'DN','HV',NULL,NULL,NULL,NULL,'IA','HA',NULL,49.3,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,TIMESTAMP'1998-06-01 00:00:00',TIMESTAMP'1998-08-01 00:00:00','Y',NULL,NULL,NULL,'L','CLEAR',NULL,'REMOV',NULL,NULL,'MLSIS',TIMESTAMP'1999-10-15 12:31:18','BMCLENNA',TIMESTAMP'1999-10-15 12:31:18',1),
	 (777297,2,2,60000,NULL,NULL,'PL','PL','CTAIN','CE',NULL,NULL,'IA','HA',NULL,39,NULL,65385,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,TIMESTAMP'2000-07-01 00:00:00','Y',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'MLSEDT',TIMESTAMP'2001-02-01 09:43:36','MLSEDT',TIMESTAMP'2001-02-01 09:43:36',1),
	 (935381,5,2,60000,NULL,NULL,'BR','CA','HELI','CGE',NULL,NULL,'IA','HA',NULL,38.5,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,TIMESTAMP'2002-08-01 00:00:00','Y',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'MLSEDT',TIMESTAMP'2003-02-20 07:28:16','MLSEDT',TIMESTAMP'2003-02-20 07:28:16',1),
	 (868501,3,2,60000,NULL,NULL,'SU','RG','PLOT','RG',NULL,NULL,'IA','HA',NULL,39,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,TIMESTAMP'2001-10-01 00:00:00','Y',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'MLSEDT',TIMESTAMP'2002-03-20 13:38:40','MLSEDT',TIMESTAMP'2002-03-20 13:38:40',1),
	 (868502,4,2,60000,NULL,NULL,'SU','RG','WALK','RG','BR','CE','IA','HA',NULL,39,NULL,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,TIMESTAMP'2001-10-01 00:00:00','Y',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'MLSEDT',TIMESTAMP'2002-03-20 13:38:41','MLSEDT',TIMESTAMP'2002-03-20 13:38:41',1),
	 (1661074,1,2,60000,NULL,NULL,'SU','RA','WALK','RA',NULL,NULL,'IA','HA',NULL,39,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,TIMESTAMP'2003-09-01 00:00:00','Y',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'IDIR\RNEAGELE',TIMESTAMP'2005-02-11 16:36:00','IDIR\RNEAGELE',TIMESTAMP'2005-02-11 16:36:00',1),
	 (1729448,6,2,60000,NULL,NULL,'SU','RA','PLOT','RA',NULL,NULL,'IA','HA',NULL,39,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,TIMESTAMP'2005-05-30 00:00:00','Y',NULL,NULL,129231,NULL,NULL,NULL,NULL,NULL,NULL,'BCEID\DSCOTT',TIMESTAMP'2005-11-16 07:28:17','BCEID\DSCOTT',TIMESTAMP'2005-11-16 07:28:17',2);

INSERT INTO silva.stocking_standard_unit (stocking_standard_unit_id, opening_id, standards_unit_id, standards_regime_id, net_area, max_allow_soil_disturbance_pct, variance_ind, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (101419,60000,'1',NULL,13.3,10,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-15 19:46:02','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:58:18',7),
	 (101420,60000,'2',NULL,10.1,10,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-15 19:46:02','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:38:07',6),
	 (101421,60000,'3',NULL,10.5,5,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-15 19:46:02','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:38:07',7),
	 (101422,60000,'4',NULL,1.9,10,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-15 19:46:02','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:38:07',7);

INSERT INTO silva.stocking_milestone (stocking_standard_unit_id, silv_milestone_type_code, results_submission_id, declared_date, declared_userid, declare_ind, declaration_submitted_date, early_offset_years, late_offset_years, due_early_date, due_late_date, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count, extent_feasible_declared_ind) VALUES
	 (101419,'FG',1106043,TIMESTAMP'2011-05-25 00:00:00','BCEID\DSCOTT','Y',TIMESTAMP'2012-04-16 08:37:33',12,15,TIMESTAMP'2010-06-01 00:00:00',TIMESTAMP'2013-06-01 00:00:00','RESULTS_CONV',TIMESTAMP'2003-11-15 19:43:53','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',6,'N'),
	 (101419,'PH',NULL,NULL,NULL,'N',NULL,NULL,NULL,NULL,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-15 19:43:53','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',6,'N'),
	 (101419,'RG',NULL,TIMESTAMP'2001-10-11 00:00:00','LKSTRAND','Y',TIMESTAMP'2003-11-15 19:43:53',NULL,7,NULL,TIMESTAMP'2005-06-01 00:00:00','RESULTS_CONV',TIMESTAMP'2003-11-15 19:43:53','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',5,'N'),
	 (101420,'FG',1106043,TIMESTAMP'2011-05-25 00:00:00','BCEID\DSCOTT','Y',TIMESTAMP'2012-04-16 08:37:33',9,15,TIMESTAMP'2007-06-01 00:00:00',TIMESTAMP'2013-06-01 00:00:00','RESULTS_CONV',TIMESTAMP'2003-11-15 19:43:53','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',6,'N'),
	 (101420,'PH',NULL,NULL,NULL,'N',NULL,NULL,NULL,NULL,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-15 19:43:53','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',6,'N'),
	 (101420,'RG',NULL,TIMESTAMP'2001-10-11 00:00:00','LKSTRAND','Y',TIMESTAMP'2003-11-15 19:43:53',NULL,4,NULL,TIMESTAMP'2002-06-01 00:00:00','RESULTS_CONV',TIMESTAMP'2003-11-15 19:43:53','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',5,'N'),
	 (101421,'FG',1106043,TIMESTAMP'2011-05-25 00:00:00','BCEID\DSCOTT','Y',TIMESTAMP'2012-04-16 08:37:33',12,15,TIMESTAMP'2010-06-01 00:00:00',TIMESTAMP'2013-06-01 00:00:00','RESULTS_CONV',TIMESTAMP'2003-11-15 19:43:53','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',6,'N'),
	 (101421,'PH',NULL,NULL,NULL,'N',NULL,NULL,NULL,NULL,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-15 19:43:53','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',6,'N'),
	 (101421,'RG',NULL,TIMESTAMP'2001-10-11 00:00:00','LKSTRAND','Y',TIMESTAMP'2003-11-15 19:43:53',NULL,7,NULL,TIMESTAMP'2005-06-01 00:00:00','RESULTS_CONV',TIMESTAMP'2003-11-15 19:43:53','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',5,'N'),
	 (101422,'FG',1106043,TIMESTAMP'2011-05-25 00:00:00','BCEID\DSCOTT','Y',TIMESTAMP'2012-04-16 08:37:33',9,15,TIMESTAMP'2007-06-01 00:00:00',TIMESTAMP'2013-06-01 00:00:00','RESULTS_CONV',TIMESTAMP'2003-11-15 19:43:53','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',6,'N'),
	 (101422,'PH',NULL,NULL,NULL,'N',NULL,NULL,NULL,NULL,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-15 19:43:53','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',6,'N'),
	 (101422,'RG',NULL,TIMESTAMP'2001-10-11 00:00:00','LKSTRAND','Y',TIMESTAMP'2003-11-15 19:43:53',NULL,4,NULL,TIMESTAMP'2002-06-01 00:00:00','RESULTS_CONV',TIMESTAMP'2003-11-15 19:43:53','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:33',5,'N');

INSERT INTO silva.stocking_ecology (stocking_ecology_id, opening_id, stocking_standard_unit_id, bec_region_code, bgc_zone_code, bgc_subzone_code, bgc_variant, bgc_phase, bec_site_series, bec_site_type, bec_seral, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (103858,60000,101419,NULL,'BWBS','wk','1',NULL,'03',NULL,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-15 18:54:41','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:38:07',2),
	 (103859,60000,101420,NULL,'BWBS','wk','1',NULL,'01',NULL,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-15 18:54:41','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:38:07',2),
	 (103860,60000,101421,NULL,'BWBS','wk','1',NULL,'05',NULL,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-15 18:54:41','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:38:07',2),
	 (103861,60000,101422,NULL,'BWBS','wk','1',NULL,'08',NULL,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-15 18:54:41','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:38:07',2);

INSERT INTO silva.stocking_layer (stocking_layer_id, stocking_standard_unit_id, opening_id, stocking_layer_code, target_stocking, residual_basal_area, min_horizontal_distance, min_pref_stocking_standard, min_post_spacing, min_stocking_standard, max_post_spacing, max_conifer, hght_relative_to_comp, tree_size_unit_code, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (1086386,101419,60000,'I',1200,NULL,2,600,NULL,700,NULL,NULL,150,'%','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:51:55','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:51:55',1),
	 (1086388,101420,60000,'I',1200,NULL,2,600,NULL,700,NULL,NULL,150,'%','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:53:17','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:53:17',1),
	 (1086389,101421,60000,'I',1200,NULL,2,600,NULL,700,NULL,NULL,150,'%','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:54:30','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:54:30',1),
	 (1086390,101422,60000,'I',400,NULL,1,200,NULL,200,NULL,NULL,150,'%','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:55:45','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:55:45',1);

INSERT INTO silva.stocking_layer_species (stocking_layer_id, silv_tree_species_code, species_order, preferred_ind, min_height, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (1086386,'PLI',1,'Y',2,'BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:52:23','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:52:23',1),
	 (1086386,'SW',2,'Y',1,'BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:52:35','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:52:35',1),
	 (1086388,'PLI',1,'Y',2,'BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:53:31','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:53:31',1),
	 (1086388,'SW',2,'Y',1,'BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:53:47','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:53:47',1),
	 (1086389,'PLI',2,'Y',2,'BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:55:18','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:55:18',1),
	 (1086389,'SW',1,'Y',1,'BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:55:01','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:55:01',1),
	 (1086390,'PLI',2,'Y',1.4,'BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:56:13','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:56:13',1),
	 (1086390,'SW',1,'Y',0.8,'BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:55:58','BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:55:58',1);

INSERT INTO silva.forest_cover (forest_cover_id, opening_id, stocking_standard_unit_id, silv_polygon_no, silv_polygon_area, silv_polygon_net_area, stocking_class_code, stocking_status_code, stocking_type_code, reference_year, reentry_year, site_class_code, site_index, site_index_source_code, silv_reserve_code, silv_reserve_objective_code, tree_species_code, tree_cover_pattern_code, results_submission_id, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (2638619,60000,101419,'A1',13.3,13.3,NULL,'IMM','ART',2011,NULL,NULL,12,'E',NULL,NULL,'PLI',NULL,1106043,'BCEID\DSCOTT',TIMESTAMP'2002-04-26 00:00:00','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',5),
	 (2638620,60000,101420,'B2',10.1,10.1,NULL,'IMM','NAT',2011,NULL,NULL,15,'E',NULL,NULL,'PLI',NULL,1106043,'BCEID\DSCOTT',TIMESTAMP'2002-04-26 00:00:00','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',5),
	 (2638621,60000,101421,'C3',10.5,10.5,NULL,'IMM','ART',2011,NULL,NULL,15,'E',NULL,NULL,'SX',NULL,1106043,'BCEID\DSCOTT',TIMESTAMP'2002-04-26 00:00:00','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',6),
	 (2638622,60000,101422,'D4',1.9,1.9,NULL,'IMM','ART',2011,NULL,NULL,6,'E',NULL,NULL,'SX',NULL,1106043,'BCEID\DSCOTT',TIMESTAMP'2002-04-26 00:00:00','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',7),
	 (2638623,60000,NULL,'E',9.3,9.3,NULL,'MAT','NAT',2001,NULL,NULL,15,'H','G','WTR','PLI',NULL,1106043,'BCEID\DSCOTT',TIMESTAMP'2002-04-26 00:00:00','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',3),
	 (2638624,60000,NULL,'L',2.6,2.6,NULL,'NP','UNN',2006,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1106043,'BCEID\DSCOTT',TIMESTAMP'2002-04-26 00:00:00','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1);

INSERT INTO silva.opening_amendment_history (opening_id, opening_amendment_number, amendment_userid, amendment_date, submitted_by_userid, submitted_date, app_ent_by_userid, approve_date, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (60000,1,'RNEAGELE',TIMESTAMP'1999-09-01 00:00:00','D.L. Way, D.M. Designate',TIMESTAMP'2003-11-15 18:27:52','RNEAGELE',TIMESTAMP'2000-01-21 00:00:00','RESULTS_CONV',TIMESTAMP'2003-11-15 18:27:52','RESULTS_CONV',TIMESTAMP'2003-11-15 18:27:52',1),
	 (60000,2,'RNEAGELE',TIMESTAMP'2003-09-18 00:00:00','D.L.Way',TIMESTAMP'2003-11-15 18:27:52','RNEAGELE',TIMESTAMP'2003-11-10 00:00:00','RESULTS_CONV',TIMESTAMP'2003-11-15 18:27:52','RESULTS_CONV',TIMESTAMP'2003-11-15 18:27:52',1);

INSERT INTO silva.stocking_event_history (stocking_event_history_id, opening_id, opening_amendment_id, opening_amendment_number, results_audit_action_code, submitted_userid, results_submission_id, amend_event_timestamp, entry_userid, entry_timestamp, revision_count) VALUES
	 (1344961,60000,60000,2,'O','Results data conversion',NULL,TIMESTAMP'2008-03-11 14:33:51','Results data conversion',TIMESTAMP'2008-03-11 14:33:51',1);

INSERT INTO silva.results_audit_event (results_audit_event_id, opening_id, standards_regime_id, silviculture_project_id, results_audit_action_code, action_date, description, user_id, email_sent_ind, xml_submission_id, opening_amendment_number, entry_userid, entry_timestamp) VALUES
	 (101550,60000,NULL,NULL,'UPD',TIMESTAMP'2005-02-11 16:36:00','Opening/related details were modified.',NULL,'N',NULL,NULL,'IDIR\RNEAGELE',TIMESTAMP'2005-02-11 16:36:00'),
	 (335014,60000,NULL,NULL,'ES',TIMESTAMP'2005-11-16 07:28:17','Inserting Activity: Activity Id = 1729448, Activity Base = ''SU''',NULL,'Y',129231,NULL,'BCEID\DSCOTT',TIMESTAMP'2005-11-16 07:28:17'),
	 (672650,60000,NULL,NULL,'COR',TIMESTAMP'2006-11-27 13:38:07','Stocking information modified.',NULL,'Y',279646,NULL,'BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:38:07'),
	 (672649,60000,NULL,NULL,'ES',TIMESTAMP'2006-11-27 13:38:07','Opening ID 60000 updated',NULL,'Y',279646,NULL,'BCEID\DSCOTT',TIMESTAMP'2006-11-27 13:38:07'),
	 (2483912,60000,NULL,NULL,'ES',TIMESTAMP'2012-04-16 08:37:31','Forest Cover Polygon No. A1 (Id=2638619)',NULL,'Y',1106043,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31'),
	 (2483913,60000,NULL,NULL,'ES',TIMESTAMP'2012-04-16 08:37:31','Forest Cover Polygon No. B2 (Id=2638620)',NULL,'Y',1106043,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31'),
	 (2483914,60000,NULL,NULL,'ES',TIMESTAMP'2012-04-16 08:37:31','Forest Cover Polygon No. C3 (Id=2638621)',NULL,'Y',1106043,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31'),
	 (2483915,60000,NULL,NULL,'ES',TIMESTAMP'2012-04-16 08:37:31','Forest Cover Polygon No. D4 (Id=2638622)',NULL,'Y',1106043,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31'),
	 (2483916,60000,NULL,NULL,'ES',TIMESTAMP'2012-04-16 08:37:31','Forest Cover Polygon No. E (Id=2638623)',NULL,'Y',1106043,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31'),
	 (2483917,60000,NULL,NULL,'ES',TIMESTAMP'2012-04-16 08:37:31','Forest Cover Polygon No. L (Id=2638624)',NULL,'Y',1106043,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31'),
	 (2483918,60000,NULL,NULL,'MIL',TIMESTAMP'2012-04-16 08:37:32','Milestone was modified',NULL,'Y',1106043,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:32');

INSERT INTO silva.planting_rslt (activity_treatment_unit_id, results_ind, plant_rslt_seq_no, silv_tree_species_code, number_planted, planted_no_beyond_xfer_limit, bid_price_per_tree, seedlot_number, veg_lot_id, request_skey, item_id, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count, climate_based_seed_xfer_ind) VALUES
	 (777297,'Y',1,'PLI',36430,NULL,NULL,'39517',NULL,NULL,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-16 08:55:17','MLSEDT',TIMESTAMP'2001-02-01 09:43:37',1,'N'),
	 (777297,'Y',2,'SX',25070,NULL,NULL,'40225',NULL,NULL,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-16 08:55:17','MLSEDT',TIMESTAMP'2001-02-01 09:43:38',1,'N'),
	 (777297,'Y',3,'SX',3885,NULL,NULL,'04723',NULL,NULL,NULL,'RESULTS_CONV',TIMESTAMP'2003-11-16 08:55:17','MLSEDT',TIMESTAMP'2001-02-01 09:43:38',1,'N');

INSERT INTO silva.forest_cover_layer (forest_cover_id, forest_cover_layer_id, forest_cover_layer_code, stocking_type_code, stocking_status_code, total_stems_per_ha, total_well_spaced_stems_per_ha, well_spaced_stems_per_ha, free_growing_stems_per_ha, crown_closure_pct, basal_area, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (2638619,3095550,'S',NULL,NULL,NULL,1757,1171,986,NULL,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638619,3095551,'I',NULL,NULL,4914,NULL,NULL,NULL,4,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638620,3095552,'S',NULL,NULL,NULL,1164,1127,1127,NULL,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638620,3095553,'I',NULL,NULL,3327,NULL,NULL,NULL,7,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638621,3095554,'S',NULL,NULL,NULL,1164,1164,1073,NULL,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638621,3095555,'I',NULL,NULL,3164,NULL,NULL,NULL,4,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638622,3095556,'S',NULL,NULL,NULL,400,400,320,NULL,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638622,3095557,'I',NULL,NULL,4120,NULL,NULL,NULL,6,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638623,3095558,'I',NULL,NULL,1000,NULL,NULL,NULL,75,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1);

INSERT INTO silva.forest_cover_layer_species (forest_cover_id, forest_cover_layer_id, species_order, tree_species_code, tree_species_pct, avg_age, avg_height, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (2638619,3095550,1,'PLI',85,11,3,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638619,3095550,2,'SX',15,9,1.2,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638619,3095551,1,'PLI',60,11,3.1,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638619,3095551,2,'SX',40,9,1.3,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638620,3095552,1,'PLI',59,11,3.1,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638620,3095552,2,'SX',41,10,2.4,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638620,3095553,1,'PLI',60,11,3.6,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638620,3095553,2,'SX',40,10,2.4,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638621,3095554,1,'SX',73,12,1.9,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638621,3095554,2,'PLI',27,11,2.2,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638621,3095555,1,'SX',50,12,2.3,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638621,3095555,2,'PLI',40,11,2.6,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638621,3095555,3,'ACB',10,NULL,NULL,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638622,3095556,1,'SX',50,12,2.5,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638622,3095556,2,'PLI',50,11,1.9,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638622,3095557,1,'SX',40,12,2,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638622,3095557,2,'PLI',40,11,2.3,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638622,3095557,3,'AT',10,7,6,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638622,3095557,4,'ACB',10,7,4.6,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1),
	 (2638623,3095558,1,'PLI',80,90,28,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1);
INSERT INTO silva.forest_cover_layer_species (forest_cover_id, forest_cover_layer_id, species_order, tree_species_code, tree_species_pct, avg_age, avg_height, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (2638623,3095558,2,'SW',20,90,28,'BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1);

INSERT INTO silva.forhealth_rslt (forhealth_rslt_id, opening_id, forest_cover_id, forest_cover_layer_id, silv_damage_agent_code, incidence_pct, incidence_area, observation_date, entry_userid, entry_timestamp, update_userid, update_timestamp, revision_count) VALUES
	 (1668933,60000,2638620,3095553,'IWS',1,NULL,TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31','BCEID\DSCOTT',TIMESTAMP'2012-04-16 08:37:31',1);
