
  CREATE OR REPLACE EDITIONABLE TRIGGER "THE"."RESULTS_SSUA_AR_IUD_TRG" 
/******************************************************************************
   Trigger: RESULTS_SSUA_AR_IUD_TRG
   Purpose: This trigger will call the  RESULTS_AUDIT.INSERT_AUDIT_DETAIL
            procedure when the status of the opening or standards regime is
            either  'APP' or 'AMD'. The procedure inserts data into the
            RESULTS_AUDIT_DETAIL table based on the data that was passed in.
   Revision History
   Person               Date       Comments
   -----------------    ---------  --------------------------------
   R.A.Robb             2004-05-13 CR#46 - cloned from RESULTS_SSU_AR_IUD_TRG
******************************************************************************/
AFTER INSERT OR UPDATE OR DELETE ON STOCKING_STANDARD_UNIT_AMD FOR EACH ROW
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
 v_table_name := 'STOCKING_STANDARD_UNIT_AMD';
 v_opening_id := RESULTS_GLOBALS.GET_OPENING_ID;
 v_status := RESULTS_GLOBALS.GET_RESULTS_AUDIT_STATUS;
 v_audit_enabled_ind := RESULTS_GLOBALS.GET_AUDITING_ENABLED_IND;
 v_business_identifier := 'Standards Unit '||
                          NVL(:NEW.standards_unit_id,:OLD.standards_unit_id);

IF v_status = 'AMD'
AND v_audit_enabled_ind = 'Y' THEN

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
IF NVL(:NEW.REGEN_DELAY_OFFSET_YRS,0) != NVL(:OLD.REGEN_DELAY_OFFSET_YRS,0) THEN
  v_column_name := 'REGEN_DELAY_OFFSET_YRS';
  v_old_value := :OLD.REGEN_DELAY_OFFSET_YRS;
  v_new_value := :NEW.REGEN_DELAY_OFFSET_YRS;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
END IF;
IF NVL(:NEW.REGEN_OBLIGATION_IND,0) != NVL(:OLD.REGEN_OBLIGATION_IND,0) THEN
  v_column_name := 'REGEN_OBLIGATION_IND';
  v_old_value := :OLD.REGEN_OBLIGATION_IND;
  v_new_value := :NEW.REGEN_OBLIGATION_IND;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
END IF;
IF NVL(:NEW.NO_REGEN_EARLY_OFFSET_YRS,0) != NVL(:OLD.NO_REGEN_EARLY_OFFSET_YRS,0) THEN
  v_column_name := 'NO_REGEN_EARLY_OFFSET_YRS';
  v_old_value := :OLD.NO_REGEN_EARLY_OFFSET_YRS;
  v_new_value := :NEW.NO_REGEN_EARLY_OFFSET_YRS;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
END IF;
IF NVL(:NEW.NO_REGEN_LATE_OFFSET_YRS,0) != NVL(:OLD.NO_REGEN_LATE_OFFSET_YRS,0) THEN
  v_column_name := 'NO_REGEN_LATE_OFFSET_YRS';
  v_old_value := :OLD.NO_REGEN_LATE_OFFSET_YRS;
  v_new_value := :NEW.NO_REGEN_LATE_OFFSET_YRS;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
END IF;
IF NVL(:NEW.FREE_GROWING_EARLY_OFFSET_YRS,0) != NVL(:OLD.FREE_GROWING_EARLY_OFFSET_YRS,0) THEN
  v_column_name := 'FREE_GROWING_EARLY_OFFSET_YRS';
  v_old_value := :OLD.FREE_GROWING_EARLY_OFFSET_YRS;
  v_new_value := :NEW.FREE_GROWING_EARLY_OFFSET_YRS;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
END IF;
IF NVL(:NEW.FREE_GROWING_LATE_OFFSET_YRS,0) != NVL(:OLD.FREE_GROWING_LATE_OFFSET_YRS,0) THEN
  v_column_name := 'FREE_GROWING_LATE_OFFSET_YRS';
  v_old_value := :OLD.FREE_GROWING_LATE_OFFSET_YRS;
  v_new_value := :NEW.FREE_GROWING_LATE_OFFSET_YRS;
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
  v_column_name := 'REGEN_DELAY_OFFSET_YRS';
  v_old_value := :OLD.REGEN_DELAY_OFFSET_YRS;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'REGEN_OBLIGATION_IND';
  v_old_value := :OLD.REGEN_OBLIGATION_IND;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'NO_REGEN_EARLY_OFFSET_YRS';
  v_old_value := :OLD.NO_REGEN_EARLY_OFFSET_YRS;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'NO_REGEN_LATE_OFFSET_YRS';
  v_old_value := :OLD.NO_REGEN_LATE_OFFSET_YRS;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'FREE_GROWING_EARLY_OFFSET_YRS';
  v_old_value := :OLD.FREE_GROWING_EARLY_OFFSET_YRS;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'FREE_GROWING_LATE_OFFSET_YRS';
  v_old_value := :OLD.FREE_GROWING_LATE_OFFSET_YRS;
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
  v_column_name := 'REGEN_DELAY_OFFSET_YRS';
  v_new_value := :NEW.REGEN_DELAY_OFFSET_YRS;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'REGEN_OBLIGATION_IND';
  v_new_value := :NEW.REGEN_OBLIGATION_IND;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'NO_REGEN_EARLY_OFFSET_YRS';
  v_new_value := :NEW.NO_REGEN_EARLY_OFFSET_YRS;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'NO_REGEN_LATE_OFFSET_YRS';
  v_new_value := :NEW.NO_REGEN_LATE_OFFSET_YRS;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'FREE_GROWING_EARLY_OFFSET_YRS';
  v_new_value := :NEW.FREE_GROWING_EARLY_OFFSET_YRS;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name    => v_table_name
                                   , p_column_name   => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value     => v_old_value
                                   , p_new_value     => v_new_value);
   END IF;
  v_column_name := 'FREE_GROWING_LATE_OFFSET_YRS';
  v_new_value := :NEW.FREE_GROWING_LATE_OFFSET_YRS;
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

END RESULTS_SSUA_AR_IUD_TRG;




/
ALTER TRIGGER "THE"."RESULTS_SSUA_AR_IUD_TRG" ENABLE;

  CREATE OR REPLACE EDITIONABLE TRIGGER "THE"."RESULTS_SM_AR_IUD_TRG" 
/******************************************************************************
   Trigger:  RESULTS_SM_AR_IUD_TRG
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
   R.A.Robb             2006-03-18 CR#127 - update auditing
   x.chen               2009-01-07 fixed the bug that didn't consider the
                                   stocking_standard_unit_amd table
   xchen                2009-02-03 the 'selection' will throw NO_DATA_FOUND for
                                   screens other than 311/312 because for other
                                   screens the 'status' will not have anything to
                                   do with the 'stocking_standard_unit(_amd) table
                                   it's always stocking_standard_unit table.
******************************************************************************/
AFTER INSERT OR UPDATE OR DELETE
  ON stocking_milestone
  FOR EACH ROW
DECLARE
  v_opening_id                       results_audit_event.opening_id%TYPE;
  v_results_audit_event_id           results_audit_event.results_audit_event_id%TYPE;
  v_update_userid                    results_audit_detail.entry_userid%TYPE;
  v_status                           VARCHAR2(3);
  v_table_name                       results_audit_detail.table_name%TYPE;
  v_column_name                      results_audit_detail.column_name%TYPE;
  v_old_value                        results_audit_detail.old_value%TYPE;
  v_new_value                        results_audit_detail.new_value%TYPE;
  v_business_identifier              results_audit_detail.business_identifier%TYPE;
  v_audit_enabled_ind                VARCHAR2(1);
  v_dummy_date              CONSTANT DATE := TO_DATE('1888-01-01', 'YYYY-MM-DD');
BEGIN
  v_table_name := 'STOCKING_MILESTONE';
  v_opening_id := results_globals.get_opening_id;
  v_status := results_globals.get_results_audit_status;
  v_audit_enabled_ind := results_globals.get_auditing_enabled_ind;

  IF v_audit_enabled_ind = 'Y'
     AND results_opening.opening_can_be_updated(v_status) THEN

    begin
      -- New logic:
      -- Base one the opening status code, we will consider both
      -- 'stocking_standard_unit' and 'stocking_standard_unit_amd'
      if(v_status ='AMD') then
          SELECT    'Standards Unit '
               || standards_unit_id
               || ' - '
               || NVL(:NEW.silv_milestone_type_code, :OLD.silv_milestone_type_code)
            INTO v_business_identifier
            FROM stocking_standard_unit_amd
          WHERE stocking_standard_unit_id =
                                      NVL(:NEW.stocking_standard_unit_id
                                        , :OLD.stocking_standard_unit_id);
      else
          SELECT    'Standards Unit '
               || standards_unit_id
               || ' - '
               || NVL(:NEW.silv_milestone_type_code, :OLD.silv_milestone_type_code)
            INTO v_business_identifier
            FROM stocking_standard_unit
          WHERE stocking_standard_unit_id =
                                      NVL(:NEW.stocking_standard_unit_id
                                        , :OLD.stocking_standard_unit_id);
      end if;

      exception

        when NO_DATA_FOUND then
          -- this is a kind of unexpected condition. Not sure what's going on in the
	  -- DB. It looks like other than 311/312 screen,the data saved in
	  -- stocking_standard_unit table  no matter what the tombstone status is ??.
	  --if(v_tombstone_status = 'AMD') then
	    SELECT  'Standards Unit '
	             || standards_unit_id
	             || ' - '
	             || NVL(:NEW.silv_milestone_type_code, :OLD.silv_milestone_type_code)
	    INTO v_business_identifier
	    FROM stocking_standard_unit
	    WHERE stocking_standard_unit_id =
	                                    NVL(:NEW.stocking_standard_unit_id
	                                      , :OLD.stocking_standard_unit_id);
          --end if;
    end;

    --------------------------------------
    -- UPDATING
    --------------------------------------
    IF UPDATING THEN
      IF :NEW.stocking_standard_unit_id != :OLD.stocking_standard_unit_id THEN
        v_column_name := 'STOCKING_STANDARD_UNIT_ID';
        v_old_value := :OLD.stocking_standard_unit_id;
        v_new_value := :NEW.stocking_standard_unit_id;
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF :NEW.silv_milestone_type_code != :OLD.silv_milestone_type_code THEN
        v_column_name := 'SILV_MILESTONE_TYPE_CODE';
        v_old_value := :OLD.silv_milestone_type_code;
        v_new_value := :NEW.silv_milestone_type_code;
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.results_submission_id, 0) != NVL(:OLD.results_submission_id, 0) THEN
        v_column_name := 'RESULTS_SUBMISSION_ID';
        v_old_value := :OLD.results_submission_id;
        v_new_value := :NEW.results_submission_id;
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(pkg_sil_date_conversion.convert_to_char(:NEW.declared_date, NULL, 'Y'), v_dummy_date) !=
           NVL(pkg_sil_date_conversion.convert_to_char(:OLD.declared_date, NULL, 'Y'), v_dummy_date) THEN
        v_column_name := 'DECLARED_DATE';
        v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.declared_date);
        v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.declared_date);
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.declared_userid, 0) != NVL(:OLD.declared_userid, 0) THEN
        v_column_name := 'DECLARED_USERID';
        v_old_value := :OLD.declared_userid;
        v_new_value := :NEW.declared_userid;
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.declare_ind, 0) != NVL(:OLD.declare_ind, 0) THEN
        v_column_name := 'DECLARE_IND';
        v_old_value := :OLD.declare_ind;
        v_new_value := :NEW.declare_ind;
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.early_offset_years, 0) != NVL(:OLD.early_offset_years, 0) THEN
        v_column_name := 'EARLY_OFFSET_YEARS';
        v_old_value := :OLD.early_offset_years;
        v_new_value := :NEW.early_offset_years;
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.late_offset_years, 0) != NVL(:OLD.late_offset_years, 0) THEN
        v_column_name := 'LATE_OFFSET_YEARS';
        v_old_value := :OLD.late_offset_years;
        v_new_value := :NEW.late_offset_years;
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(pkg_sil_date_conversion.convert_to_char(:NEW.due_early_date, NULL, 'Y'), v_dummy_date) !=
           NVL(pkg_sil_date_conversion.convert_to_char(:OLD.due_early_date, NULL, 'Y')
             , v_dummy_date) THEN
        v_column_name := 'DUE_EARLY_DATE';
        v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.due_early_date);
        v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.due_early_date);
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(pkg_sil_date_conversion.convert_to_char(:NEW.due_late_date, NULL, 'Y'), v_dummy_date) !=
           NVL(pkg_sil_date_conversion.convert_to_char(:OLD.due_late_date, NULL, 'Y'), v_dummy_date) THEN
        v_column_name := 'DUE_LATE_DATE';
        v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.due_late_date);
        v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.due_late_date);
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(pkg_sil_date_conversion.convert_to_char(:NEW.declaration_submitted_date, NULL, 'Y')
           , v_dummy_date) !=
           NVL(pkg_sil_date_conversion.convert_to_char(:OLD.declaration_submitted_date, NULL, 'Y')
             , v_dummy_date) THEN
        v_column_name := 'DECLARATION_SUBMITTED_DATE';
        v_old_value :=
                    sil_date_conversion.convert_to_char_trunc_time(:OLD.declaration_submitted_date);
        v_new_value :=
                    sil_date_conversion.convert_to_char_trunc_time(:NEW.declaration_submitted_date);
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;
    --------------------------------------
    -- DELETING
    --------------------------------------
    ELSIF DELETING THEN
      v_column_name := 'STOCKING_STANDARD_UNIT_ID';
      v_old_value := :OLD.stocking_standard_unit_id;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_MILESTONE_TYPE_CODE';
      v_old_value := :OLD.silv_milestone_type_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'RESULTS_SUBMISSION_ID';
      v_old_value := :OLD.results_submission_id;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DECLARED_DATE';
      v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.declared_date);
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DECLARED_USERID';
      v_old_value := :OLD.declared_userid;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DECLARE_IND';
      v_old_value := :OLD.declare_ind;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'EARLY_OFFSET_YEARS';
      v_old_value := :OLD.early_offset_years;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'LATE_OFFSET_YEARS';
      v_old_value := :OLD.late_offset_years;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DUE_EARLY_DATE';
      v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.due_early_date);
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DUE_LATE_DATE';
      v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.due_late_date);
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ENTRY_USERID';
      v_old_value := :OLD.entry_userid;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ENTRY_TIMESTAMP';
      v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.entry_timestamp);
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'UPDATE_USERID';
      v_old_value := :OLD.update_userid;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'UPDATE_TIMESTAMP';
      v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.update_timestamp);
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DECLARATION_SUBMITTED_DATE';
      v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.declaration_submitted_date);
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;
    --------------------------------------
    -- INSERTING
    --------------------------------------
    ELSIF INSERTING THEN
      v_column_name := 'STOCKING_STANDARD_UNIT_ID';
      v_new_value := :NEW.stocking_standard_unit_id;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_MILESTONE_TYPE_CODE';
      v_new_value := :NEW.silv_milestone_type_code;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'RESULTS_SUBMISSION_ID';
      v_new_value := :NEW.results_submission_id;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DECLARED_DATE';
      v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.declared_date);
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DECLARED_USERID';
      v_new_value := :NEW.declared_userid;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DECLARE_IND';
      v_new_value := :NEW.declare_ind;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'EARLY_OFFSET_YEARS';
      v_new_value := :NEW.early_offset_years;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'LATE_OFFSET_YEARS';
      v_new_value := :NEW.late_offset_years;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DUE_EARLY_DATE';
      v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.due_early_date);
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DUE_LATE_DATE';
      v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.due_late_date);
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ENTRY_USERID';
      v_new_value := :NEW.entry_userid;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ENTRY_TIMESTAMP';
      v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.entry_timestamp);
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DECLARATION_SUBMITTED_DATE';
      v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.declaration_submitted_date);
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail(p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;
    END IF;
  END IF;
END results_sm_ar_iud_trg;




/
ALTER TRIGGER "THE"."RESULTS_SM_AR_IUD_TRG" ENABLE;
