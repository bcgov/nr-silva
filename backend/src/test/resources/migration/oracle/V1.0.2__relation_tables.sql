-- Foreign keys references. Some are commented due to the table not being onboarded yet or due to the table belonging to another application

--- There is some FK references that are not properly identified in the model
ALTER TABLE THE.CUT_BLOCK_OPEN_ADMIN ADD CONSTRAINT CBOA_CB_FK FOREIGN KEY (CB_SKEY) REFERENCES THE.CUT_BLOCK(CB_SKEY);
ALTER TABLE THE.CUT_BLOCK_OPEN_ADMIN ADD CONSTRAINT CBOA_O_FK FOREIGN KEY (OPENING_ID) REFERENCES THE.OPENING(OPENING_ID);

--- There is some FK references that are not properly identified in the model
ALTER TABLE THE.OPENING_ATTACHMENT ADD CONSTRAINT OA_MTC_FK FOREIGN KEY (MIME_TYPE_CODE) REFERENCES THE.MIME_TYPE_CODE(MIME_TYPE_CODE);
ALTER TABLE THE.OPENING_ATTACHMENT ADD CONSTRAINT OA_O_FK FOREIGN KEY (OPENING_ID) REFERENCES THE.OPENING(OPENING_ID);

ALTER TABLE THE.OPENING ADD CONSTRAINT O_ACC_FK FOREIGN KEY (PREV_AGE_CLASS_CODE) REFERENCES THE.AGE_CLASS_CODE(AGE_CLASS_CODE);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_HCC_FK FOREIGN KEY (PREV_HEIGHT_CLASS_CODE) REFERENCES THE.HEIGHT_CLASS_CODE(HEIGHT_CLASS_CODE);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_OCC_FK FOREIGN KEY (OPEN_CATEGORY_CODE) REFERENCES THE.OPEN_CATEGORY_CODE(OPEN_CATEGORY_CODE);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_OSC_FK FOREIGN KEY (OPENING_STATUS_CODE) REFERENCES THE.OPENING_STATUS_CODE(OPENING_STATUS_CODE);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_OU_FK FOREIGN KEY (ADMIN_DISTRICT_NO) REFERENCES THE.ORG_UNIT(ORG_UNIT_NO);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_OU_RESIDES_WITHIN_FK FOREIGN KEY (GEO_DISTRICT_NO) REFERENCES THE.ORG_UNIT(ORG_UNIT_NO);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_RES_FK FOREIGN KEY (RESULTS_SUBMISSION_ID) REFERENCES THE.RESULTS_ELECTRONIC_SUBMISSION(RESULTS_SUBMISSION_ID);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_SAZ_FK FOREIGN KEY (ORG_UNIT_NO,DIST_ADMIN_ZONE) REFERENCES THE.SILV_ADMIN_ZONE(ORG_UNIT_NO,DIST_ADMIN_ZONE);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_SCC1_FK FOREIGN KEY (PREV_STOCKING_CLASS_CODE) REFERENCES THE.STOCKING_CLASS_CODE(STOCKING_CLASS_CODE);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_SCC2_FK FOREIGN KEY (PREV_SITE_CLASS_CODE) REFERENCES THE.SITE_CLASS_CODE(SITE_CLASS_CODE);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_SISC_FK FOREIGN KEY (PREV_SITE_INDEX_SOURCE_CODE) REFERENCES THE.SITE_INDEX_SOURCE_CODE(SITE_INDEX_SOURCE_CODE);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_SSC6_FK FOREIGN KEY (PREV_STOCKING_STATUS_CODE) REFERENCES THE.STOCKING_STATUS_CODE(STOCKING_STATUS_CODE);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_TNC1_FK FOREIGN KEY (TSB_NUMBER_CODE) REFERENCES THE.TSB_NUMBER_CODE(TSB_NUMBER_CODE);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_TSC1_FK FOREIGN KEY (PREV_TREE_SPP1_CODE) REFERENCES THE.TREE_SPECIES_CODE(TREE_SPECIES_CODE);
ALTER TABLE THE.OPENING ADD CONSTRAINT O_TSC2_FK FOREIGN KEY (PREV_TREE_SPP2_CODE) REFERENCES THE.TREE_SPECIES_CODE(TREE_SPECIES_CODE);

--- There is some FK references that are not properly identified in the model, This one references THE.CLIENT_LOCATION that belongs to the CLIENT application
ALTER TABLE THE.RESULTS_ELECTRONIC_SUBMISSION ADD CONSTRAINT RES_OU_FK FOREIGN KEY (ORG_UNIT_NO) REFERENCES THE.ORG_UNIT(ORG_UNIT_NO);

ALTER TABLE THE.STOCKING_EVENT_HISTORY ADD CONSTRAINT SEH_OAH_FK FOREIGN KEY (OPENING_AMENDMENT_ID,OPENING_AMENDMENT_NUMBER) REFERENCES THE.OPENING_AMENDMENT_HISTORY(OPENING_ID,OPENING_AMENDMENT_NUMBER);
ALTER TABLE THE.STOCKING_EVENT_HISTORY ADD CONSTRAINT SEH_O_FK FOREIGN KEY (OPENING_ID) REFERENCES THE.OPENING(OPENING_ID);
ALTER TABLE THE.STOCKING_EVENT_HISTORY ADD CONSTRAINT SEH_RAAC_FK FOREIGN KEY (RESULTS_AUDIT_ACTION_CODE) REFERENCES THE.RESULTS_AUDIT_ACTION_CODE(RESULTS_AUDIT_ACTION_CODE);


ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_CBOA_FK FOREIGN KEY (CUT_BLOCK_OPEN_ADMIN_ID) REFERENCES THE.CUT_BLOCK_OPEN_ADMIN(CUT_BLOCK_OPEN_ADMIN_ID);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_DC_FK FOREIGN KEY (DISTURBANCE_CODE) REFERENCES THE.DISTURBANCE_CODE(DISTURBANCE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_OU_FK FOREIGN KEY (ORG_UNIT_NO) REFERENCES THE.ORG_UNIT(ORG_UNIT_NO);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_O_FK FOREIGN KEY (OPENING_ID) REFERENCES THE.OPENING(OPENING_ID);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_PROJ_FK FOREIGN KEY (SILVICULTURE_PROJECT_ID) REFERENCES THE.SILVICULTURE_PROJECT(SILVICULTURE_PROJECT_ID);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_RES_FK FOREIGN KEY (RESULTS_SUBMISSION_ID) REFERENCES THE.RESULTS_ELECTRONIC_SUBMISSION(RESULTS_SUBMISSION_ID);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SAM_FK FOREIGN KEY (SILV_ACTIVITY_MEASUREMENT_CODE) REFERENCES THE.SILV_ACTIVITY_MEASUREMENT_CODE(SILV_ACTIVITY_MEASUREMENT_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SBC_FK FOREIGN KEY (SILV_BASE_CODE) REFERENCES THE.SILV_BASE_CODE(SILV_BASE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SCPC_FK FOREIGN KEY (SILV_CUT_PHASE_CODE) REFERENCES THE.SILV_CUT_PHASE_CODE(SILV_CUT_PHASE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SFSC_FK FOREIGN KEY (SILV_FUND_SRCE_CODE) REFERENCES THE.SILV_FUND_SRCE_CODE(SILV_FUND_SRCE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SFSC_PLND_FK FOREIGN KEY (PLAN_SILV_FUND_SRCE_CODE) REFERENCES THE.SILV_FUND_SRCE_CODE(SILV_FUND_SRCE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SMC_FK FOREIGN KEY (SILV_METHOD_CODE) REFERENCES THE.SILV_METHOD_CODE(SILV_METHOD_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SMC_PLND_FK FOREIGN KEY (PLAN_SILV_METHOD_CODE) REFERENCES THE.SILV_METHOD_CODE(SILV_METHOD_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SOC1_FK FOREIGN KEY (SILV_OBJECTIVE_CODE_1) REFERENCES THE.SILV_OBJECTIVE_CODE(SILV_OBJECTIVE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SOC2_FK FOREIGN KEY (SILV_OBJECTIVE_CODE_2) REFERENCES THE.SILV_OBJECTIVE_CODE(SILV_OBJECTIVE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SOC3_FK FOREIGN KEY (SILV_OBJECTIVE_CODE_3) REFERENCES THE.SILV_OBJECTIVE_CODE(SILV_OBJECTIVE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SOC_PLND1_FK FOREIGN KEY (PLAN_SILV_OBJECTIVE_CODE_1) REFERENCES THE.SILV_OBJECTIVE_CODE(SILV_OBJECTIVE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SOC_PLND2_FK FOREIGN KEY (PLAN_SILV_OBJECTIVE_CODE_2) REFERENCES THE.SILV_OBJECTIVE_CODE(SILV_OBJECTIVE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SOC_PLND3_FK FOREIGN KEY (PLAN_SILV_OBJECTIVE_CODE_3) REFERENCES THE.SILV_OBJECTIVE_CODE(SILV_OBJECTIVE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SSC_FK FOREIGN KEY (SILV_SYSTEM_CODE) REFERENCES THE.SILV_SYSTEM_CODE(SILV_SYSTEM_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_SSVC_FK FOREIGN KEY (SILV_SYSTEM_VARIANT_CODE) REFERENCES THE.SILV_SYSTEM_VARIANT_CODE(SILV_SYSTEM_VARIANT_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_STC_FK FOREIGN KEY (SILV_TECHNIQUE_CODE) REFERENCES THE.SILV_TECHNIQUE_CODE(SILV_TECHNIQUE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_STC_PLND_FK FOREIGN KEY (PLAN_SILV_TECHNIQUE_CODE) REFERENCES THE.SILV_TECHNIQUE_CODE(SILV_TECHNIQUE_CODE);
ALTER TABLE THE.ACTIVITY_TREATMENT_UNIT ADD CONSTRAINT ATU_STSC_FK FOREIGN KEY (SILV_TREE_SPECIES_CODE) REFERENCES THE.SILV_TREE_SPECIES_CODE(SILV_TREE_SPECIES_CODE);

-- THE.SILV_RELIEF_APPLICATION foreign keys
ALTER TABLE THE.SILV_RELIEF_APPLICATION ADD CONSTRAINT SRA_ATU_FK FOREIGN KEY (ACTIVITY_TREATMENT_UNIT_ID) REFERENCES THE.ACTIVITY_TREATMENT_UNIT(ACTIVITY_TREATMENT_UNIT_ID);
ALTER TABLE THE.SILV_RELIEF_APPLICATION ADD CONSTRAINT SRA_OAH_FK FOREIGN KEY (AMENDMENT_OPENING_ID,OPENING_AMENDMENT_NUMBER) REFERENCES THE.OPENING_AMENDMENT_HISTORY(OPENING_ID,OPENING_AMENDMENT_NUMBER);
ALTER TABLE THE.SILV_RELIEF_APPLICATION ADD CONSTRAINT SRA_SRASCD_FK FOREIGN KEY (SILV_RELIEF_APPL_STATUS_CODE) REFERENCES THE.SILV_RELIEF_APPL_STATUS_CODE(SILV_RELIEF_APPL_STATUS_CODE);

-- THE.STOCKING_STANDARD_UNIT foreign keys
ALTER TABLE THE.STOCKING_STANDARD_UNIT ADD CONSTRAINT SSU_O_FK FOREIGN KEY (OPENING_ID) REFERENCES THE.OPENING(OPENING_ID);
ALTER TABLE THE.STOCKING_STANDARD_UNIT ADD CONSTRAINT SSU_SR_FK FOREIGN KEY (STANDARDS_REGIME_ID) REFERENCES THE.STANDARDS_REGIME(STANDARDS_REGIME_ID);

-- THE.FOREST_FILE_CLIENT foreign keys
ALTER TABLE THE.FOREST_FILE_CLIENT ADD CONSTRAINT FFC_FFCTC_FK FOREIGN KEY (FOREST_FILE_CLIENT_TYPE_CODE) REFERENCES THE.FILE_CLIENT_TYPE_CODE(FILE_CLIENT_TYPE_CODE);
ALTER TABLE THE.FOREST_FILE_CLIENT ADD CONSTRAINT FFC_PFU_FK FOREIGN KEY (FOREST_FILE_ID) REFERENCES THE.PROV_FOREST_USE(FOREST_FILE_ID);

-- THE.PROV_FOREST_USE foreign keys
ALTER TABLE THE.PROV_FOREST_USE ADD	CONSTRAINT PFU_BCTS_OU_FK FOREIGN KEY (BCTS_ORG_UNIT) REFERENCES THE.ORG_UNIT(ORG_UNIT_NO);
ALTER TABLE THE.PROV_FOREST_USE ADD CONSTRAINT PFU_FMU_FK FOREIGN KEY (MGMT_UNIT_ID, MGMT_UNIT_TYPE) REFERENCES THE.FOREST_MGMT_UNIT (MGMT_UNIT_ID, MGMT_UNIT_TYPE_CODE);
ALTER TABLE THE.PROV_FOREST_USE ADD CONSTRAINT PFU_FTCD_FK FOREIGN KEY (FILE_TYPE_CODE) REFERENCES THE.FILE_TYPE_CODE(FILE_TYPE_CODE);
ALTER TABLE THE.PROV_FOREST_USE ADD CONSTRAINT PFU_OU_FK FOREIGN KEY (FOREST_REGION) REFERENCES THE.ORG_UNIT(ORG_UNIT_NO);
ALTER TABLE THE.PROV_FOREST_USE ADD CONSTRAINT PFU_TFSCD_FK FOREIGN KEY (FILE_STATUS_ST) REFERENCES THE.TENURE_FILE_STATUS_CODE(TENURE_FILE_STATUS_CODE);

-- THE.CUT_BLOCK_CLIENT foreign keys
ALTER TABLE THE.CUT_BLOCK_CLIENT ADD CONSTRAINT CBC_CBCTC_FK FOREIGN KEY (CUT_BLOCK_CLIENT_TYPE_CODE) REFERENCES THE.CUT_BLOCK_CLIENT_TYPE_CODE(CUT_BLOCK_CLIENT_TYPE_CODE);
ALTER TABLE THE.CUT_BLOCK_CLIENT ADD CONSTRAINT CBC_CB_FK FOREIGN KEY (CB_SKEY) REFERENCES THE.CUT_BLOCK(CB_SKEY);

-- THE.CUT_BLOCK foreign keys
ALTER TABLE THE.CUT_BLOCK ADD CONSTRAINT CBLK_BSCD_FK FOREIGN KEY (BLOCK_STATUS_ST) REFERENCES THE.BLOCK_STATUS_CODE(BLOCK_STATUS_CODE);
ALTER TABLE THE.CUT_BLOCK ADD CONSTRAINT CBLK_FHRC_FK FOREIGN KEY (FIRE_HARVESTING_REASON_CODE) REFERENCES THE.FIRE_HARVESTING_REASON_CODE(FIRE_HARVESTING_REASON_CODE);
ALTER TABLE THE.CUT_BLOCK ADD CONSTRAINT CBLK_TM_FK FOREIGN KEY (TIMBER_MARK) REFERENCES THE.TIMBER_MARK(TIMBER_MARK);
ALTER TABLE THE.CUT_BLOCK ADD CONSTRAINT CB_CRC_FK FOREIGN KEY (CUT_REGULATION_CODE) REFERENCES THE.CUT_REGULATION_CODE(CUT_REGULATION_CODE);
ALTER TABLE THE.CUT_BLOCK ADD CONSTRAINT CB_HAA_FK FOREIGN KEY (TIMBER_MARK) REFERENCES THE.HAULING_AUTHORITY(TIMBER_MARK);
ALTER TABLE THE.CUT_BLOCK ADD CONSTRAINT CB_HVA_FK FOREIGN KEY (HVA_SKEY) REFERENCES THE.HARVESTING_AUTHORITY(HVA_SKEY);
ALTER TABLE THE.CUT_BLOCK ADD CONSTRAINT CB_RDTC_FK FOREIGN KEY (REFOREST_DECLARE_TYPE_CODE) REFERENCES THE.REFOREST_DECLARE_TYPE_CODE(REFOREST_DECLARE_TYPE_CODE);

-- THE.OPEN_VIEWABLE_CATEGORY foreign keys
ALTER TABLE THE.OPEN_VIEWABLE_CATEGORY ADD CONSTRAINT OVC_OCC_FK FOREIGN KEY (OPEN_CATEGORY_CODE) REFERENCES THE.OPEN_CATEGORY_CODE(OPEN_CATEGORY_CODE);
ALTER TABLE THE.OPEN_VIEWABLE_CATEGORY ADD CONSTRAINT OVC_OSC_FK FOREIGN KEY (OPENING_STATUS_CODE) REFERENCES THE.OPENING_STATUS_CODE(OPENING_STATUS_CODE);

-- THE.FOR_CLIENT_LINK foreign keys
ALTER TABLE THE.FOR_CLIENT_LINK ADD CONSTRAINT FCL_FCTC_FK FOREIGN KEY (FILE_CLIENT_TYPE) REFERENCES THE.FILE_CLIENT_TYPE_CODE(FILE_CLIENT_TYPE_CODE);
ALTER TABLE THE.FOR_CLIENT_LINK ADD CONSTRAINT FCL_PFU_FK FOREIGN KEY (FOREST_FILE_ID) REFERENCES THE.PROV_FOREST_USE(FOREST_FILE_ID);

-- THE.FOREST_MGMT_UNIT foreign keys
ALTER TABLE THE.FOREST_MGMT_UNIT ADD CONSTRAINT FMU_MUTC_FK FOREIGN KEY (MGMT_UNIT_TYPE_CODE) REFERENCES THE.MGMT_UNIT_TYPE_CODE (MGMT_UNIT_TYPE_CODE);

-- THE.TIMBER_MARK foreign keys
ALTER TABLE THE.TIMBER_MARK ADD CONSTRAINT TM_PFU_FK FOREIGN KEY (FOREST_FILE_ID) REFERENCES THE.PROV_FOREST_USE (FOREST_FILE_ID);
ALTER TABLE THE.TIMBER_MARK ADD CONSTRAINT TM_CASCD_FK FOREIGN KEY (CASCADE_SPLIT_CODE) REFERENCES THE.CASCADE_SPLIT_CODE (CASCADE_SPLIT_CODE);
ALTER TABLE THE.TIMBER_MARK ADD CONSTRAINT TM_MERCD_FK FOREIGN KEY (MARK_EXTEND_RSN_CD) REFERENCES THE.MARK_EXTENSION_REASON_CODE (MARK_EXTENSION_REASON_CODE);
ALTER TABLE THE.TIMBER_MARK ADD CONSTRAINT TM_MICD_FK FOREIGN KEY (MARKNG_INSTRMNT_CD) REFERENCES THE.MARKING_INSTRUMENT_CODE (MARKING_INSTRUMENT_CODE);
ALTER TABLE THE.TIMBER_MARK ADD CONSTRAINT TFALLING_GEOGRAPHICALLY_WIT_FK FOREIGN KEY (GEOGRAPHIC_DISTRCT) REFERENCES THE.ORG_UNIT (ORG_UNIT_NO);
ALTER TABLE THE.TIMBER_MARK ADD CONSTRAINT TM_OU_FK FOREIGN KEY (FOREST_DISTRICT) REFERENCES THE.ORG_UNIT (ORG_UNIT_NO);
ALTER TABLE THE.TIMBER_MARK ADD CONSTRAINT TM_QTCD_FK FOREIGN KEY (QUOTA_TYPE_CODE) REFERENCES THE.QUOTA_TYPE_CODE (QUOTA_TYPE_CODE);
ALTER TABLE THE.TIMBER_MARK ADD CONSTRAINT TM_MMCD_FK FOREIGN KEY (MARKING_METHOD_CD) REFERENCES THE.MARKING_METHOD_CODE (MARKING_METHOD_CODE);
ALTER TABLE THE.TIMBER_MARK ADD CONSTRAINT TM_TSCD_FK FOREIGN KEY (MARK_STATUS_ST) REFERENCES THE.TENURE_FILE_STATUS_CODE (TENURE_FILE_STATUS_CODE);
ALTER TABLE THE.TIMBER_MARK ADD CONSTRAINT TM_CLRC_FK FOREIGN KEY (LANDS_REGION) REFERENCES THE.CROWN_LANDS_REGION_CODE (CROWN_LANDS_REGION_CODE);
ALTER TABLE THE.TIMBER_MARK ADD CONSTRAINT TM_SALVTCD_FK FOREIGN KEY (SALVAGE_TYPE_CODE) REFERENCES THE.SALVAGE_TYPE_CODE (SALVAGE_TYPE_CODE);


-- THE.HARVESTING_AUTHORITY foreign keys
ALTER TABLE THE.HARVESTING_AUTHORITY ADD CONSTRAINT HVA_SALVTCD_FK FOREIGN KEY (SALVAGE_TYPE_CODE) REFERENCES THE.SALVAGE_TYPE_CODE (SALVAGE_TYPE_CODE);
ALTER TABLE THE.HARVESTING_AUTHORITY ADD CONSTRAINT HVA_HTC_FK FOREIGN KEY (HARVEST_TYPE_CODE) REFERENCES THE.HARVEST_TYPE_CODE (HARVEST_TYPE_CODE);
ALTER TABLE THE.HARVESTING_AUTHORITY ADD CONSTRAINT HVA_QTC_FK FOREIGN KEY (QUOTA_TYPE_CODE) REFERENCES THE.QUOTA_TYPE_CODE (QUOTA_TYPE_CODE);
ALTER TABLE THE.HARVESTING_AUTHORITY ADD CONSTRAINT HVA_LTCCD_FK FOREIGN KEY (LICENCE_TO_CUT_CODE) REFERENCES THE.LICENCE_TO_CUT_CODE (LICENCE_TO_CUT_CODE);
ALTER TABLE THE.HARVESTING_AUTHORITY ADD CONSTRAINT HVA_GEO_OU_FK FOREIGN KEY (GEOGRAPHIC_DISTRICT) REFERENCES THE.ORG_UNIT (ORG_UNIT_NO);
ALTER TABLE THE.HARVESTING_AUTHORITY ADD CONSTRAINT HVA_HAERC_FK FOREIGN KEY (HARVEST_AUTH_EXTEND_REAS_CODE) REFERENCES THE.HARVEST_AUTH_EXTEND_REAS_CODE (HARVEST_AUTH_EXTEND_REAS_CODE);
ALTER TABLE THE.HARVESTING_AUTHORITY ADD CONSTRAINT HVA_CSC_FK FOREIGN KEY (CASCADE_SPLIT_CODE) REFERENCES THE.CASCADE_SPLIT_CODE (CASCADE_SPLIT_CODE);
ALTER TABLE THE.HARVESTING_AUTHORITY ADD CONSTRAINT HVA_PFU_FK FOREIGN KEY (FOREST_FILE_ID) REFERENCES THE.PROV_FOREST_USE (FOREST_FILE_ID);
ALTER TABLE THE.HARVESTING_AUTHORITY ADD CONSTRAINT HVA_HASC_FK FOREIGN KEY (HARVEST_AUTH_STATUS_CODE) REFERENCES THE.HARVEST_AUTH_STATUS_CODE (HARVEST_AUTH_STATUS_CODE);
ALTER TABLE THE.HARVESTING_AUTHORITY ADD CONSTRAINT HVA_OU_FK FOREIGN KEY (FOREST_DISTRICT) REFERENCES THE.ORG_UNIT (ORG_UNIT_NO);
ALTER TABLE THE.HARVESTING_AUTHORITY ADD CONSTRAINT HVA_MUTC_FK FOREIGN KEY (MGMT_UNIT_TYPE_CODE) REFERENCES THE.MGMT_UNIT_TYPE_CODE (MGMT_UNIT_TYPE_CODE);
ALTER TABLE THE.HARVESTING_AUTHORITY ADD CONSTRAINT HVA_CLRC_FK FOREIGN KEY (CROWN_LANDS_REGION_CODE) REFERENCES THE.CROWN_LANDS_REGION_CODE (CROWN_LANDS_REGION_CODE);

-- THE.HAULING_AUTHORITY foreign keys
ALTER TABLE THE.HAULING_AUTHORITY ADD CONSTRAINT HAA_PFU_FK FOREIGN KEY (FOREST_FILE_ID) REFERENCES THE.PROV_FOREST_USE (FOREST_FILE_ID);
ALTER TABLE THE.HAULING_AUTHORITY ADD CONSTRAINT HAA_MICD_FK FOREIGN KEY (MARKING_INSTRUMENT_CODE) REFERENCES THE.MARKING_INSTRUMENT_CODE (MARKING_INSTRUMENT_CODE);
ALTER TABLE THE.HAULING_AUTHORITY ADD CONSTRAINT HAA_MMCD_FK FOREIGN KEY (MARKING_METHOD_CODE) REFERENCES THE.MARKING_METHOD_CODE (MARKING_METHOD_CODE);

-- THE.RESULTS_AUDIT_DETAIL foreign keys
ALTER TABLE THE.RESULTS_AUDIT_DETAIL ADD CONSTRAINT RAD_RAE_FK FOREIGN KEY (RESULTS_AUDIT_EVENT_ID) REFERENCES THE.RESULTS_AUDIT_EVENT (RESULTS_AUDIT_EVENT_ID);

-- THE.OPENING_AMENDMENT_HISTORY foreign keys
ALTER TABLE THE.OPENING_AMENDMENT_HISTORY ADD CONSTRAINT OAH_O_FK FOREIGN KEY (OPENING_ID) REFERENCES THE.OPENING (OPENING_ID);

-- THE.STANDARDS_REGIME foreign keys
ALTER TABLE THE.STANDARDS_REGIME ADD CONSTRAINT SR_SRSC_FK FOREIGN KEY (STANDARDS_REGIME_STATUS_CODE) REFERENCES THE.STANDARDS_REGIME_STATUS_CODE (STANDARDS_REGIME_STATUS_CODE);
ALTER TABLE THE.STANDARDS_REGIME ADD CONSTRAINT SR_SSTCD_FK FOREIGN KEY (SILV_STATUTE_CODE) REFERENCES THE.SILV_STATUTE_CODE (SILV_STATUTE_CODE);

-- THE.FOREST_COVER
ALTER TABLE THE.FOREST_COVER ADD CONSTRAINT FC_SSU_FK FOREIGN KEY (STOCKING_STANDARD_UNIT_ID) REFERENCES THE.STOCKING_STANDARD_UNIT (STOCKING_STANDARD_UNIT_ID);
ALTER TABLE THE.FOREST_COVER ADD CONSTRAINT FC_SRC_FK FOREIGN KEY (SILV_RESERVE_CODE) REFERENCES THE.SILV_RESERVE_CODE (SILV_RESERVE_CODE);
ALTER TABLE THE.FOREST_COVER ADD CONSTRAINT FC_SSC6_FK FOREIGN KEY (STOCKING_STATUS_CODE) REFERENCES THE.STOCKING_STATUS_CODE (STOCKING_STATUS_CODE);
ALTER TABLE THE.FOREST_COVER ADD CONSTRAINT FC_RES_FK FOREIGN KEY (RESULTS_SUBMISSION_ID) REFERENCES THE.RESULTS_ELECTRONIC_SUBMISSION (RESULTS_SUBMISSION_ID);
ALTER TABLE THE.FOREST_COVER ADD CONSTRAINT FC_SCC1_FK FOREIGN KEY (STOCKING_CLASS_CODE) REFERENCES THE.STOCKING_CLASS_CODE (STOCKING_CLASS_CODE);
ALTER TABLE THE.FOREST_COVER ADD CONSTRAINT FC_TCPC_FK FOREIGN KEY (TREE_COVER_PATTERN_CODE) REFERENCES THE.TREE_COVER_PATTERN_CODE (TREE_COVER_PATTERN_CODE);
ALTER TABLE THE.FOREST_COVER ADD CONSTRAINT FC_O_FK FOREIGN KEY (OPENING_ID) REFERENCES THE.OPENING (OPENING_ID);
ALTER TABLE THE.FOREST_COVER ADD CONSTRAINT FC_SCC2_FK FOREIGN KEY (SITE_CLASS_CODE) REFERENCES THE.SITE_CLASS_CODE (SITE_CLASS_CODE);
ALTER TABLE THE.FOREST_COVER ADD CONSTRAINT FC_SROC_FK FOREIGN KEY (SILV_RESERVE_OBJECTIVE_CODE) REFERENCES THE.SILV_RESERVE_OBJECTIVE_CODE (SILV_RESERVE_OBJECTIVE_CODE);
ALTER TABLE THE.FOREST_COVER ADD CONSTRAINT FC_TSC_FK FOREIGN KEY (TREE_SPECIES_CODE) REFERENCES THE.TREE_SPECIES_CODE (TREE_SPECIES_CODE);
ALTER TABLE THE.FOREST_COVER ADD CONSTRAINT FC_SISC_FK FOREIGN KEY (SITE_INDEX_SOURCE_CODE) REFERENCES THE.SITE_INDEX_SOURCE_CODE (SITE_INDEX_SOURCE_CODE);
ALTER TABLE THE.FOREST_COVER ADD CONSTRAINT FC_STC3_FK FOREIGN KEY (STOCKING_TYPE_CODE) REFERENCES THE.STOCKING_TYPE_CODE (STOCKING_TYPE_CODE);

-- THE.FEATURE_CLASSES foreign keys
ALTER TABLE THE.FEATURE_CLASSES ADD CONSTRAINT FEATUR_CLS_FILE_FK FOREIGN KEY (GROUP_NAME_CD) REFERENCES THE.STORAGE_FILES (GROUP_NAME_CD);

-- THE.FOREST_COVER_GEOMETRY foreign keys
ALTER TABLE THE.FOREST_COVER_GEOMETRY ADD CONSTRAINT FCG_CMC_FK FOREIGN KEY (CAPTURE_METHOD_CODE) REFERENCES THE.CORP_CAPTURE_METHOD (CAPTURE_METHOD_CODE);
ALTER TABLE THE.FOREST_COVER_GEOMETRY ADD CONSTRAINT FCG_DSC_FK FOREIGN KEY (DATA_SOURCE_CODE) REFERENCES THE.DATA_SOURCE_CODE (DATA_SOURCE_CODE);
ALTER TABLE THE.FOREST_COVER_GEOMETRY ADD CONSTRAINT FCG_CORPFCC_FK FOREIGN KEY (FEATURE_CLASS_SKEY) REFERENCES THE.FEATURE_CLASSES (FEATURE_CLASS_SKEY);
ALTER TABLE THE.FOREST_COVER_GEOMETRY ADD CONSTRAINT FCG_FC_FK FOREIGN KEY (FOREST_COVER_ID) REFERENCES THE.FOREST_COVER (FOREST_COVER_ID);


-- THE.SILVICULTURE_PROJECT foreign keys
ALTER TABLE THE.SILVICULTURE_PROJECT ADD CONSTRAINT PROJ_SBC_FK FOREIGN KEY (SILV_BASE_CODE) REFERENCES THE.SILV_BASE_CODE (SILV_BASE_CODE);
ALTER TABLE THE.SILVICULTURE_PROJECT ADD CONSTRAINT PROJ_SAZ_FK FOREIGN KEY (ORG_UNIT_NO, DIST_ADMIN_ZONE) REFERENCES THE.SILV_ADMIN_ZONE (ORG_UNIT_NO, DIST_ADMIN_ZONE);
ALTER TABLE THE.SILVICULTURE_PROJECT ADD CONSTRAINT PROJ_UBCD_FK FOREIGN KEY (UNIT_BID_CODE) REFERENCES THE.UNIT_BID_CODE (UNIT_BID_CODE);
ALTER TABLE THE.SILVICULTURE_PROJECT ADD CONSTRAINT PROJ_OU_FK FOREIGN KEY (ORG_UNIT_NO) REFERENCES THE.ORG_UNIT (ORG_UNIT_NO);
ALTER TABLE THE.SILVICULTURE_PROJECT ADD CONSTRAINT PROJ_PSCD_FK FOREIGN KEY (SILV_PROJECT_STATUS_CODE) REFERENCES THE.SILV_PROJECT_STATUS_CODE (SILV_PROJECT_STATUS_CODE);
ALTER TABLE THE.SILVICULTURE_PROJECT ADD CONSTRAINT PROJ_CCHCD_FK FOREIGN KEY (CREW_CONTRACT_HIRE_CODE) REFERENCES THE.CREW_CONTRACT_HIRE_CODE (CREW_CONTRACT_HIRE_CODE);

-- THE.SILV_COMMENT_XREF foreign keys

ALTER TABLE THE.SILV_COMMENT_XREF ADD CONSTRAINT SCX_SCSC_FK FOREIGN KEY (SILV_COMMENT_SOURCE_CODE) REFERENCES THE.SILV_COMMENT_SOURCE_CODE(SILV_COMMENT_SOURCE_CODE);
ALTER TABLE THE.SILV_COMMENT_XREF ADD CONSTRAINT SCX_SCTC_FK FOREIGN KEY (SILV_COMMENT_TYPE_CODE) REFERENCES THE.SILV_COMMENT_TYPE_CODE(SILV_COMMENT_TYPE_CODE);


-- THE.OPENING_COMMENT_LINK foreign keys

ALTER TABLE THE.OPENING_COMMENT_LINK ADD CONSTRAINT OCL_O_FK FOREIGN KEY (OPENING_ID) REFERENCES THE.OPENING(OPENING_ID);
ALTER TABLE THE.OPENING_COMMENT_LINK ADD CONSTRAINT OCL_SILVC_FK FOREIGN KEY (SILVICULTURE_COMMENT_ID) REFERENCES THE.SILVICULTURE_COMMENT(SILVICULTURE_COMMENT_ID);

-- THE.SILVICULTURE_COMMENT foreign keys

ALTER TABLE THE.SILVICULTURE_COMMENT ADD CONSTRAINT SILVC_SCX_FK FOREIGN KEY (SILV_COMMENT_TYPE_CODE,SILV_COMMENT_SOURCE_CODE) REFERENCES THE.SILV_COMMENT_XREF(SILV_COMMENT_TYPE_CODE,SILV_COMMENT_SOURCE_CODE);