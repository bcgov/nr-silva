
  CREATE TABLE "THE"."SITE_CLASS_CODE" 
   (	"SITE_CLASS_CODE" VARCHAR2(1) NOT NULL ENABLE, 
	"DESCRIPTION" VARCHAR2(120) NOT NULL ENABLE, 
	"EFFECTIVE_DATE" DATE NOT NULL ENABLE, 
	"EXPIRY_DATE" DATE NOT NULL ENABLE, 
	"UPDATE_TIMESTAMP" DATE NOT NULL ENABLE
   ) ;
  CREATE UNIQUE INDEX "THE"."SITE_CLASS_CODE_PK" ON "THE"."SITE_CLASS_CODE" ("SITE_CLASS_CODE") 
  ;
ALTER TABLE "THE"."SITE_CLASS_CODE" ADD CONSTRAINT "SITE_CLASS_CODE_PK" PRIMARY KEY ("SITE_CLASS_CODE")
  USING INDEX "THE"."SITE_CLASS_CODE_PK"  ENABLE;
