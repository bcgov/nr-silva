
  CREATE OR REPLACE EDITIONABLE TRIGGER "THE"."RESULTS_FCG_BR_IU_TRG" 
BEFORE INSERT OR UPDATE
  ON forest_cover_geometry
  FOR EACH ROW
DECLARE
BEGIN
 mof_spatial_validation.validate_and_fix
                                         (:NEW.geometry
                                        , mof_spatial_validation.c_bcalbers_sdo_tolerance);
END results_fcg_br_iu_trg;




/
ALTER TRIGGER "THE"."RESULTS_FCG_BR_IU_TRG" ENABLE;

  CREATE OR REPLACE EDITIONABLE TRIGGER "THE"."RSLT_VRIMS_EVENT_O_TRG" 
/******************************************************************************
   Trigger: RSLT_VRIMS_EVENT_O_TRG
   Purpose: This trigger will result in the call to generate_vrims_event()
            procedure which Notifies the External VRIMS system of
            Create, Update and Delete of any row in this table.
   Revision History
   Person               Date       Comments
   -----------------    ---------  --------------------------------
   Charles Devjayanth   2012-02-15 Created
   Charles Devjayanth   2015-06-23 OPENING_STATUS_CODE is updated to "FG" AND FOREST_COVER_GEOMETRY exists.
******************************************************************************/
AFTER INSERT OR UPDATE OR DELETE ON OPENING FOR EACH ROW
DECLARE
  v_count NUMBER := 0;
BEGIN

  IF UPDATING THEN
    -- OPENING_STATUS_CODE is updated to "FG"
    -- AND FOREST_COVER_GEOMETRY exists.
    SELECT COUNT(1) INTO v_count
    FROM forest_cover fc, forest_cover_geometry fcg
    WHERE fc.forest_cover_id = fcg.forest_cover_id AND fc.opening_id = :OLD.OPENING_ID;

    IF v_count > 0 AND :OLD.OPENING_STATUS_CODE != 'FG' AND :NEW.OPENING_STATUS_CODE = 'FG' THEN
      RESULTS_VRIMS_EVENT_GEN.generate_vrims_event(:OLD.OPENING_ID);
    END IF;
  END IF;

EXCEPTION

  WHEN NO_DATA_FOUND THEN
    NULL;
  WHEN OTHERS THEN
    NULL;

END RSLT_VRIMS_EVENT_O_TRG;




/
ALTER TRIGGER "THE"."RSLT_VRIMS_EVENT_O_TRG" ENABLE;

  CREATE OR REPLACE EDITIONABLE TRIGGER "THE"."RESULTS_O_AR_IUD_TRG" 
/******************************************************************************
   Trigger:  RESULTS_O_AR_IUD_TRG
   Purpose: This trigger will call the  RESULTS_AUDIT.INSERT_AUDIT_DETAIL
            procedure when the status of the opening or standards regime is
            either  'APP' or 'AMD'. The procedure inserts data into the
            RESULTS_AUDIT_DETAIL table based on the data that was passed in.
   Revision History
   Person               Date       Comments
   -----------------    ---------  --------------------------------
   wcound (Pangaea)     2003-03-07 Created
   wcound               2003-03-27 Added NVL to Update to capture null values.
   R.A.Robb             2004-05-14 CR#46 - updated auditing and added dist_admin_zone
   Ian Fraser           2004-06-30 Removed extraneous variables and code
   R.A.Robb             2005-09-01 PT#27866 - NVL missing on some columns
   P. Stinson           2007-03-16 RESULTS ILRR interface additions
   R. Pardo Figueroa    2007-05-25 Force submissions to RESULTS ILRR to be executed
                                   always (regardless of v_audit_enabled_ind)
******************************************************************************/
AFTER INSERT OR UPDATE OR DELETE ON OPENING FOR EACH ROW
DECLARE
  v_opening_id             RESULTS_AUDIT_EVENT.OPENING_ID%TYPE;
  v_status                 VARCHAR2(3);
  v_table_name             RESULTS_AUDIT_DETAIL.TABLE_NAME%TYPE;
  v_column_name            RESULTS_AUDIT_DETAIL.COLUMN_NAME%TYPE;
  v_old_value              RESULTS_AUDIT_DETAIL.OLD_VALUE%TYPE;
  v_new_value              RESULTS_AUDIT_DETAIL.NEW_VALUE%TYPE;
  v_business_identifier    RESULTS_AUDIT_DETAIL.BUSINESS_IDENTIFIER%TYPE;
  v_audit_enabled_ind      VARCHAR2(1);
  c_op_scope_interest      CONSTANT    varchar2(8) := 'Interest';

BEGIN
  v_table_name := 'OPENING';
  v_opening_id := Results_Globals.GET_OPENING_ID;
  v_status := Results_Globals.GET_RESULTS_AUDIT_STATUS;
  v_audit_enabled_ind := Results_Globals.GET_AUDITING_ENABLED_IND;

IF v_audit_enabled_ind = 'Y'
AND results_opening.opening_can_be_updated(v_status) THEN
 -------------------------
 --UPDATING
 -------------------------
 IF UPDATING THEN

   v_business_identifier := 'Opening Id: '||TO_CHAR(:NEW.opening_id);

   IF NVL(:NEW.OPENING_ID,0) != NVL(:OLD.OPENING_ID,0) THEN
     v_column_name := 'OPENING_ID';
     v_old_value := :OLD.OPENING_ID;
     v_new_value := :NEW.OPENING_ID;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.GEO_DISTRICT_NO,0) != NVL(:OLD.GEO_DISTRICT_NO,0) THEN
     v_column_name := 'GEO_DISTRICT_NO';
     v_old_value := :OLD.GEO_DISTRICT_NO;
     v_new_value := :NEW.GEO_DISTRICT_NO;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.ADMIN_DISTRICT_NO,0) != NVL(:OLD.ADMIN_DISTRICT_NO,0) THEN
     v_column_name := 'ADMIN_DISTRICT_NO';
     v_old_value := :OLD.ADMIN_DISTRICT_NO;
     v_new_value := :NEW.ADMIN_DISTRICT_NO;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.MAPSHEET_GRID,'~') != NVL(:OLD.MAPSHEET_GRID,'~') THEN
     v_column_name := 'MAPSHEET_GRID';
     v_old_value := :OLD.MAPSHEET_GRID;
     v_new_value := :NEW.MAPSHEET_GRID;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.MAPSHEET_LETTER,'~') != NVL(:OLD.MAPSHEET_LETTER,'~') THEN
     v_column_name := 'MAPSHEET_LETTER';
     v_old_value := :OLD.MAPSHEET_LETTER;
     v_new_value := :NEW.MAPSHEET_LETTER;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.MAPSHEET_SQUARE,'~') != NVL(:OLD.MAPSHEET_SQUARE,'~') THEN
     v_column_name := 'MAPSHEET_SQUARE';
     v_old_value := :OLD.MAPSHEET_SQUARE;
     v_new_value := :NEW.MAPSHEET_SQUARE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.MAPSHEET_QUAD,'~') != NVL(:OLD.MAPSHEET_QUAD,'~') THEN
     v_column_name := 'MAPSHEET_QUAD';
     v_old_value := :OLD.MAPSHEET_QUAD;
     v_new_value := :NEW.MAPSHEET_QUAD;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.MAPSHEET_SUB_QUAD,'~') != NVL(:OLD.MAPSHEET_SUB_QUAD,'~') THEN
     v_column_name := 'MAPSHEET_SUB_QUAD';
     v_old_value := :OLD.MAPSHEET_SUB_QUAD;
     v_new_value := :NEW.MAPSHEET_SUB_QUAD;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.OPENING_NUMBER,'~') != NVL(:OLD.OPENING_NUMBER,'~') THEN
     v_column_name := 'OPENING_NUMBER';
     v_old_value := :OLD.OPENING_NUMBER;
     v_new_value := :NEW.OPENING_NUMBER;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.OPENING_LOCN_NAME,'~') != NVL(:OLD.OPENING_LOCN_NAME,'~') THEN
     v_column_name := 'OPENING_LOCN_NAME';
     v_old_value := :OLD.OPENING_LOCN_NAME;
     v_new_value := :NEW.OPENING_LOCN_NAME;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.OPEN_CATEGORY_CODE,'~') != NVL(:OLD.OPEN_CATEGORY_CODE,'~') THEN
     v_column_name := 'OPEN_CATEGORY_CODE';
     v_old_value := :OLD.OPEN_CATEGORY_CODE;
     v_new_value := :NEW.OPEN_CATEGORY_CODE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.LICENSEE_OPENING_ID, 0) != NVL(:OLD.LICENSEE_OPENING_ID, 0) THEN
     v_column_name := 'LICENSEE_OPENING_ID';
     v_old_value := :OLD.LICENSEE_OPENING_ID;
     v_new_value := :NEW.LICENSEE_OPENING_ID;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.TSB_NUMBER_CODE,'~') != NVL(:OLD.TSB_NUMBER_CODE,'~') THEN
     v_column_name := 'TSB_NUMBER_CODE';
     v_old_value := :OLD.TSB_NUMBER_CODE;
     v_new_value := :NEW.TSB_NUMBER_CODE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.OPENING_STATUS_CODE,'~') != NVL(:OLD.OPENING_STATUS_CODE,'~') THEN
     v_column_name := 'OPENING_STATUS_CODE';
     v_old_value := :OLD.OPENING_STATUS_CODE;
     v_new_value := :NEW.OPENING_STATUS_CODE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.MAX_ALLOW_PERMNT_ACCESS_PCT,0) != NVL(:OLD.MAX_ALLOW_PERMNT_ACCESS_PCT,0) THEN
     v_column_name := 'MAX_ALLOW_PERMNT_ACCESS_PCT';
     v_old_value := :OLD.MAX_ALLOW_PERMNT_ACCESS_PCT;
     v_new_value := :NEW.MAX_ALLOW_PERMNT_ACCESS_PCT;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.PREV_AGE_CLASS_CODE,0) != NVL(:OLD.PREV_AGE_CLASS_CODE,0) THEN
     v_column_name := 'PREV_AGE_CLASS_CODE';
     v_old_value := :OLD.PREV_AGE_CLASS_CODE;
     v_new_value := :NEW.PREV_AGE_CLASS_CODE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.PREV_SITE_INDEX,0) != NVL(:OLD.PREV_SITE_INDEX,0) THEN
     v_column_name := 'PREV_SITE_INDEX';
     v_old_value := :OLD.PREV_SITE_INDEX;
     v_new_value := :NEW.PREV_SITE_INDEX;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.PREV_SITE_INDEX_SOURCE_CODE,0) != NVL(:OLD.PREV_SITE_INDEX_SOURCE_CODE,0) THEN
     v_column_name := 'PREV_SITE_INDEX_SOURCE_CODE';
     v_old_value := :OLD.PREV_SITE_INDEX_SOURCE_CODE;
     v_new_value := :NEW.PREV_SITE_INDEX_SOURCE_CODE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.PREV_HEIGHT_CLASS_CODE,0) != NVL(:OLD.PREV_HEIGHT_CLASS_CODE,0) THEN
     v_column_name := 'PREV_HEIGHT_CLASS_CODE';
     v_old_value := :OLD.PREV_HEIGHT_CLASS_CODE;
     v_new_value := :NEW.PREV_HEIGHT_CLASS_CODE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.PREV_SITE_CLASS_CODE,0) != NVL(:OLD.PREV_SITE_CLASS_CODE,0) THEN
     v_column_name := 'PREV_SITE_CLASS_CODE';
     v_old_value := :OLD.PREV_SITE_CLASS_CODE;
     v_new_value := :NEW.PREV_SITE_CLASS_CODE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.PREV_STOCKING_CLASS_CODE,0) != NVL(:OLD.PREV_STOCKING_CLASS_CODE,0) THEN
     v_column_name := 'PREV_STOCKING_CLASS_CODE';
     v_old_value := :OLD.PREV_STOCKING_CLASS_CODE;
     v_new_value := :NEW.PREV_STOCKING_CLASS_CODE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.PREV_STOCKING_STATUS_CODE,0) != NVL(:OLD.PREV_STOCKING_STATUS_CODE,0) THEN
     v_column_name := 'PREV_STOCKING_STATUS_CODE';
     v_old_value := :OLD.PREV_STOCKING_STATUS_CODE;
     v_new_value := :NEW.PREV_STOCKING_STATUS_CODE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.PREV_TREE_SPP1_CODE,0) != NVL(:OLD.PREV_TREE_SPP1_CODE,0) THEN
     v_column_name := 'PREV_TREE_SPP1_CODE';
     v_old_value := :OLD.PREV_TREE_SPP1_CODE;
     v_new_value := :NEW.PREV_TREE_SPP1_CODE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.PREV_TREE_SPP2_CODE,0) != NVL(:OLD.PREV_TREE_SPP2_CODE,0) THEN
     v_column_name := 'PREV_TREE_SPP2_CODE';
     v_old_value := :OLD.PREV_TREE_SPP2_CODE;
     v_new_value := :NEW.PREV_TREE_SPP2_CODE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.APP_ENT_BY_USERID,0) != NVL(:OLD.APP_ENT_BY_USERID,0) THEN
     v_column_name := 'APP_ENT_BY_USERID';
     v_old_value := :OLD.APP_ENT_BY_USERID;
     v_new_value := :NEW.APP_ENT_BY_USERID;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(Pkg_Sil_Date_Conversion.CONVERT_TO_CHAR(:NEW.APPROVE_DATE,NULL,'Y'),'0')
   != NVL(Pkg_Sil_Date_Conversion.CONVERT_TO_CHAR(:OLD.APPROVE_DATE,NULL,'Y'),'0') THEN
     v_column_name := 'APPROVE_DATE';
     v_old_value := Sil_Date_Conversion.convert_to_char_trunc_time(:OLD.APPROVE_DATE);
     v_new_value := Sil_Date_Conversion.convert_to_char_trunc_time(:NEW.APPROVE_DATE);
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.AMENDMENT_IND,0) != NVL(:OLD.AMENDMENT_IND,0) THEN
     v_column_name := 'AMENDMENT_IND';
     v_old_value := :OLD.AMENDMENT_IND;
     v_new_value := :NEW.AMENDMENT_IND;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.RESULTS_SUBMISSION_ID,0) != NVL(:OLD.RESULTS_SUBMISSION_ID,0) THEN
     v_column_name := 'RESULTS_SUBMISSION_ID';
     v_old_value := :OLD.RESULTS_SUBMISSION_ID;
     v_new_value := :NEW.RESULTS_SUBMISSION_ID;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   IF NVL(:NEW.DIST_ADMIN_ZONE,'!!') != NVL(:OLD.DIST_ADMIN_ZONE,'!!') THEN
     v_column_name := 'DIST_ADMIN_ZONE';
     v_old_value := :OLD.DIST_ADMIN_ZONE;
     v_new_value := :NEW.DIST_ADMIN_ZONE;
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

 -------------------------
 --DELETING
 -------------------------
 ELSIF DELETING THEN
   v_business_identifier := 'Opening Id: '||TO_CHAR(:OLD.opening_id);
   v_new_value := NULL;

   v_column_name := 'OPENING_ID';
   v_old_value := :OLD.OPENING_ID;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'GEO_DISTRICT_NO';
   v_old_value := :OLD.GEO_DISTRICT_NO;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'ADMIN_DISTRICT_NO';
   v_old_value := :OLD.ADMIN_DISTRICT_NO;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'MAPSHEET_GRID';
   v_old_value := :OLD.MAPSHEET_GRID;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'MAPSHEET_LETTER';
   v_old_value := :OLD.MAPSHEET_LETTER;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'MAPSHEET_SQUARE';
   v_old_value := :OLD.MAPSHEET_SQUARE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'MAPSHEET_QUAD';
   v_old_value := :OLD.MAPSHEET_QUAD;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'MAPSHEET_SUB_QUAD';
   v_old_value := :OLD.MAPSHEET_SUB_QUAD;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'OPENING_NUMBER';
   v_old_value := :OLD.OPENING_NUMBER;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'OPENING_LOCN_NAME';
   v_old_value := :OLD.OPENING_LOCN_NAME;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'OPEN_CATEGORY_CODE';
   v_old_value := :OLD.OPEN_CATEGORY_CODE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'LICENSEE_OPENING_ID';
   v_old_value := :OLD.LICENSEE_OPENING_ID;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'TSB_NUMBER_CODE';
   v_old_value := :OLD.TSB_NUMBER_CODE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'OPENING_STATUS_CODE';
   v_old_value := :OLD.OPENING_STATUS_CODE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'MAX_ALLOW_PERMNT_ACCESS_PCT';
   v_old_value := :OLD.MAX_ALLOW_PERMNT_ACCESS_PCT;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_AGE_CLASS_CODE';
   v_old_value := :OLD.PREV_AGE_CLASS_CODE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_SITE_INDEX';
   v_old_value := :OLD.PREV_SITE_INDEX;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_SITE_INDEX_SOURCE_CODE';
   v_old_value := :OLD.PREV_SITE_INDEX_SOURCE_CODE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_HEIGHT_CLASS_CODE';
   v_old_value := :OLD.PREV_HEIGHT_CLASS_CODE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_SITE_CLASS_CODE';
   v_old_value := :OLD.PREV_SITE_CLASS_CODE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_STOCKING_CLASS_CODE';
   v_old_value := :OLD.PREV_STOCKING_CLASS_CODE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_STOCKING_STATUS_CODE';
   v_old_value := :OLD.PREV_STOCKING_STATUS_CODE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_TREE_SPP1_CODE';
   v_old_value := :OLD.PREV_TREE_SPP1_CODE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_TREE_SPP2_CODE';
   v_old_value := :OLD.PREV_TREE_SPP2_CODE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'APP_ENT_BY_USERID';
   v_old_value := :OLD.APP_ENT_BY_USERID;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'AMENDMENT_IND';
   v_old_value := :OLD.AMENDMENT_IND;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'RESULTS_SUBMISSION_ID';
   v_old_value := :OLD.RESULTS_SUBMISSION_ID;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'DIST_ADMIN_ZONE';
   v_old_value := :OLD.DIST_ADMIN_ZONE;

   IF v_old_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

 -------------------------
 --INSERTING
 -------------------------
 ELSIF INSERTING THEN
   v_business_identifier := 'Opening Id: '||TO_CHAR(:NEW.opening_id);

   v_column_name := 'OPENING_ID';
   v_new_value := :NEW.OPENING_ID;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'GEO_DISTRICT_NO';
   v_new_value := :NEW.GEO_DISTRICT_NO;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'ADMIN_DISTRICT_NO';
   v_new_value := :NEW.ADMIN_DISTRICT_NO;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'MAPSHEET_GRID';
   v_new_value := :NEW.MAPSHEET_GRID;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'MAPSHEET_LETTER';
   v_new_value := :NEW.MAPSHEET_LETTER;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'MAPSHEET_SQUARE';
   v_new_value := :NEW.MAPSHEET_SQUARE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'MAPSHEET_QUAD';
   v_new_value := :NEW.MAPSHEET_QUAD;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'MAPSHEET_SUB_QUAD';
   v_new_value := :NEW.MAPSHEET_SUB_QUAD;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'OPENING_NUMBER';
   v_new_value := :NEW.OPENING_NUMBER;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'OPENING_LOCN_NAME';
   v_new_value := :NEW.OPENING_LOCN_NAME;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'OPEN_CATEGORY_CODE';
   v_new_value := :NEW.OPEN_CATEGORY_CODE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'LICENSEE_OPENING_ID';
   v_new_value := :NEW.LICENSEE_OPENING_ID;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'TSB_NUMBER_CODE';
   v_new_value := :NEW.TSB_NUMBER_CODE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'OPENING_STATUS_CODE';
   v_new_value := :NEW.OPENING_STATUS_CODE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'MAX_ALLOW_PERMNT_ACCESS_PCT';
   v_new_value := :NEW.MAX_ALLOW_PERMNT_ACCESS_PCT;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_AGE_CLASS_CODE';
   v_new_value := :NEW.PREV_AGE_CLASS_CODE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_SITE_INDEX';
   v_new_value := :NEW.PREV_SITE_INDEX;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_SITE_INDEX_SOURCE_CODE';
   v_new_value := :NEW.PREV_SITE_INDEX_SOURCE_CODE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_HEIGHT_CLASS_CODE';
   v_new_value := :NEW.PREV_HEIGHT_CLASS_CODE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_SITE_CLASS_CODE';
   v_new_value := :NEW.PREV_SITE_CLASS_CODE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_STOCKING_CLASS_CODE';
   v_new_value := :NEW.PREV_STOCKING_CLASS_CODE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_STOCKING_STATUS_CODE';
   v_new_value := :NEW.PREV_STOCKING_STATUS_CODE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_TREE_SPP1_CODE';
   v_new_value := :NEW.PREV_TREE_SPP1_CODE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'PREV_TREE_SPP2_CODE';
   v_new_value := :NEW.PREV_TREE_SPP2_CODE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'APP_ENT_BY_USERID';
   v_new_value := :NEW.APP_ENT_BY_USERID;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'APPROVE_DATE';
   v_new_value := Sil_Date_Conversion.convert_to_char_trunc_time(:NEW.APPROVE_DATE);
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'AMENDMENT_IND';
   v_new_value := :NEW.AMENDMENT_IND;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                       , p_column_name => v_column_name
                                       , p_business_identifier => v_business_identifier
                                       , p_old_value => v_old_value
                                       , p_new_value => v_new_value);
   END IF;

   v_column_name := 'RESULTS_SUBMISSION_ID';
   v_new_value := :NEW.RESULTS_SUBMISSION_ID;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

   v_column_name := 'DIST_ADMIN_ZONE';
   v_new_value := :NEW.DIST_ADMIN_ZONE;
   v_old_value := NULL;

   IF v_new_value IS NOT NULL THEN
     Results_Audit.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                      , p_column_name => v_column_name
                                      , p_business_identifier => v_business_identifier
                                      , p_old_value => v_old_value
                                      , p_new_value => v_new_value);
   END IF;

  END IF;
END IF;

  IF UPDATING then
     -- call to RESULTS_ILRR_ACQR_PLUGIN_PKG to construct update event queue entry
     Results_Ilrr_Acqr_Plugin_Pkg.MAINLINE('Update',:OLD.opening_id, c_op_scope_interest);
  ELSIF DELETING THEN
     -- call to RESULTS_ILRR_ACQR_PLUGIN_PKG to construct delete event queue entry
     Results_Ilrr_Acqr_Plugin_Pkg.MAINLINE('Delete',:OLD.opening_id, c_op_scope_interest);
  ELSIF INSERTING THEN
     -- call to RESULTS_ILRR_ACQR_PLUGIN_PKG to construct insert event queue entry
     Results_Ilrr_Acqr_Plugin_Pkg.MAINLINE('Insert',:NEW.opening_id, c_op_scope_interest);
  END IF;

END RESULTS_O_AR_IUD_TRG;




/
ALTER TRIGGER "THE"."RESULTS_O_AR_IUD_TRG" ENABLE;
