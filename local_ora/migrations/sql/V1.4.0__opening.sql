
  CREATE TABLE "THE"."CORP_CAPTURE_METHOD" 
   (	"CAPTURE_METHOD_CODE" VARCHAR2(90) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(150) NOT NULL ENABLE, 
	"EFFECTIVE_DATE" DATE NOT NULL ENABLE, 
	"EXPIRY_DATE" DATE NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE, 
	 CONSTRAINT "CCM_PK" PRIMARY KEY ("CAPTURE_METHOD_CODE")
  USING INDEX  ENABLE
   ) ;

  CREATE TABLE "THE"."STANDARDS_REGIME_STATUS_CODE" 
   (	"STANDARDS_REGIME_STATUS_CODE" VARCHAR2(3) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(120) NOT NULL ENABLE, 
	"EFFECTIVE_DATE" DATE NOT NULL ENABLE, 
	"EXPIRY_DATE" DATE NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE
   ) ;
  CREATE UNIQUE INDEX "THE"."SRSC_PK" ON "THE"."STANDARDS_REGIME_STATUS_CODE" ("STANDARDS_REGIME_STATUS_CODE") 
  ;
ALTER TABLE "THE"."STANDARDS_REGIME_STATUS_CODE" ADD CONSTRAINT "SRSC_PK" PRIMARY KEY ("STANDARDS_REGIME_STATUS_CODE")
  USING INDEX "THE"."SRSC_PK"  ENABLE;

  CREATE TABLE "THE"."SILV_STATUTE_CODE" 
   (	"SILV_STATUTE_CODE" VARCHAR2(3) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(120) NOT NULL ENABLE, 
	"EFFECTIVE_DATE" DATE NOT NULL ENABLE, 
	"EXPIRY_DATE" DATE NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE, 
	 CONSTRAINT "SSTCD_PK" PRIMARY KEY ("SILV_STATUTE_CODE")
  USING INDEX  ENABLE
   ) ;

  CREATE TABLE "THE"."STANDARDS_REGIME" 
   (	"STANDARDS_REGIME_ID" NUMBER(10,0) NOT NULL ENABLE, 
	"STANDARDS_REGIME_NAME" VARCHAR2(50), 
	"STANDARDS_REGIME_STATUS_CODE" VARCHAR2(3) NOT NULL ENABLE, 
	"SILV_STATUTE_CODE" VARCHAR2(3), 
	"STANDARDS_OBJECTIVE" VARCHAR2(50), 
	"GEOGRAPHIC_DESCRIPTION" VARCHAR2(50), 
	"MOF_DEFAULT_STANDARD_IND" VARCHAR2(1) DEFAULT 'N' NOT NULL ENABLE, 
	"REGEN_DELAY_OFFSET_YRS" NUMBER(2,0), 
	"REGEN_OBLIGATION_IND" VARCHAR2(1) NOT NULL ENABLE, 
	"NO_REGEN_EARLY_OFFSET_YRS" NUMBER(2,0), 
	"NO_REGEN_LATE_OFFSET_YRS" NUMBER(2,0), 
	"FREE_GROWING_EARLY_OFFSET_YRS" NUMBER(2,0), 
	"FREE_GROWING_LATE_OFFSET_YRS" NUMBER(2,0), 
	"APPROVED_BY_USERID" VARCHAR2(30), 
	"APPROVED_DATE" DATE, 
	"SUBMITTED_BY_USERID" VARCHAR2(30), 
	"SUBMITTED_DATE" DATE, 
	"EFFECTIVE_DATE" DATE, 
	"EXPIRY_DATE" DATE, 
	"ADDITIONAL_STANDARDS" VARCHAR2(4000), 
	"REJECT_NOTE" VARCHAR2(2000), 
	"ENTRY_USERID" VARCHAR2(30) NOT NULL ENABLE, 
	"ENTRY_TIMESTAMP" DATE NOT NULL ENABLE, 
	"UPDATE_USERID" VARCHAR2(30) NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE, 
	"REVISION_COUNT" NUMBER(5,0) NOT NULL ENABLE, 
	 CONSTRAINT "SR_PK" PRIMARY KEY ("STANDARDS_REGIME_ID")
  USING INDEX  ENABLE, 
	 CONSTRAINT "SR_SRSC_FK" FOREIGN KEY ("STANDARDS_REGIME_STATUS_CODE")
	  REFERENCES "THE"."STANDARDS_REGIME_STATUS_CODE" ("STANDARDS_REGIME_STATUS_CODE") ENABLE, 
	 CONSTRAINT "SR_SSTCD_FK" FOREIGN KEY ("SILV_STATUTE_CODE")
	  REFERENCES "THE"."SILV_STATUTE_CODE" ("SILV_STATUTE_CODE") ENABLE
   ) ;

  CREATE TABLE "THE"."STOCKING_STANDARD_UNIT" 
   (	"STOCKING_STANDARD_UNIT_ID" NUMBER(10,0) NOT NULL ENABLE, 
	"OPENING_ID" NUMBER(10,0) NOT NULL ENABLE, 
	"STANDARDS_UNIT_ID" VARCHAR2(4) NOT NULL ENABLE, 
	"STANDARDS_REGIME_ID" NUMBER(10,0), 
	"NET_AREA" NUMBER(7,1) NOT NULL ENABLE, 
	"MAX_ALLOW_SOIL_DISTURBANCE_PCT" NUMBER(3,1), 
	"VARIANCE_IND" VARCHAR2(1), 
	"ENTRY_USERID" VARCHAR2(30) NOT NULL ENABLE, 
	"ENTRY_TIMESTAMP" DATE NOT NULL ENABLE, 
	"UPDATE_USERID" VARCHAR2(30) NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE, 
	"REVISION_COUNT" NUMBER(5,0) NOT NULL ENABLE, 
	 CONSTRAINT "SSU_PK" PRIMARY KEY ("STOCKING_STANDARD_UNIT_ID")
  USING INDEX  ENABLE, 
	 CONSTRAINT "SSU_SR_FK" FOREIGN KEY ("STANDARDS_REGIME_ID")
	  REFERENCES "THE"."STANDARDS_REGIME" ("STANDARDS_REGIME_ID") ENABLE, 
	 CONSTRAINT "SSU_O_FK" FOREIGN KEY ("OPENING_ID")
	  REFERENCES "THE"."OPENING" ("OPENING_ID") ENABLE
   ) ;

  CREATE TABLE "THE"."SILV_RESERVE_OBJECTIVE_CODE" 
   (	"SILV_RESERVE_OBJECTIVE_CODE" VARCHAR2(3) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(120) NOT NULL ENABLE, 
	"EFFECTIVE_DATE" DATE NOT NULL ENABLE, 
	"EXPIRY_DATE" DATE NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE
   ) ;
  CREATE UNIQUE INDEX "THE"."SROC_PK" ON "THE"."SILV_RESERVE_OBJECTIVE_CODE" ("SILV_RESERVE_OBJECTIVE_CODE") 
  ;
ALTER TABLE "THE"."SILV_RESERVE_OBJECTIVE_CODE" ADD CONSTRAINT "SROC_PK" PRIMARY KEY ("SILV_RESERVE_OBJECTIVE_CODE")
  USING INDEX "THE"."SROC_PK"  ENABLE;

  CREATE TABLE "THE"."TREE_COVER_PATTERN_CODE" 
   (	"TREE_COVER_PATTERN_CODE" VARCHAR2(1) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(120) NOT NULL ENABLE, 
	"EFFECTIVE_DATE" DATE NOT NULL ENABLE, 
	"EXPIRY_DATE" DATE NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE
   ) ;
  CREATE UNIQUE INDEX "THE"."TCPC_PK" ON "THE"."TREE_COVER_PATTERN_CODE" ("TREE_COVER_PATTERN_CODE") 
  ;
ALTER TABLE "THE"."TREE_COVER_PATTERN_CODE" ADD CONSTRAINT "TCPC_PK" PRIMARY KEY ("TREE_COVER_PATTERN_CODE")
  USING INDEX "THE"."TCPC_PK"  ENABLE;

  CREATE TABLE "THE"."STOCKING_TYPE_CODE" 
   (	"STOCKING_TYPE_CODE" VARCHAR2(3) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(120) NOT NULL ENABLE, 
	"EFFECTIVE_DATE" DATE NOT NULL ENABLE, 
	"EXPIRY_DATE" DATE NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE
   ) ;
  CREATE UNIQUE INDEX "THE"."STC3_PK" ON "THE"."STOCKING_TYPE_CODE" ("STOCKING_TYPE_CODE") 
  ;
ALTER TABLE "THE"."STOCKING_TYPE_CODE" ADD CONSTRAINT "STC3_PK" PRIMARY KEY ("STOCKING_TYPE_CODE")
  USING INDEX "THE"."STC3_PK"  ENABLE;

  CREATE TABLE "THE"."SILV_RESERVE_CODE" 
   (	"SILV_RESERVE_CODE" VARCHAR2(1) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(120) NOT NULL ENABLE, 
	"EFFECTIVE_DATE" DATE NOT NULL ENABLE, 
	"EXPIRY_DATE" DATE NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE
   ) ;
  CREATE UNIQUE INDEX "THE"."SRC_PK" ON "THE"."SILV_RESERVE_CODE" ("SILV_RESERVE_CODE") 
  ;
ALTER TABLE "THE"."SILV_RESERVE_CODE" ADD CONSTRAINT "SRC_PK" PRIMARY KEY ("SILV_RESERVE_CODE")
  USING INDEX "THE"."SRC_PK"  ENABLE;

  CREATE TABLE "THE"."FOREST_COVER" 
   (	"FOREST_COVER_ID" NUMBER(10,0) NOT NULL ENABLE, 
	"OPENING_ID" NUMBER(10,0) NOT NULL ENABLE, 
	"STOCKING_STANDARD_UNIT_ID" NUMBER(10,0), 
	"SILV_POLYGON_NO" VARCHAR2(30) NOT NULL ENABLE, 
	"SILV_POLYGON_AREA" NUMBER(7,1) NOT NULL ENABLE, 
	"SILV_POLYGON_NET_AREA" NUMBER(7,1) NOT NULL ENABLE, 
	"STOCKING_CLASS_CODE" VARCHAR2(1), 
	"STOCKING_STATUS_CODE" VARCHAR2(3), 
	"STOCKING_TYPE_CODE" VARCHAR2(3), 
	"REFERENCE_YEAR" NUMBER(4,0) NOT NULL ENABLE, 
	"REENTRY_YEAR" NUMBER(4,0), 
	"SITE_CLASS_CODE" VARCHAR2(1), 
	"SITE_INDEX" NUMBER(5,0), 
	"SITE_INDEX_SOURCE_CODE" VARCHAR2(1), 
	"SILV_RESERVE_CODE" VARCHAR2(1), 
	"SILV_RESERVE_OBJECTIVE_CODE" VARCHAR2(3), 
	"TREE_SPECIES_CODE" VARCHAR2(8), 
	"TREE_COVER_PATTERN_CODE" VARCHAR2(1), 
	"RESULTS_SUBMISSION_ID" NUMBER(10,0), 
	"ENTRY_USERID" VARCHAR2(30) NOT NULL ENABLE, 
	"ENTRY_TIMESTAMP" DATE NOT NULL ENABLE, 
	"UPDATE_USERID" VARCHAR2(30) NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE, 
	"REVISION_COUNT" NUMBER(5,0) NOT NULL ENABLE, 
	 CONSTRAINT "FC_SSU_FK" FOREIGN KEY ("STOCKING_STANDARD_UNIT_ID")
	  REFERENCES "THE"."STOCKING_STANDARD_UNIT" ("STOCKING_STANDARD_UNIT_ID") ENABLE, 
	 CONSTRAINT "FC_SRC_FK" FOREIGN KEY ("SILV_RESERVE_CODE")
	  REFERENCES "THE"."SILV_RESERVE_CODE" ("SILV_RESERVE_CODE") ENABLE, 
	 CONSTRAINT "FC_SSC6_FK" FOREIGN KEY ("STOCKING_STATUS_CODE")
	  REFERENCES "THE"."STOCKING_STATUS_CODE" ("STOCKING_STATUS_CODE") ENABLE, 
	 CONSTRAINT "FC_RES_FK" FOREIGN KEY ("RESULTS_SUBMISSION_ID")
	  REFERENCES "THE"."RESULTS_ELECTRONIC_SUBMISSION" ("RESULTS_SUBMISSION_ID") ENABLE, 
	 CONSTRAINT "FC_SCC1_FK" FOREIGN KEY ("STOCKING_CLASS_CODE")
	  REFERENCES "THE"."STOCKING_CLASS_CODE" ("STOCKING_CLASS_CODE") ENABLE, 
	 CONSTRAINT "FC_TCPC_FK" FOREIGN KEY ("TREE_COVER_PATTERN_CODE")
	  REFERENCES "THE"."TREE_COVER_PATTERN_CODE" ("TREE_COVER_PATTERN_CODE") ENABLE, 
	 CONSTRAINT "FC_O_FK" FOREIGN KEY ("OPENING_ID")
	  REFERENCES "THE"."OPENING" ("OPENING_ID") ENABLE, 
	 CONSTRAINT "FC_SCC2_FK" FOREIGN KEY ("SITE_CLASS_CODE")
	  REFERENCES "THE"."SITE_CLASS_CODE" ("SITE_CLASS_CODE") ENABLE, 
	 CONSTRAINT "FC_SROC_FK" FOREIGN KEY ("SILV_RESERVE_OBJECTIVE_CODE")
	  REFERENCES "THE"."SILV_RESERVE_OBJECTIVE_CODE" ("SILV_RESERVE_OBJECTIVE_CODE") ENABLE, 
	 CONSTRAINT "FC_TSC_FK" FOREIGN KEY ("TREE_SPECIES_CODE")
	  REFERENCES "THE"."TREE_SPECIES_CODE" ("TREE_SPECIES_CODE") ENABLE, 
	 CONSTRAINT "FC_SISC_FK" FOREIGN KEY ("SITE_INDEX_SOURCE_CODE")
	  REFERENCES "THE"."SITE_INDEX_SOURCE_CODE" ("SITE_INDEX_SOURCE_CODE") ENABLE, 
	 CONSTRAINT "FC_STC3_FK" FOREIGN KEY ("STOCKING_TYPE_CODE")
	  REFERENCES "THE"."STOCKING_TYPE_CODE" ("STOCKING_TYPE_CODE") ENABLE
   )  ENABLE ROW MOVEMENT ;
  CREATE UNIQUE INDEX "THE"."FC3_PK" ON "THE"."FOREST_COVER" ("FOREST_COVER_ID") 
  ;
ALTER TABLE "THE"."FOREST_COVER" ADD CONSTRAINT "FC3_PK" PRIMARY KEY ("FOREST_COVER_ID")
  USING INDEX "THE"."FC3_PK"  ENABLE;

  CREATE TABLE "THE"."STORAGE_FILES" 
   (	"GROUP_NAME_CD" VARCHAR2(10) NOT NULL ENABLE, 
	"NAME" VARCHAR2(60) NOT NULL ENABLE, 
	"LOCAL_DIRECTORY" VARCHAR2(255) NOT NULL ENABLE, 
	"TILE_SIZE" VARCHAR2(11) NOT NULL ENABLE, 
	"DATA_MATURITY_CD" CHAR(1) NOT NULL ENABLE, 
	"STEWARD_ORG_UNIT_CD" VARCHAR2(6), 
	"UPDATE_RESPONSIBILITY" VARCHAR2(255), 
	"ENTRY_TIMESTAMP" DATE NOT NULL ENABLE, 
	"ENTRY_USERID" VARCHAR2(32) NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE, 
	"UPDATE_USERID" VARCHAR2(32) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(2000) NOT NULL ENABLE, 
	"SPECIAL_CONCERNS" VARCHAR2(2000), 
	"DATA_AVAILABILITY" VARCHAR2(2000)
   ) ;
  CREATE UNIQUE INDEX "THE"."FILE_PK" ON "THE"."STORAGE_FILES" ("GROUP_NAME_CD") 
  ;
ALTER TABLE "THE"."STORAGE_FILES" ADD CONSTRAINT "FILE_PK" PRIMARY KEY ("GROUP_NAME_CD")
  USING INDEX "THE"."FILE_PK"  ENABLE;

  CREATE TABLE "THE"."FEATURE_CLASSES" 
   (	"FEATURE_CLASS_SKEY" NUMBER(10,0) NOT NULL ENABLE, 
	"GROUP_NAME_CD" VARCHAR2(10) NOT NULL ENABLE, 
	"NAME" VARCHAR2(60) NOT NULL ENABLE, 
	"FEATURE_TYPE_CD" VARCHAR2(10) NOT NULL ENABLE, 
	"DATA_MATURITY_CD" CHAR(1) NOT NULL ENABLE, 
	"OVERLAPPING_FEATURE_IND" CHAR(1) NOT NULL ENABLE, 
	"SPAN_TILE_IND" CHAR(1) NOT NULL ENABLE, 
	"DERIVED_DATA_IND" CHAR(1) NOT NULL ENABLE, 
	"MANDATORY_METADATA_IND" CHAR(1) NOT NULL ENABLE, 
	"CUSTODIAN_ORG_UNIT_CD" VARCHAR2(6), 
	"DATA_STANDARDS_MANAGER" VARCHAR2(60), 
	"STEWARD_ORG_UNIT_CD" VARCHAR2(6), 
	"EXTENDED_ORG_UNIT_CD" VARCHAR2(6), 
	"EXTENDED_DATA_CONTACT" VARCHAR2(60), 
	"DISCIPLINE_STANDARD_CD" VARCHAR2(4), 
	"COLLECTION_PROCESS" VARCHAR2(255), 
	"ENTRY_TIMESTAMP" DATE NOT NULL ENABLE, 
	"ENTRY_USERID" VARCHAR2(32) NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE, 
	"UPDATE_USERID" VARCHAR2(32) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(2000) NOT NULL ENABLE, 
	"COMMENTS" VARCHAR2(2000), 
	 CONSTRAINT "FEATUR_CLS_FILE_FK" FOREIGN KEY ("GROUP_NAME_CD")
	  REFERENCES "THE"."STORAGE_FILES" ("GROUP_NAME_CD") ENABLE
   ) ;
  CREATE UNIQUE INDEX "THE"."FEATUR_CLS_PK" ON "THE"."FEATURE_CLASSES" ("FEATURE_CLASS_SKEY") 
  ;
ALTER TABLE "THE"."FEATURE_CLASSES" ADD CONSTRAINT "FEATUR_CLS_PK" PRIMARY KEY ("FEATURE_CLASS_SKEY")
  USING INDEX "THE"."FEATUR_CLS_PK"  ENABLE;

  CREATE TABLE "THE"."DATA_SOURCE_CODE" 
   (	"DATA_SOURCE_CODE" VARCHAR2(10) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(120) NOT NULL ENABLE, 
	"EFFECTIVE_DATE" DATE NOT NULL ENABLE, 
	"EXPIRY_DATE" DATE NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE, 
	 CONSTRAINT "DSC_PK" PRIMARY KEY ("DATA_SOURCE_CODE")
  USING INDEX  ENABLE
   ) ;

  CREATE TABLE "THE"."FOREST_COVER_GEOMETRY" 
   (	"FOREST_COVER_ID" NUMBER(10,0) NOT NULL ENABLE, 
	"GEOMETRY" "MDSYS"."SDO_GEOMETRY"  NOT NULL ENABLE, 
	"FEATURE_AREA" NUMBER(11,4) NOT NULL ENABLE, 
	"FEATURE_PERIMETER" NUMBER(11,4) NOT NULL ENABLE, 
	"CAPTURE_METHOD_CODE" VARCHAR2(30), 
	"DATA_SOURCE_CODE" VARCHAR2(10), 
	"FEATURE_CLASS_SKEY" NUMBER(10,0) NOT NULL ENABLE, 
	"OBSERVATION_DATE" DATE, 
	"DATA_QUALITY_COMMENT" VARCHAR2(255), 
	"ENTRY_USERID" VARCHAR2(30) NOT NULL ENABLE, 
	"ENTRY_TIMESTAMP" DATE NOT NULL ENABLE, 
	"UPDATE_USERID" VARCHAR2(30) NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE, 
	"REVISION_COUNT" NUMBER(5,0) NOT NULL ENABLE, 
	 CONSTRAINT "FCG_PK" PRIMARY KEY ("FOREST_COVER_ID")
  USING INDEX  ENABLE, 
	 CONSTRAINT "FCG_CMC_FK" FOREIGN KEY ("CAPTURE_METHOD_CODE")
	  REFERENCES "THE"."CORP_CAPTURE_METHOD" ("CAPTURE_METHOD_CODE") ENABLE, 
	 CONSTRAINT "FCG_DSC_FK" FOREIGN KEY ("DATA_SOURCE_CODE")
	  REFERENCES "THE"."DATA_SOURCE_CODE" ("DATA_SOURCE_CODE") ENABLE, 
	 CONSTRAINT "FCG_CORPFCC_FK" FOREIGN KEY ("FEATURE_CLASS_SKEY")
	  REFERENCES "THE"."FEATURE_CLASSES" ("FEATURE_CLASS_SKEY") ENABLE, 
	 CONSTRAINT "FCG_FC_FK" FOREIGN KEY ("FOREST_COVER_ID")
	  REFERENCES "THE"."FOREST_COVER" ("FOREST_COVER_ID") ENABLE
   ) 
 VARRAY "GEOMETRY"."SDO_ELEM_INFO" STORE AS SECUREFILE LOB 
 VARRAY "GEOMETRY"."SDO_ORDINATES" STORE AS SECUREFILE LOB ;
