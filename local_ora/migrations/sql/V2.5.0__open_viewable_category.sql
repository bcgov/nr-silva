
  CREATE TABLE "THE"."OPEN_VIEWABLE_CATEGORY" 
   (	"OPEN_CATEGORY_CODE" VARCHAR2(7) NOT NULL ENABLE, 
	"OPENING_STATUS_CODE" VARCHAR2(3), 
	"VIEWABLE_IND" VARCHAR2(1) DEFAULT 'N' NOT NULL ENABLE, 
	 CONSTRAINT "OVC_PK" PRIMARY KEY ("OPEN_CATEGORY_CODE")
  USING INDEX  ENABLE, 
	 CONSTRAINT "AVCON_1215562930_VIEWA_000" CHECK (VIEWABLE_IND IN ('Y', 'N')) ENABLE, 
	 CONSTRAINT "OVC_OSC_FK" FOREIGN KEY ("OPENING_STATUS_CODE")
	  REFERENCES "THE"."OPENING_STATUS_CODE" ("OPENING_STATUS_CODE") ENABLE, 
	 CONSTRAINT "OVC_OCC_FK" FOREIGN KEY ("OPEN_CATEGORY_CODE")
	  REFERENCES "THE"."OPEN_CATEGORY_CODE" ("OPEN_CATEGORY_CODE") ENABLE
   ) ;
