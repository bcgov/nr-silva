INSERT INTO RESULTS_ELECTRONIC_SUBMISSION (RESULTS_SUBMISSION_ID,SUBMISSION_TIMESTAMP,SUBMITTED_BY,CLIENT_NUMBER,CLIENT_LOCN_CODE,ORG_UNIT_NO,USER_FILENAME,USER_REFERENCE,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP,REVISION_COUNT) VALUES
	 (1228561,TIMESTAMP'2013-05-31 00:00:00','BCEID\OTWIST','00010005','00',2,'DISTURBANCE_2013_05_31-11_32_04.xml',NULL,'BCEID\OTWIST',TIMESTAMP'2013-05-31 11:37:46','BCEID\OTWIST',TIMESTAMP'2013-05-31 11:37:46',1),
	 (1717504,TIMESTAMP'2017-11-15 15:57:07','BCEID\OTWIST','00010005','00',2,'FOREST COVER_2017_11_15-15_56_07.xml',NULL,'BCEID\OTWIST',TIMESTAMP'2017-11-15 15:57:42','BCEID\OTWIST',TIMESTAMP'2017-11-15 15:57:42',1),
	 (1931429,TIMESTAMP'2020-01-08 14:39:02','BCEID\MBRANDOS','00010005','00',2,'ACTIVITY_2020_01_02-18_29_01.xml',NULL,'BCEID\MBRANDOS',TIMESTAMP'2020-01-08 14:39:11','BCEID\MBRANDOS',TIMESTAMP'2020-01-08 14:39:11',1),
	 (1228929,TIMESTAMP'2013-05-31 00:00:00','BCEID\JWALKER','00010005','00',2,'MILESTONE_2013_05_31-14_27_06.xml',NULL,'BCEID\JWALKER',TIMESTAMP'2013-05-31 14:41:00','BCEID\JWALKER',TIMESTAMP'2013-05-31 14:41:00',1),
	 (1684259,TIMESTAMP'2017-07-17 13:37:19','BCEID\OTWIST','00010005','00',2,'ACTIVITY_2017_07_17-13_19_25.xml',NULL,'BCEID\OTWIST',TIMESTAMP'2017-07-17 13:38:13','BCEID\OTWIST',TIMESTAMP'2017-07-17 13:38:13',1),
	 (1246451,TIMESTAMP'2013-08-27 00:00:00','BCEID\OTWIST','00010005','00',2,'OPENING_2013_08_27-08_38_16.xml',NULL,'BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:14','BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:14',1),
	 (2102053,TIMESTAMP'2021-12-07 15:30:16','BCEID\ALOBSTER','00010005','00',2,'ACTIVITY_2021_12_07-23_28_58.xml',NULL,'BCEID\ALOBSTER',TIMESTAMP'2021-12-07 15:30:39','BCEID\ALOBSTER',TIMESTAMP'2021-12-07 15:30:39',1),
	 (1717697,TIMESTAMP'2017-11-16 11:35:18','BCEID\MBRANDOS','00010005','00',2,'EEE_883 Regen Met MILESTONE_2017_11_15-08_48_18.xm',NULL,'BCEID\MBRANDOS',TIMESTAMP'2017-11-16 11:35:43','BCEID\MBRANDOS',TIMESTAMP'2017-11-16 11:35:43',1);

INSERT INTO OPENING (OPENING_ID,GEO_DISTRICT_NO,ADMIN_DISTRICT_NO,MAPSHEET_GRID,MAPSHEET_LETTER,MAPSHEET_SQUARE,MAPSHEET_QUAD,MAPSHEET_SUB_QUAD,OPENING_NUMBER,OPENING_LOCN_NAME,OPEN_CATEGORY_CODE,LICENSEE_OPENING_ID,TSB_NUMBER_CODE,OPENING_STATUS_CODE,ORG_UNIT_NO,DIST_ADMIN_ZONE,MAX_ALLOW_PERMNT_ACCESS_PCT,PREV_AGE_CLASS_CODE,PREV_SITE_INDEX,PREV_SITE_INDEX_SOURCE_CODE,PREV_HEIGHT_CLASS_CODE,PREV_SITE_CLASS_CODE,PREV_STOCKING_CLASS_CODE,PREV_STOCKING_STATUS_CODE,PREV_TREE_SPP1_CODE,PREV_TREE_SPP2_CODE,APP_ENT_BY_USERID,APPROVE_DATE,AMENDMENT_IND,RESULTS_SUBMISSION_ID,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP,REVISION_COUNT) VALUES
(1541296,2,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'FTCF','K1A_EEE_883',NULL,'APP',2,NULL,10,'8',19,'E','3',NULL,'3','MAT','PLI',NULL,NULL,TIMESTAMP'2013-05-31 11:26:50','Y',1246451,'BCEID\PBUBBLEGUM',TIMESTAMP'2013-05-31 11:26:50','BCEID\PBUBBLEGUM',TIMESTAMP'2021-12-07 15:30:44',13);

INSERT INTO TIMBER_MARK (TIMBER_MARK,FOREST_FILE_ID,CUTTING_PERMIT_ID,FOREST_DISTRICT,GEOGRAPHIC_DISTRCT,CASCADE_SPLIT_CODE,QUOTA_TYPE_CODE,DECIDUOUS_IND,CATASTROPHIC_IND,CROWN_GRANTED_IND,CRUISE_BASED_IND,CERTIFICATE,HDBS_TIMBER_MARK,VM_TIMBER_MARK,TENURE_TERM,BCAA_FOLIO_NUMBER,ACTIVATED_USERID,AMENDED_USERID,DISTRICT_ADMN_ZONE,GRANTED_ACQRD_DATE,LANDS_REGION,CROWN_GRANTED_ACQ_DESC,MARK_STATUS_ST,MARK_STATUS_DATE,MARK_AMEND_DATE,MARK_APPL_DATE,MARK_CANCEL_DATE,MARK_EXTEND_DATE,MARK_EXTEND_RSN_CD,MARK_EXTEND_COUNT,MARK_ISSUE_DATE,MARK_EXPIRY_DATE,MARKNG_INSTRMNT_CD,MARKING_METHOD_CD,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP,REVISION_COUNT,SMALL_PATCH_SALVAGE_IND,SALVAGE_TYPE_CODE) VALUES
	 ('K1AEEE','K1A','EEE',2,2,'E','A','N','N','N','N',NULL,NULL,NULL,48,NULL,NULL,NULL,'A',NULL,NULL,NULL,'HC',TIMESTAMP'2019-06-13 00:00:00',NULL,NULL,NULL,NULL,NULL,0,TIMESTAMP'2012-11-02 00:00:00',TIMESTAMP'2016-11-01 00:00:00','H','1','BCEID\OTWIST',TIMESTAMP'2012-10-25 10:55:10','IDIR\MUGIWARA',TIMESTAMP'2021-08-31 12:20:37',3,'N',NULL);

INSERT INTO CUT_BLOCK (CB_SKEY,HVA_SKEY,FOREST_FILE_ID,CUTTING_PERMIT_ID,TIMBER_MARK,CUT_BLOCK_ID,SP_EXEMPT_IND,BLOCK_STATUS_DATE,CUT_BLOCK_DESCRIPTION,CUT_REGULATION_CODE,BLOCK_STATUS_ST,REFOREST_DECLARE_TYPE_CODE,REVISION_COUNT,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP,IS_WASTE_ASSESSMENT_REQUIRED,CUT_BLOCK_GUID,FIRE_HARVESTING_REASON_CODE,IS_UNDER_PARTITION_ORDER,REPORTED_FIRE_DATE) VALUES
	 (553088,562321,'K1A','EEE','K1AEEE','883','N',TIMESTAMP'2013-05-31 14:41:09','CP EEE Rollover from CCC',NULL,'S',NULL,3,'BCEID\OTWIST',TIMESTAMP'2012-10-25 10:55:26','BCEID\JWALKER',TIMESTAMP'2013-05-31 14:41:09','U','B9B090A22064645AE05332B3228EA5A5',NULL,NULL,NULL);

INSERT INTO CUT_BLOCK_OPEN_ADMIN (CUT_BLOCK_OPEN_ADMIN_ID,FOREST_FILE_ID,TIMBER_MARK,CUT_BLOCK_ID,CUTTING_PERMIT_ID,DISTURBANCE_GROSS_AREA,DISTURBANCE_START_DATE,DISTURBANCE_END_DATE,OPENING_ID,OPENING_GROSS_AREA,PLANNED_HARVEST_DATE,PLANNED_GROSS_BLOCK_AREA,PLANNED_NET_BLOCK_AREA,OPENING_PRIME_LICENCE_IND,CB_SKEY,REVISION_COUNT,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP) VALUES
	 (380981,'K1A','K1AEEE','883','EEE',6,TIMESTAMP'2012-12-12 00:00:00',TIMESTAMP'2013-03-27 00:00:00',1541296,6,TIMESTAMP'2012-11-15 00:00:00',6.045,4.9,'Y',553088,5,'BCEID\OTWIST',TIMESTAMP'2012-10-25 10:55:26','BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:18');

INSERT INTO ACTIVITY_TREATMENT_UNIT (ACTIVITY_TREATMENT_UNIT_ID,ACTIVITY_TU_SEQ_NO,ORG_UNIT_NO,OPENING_ID,SILVICULTURE_PROJECT_ID,PROJECT_UNIT_ID,SILV_BASE_CODE,SILV_TECHNIQUE_CODE,SILV_METHOD_CODE,SILV_OBJECTIVE_CODE_1,SILV_OBJECTIVE_CODE_2,SILV_OBJECTIVE_CODE_3,SILV_FUND_SRCE_CODE,SILV_ACTIVITY_MEASUREMENT_CODE,ACTIVITY_LICENSEE_ID,TREATMENT_AMOUNT,ACTUAL_TREATMENT_COST,ACT_PLANTED_NO,PLAN_SILV_TECHNIQUE_CODE,PLAN_SILV_METHOD_CODE,PLAN_SILV_FUND_SRCE_CODE,PLANNED_DATE,PLANNED_TREATMENT_AMOUNT,PLANNED_TREATMENT_COST,PLAN_SILV_OBJECTIVE_CODE_1,PLAN_SILV_OBJECTIVE_CODE_2,PLAN_SILV_OBJECTIVE_CODE_3,TARGET_PREPARED_SPOTS,PRUNING_MIN_CROWN_PCT,PRUNE_HEIGHT,STEMS_TO_PRUNE,MIN_ACCEPTABLE_DENSITY,TOTAL_STEMS_PER_HA,INTER_TREE_TARGET_DISTANCE,INTER_TREE_VARIATION,INTER_TREE_MIN_DISTANCE,MAX_TREES_PER_PLOT,MAX_TREES_PER_HA,SURVEY_PLANNED_NUM_PLOTS,SURVEY_ACTUAL_NUM_PLOTS,SURVEY_MIN_PLOTS_PER_STRATUM,SILV_TREE_SPECIES_CODE,ATU_START_DATE,ATU_COMPLETION_DATE,RESULTS_IND,UNIT_BID_PRICE,FIA_PROJECT_ID,RESULTS_SUBMISSION_ID,DISTURBANCE_CODE,SILV_SYSTEM_CODE,SILV_SYSTEM_VARIANT_CODE,SILV_CUT_PHASE_CODE,CUT_BLOCK_OPEN_ADMIN_ID,DISTURBANCE_COMPLETED_IND,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP,REVISION_COUNT) VALUES
	 (3679696,3,2,1541296,NULL,NULL,'SU','RG','PLOT','CE','RA','ST','IA','HA','64002901',5.2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,TIMESTAMP'2019-06-11 00:00:00','Y',NULL,NULL,1931429,NULL,NULL,NULL,NULL,NULL,NULL,'BCEID\MBRANDOS',TIMESTAMP'2020-01-08 14:39:11','BCEID\MBRANDOS',TIMESTAMP'2020-01-08 14:39:11',1),
	 (3858309,4,2,1541296,NULL,NULL,'SU','RE','PLOT','CE','RA','ST','IA','HA','64002899',5.2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,TIMESTAMP'2021-08-17 00:00:00','Y',NULL,NULL,2102053,NULL,NULL,NULL,NULL,NULL,NULL,'BCEID\ALOBSTER',TIMESTAMP'2021-12-07 15:30:44','BCEID\ALOBSTER',TIMESTAMP'2021-12-07 15:30:44',1),
	 (3453899,2,2,1541296,NULL,NULL,'PL','PL','CTAIN','CE',NULL,NULL,'IA','HA','35253 04889',5.2,NULL,9030,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,TIMESTAMP'2017-06-24 00:00:00','Y',NULL,NULL,1684259,NULL,NULL,NULL,NULL,NULL,NULL,'BCEID\OTWIST',TIMESTAMP'2017-07-17 13:38:17','BCEID\OTWIST',TIMESTAMP'2017-07-17 13:38:17',3),
	 (2963894,1,2,1541296,NULL,NULL,'DN',NULL,NULL,NULL,NULL,NULL,NULL,'HA','01752_TWIST--REMOV',6,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,TIMESTAMP'2012-12-12 00:00:00',TIMESTAMP'2013-03-27 00:00:00','Y',NULL,NULL,1228561,'L','TWIST',NULL,'REMOV',380981,'Y','BCEID\OTWIST',TIMESTAMP'2013-05-31 11:37:47','BCEID\OTWIST',TIMESTAMP'2013-05-31 11:37:47',1);

INSERT INTO STANDARDS_REGIME (STANDARDS_REGIME_ID,STANDARDS_REGIME_NAME,STANDARDS_REGIME_STATUS_CODE,SILV_STATUTE_CODE,STANDARDS_OBJECTIVE,GEOGRAPHIC_DESCRIPTION,MOF_DEFAULT_STANDARD_IND,REGEN_DELAY_OFFSET_YRS,REGEN_OBLIGATION_IND,NO_REGEN_EARLY_OFFSET_YRS,NO_REGEN_LATE_OFFSET_YRS,FREE_GROWING_EARLY_OFFSET_YRS,FREE_GROWING_LATE_OFFSET_YRS,APPROVED_BY_USERID,APPROVED_DATE,SUBMITTED_BY_USERID,SUBMITTED_DATE,EFFECTIVE_DATE,EXPIRY_DATE,ADDITIONAL_STANDARDS,REJECT_NOTE,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP,REVISION_COUNT) VALUES
	 (1010949,NULL,'APP',NULL,NULL,NULL,'N',4,'Y',NULL,NULL,9,15,NULL,NULL,NULL,NULL,TIMESTAMP'2007-07-13 00:00:00',NULL,NULL,NULL,'IDIR\DOFLAMINGO',TIMESTAMP'2007-07-17 11:11:27','IDIR\DOFLAMINGO',TIMESTAMP'2007-07-17 11:12:45',5);

INSERT INTO STOCKING_STANDARD_UNIT (STOCKING_STANDARD_UNIT_ID,OPENING_ID,STANDARDS_UNIT_ID,STANDARDS_REGIME_ID,NET_AREA,MAX_ALLOW_SOIL_DISTURBANCE_PCT,VARIANCE_IND,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP,REVISION_COUNT) VALUES
	 (1894782,1541296,'1',1010949,5.2,5,'Y','BCEID\OTWIST',TIMESTAMP'2013-05-31 11:26:50','BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:18',2);

INSERT INTO STOCKING_MILESTONE (STOCKING_STANDARD_UNIT_ID,SILV_MILESTONE_TYPE_CODE,RESULTS_SUBMISSION_ID,DECLARED_DATE,DECLARED_USERID,DECLARE_IND,DECLARATION_SUBMITTED_DATE,EARLY_OFFSET_YEARS,LATE_OFFSET_YEARS,DUE_EARLY_DATE,DUE_LATE_DATE,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP,REVISION_COUNT,EXTENT_FEASIBLE_DECLARED_IND) VALUES
	 (1894782,'FG',NULL,NULL,NULL,NULL,NULL,9,15,TIMESTAMP'2021-12-12 00:00:00',TIMESTAMP'2027-12-12 00:00:00','BCEID\OTWIST',TIMESTAMP'2013-05-31 11:26:50','BCEID\ALOBSTER',TIMESTAMP'2021-12-07 15:30:44',18,'N'),
	 (1894782,'PH',1228929,TIMESTAMP'2013-03-27 00:00:00','BCEID\JWALKER','Y',TIMESTAMP'2013-05-31 14:41:09',NULL,NULL,NULL,NULL,'BCEID\OTWIST',TIMESTAMP'2013-05-31 11:26:50','BCEID\ALOBSTER',TIMESTAMP'2021-12-07 15:30:44',19,'N'),
	 (1894782,'RG',1717697,TIMESTAMP'2017-06-24 00:00:00','BCEID\MBRANDOS','Y',TIMESTAMP'2017-11-16 11:35:43',NULL,4,NULL,TIMESTAMP'2016-12-12 00:00:00','BCEID\OTWIST',TIMESTAMP'2013-05-31 11:26:50','BCEID\ALOBSTER',TIMESTAMP'2021-12-07 15:30:44',19,'N');

INSERT INTO STOCKING_ECOLOGY (STOCKING_ECOLOGY_ID,OPENING_ID,STOCKING_STANDARD_UNIT_ID,BEC_REGION_CODE,BGC_ZONE_CODE,BGC_SUBZONE_CODE,BGC_VARIANT,BGC_PHASE,BEC_SITE_SERIES,BEC_SITE_TYPE,BEC_SERAL,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP,REVISION_COUNT) VALUES
	 (1895897,1541296,1894782,NULL,'SBS','mc','2',NULL,'05',NULL,NULL,'BCEID\OTWIST',TIMESTAMP'2013-05-31 11:26:50','BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:18',2);

INSERT INTO FOREST_STEWARDSHIP_PLAN (FSP_ID,FSP_AMENDMENT_NUMBER,FSP_STATUS_CODE,FSP_AMENDMENT_CODE,LICENSEE_AMENDMENT_NAME,PLAN_NAME,PLAN_TERM_YEARS,PLAN_TERM_MONTHS,PLAN_START_DATE,PLAN_END_DATE,PLAN_SUBMISSION_DATE,PLAN_DECISION_DATE,PLAN_DECISION_USERID,TRANSITION_IND,IDENTIFIED_AREAS_UPDATE_IND,FDU_UPDATE_IND,STOCKING_STANDARD_UPDATE_IND,FRPA197_ELECTION_IND,CONTACT_NAME,TELEPHONE_NUMBER,EMAIL_ADDRESS,AMENDMENT_APPROVAL_DATE,AMENDMENT_AUTHORITY,AMENDMENT_APPROVAL_REQUIRD_IND,AMENDMENT_SUBMISSION_DATE,REVISION_COUNT,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP) VALUES
	 (105,0,'APP','ORG',NULL,'BL Comfor 2007',NULL,NULL,TIMESTAMP'2007-07-13 00:00:00',TIMESTAMP'2012-07-12 00:00:00',TIMESTAMP'2007-03-14 00:00:00',TIMESTAMP'2007-07-13 00:00:00','IDIR\JDPICKFO','N','N','N','N','N','Allister Schroff','2506927724','Not Available',TIMESTAMP'2007-07-13 00:00:00','IDIR\DOFLAMINGO','Y',TIMESTAMP'2007-03-14 00:00:00',11,'IDIR\DOFLAMINGO',TIMESTAMP'2007-07-17 09:02:49','IDIR\JDPICKFO',TIMESTAMP'2009-02-06 10:27:06'),
	 (105,1,'REJ','AMD','Bl Comfor amendment #1','BL Comfor 2007',NULL,NULL,NULL,TIMESTAMP'2012-07-12 00:00:00',TIMESTAMP'2007-03-14 00:00:00',TIMESTAMP'2009-08-04 00:00:00',NULL,'N','N','N','N','N','Dawn Stronstad','2506927724','Not Available',NULL,NULL,'Y',TIMESTAMP'2009-08-24 00:00:00',13,'IDIR\DOFLAMINGO',TIMESTAMP'2009-04-20 10:46:45','IDIR\DOFLAMINGO',TIMESTAMP'2009-08-24 10:50:08'),
	 (105,2,'APP','AMD',NULL,'BL Comfor 2007',NULL,NULL,TIMESTAMP'2010-01-21 00:00:00',TIMESTAMP'2014-07-12 00:00:00',TIMESTAMP'2007-03-14 00:00:00',TIMESTAMP'2010-01-21 00:00:00','IDIR\DOFLAMINGO','N','N','Y','N','N','Dawn Stronstad','2506927724','Not Available',TIMESTAMP'2010-01-27 00:00:00','IDIR\DOFLAMINGO','Y',TIMESTAMP'2010-01-27 00:00:00',14,'IDIR\DOFLAMINGO',TIMESTAMP'2009-09-29 16:14:05','AMND_PLAN_UPDATE',TIMESTAMP'2013-06-06 08:22:43'),
	 (105,3,'DFT','AMD',NULL,'BL Comfor 2007',NULL,NULL,NULL,TIMESTAMP'2014-07-12 00:00:00',TIMESTAMP'2007-03-14 00:00:00',NULL,NULL,'N','N','Y','Y','N','Melissa Steidle','2505625541','msteidle@dwbconsulting.ca',NULL,NULL,'Y',NULL,3,'BCEID\JWALKER',TIMESTAMP'2013-11-07 16:22:30','BCEID\JWALKER',TIMESTAMP'2013-11-07 16:23:20');

INSERT INTO FSP_STANDARDS_REGIME_XREF (FSP_ID,FSP_AMENDMENT_NUMBER,STANDARDS_REGIME_ID,REVISION_COUNT,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP) VALUES
	 (105,0,1010949,1,'IDIR\DOFLAMINGO',TIMESTAMP'2007-07-17 11:11:28','IDIR\DOFLAMINGO',TIMESTAMP'2007-07-17 11:11:28');

INSERT INTO STOCKING_LAYER (STOCKING_LAYER_ID,STOCKING_STANDARD_UNIT_ID,OPENING_ID,STOCKING_LAYER_CODE,TARGET_STOCKING,RESIDUAL_BASAL_AREA,MIN_HORIZONTAL_DISTANCE,MIN_PREF_STOCKING_STANDARD,MIN_POST_SPACING,MIN_STOCKING_STANDARD,MAX_POST_SPACING,MAX_CONIFER,HGHT_RELATIVE_TO_COMP,TREE_SIZE_UNIT_CODE,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP,REVISION_COUNT) VALUES
	 (1839703,1894782,1541296,'I',1200,NULL,2,600,NULL,700,NULL,10000,150,'%','BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:19','BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:19',1);

INSERT INTO STOCKING_LAYER_SPECIES (STOCKING_LAYER_ID,SILV_TREE_SPECIES_CODE,SPECIES_ORDER,PREFERRED_IND,MIN_HEIGHT,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP,REVISION_COUNT) VALUES
	 (1839703,'BL',1,'N',0.8,'BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:19','BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:19',1),
	 (1839703,'PLI',1,'Y',1.6,'BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:19','BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:19',1),
	 (1839703,'SX',2,'Y',0.8,'BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:19','BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:19',1);

INSERT INTO FOREST_COVER (FOREST_COVER_ID,OPENING_ID,STOCKING_STANDARD_UNIT_ID,SILV_POLYGON_NO,SILV_POLYGON_AREA,SILV_POLYGON_NET_AREA,STOCKING_CLASS_CODE,STOCKING_STATUS_CODE,STOCKING_TYPE_CODE,REFERENCE_YEAR,REENTRY_YEAR,SITE_CLASS_CODE,SITE_INDEX,SITE_INDEX_SOURCE_CODE,SILV_RESERVE_CODE,SILV_RESERVE_OBJECTIVE_CODE,TREE_SPECIES_CODE,TREE_COVER_PATTERN_CODE,RESULTS_SUBMISSION_ID,ENTRY_USERID,ENTRY_TIMESTAMP,UPDATE_USERID,UPDATE_TIMESTAMP,REVISION_COUNT) VALUES
	 (3634219,1541296,NULL,'RS1',0.5,0.5,NULL,'MAT','NAT',2010,NULL,NULL,19,'H','G','WTR','PLI',NULL,1717504,'BCEID\OTWIST',TIMESTAMP'2017-11-15 00:00:00','BCEID\OTWIST',TIMESTAMP'2017-11-15 15:57:45',2),
	 (3634220,1541296,1894782,'1',5.2,5.2,NULL,'IMM','ART',2017,NULL,NULL,19,'E',NULL,NULL,'PLI',NULL,1717504,'BCEID\OTWIST',TIMESTAMP'2017-11-15 00:00:00','BCEID\OTWIST',TIMESTAMP'2017-11-15 15:57:45',2),
	 (3634221,1541296,NULL,'R',0.3,0.3,NULL,'NP','UNN',2013,NULL,NULL,NULL,NULL,NULL,NULL,'PLI',NULL,1717504,'BCEID\OTWIST',TIMESTAMP'2017-11-15 00:00:00','BCEID\OTWIST',TIMESTAMP'2017-11-15 15:57:45',1);

INSERT INTO STOCKING_EVENT_HISTORY (STOCKING_EVENT_HISTORY_ID,OPENING_ID,OPENING_AMENDMENT_ID,OPENING_AMENDMENT_NUMBER,RESULTS_AUDIT_ACTION_CODE,SUBMITTED_USERID,RESULTS_SUBMISSION_ID,AMEND_EVENT_TIMESTAMP,ENTRY_USERID,ENTRY_TIMESTAMP,REVISION_COUNT) VALUES
	 (1544559,1541296,1541296,NULL,'ES','BCEID\OTWIST',1228545,TIMESTAMP'2013-05-31 11:26:50','BCEID\OTWIST',TIMESTAMP'2013-05-31 11:26:50',1),
	 (1547412,1541296,1541296,NULL,'VAR','BCEID\OTWIST',1246451,TIMESTAMP'2013-08-27 08:40:19','BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:19',1);

INSERT INTO RESULTS_AUDIT_EVENT (RESULTS_AUDIT_EVENT_ID,OPENING_ID,STANDARDS_REGIME_ID,SILVICULTURE_PROJECT_ID,RESULTS_AUDIT_ACTION_CODE,ACTION_DATE,DESCRIPTION,USER_ID,EMAIL_SENT_IND,XML_SUBMISSION_ID,OPENING_AMENDMENT_NUMBER,ENTRY_USERID,ENTRY_TIMESTAMP) VALUES
	 (2856895,1541296,NULL,NULL,'ES',TIMESTAMP'2013-05-31 11:26:50','Opening ID 1541296 created',NULL,'Y',1228545,NULL,'BCEID\OTWIST',TIMESTAMP'2013-05-31 11:26:50'),
	 (2856896,1541296,1010949,NULL,'ES',TIMESTAMP'2013-05-31 11:26:50','Standard was changed',NULL,'Y',1228545,NULL,'BCEID\OTWIST',TIMESTAMP'2013-05-31 11:26:50'),
	 (2856958,1541296,NULL,NULL,'ES',TIMESTAMP'2013-05-31 11:37:47','Creating Disturbance Activity: Activity Id = 2963894',NULL,'Y',1228561,NULL,'BCEID\OTWIST',TIMESTAMP'2013-05-31 11:37:47'),
	 (2857791,1541296,NULL,NULL,'MIL',TIMESTAMP'2013-05-31 14:41:09','Milestone was modified',NULL,'Y',1228929,NULL,'BCEID\JWALKER',TIMESTAMP'2013-05-31 14:41:09'),
	 (2862260,1541296,NULL,NULL,'ES',TIMESTAMP'2013-06-06 13:13:24','Forest Cover Polygon No. K1A_EEE_883_RS1 (Id=2914633)',NULL,'Y',1230607,NULL,'BCEID\OTWIST',TIMESTAMP'2013-06-06 13:13:24'),
	 (2862261,1541296,NULL,NULL,'ES',TIMESTAMP'2013-06-06 13:13:24','Forest Cover Polygon No. K1A_EEE_883_1 (Id=2914634)',NULL,'Y',1230607,NULL,'BCEID\OTWIST',TIMESTAMP'2013-06-06 13:13:24'),
	 (2862262,1541296,NULL,NULL,'ES',TIMESTAMP'2013-06-06 13:13:24','Forest Cover Polygon No. K1A_EEE_883_R (Id=2914635)',NULL,'Y',1230607,NULL,'BCEID\OTWIST',TIMESTAMP'2013-06-06 13:13:24'),
	 (2895408,1541296,NULL,NULL,'ES',TIMESTAMP'2013-08-27 08:40:18','Opening ID 1541296 updated',NULL,'Y',1246451,NULL,'BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:18'),
	 (2895409,1541296,NULL,NULL,'VAR',TIMESTAMP'2013-08-27 08:40:18','Stocking information modified.',NULL,'Y',1246451,NULL,'BCEID\OTWIST',TIMESTAMP'2013-08-27 08:40:18'),
	 (3963138,1541296,NULL,NULL,'ES',TIMESTAMP'2017-07-17 13:38:17','Inserting Activity: Activity Id = 3453899, Activity Base = ''PL''',NULL,'Y',1684259,NULL,'BCEID\OTWIST',TIMESTAMP'2017-07-17 13:38:17'),
	 (4019981,1541296,NULL,NULL,'ES',TIMESTAMP'2017-11-15 09:00:46','Forest Cover Polygon No. RS1 (Id=3633916)',NULL,'Y',1717299,NULL,'BCEID\OTWIST',TIMESTAMP'2017-11-15 09:00:46'),
	 (4019982,1541296,NULL,NULL,'ES',TIMESTAMP'2017-11-15 09:00:46','Forest Cover Polygon No. 1 (Id=3633917)',NULL,'Y',1717299,NULL,'BCEID\OTWIST',TIMESTAMP'2017-11-15 09:00:46'),
	 (4019983,1541296,NULL,NULL,'ES',TIMESTAMP'2017-11-15 09:00:46','Forest Cover Polygon No. R (Id=3633918)',NULL,'Y',1717299,NULL,'BCEID\OTWIST',TIMESTAMP'2017-11-15 09:00:46'),
	 (4020607,1541296,NULL,NULL,'ES',TIMESTAMP'2017-11-15 15:57:45','Forest Cover Polygon No. RS1 (Id=3634219)',NULL,'Y',1717504,NULL,'BCEID\OTWIST',TIMESTAMP'2017-11-15 15:57:45'),
	 (4020608,1541296,NULL,NULL,'ES',TIMESTAMP'2017-11-15 15:57:45','Forest Cover Polygon No. 1 (Id=3634220)',NULL,'Y',1717504,NULL,'BCEID\OTWIST',TIMESTAMP'2017-11-15 15:57:45'),
	 (4020609,1541296,NULL,NULL,'ES',TIMESTAMP'2017-11-15 15:57:45','Forest Cover Polygon No. R (Id=3634221)',NULL,'Y',1717504,NULL,'BCEID\OTWIST',TIMESTAMP'2017-11-15 15:57:45'),
	 (4021288,1541296,NULL,NULL,'MIL',TIMESTAMP'2017-11-16 11:35:43','Milestone was modified',NULL,'Y',1717697,NULL,'BCEID\MBRANDOS',TIMESTAMP'2017-11-16 11:35:43'),
	 (4601567,1541296,NULL,NULL,'ES',TIMESTAMP'2020-01-08 14:39:11','Inserting Activity: Activity Id = 3679696, Activity Base = ''SU''',NULL,'Y',1931429,NULL,'BCEID\MBRANDOS',TIMESTAMP'2020-01-08 14:39:11'),
	 (5073307,1541296,NULL,NULL,'ES',TIMESTAMP'2021-12-07 15:30:44','Inserting Activity: Activity Id = 3858309, Activity Base = ''SU''',NULL,'Y',2102053,NULL,'BCEID\ALOBSTER',TIMESTAMP'2021-12-07 15:30:44');
