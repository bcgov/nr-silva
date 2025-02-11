
  CREATE OR REPLACE EDITIONABLE TRIGGER "THE"."RESULTS_SSU_AR_IUD_TRG" 
/******************************************************************************
   Trigger:   RESULTS_SSU_AR_IUD_TRG
   Purpose: This trigger will call the  RESULTS_AUDIT.INSERT_AUDIT_DETAIL
            procedure when the status of the opening or standards regime is
            either  'APP' or 'AMD'. The procedure inserts data into the
            RESULTS_AUDIT_DETAIL table based on the data that was passed in.
   Revision History
   Person               Date       Comments
   -----------------    ---------  --------------------------------
   wcound (Pangaea)     2003-03-07 Created
   wcound               2003-03-27 Added NVL to Update to capture null values.
   R.A.Robb             2004-05-13 CR#46 - update auditing
******************************************************************************/
AFTER INSERT OR UPDATE OR DELETE ON STOCKING_STANDARD_UNIT FOR EACH ROW
DECLARE
 v_opening_id             RESULTS_AUDIT_EVENT.OPENING_ID%TYPE;
 v_results_audit_event_id RESULTS_AUDIT_EVENT.RESULTS_AUDIT_EVENT_ID%type;
 v_update_userid          RESULTS_AUDIT_DETAIL.ENTRY_USERID%TYPE;
 v_status                 VARCHAR2(3);
 v_table_name             RESULTS_AUDIT_DETAIL.TABLE_NAME%TYPE;
 v_column_name            RESULTS_AUDIT_DETAIL.COLUMN_NAME%TYPE;
 v_old_value              RESULTS_AUDIT_DETAIL.OLD_VALUE%TYPE;
 v_new_value              RESULTS_AUDIT_DETAIL.NEW_VALUE%TYPE;
 v_business_identifier    RESULTS_AUDIT_DETAIL.BUSINESS_IDENTIFIER%TYPE;
 v_audit_enabled_ind      VARCHAR2(1);
 BEGIN
 v_table_name := 'STOCKING_STANDARD_UNIT';
 v_opening_id := RESULTS_GLOBALS.GET_OPENING_ID;
 v_status := RESULTS_GLOBALS.GET_RESULTS_AUDIT_STATUS;
 v_audit_enabled_ind := RESULTS_GLOBALS.GET_AUDITING_ENABLED_IND;
 v_business_identifier := 'Standards Unit '||
                          NVL(:NEW.standards_unit_id,:OLD.standards_unit_id);

IF v_audit_enabled_ind = 'Y'
AND results_opening.opening_can_be_updated(v_status) THEN
 --------------------------------------
 -- UPDATING
 --------------------------------------
IF UPDATING THEN

IF :NEW.STOCKING_STANDARD_UNIT_ID != :OLD.STOCKING_STANDARD_UNIT_ID THEN
  v_column_name := 'STOCKING_STANDARD_UNIT_ID';
  v_old_value := :OLD.STOCKING_STANDARD_UNIT_ID;
  v_new_value := :NEW.STOCKING_STANDARD_UNIT_ID;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
END IF;
IF :NEW.OPENING_ID != :OLD.OPENING_ID THEN
  v_column_name := 'OPENING_ID';
  v_old_value := :OLD.OPENING_ID;
  v_new_value := :NEW.OPENING_ID;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
END IF;
IF :NEW.STANDARDS_UNIT_ID != :OLD.STANDARDS_UNIT_ID THEN
  v_column_name := 'STANDARDS_UNIT_ID';
  v_old_value := :OLD.STANDARDS_UNIT_ID;
  v_new_value := :NEW.STANDARDS_UNIT_ID;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
END IF;
IF NVL(:NEW.STANDARDS_REGIME_ID,0) != NVL(:OLD.STANDARDS_REGIME_ID,0) THEN
  v_column_name := 'STANDARDS_REGIME_ID';
  v_old_value := :OLD.STANDARDS_REGIME_ID;
  v_new_value := :NEW.STANDARDS_REGIME_ID;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
END IF;
IF NVL(:NEW.NET_AREA,0) != NVL(:OLD.NET_AREA,0) THEN
  v_column_name := 'NET_AREA';
  v_old_value := :OLD.NET_AREA;
  v_new_value := :NEW.NET_AREA;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
END IF;
IF NVL(:NEW.MAX_ALLOW_SOIL_DISTURBANCE_PCT,0) != NVL(:OLD.MAX_ALLOW_SOIL_DISTURBANCE_PCT,0) THEN
  v_column_name := 'MAX_ALLOW_SOIL_DISTURBANCE_PCT';
  v_old_value := :OLD.MAX_ALLOW_SOIL_DISTURBANCE_PCT;
  v_new_value := :NEW.MAX_ALLOW_SOIL_DISTURBANCE_PCT;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
END IF;
IF NVL(:NEW.VARIANCE_IND,0) != NVL(:OLD.VARIANCE_IND,0) THEN
  v_column_name := 'VARIANCE_IND';
  v_old_value := :OLD.VARIANCE_IND;
  v_new_value := :NEW.VARIANCE_IND;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
END IF;
 --------------------------------------
 -- DELETING
 --------------------------------------
ELSIF DELETING THEN
  v_column_name := 'ENTRY_TIMESTAMP';
  v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.ENTRY_TIMESTAMP);
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'UPDATE_USERID';
  v_old_value := :OLD.UPDATE_USERID;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'UPDATE_TIMESTAMP';
  v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.UPDATE_TIMESTAMP);
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;

  v_column_name := 'STOCKING_STANDARD_UNIT_ID';
  v_old_value := :OLD.STOCKING_STANDARD_UNIT_ID;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'OPENING_ID';
  v_old_value := :OLD.OPENING_ID;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'STANDARDS_UNIT_ID';
  v_old_value := :OLD.STANDARDS_UNIT_ID;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'STANDARDS_REGIME_ID';
  v_old_value := :OLD.STANDARDS_REGIME_ID;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'NET_AREA';
  v_old_value := :OLD.NET_AREA;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'MAX_ALLOW_SOIL_DISTURBANCE_PCT';
  v_old_value := :OLD.MAX_ALLOW_SOIL_DISTURBANCE_PCT;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'VARIANCE_IND';
  v_old_value := :OLD.VARIANCE_IND;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;

  v_column_name := 'ENTRY_USERID';
  v_old_value := :OLD.ENTRY_USERID;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;


 --------------------------------------
 -- INSERTING
 --------------------------------------
ELSIF INSERTING THEN
  v_column_name := 'STOCKING_STANDARD_UNIT_ID';
  v_new_value := :NEW.STOCKING_STANDARD_UNIT_ID;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'OPENING_ID';
  v_new_value := :NEW.OPENING_ID;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'STANDARDS_UNIT_ID';
  v_new_value := :NEW.STANDARDS_UNIT_ID;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'STANDARDS_REGIME_ID';
  v_new_value := :NEW.STANDARDS_REGIME_ID;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'NET_AREA';
  v_new_value := :NEW.NET_AREA;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'MAX_ALLOW_SOIL_DISTURBANCE_PCT';
  v_new_value := :NEW.MAX_ALLOW_SOIL_DISTURBANCE_PCT;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'VARIANCE_IND';
  v_new_value := :NEW.VARIANCE_IND;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  END IF;

  END IF;

END RESULTS_SSU_AR_IUD_TRG;




/
ALTER TRIGGER "THE"."RESULTS_SSU_AR_IUD_TRG" ENABLE;
