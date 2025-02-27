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


  CREATE OR REPLACE EDITIONABLE TRIGGER "THE"."RESULTS_CBOA_AR_IUD_TRG"
/******************************************************************************
   Trigger: RESULTS_CBOA_AR_IUD_TRG
   Purpose: This trigger will call the  RESULTS_AUDIT.INSERT_AUDIT_DETAIL
            procedure when the status of the opening or standards regime is
            either  'APP' or 'AMD'. The procedure inserts data into the
            RESULTS_AUDIT_DETAIL table based on the data that was passed in.
   Revision History
   Person               Date       Comments
   -----------------    ---------  --------------------------------
   wcound (Pangaea)     2003-03-05 Created
   wcound               2003-03-27 Added NVL to Update to capture null values
   R.A.Robb             2004-05-14 CR#46 - updated auditing
******************************************************************************/
AFTER INSERT OR UPDATE OR DELETE ON CUT_BLOCK_OPEN_ADMIN FOR EACH ROW
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
 v_table_name := 'CUT_BLOCK_OPEN_ADMIN';
 v_opening_id := RESULTS_GLOBALS.GET_OPENING_ID;
 v_status := RESULTS_GLOBALS.GET_RESULTS_AUDIT_STATUS;
 v_audit_enabled_ind := RESULTS_GLOBALS.GET_AUDITING_ENABLED_IND;

IF v_audit_enabled_ind = 'Y'
AND results_opening.opening_can_be_updated(v_status) THEN
 -----------------------------------
 --UPDATING
 -----------------------------------
 IF UPDATING THEN
   v_business_identifier := 'Opening Id: '||TO_CHAR(:NEW.opening_id);


IF NVL(:NEW.FOREST_FILE_ID,0) != NVL(:OLD.FOREST_FILE_ID,0) THEN
  v_column_name := 'FOREST_FILE_ID';
  v_old_value := :OLD.FOREST_FILE_ID;
  v_new_value := :NEW.FOREST_FILE_ID;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
IF NVL(:NEW.TIMBER_MARK,0) != NVL(:OLD.TIMBER_MARK,0) THEN
  v_column_name := 'TIMBER_MARK';
  v_old_value := :OLD.TIMBER_MARK;
  v_new_value := :NEW.TIMBER_MARK;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
IF NVL(:NEW.CUT_BLOCK_ID,0) != NVL(:OLD.CUT_BLOCK_ID,0) THEN
  v_column_name := 'CUT_BLOCK_ID';
  v_old_value := :OLD.CUT_BLOCK_ID;
  v_new_value := :NEW.CUT_BLOCK_ID;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
IF NVL(:NEW.CUTTING_PERMIT_ID,0) != NVL(:OLD.CUTTING_PERMIT_ID,0) THEN
  v_column_name := 'CUTTING_PERMIT_ID';
  v_old_value := :OLD.CUTTING_PERMIT_ID;
  v_new_value := :NEW.CUTTING_PERMIT_ID;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
IF NVL(:NEW.DISTURBANCE_GROSS_AREA,0) != NVL(:OLD.DISTURBANCE_GROSS_AREA,0) THEN
  v_column_name := 'DISTURBANCE_GROSS_AREA';
  v_old_value := :OLD.DISTURBANCE_GROSS_AREA;
  v_new_value := :NEW.DISTURBANCE_GROSS_AREA;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
IF NVL(PKG_SIL_DATE_CONVERSION.CONVERT_TO_CHAR(:NEW.DISTURBANCE_START_DATE,NULL,'Y'),0)
!= NVL(PKG_SIL_DATE_CONVERSION.CONVERT_TO_CHAR(:OLD.DISTURBANCE_START_DATE,NULL,'Y'),0) THEN
  v_column_name := 'DISTURBANCE_START_DATE';
  v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.DISTURBANCE_START_DATE);
  v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.DISTURBANCE_START_DATE);
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
IF NVL(PKG_SIL_DATE_CONVERSION.CONVERT_TO_CHAR(:NEW.DISTURBANCE_END_DATE,NULL,'Y'),0)
 != NVL(PKG_SIL_DATE_CONVERSION.CONVERT_TO_CHAR(:OLD.DISTURBANCE_END_DATE,NULL,'Y'),0) THEN
  v_column_name := 'DISTURBANCE_END_DATE';
  v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.DISTURBANCE_END_DATE);
  v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.DISTURBANCE_END_DATE);
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
IF NVL(:NEW.OPENING_ID,0) != NVL(:OLD.OPENING_ID,0) THEN
  v_column_name := 'OPENING_ID';
  v_old_value := :OLD.OPENING_ID;
  v_new_value := :NEW.OPENING_ID;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
IF NVL(:NEW.OPENING_GROSS_AREA,0) != NVL(:OLD.OPENING_GROSS_AREA,0) THEN
  v_column_name := 'OPENING_GROSS_AREA';
  v_old_value := :OLD.OPENING_GROSS_AREA;
  v_new_value := :NEW.OPENING_GROSS_AREA;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
IF NVL(PKG_SIL_DATE_CONVERSION.CONVERT_TO_CHAR(:NEW.PLANNED_HARVEST_DATE,NULL,'Y'),0)
!= NVL(PKG_SIL_DATE_CONVERSION.CONVERT_TO_CHAR(:OLD.PLANNED_HARVEST_DATE,NULL,'Y'),0) THEN
  v_column_name := 'PLANNED_HARVEST_DATE';
  v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.PLANNED_HARVEST_DATE);
  v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.PLANNED_HARVEST_DATE);
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
IF NVL(:NEW.PLANNED_GROSS_BLOCK_AREA,0) != NVL(:OLD.PLANNED_GROSS_BLOCK_AREA,0) THEN
  v_column_name := 'PLANNED_GROSS_BLOCK_AREA';
  v_old_value := :OLD.PLANNED_GROSS_BLOCK_AREA;
  v_new_value := :NEW.PLANNED_GROSS_BLOCK_AREA;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
IF NVL(:NEW.PLANNED_NET_BLOCK_AREA,0) != NVL(:OLD.PLANNED_NET_BLOCK_AREA,0) THEN
  v_column_name := 'PLANNED_NET_BLOCK_AREA';
  v_old_value := :OLD.PLANNED_NET_BLOCK_AREA;
  v_new_value := :NEW.PLANNED_NET_BLOCK_AREA;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
IF NVL(:NEW.OPENING_PRIME_LICENCE_IND,0) != NVL(:OLD.OPENING_PRIME_LICENCE_IND,0) THEN
  v_column_name := 'OPENING_PRIME_LICENCE_IND';
  v_old_value := :OLD.OPENING_PRIME_LICENCE_IND;
  v_new_value := :NEW.OPENING_PRIME_LICENCE_IND;
  RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                   , p_column_name => v_column_name
                                   , p_business_identifier => v_business_identifier
                                   , p_old_value => v_old_value
                                   , p_new_value => v_new_value);
END IF;
 -----------------------------------
 --DELETING
 -----------------------------------
ELSIF DELETING THEN
  v_business_identifier := 'Opening Id: '||TO_CHAR(:OLD.opening_id);


  v_column_name := 'FOREST_FILE_ID';
  v_old_value := :OLD.FOREST_FILE_ID;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'TIMBER_MARK';
  v_old_value := :OLD.TIMBER_MARK;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'CUT_BLOCK_ID';
  v_old_value := :OLD.CUT_BLOCK_ID;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'CUTTING_PERMIT_ID';
  v_old_value := :OLD.CUTTING_PERMIT_ID;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'DISTURBANCE_GROSS_AREA';
  v_old_value := :OLD.DISTURBANCE_GROSS_AREA;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'DISTURBANCE_START_DATE';
  v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.DISTURBANCE_START_DATE);
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'DISTURBANCE_END_DATE';
  v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.DISTURBANCE_END_DATE);
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'OPENING_ID';
  v_old_value := :OLD.OPENING_ID;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'OPENING_GROSS_AREA';
  v_old_value := :OLD.OPENING_GROSS_AREA;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'PLANNED_HARVEST_DATE';
  v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.PLANNED_HARVEST_DATE);
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'PLANNED_GROSS_BLOCK_AREA';
  v_old_value := :OLD.PLANNED_GROSS_BLOCK_AREA;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'PLANNED_NET_BLOCK_AREA';
  v_old_value := :OLD.PLANNED_NET_BLOCK_AREA;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'OPENING_PRIME_LICENCE_IND';
  v_old_value := :OLD.OPENING_PRIME_LICENCE_IND;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'ENTRY_USERID';
  v_old_value := :OLD.ENTRY_USERID;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'ENTRY_TIMESTAMP';
  v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.ENTRY_TIMESTAMP);
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'UPDATE_USERID';
  v_old_value := :OLD.UPDATE_USERID;
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'UPDATE_TIMESTAMP';
  v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.UPDATE_TIMESTAMP);
  v_new_value := NULL;
 IF v_old_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;



 -----------------------------------
 --INSERTING
 -----------------------------------
ELSIF INSERTING THEN
  v_business_identifier := 'Opening Id: '||TO_CHAR(:NEW.opening_id);


  v_column_name := 'FOREST_FILE_ID';
  v_new_value := :NEW.FOREST_FILE_ID;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'TIMBER_MARK';
  v_new_value := :NEW.TIMBER_MARK;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'CUT_BLOCK_ID';
  v_new_value := :NEW.CUT_BLOCK_ID;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'CUTTING_PERMIT_ID';
  v_new_value := :NEW.CUTTING_PERMIT_ID;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'DISTURBANCE_GROSS_AREA';
  v_new_value := :NEW.DISTURBANCE_GROSS_AREA;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'DISTURBANCE_START_DATE';
  v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.DISTURBANCE_START_DATE);
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'DISTURBANCE_END_DATE';
  v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.DISTURBANCE_END_DATE);
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'OPENING_ID';
  v_new_value := :NEW.OPENING_ID;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'OPENING_GROSS_AREA';
  v_new_value := :NEW.OPENING_GROSS_AREA;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'PLANNED_HARVEST_DATE';
  v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.PLANNED_HARVEST_DATE);
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'PLANNED_GROSS_BLOCK_AREA';
  v_new_value := :NEW.PLANNED_GROSS_BLOCK_AREA;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'PLANNED_NET_BLOCK_AREA';
  v_new_value := :NEW.PLANNED_NET_BLOCK_AREA;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
   END IF;
  v_column_name := 'OPENING_PRIME_LICENCE_IND';
  v_new_value := :NEW.OPENING_PRIME_LICENCE_IND;
  v_old_value := NULL;
 IF v_new_value IS NOT NULL THEN
   RESULTS_AUDIT.INSERT_AUDIT_DETAIL( p_table_name => v_table_name
                                     , p_column_name => v_column_name
                                     , p_business_identifier => v_business_identifier
                                     , p_old_value => v_old_value
                                     , p_new_value => v_new_value);
  END IF;
  END IF;

  END IF;

END RESULTS_CBOA_AR_IUD_TRG;

/
ALTER TRIGGER "THE"."RESULTS_CBOA_AR_IUD_TRG" ENABLE;

  CREATE OR REPLACE EDITIONABLE TRIGGER "THE"."FTA_CPFILL_CBOA"
  BEFORE INSERT OR UPDATE
  ON cut_block_open_admin
  FOR EACH ROW
BEGIN
  IF :NEW.forest_file_id IS NOT NULL
     AND :NEW.cut_block_id IS NOT NULL
     AND :NEW.cutting_permit_id IS NULL THEN
    :NEW.cutting_permit_id := ' ';
  END IF;
END fta_cpfill_cboa;

/
ALTER TRIGGER "THE"."FTA_CPFILL_CBOA" ENABLE;


  CREATE OR REPLACE EDITIONABLE TRIGGER "THE"."RESULTS_ATU_AR_IUD_TRG"
/******************************************************************************
   Trigger:   RESULTS_ATU_AR_IUD_TRG
   Purpose: This trigger will call the  RESULTS_AUDIT.INSERT_AUDIT_DETAIL
            procedure when the status of the opening or standards regime is
            either  'APP' or 'AMD'. The procedure inserts data into the
            RESULTS_AUDIT_DETAIL table based on the data that was passed in.
   Revision History
   Person               Date       Comments
   -----------------    ---------  --------------------------------
   wcound (Pangaea)     2003-03-05 Created
   wcound               2003-03-27 Added NVL to Update to capture null values.
   R.A.Robb             2004-05-13 CR#46 - update auditing
   R.A.Robb             2005-03-08 added release 2.0 columns
   G.Saxon              2006-03-21 added release 3.0 columns
******************************************************************************/
AFTER INSERT OR UPDATE OR DELETE
  ON activity_treatment_unit
  FOR EACH ROW
DECLARE
  v_opening_id                       results_audit_event.opening_id%TYPE;
  v_results_audit_event_id           results_audit_event.results_audit_event_id%TYPE;
  v_update_userid                    results_audit_detail.entry_userid%TYPE;
  v_standards_regime_id              results_audit_event.standards_regime_id%TYPE;
  v_status                           VARCHAR2(3);
  v_table_name                       results_audit_detail.table_name%TYPE;
  v_column_name                      results_audit_detail.column_name%TYPE;
  v_old_value                        results_audit_detail.old_value%TYPE;
  v_new_value                        results_audit_detail.new_value%TYPE;
  v_business_identifier              results_audit_detail.business_identifier%TYPE;
  v_audit_enabled_ind                VARCHAR2(1);
  v_comp_date               CONSTANT DATE := TO_DATE('8765-04-03', 'YYYY-MM-DD');
BEGIN
  v_table_name := 'ACTIVITY_TREATMENT_UNIT';
  v_opening_id := results_globals.get_opening_id;
  v_status := results_globals.get_results_audit_status;
  v_audit_enabled_ind := results_globals.get_auditing_enabled_ind;

  IF v_audit_enabled_ind = 'Y'
     AND results_opening.opening_can_be_updated(v_status) THEN
    v_business_identifier :=
         'ATU Id '
      || TO_CHAR(NVL(:NEW.activity_treatment_unit_id, :OLD.activity_treatment_unit_id));

    -----------------------------------
    -- UPDATING
    -----------------------------------
    IF UPDATING THEN
      IF :NEW.activity_treatment_unit_id != :OLD.activity_treatment_unit_id THEN
        v_column_name := 'ACTIVITY_TREATMENT_UNIT_ID';
        v_old_value := :OLD.activity_treatment_unit_id;
        v_new_value := :NEW.activity_treatment_unit_id;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.atu_completion_date, v_comp_date) !=
                                                NVL(:OLD.atu_completion_date, v_comp_date) THEN
        v_column_name := 'ATU_COMPLETION_DATE';
        v_old_value :=
                 sil_date_conversion.convert_to_char_trunc_time(:OLD.atu_completion_date);
        v_new_value :=
                 sil_date_conversion.convert_to_char_trunc_time(:NEW.atu_completion_date);
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.results_ind, '~') != NVL(:OLD.results_ind, '~') THEN
        v_column_name := 'RESULTS_IND';
        v_old_value := :OLD.results_ind;
        v_new_value := :NEW.results_ind;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.fia_project_id, '~') != NVL(:OLD.fia_project_id, '~') THEN
        v_column_name := 'FIA_PROJECT_ID';
        v_old_value := :OLD.fia_project_id;
        v_new_value := :NEW.fia_project_id;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.results_submission_id, 0) != NVL(:OLD.results_submission_id, 0) THEN
        v_column_name := 'RESULTS_SUBMISSION_ID';
        v_old_value := :OLD.results_submission_id;
        v_new_value := :NEW.results_submission_id;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.activity_licensee_id, '~') != NVL(:OLD.activity_licensee_id, '~') THEN
        v_column_name := 'ACTIVITY_LICENSEE_ID';
        v_old_value := :OLD.activity_licensee_id;
        v_new_value := :NEW.activity_licensee_id;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF :NEW.org_unit_no != :OLD.org_unit_no THEN
        v_column_name := 'ORG_UNIT_NO';
        v_old_value := :OLD.org_unit_no;
        v_new_value := :NEW.org_unit_no;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.opening_id, 0) != NVL(:OLD.opening_id, 0) THEN
        v_column_name := 'OPENING_ID';
        v_old_value := :OLD.opening_id;
        v_new_value := :NEW.opening_id;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF :NEW.silv_base_code != :OLD.silv_base_code THEN
        v_column_name := 'SILV_BASE_CODE';
        v_old_value := :OLD.silv_base_code;
        v_new_value := :NEW.silv_base_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.silv_technique_code, '~') != NVL(:OLD.silv_technique_code, '~') THEN
        v_column_name := 'SILV_TECHNIQUE_CODE';
        v_old_value := :OLD.silv_technique_code;
        v_new_value := :NEW.silv_technique_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.silv_method_code, '~') != NVL(:OLD.silv_method_code, '~') THEN
        v_column_name := 'SILV_METHOD_CODE';
        v_old_value := :OLD.silv_method_code;
        v_new_value := :NEW.silv_method_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.silv_objective_code_1, '~') != NVL(:OLD.silv_objective_code_1, '~') THEN
        v_column_name := 'SILV_OBJECTIVE_CODE_1';
        v_old_value := :OLD.silv_objective_code_1;
        v_new_value := :NEW.silv_objective_code_1;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.silv_objective_code_2, '~') != NVL(:OLD.silv_objective_code_2, '~') THEN
        v_column_name := 'SILV_OBJECTIVE_CODE_2';
        v_old_value := :OLD.silv_objective_code_2;
        v_new_value := :NEW.silv_objective_code_2;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.silv_objective_code_3, '~') != NVL(:OLD.silv_objective_code_3, '~') THEN
        v_column_name := 'SILV_OBJECTIVE_CODE_3';
        v_old_value := :OLD.silv_objective_code_3;
        v_new_value := :NEW.silv_objective_code_3;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.silv_fund_srce_code, '~') != NVL(:OLD.silv_fund_srce_code, '~') THEN
        v_column_name := 'SILV_FUND_SRCE_CODE';
        v_old_value := :OLD.silv_fund_srce_code;
        v_new_value := :NEW.silv_fund_srce_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF :NEW.silv_activity_measurement_code != :OLD.silv_activity_measurement_code THEN
        v_column_name := 'SILV_ACTIVITY_MEASUREMENT_CODE';
        v_old_value := :OLD.silv_activity_measurement_code;
        v_new_value := :NEW.silv_activity_measurement_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.treatment_amount, 0) != NVL(:OLD.treatment_amount, 0) THEN
        v_column_name := 'TREATMENT_AMOUNT';
        v_old_value := :OLD.treatment_amount;
        v_new_value := :NEW.treatment_amount;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.act_planted_no, 0) != NVL(:OLD.act_planted_no, 0) THEN
        v_column_name := 'ACT_PLANTED_NO';
        v_old_value := :OLD.act_planted_no;
        v_new_value := :NEW.act_planted_no;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.planned_date, v_comp_date) != NVL(:OLD.planned_date, v_comp_date) THEN
        v_column_name := 'PLANNED_DATE';
        v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.planned_date);
        v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.planned_date);
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.planned_treatment_cost, -99999) !=
                                                   NVL(:OLD.planned_treatment_cost
                                                     , -99999) THEN
        v_column_name := 'PLANNED_TREATMENT_COST';
        v_old_value := :OLD.planned_treatment_cost;
        v_new_value := :NEW.planned_treatment_cost;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.atu_start_date, v_comp_date) != NVL(:OLD.atu_start_date, v_comp_date) THEN
        v_column_name := 'ATU_START_DATE';
        v_old_value := :OLD.atu_start_date;
        v_new_value := :NEW.atu_start_date;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.disturbance_code, '~') != NVL(:OLD.disturbance_code, '~') THEN
        v_column_name := 'DISTURBANCE_CODE';
        v_old_value := :OLD.disturbance_code;
        v_new_value := :NEW.disturbance_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.silv_system_code, '~') != NVL(:OLD.silv_system_code, '~') THEN
        v_column_name := 'SILV_SYSTEM_CODE';
        v_old_value := :OLD.silv_system_code;
        v_new_value := :NEW.silv_system_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.silv_system_variant_code, '~') !=
                                                   NVL(:OLD.silv_system_variant_code, '~') THEN
        v_column_name := 'SILV_SYSTEM_VARIANT_CODE';
        v_old_value := :OLD.silv_system_variant_code;
        v_new_value := :NEW.silv_system_variant_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.silv_cut_phase_code, '~') != NVL(:OLD.silv_cut_phase_code, '~') THEN
        v_column_name := 'SILV_CUT_PHASE_CODE';
        v_old_value := :OLD.silv_cut_phase_code;
        v_new_value := :NEW.silv_cut_phase_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.disturbance_completed_ind, '~') !=
                                                  NVL(:OLD.disturbance_completed_ind, '~') THEN
        v_column_name := 'DISTURBANCE_COMPLETED_IND';
        v_old_value := :OLD.disturbance_completed_ind;
        v_new_value := :NEW.disturbance_completed_ind;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.cut_block_open_admin_id, -99999) !=
                                                  NVL(:OLD.cut_block_open_admin_id
                                                    , -99999) THEN
        v_column_name := 'CUT_BLOCK_OPEN_ADMIN_ID';
        v_old_value := :OLD.cut_block_open_admin_id;
        v_new_value := :NEW.cut_block_open_admin_id;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.actual_treatment_cost, -99999) != NVL(:OLD.actual_treatment_cost
                                                      , -99999) THEN
        v_column_name := 'ACTUAL_TREATMENT_COST';
        v_old_value := :OLD.actual_treatment_cost;
        v_new_value := :NEW.actual_treatment_cost;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.inter_tree_min_distance, -99999) !=
                                                  NVL(:OLD.inter_tree_min_distance
                                                    , -99999) THEN
        v_column_name := 'INTER_TREE_MIN_DISTANCE';
        v_old_value := :OLD.inter_tree_min_distance;
        v_new_value := :NEW.inter_tree_min_distance;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.inter_tree_target_distance, -99999) !=
                                               NVL(:OLD.inter_tree_target_distance
                                                 , -99999) THEN
        v_column_name := 'INTER_TREE_TARGET_DISTANCE';
        v_old_value := :OLD.inter_tree_target_distance;
        v_new_value := :NEW.inter_tree_target_distance;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.inter_tree_variation, -99999) != NVL(:OLD.inter_tree_variation, -99999) THEN
        v_column_name := 'INTER_TREE_VARIATION';
        v_old_value := :OLD.inter_tree_variation;
        v_new_value := :NEW.inter_tree_variation;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.max_trees_per_plot, -99999) != NVL(:OLD.max_trees_per_plot, -99999) THEN
        v_column_name := 'MAX_TREES_PER_PLOT';
        v_old_value := :OLD.max_trees_per_plot;
        v_new_value := :NEW.max_trees_per_plot;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.max_trees_per_ha, -99999) != NVL(:OLD.max_trees_per_ha, -99999) THEN
        v_column_name := 'MAX_TREES_PER_HA';
        v_old_value := :OLD.max_trees_per_ha;
        v_new_value := :NEW.max_trees_per_ha;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.planned_treatment_amount, -99999) !=
                                                 NVL(:OLD.planned_treatment_amount
                                                   , -99999) THEN
        v_column_name := 'PLANNED_TREATMENT_AMOUNT';
        v_old_value := :OLD.planned_treatment_amount;
        v_new_value := :NEW.planned_treatment_amount;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.plan_silv_fund_srce_code, '~') !=
                                                   NVL(:OLD.plan_silv_fund_srce_code, '~') THEN
        v_column_name := 'PLAN_SILV_FUND_SRCE_CODE';
        v_old_value := :OLD.plan_silv_fund_srce_code;
        v_new_value := :NEW.plan_silv_fund_srce_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.plan_silv_method_code, '~') != NVL(:OLD.plan_silv_method_code, '~') THEN
        v_column_name := 'PLAN_SILV_METHOD_CODE';
        v_old_value := :OLD.plan_silv_method_code;
        v_new_value := :NEW.plan_silv_method_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.plan_silv_objective_code_1, '~') !=
                                                 NVL(:OLD.plan_silv_objective_code_1, '~') THEN
        v_column_name := 'PLAN_SILV_OBJECTIVE_CODE_1';
        v_old_value := :OLD.plan_silv_objective_code_1;
        v_new_value := :NEW.plan_silv_objective_code_1;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.plan_silv_objective_code_2, '~') !=
                                                 NVL(:OLD.plan_silv_objective_code_2, '~') THEN
        v_column_name := 'PLAN_SILV_OBJECTIVE_CODE_2';
        v_old_value := :OLD.plan_silv_objective_code_2;
        v_new_value := :NEW.plan_silv_objective_code_2;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.plan_silv_objective_code_3, '~') !=
                                                 NVL(:OLD.plan_silv_objective_code_3, '~') THEN
        v_column_name := 'PLAN_SILV_OBJECTIVE_CODE_3';
        v_old_value := :OLD.plan_silv_objective_code_3;
        v_new_value := :NEW.plan_silv_objective_code_3;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.plan_silv_technique_code, '~') !=
                                                   NVL(:OLD.plan_silv_technique_code, '~') THEN
        v_column_name := 'PLAN_SILV_TECHNIQUE_CODE';
        v_old_value := :OLD.plan_silv_technique_code;
        v_new_value := :NEW.plan_silv_technique_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.project_unit_id, -99999) != NVL(:OLD.project_unit_id, -99999) THEN
        v_column_name := 'PROJECT_UNIT_ID';
        v_old_value := :OLD.project_unit_id;
        v_new_value := :NEW.project_unit_id;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.prune_height, -99999) != NVL(:OLD.prune_height, -99999) THEN
        v_column_name := 'PRUNE_HEIGHT';
        v_old_value := :OLD.prune_height;
        v_new_value := :NEW.prune_height;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.pruning_min_crown_pct, -99999) != NVL(:OLD.pruning_min_crown_pct
                                                      , -99999) THEN
        v_column_name := 'PRUNING_MIN_CROWN_PCT';
        v_old_value := :OLD.pruning_min_crown_pct;
        v_new_value := :NEW.pruning_min_crown_pct;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.silviculture_project_id, -99999) !=
                                                  NVL(:OLD.silviculture_project_id
                                                    , -99999) THEN
        v_column_name := 'SILVICULTURE_PROJECT_ID';
        v_old_value := :OLD.silviculture_project_id;
        v_new_value := :NEW.silviculture_project_id;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.silv_tree_species_code, '~') != NVL(:OLD.silv_tree_species_code, '~') THEN
        v_column_name := 'SILV_TREE_SPECIES_CODE';
        v_old_value := :OLD.silv_tree_species_code;
        v_new_value := :NEW.silv_tree_species_code;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.stems_to_prune, -99999) != NVL(:OLD.stems_to_prune, -99999) THEN
        v_column_name := 'STEMS_TO_PRUNE';
        v_old_value := :OLD.stems_to_prune;
        v_new_value := :NEW.stems_to_prune;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.survey_actual_num_plots, -99999) !=
                                                  NVL(:OLD.survey_actual_num_plots
                                                    , -99999) THEN
        v_column_name := 'SURVEY_ACTUAL_NUM_PLOTS';
        v_old_value := :OLD.survey_actual_num_plots;
        v_new_value := :NEW.survey_actual_num_plots;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.survey_planned_num_plots, -99999) !=
                                                 NVL(:OLD.survey_planned_num_plots
                                                   , -99999) THEN
        v_column_name := 'SURVEY_PLANNED_NUM_PLOTS';
        v_old_value := :OLD.survey_planned_num_plots;
        v_new_value := :NEW.survey_planned_num_plots;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.survey_min_plots_per_stratum, -99999) !=
                                             NVL(:OLD.survey_min_plots_per_stratum
                                               , -99999) THEN
        v_column_name := 'SURVEY_MIN_PLOTS_PER_STRATUM';
        v_old_value := :OLD.survey_min_plots_per_stratum;
        v_new_value := :NEW.survey_min_plots_per_stratum;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.target_prepared_spots, -99999) != NVL(:OLD.target_prepared_spots
                                                      , -99999) THEN
        v_column_name := 'TARGET_PREPARED_SPOTS';
        v_old_value := :OLD.target_prepared_spots;
        v_new_value := :NEW.target_prepared_spots;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.total_stems_per_ha, -99999) != NVL(:OLD.total_stems_per_ha, -99999) THEN
        v_column_name := 'TOTAL_STEMS_PER_HA';
        v_old_value := :OLD.total_stems_per_ha;
        v_new_value := :NEW.total_stems_per_ha;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.unit_bid_price, -99999) != NVL(:OLD.unit_bid_price, -99999) THEN
        v_column_name := 'UNIT_BID_PRICE';
        v_old_value := :OLD.unit_bid_price;
        v_new_value := :NEW.unit_bid_price;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      IF NVL(:NEW.min_acceptable_density, -99999) !=
                                                   NVL(:OLD.min_acceptable_density
                                                     , -99999) THEN
        v_column_name := 'MIN_ACCEPTABLE_DENSITY';
        v_old_value := :OLD.min_acceptable_density;
        v_new_value := :NEW.min_acceptable_density;
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;
    -----------------------------------
    -- DELETING
    -----------------------------------
    ELSIF DELETING THEN
      v_column_name := 'ATU_COMPLETION_DATE';
      v_old_value :=
                 sil_date_conversion.convert_to_char_trunc_time(:OLD.atu_completion_date);
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'RESULTS_IND';
      v_old_value := :OLD.results_ind;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'FIA_PROJECT_ID';
      v_old_value := :OLD.fia_project_id;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'RESULTS_SUBMISSION_ID';
      v_old_value := :OLD.results_submission_id;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ENTRY_USERID';
      v_old_value := :OLD.entry_userid;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ENTRY_TIMESTAMP';
      v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.entry_timestamp);
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'UPDATE_USERID';
      v_old_value := :OLD.update_userid;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'UPDATE_TIMESTAMP';
      v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.update_timestamp);
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ACTIVITY_LICENSEE_ID';
      v_old_value := :OLD.activity_licensee_id;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ACTIVITY_TREATMENT_UNIT_ID';
      v_old_value := :OLD.activity_treatment_unit_id;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ORG_UNIT_NO';
      v_old_value := :OLD.org_unit_no;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'OPENING_ID';
      v_old_value := :OLD.opening_id;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_BASE_CODE';
      v_old_value := :OLD.silv_base_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_TECHNIQUE_CODE';
      v_old_value := :OLD.silv_technique_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_METHOD_CODE';
      v_old_value := :OLD.silv_method_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_OBJECTIVE_CODE_1';
      v_old_value := :OLD.silv_objective_code_1;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_OBJECTIVE_CODE_2';
      v_old_value := :OLD.silv_objective_code_2;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_OBJECTIVE_CODE_3';
      v_old_value := :OLD.silv_objective_code_3;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_FUND_SRCE_CODE';
      v_old_value := :OLD.silv_fund_srce_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_ACTIVITY_MEASUREMENT_CODE';
      v_old_value := :OLD.silv_activity_measurement_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'TREATMENT_AMOUNT';
      v_old_value := :OLD.treatment_amount;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ACT_PLANTED_NO';
      v_old_value := :OLD.act_planted_no;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLANNED_DATE';
      v_old_value := sil_date_conversion.convert_to_char_trunc_time(:OLD.planned_date);
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLANNED_TREATMENT_COST';
      v_old_value := :OLD.planned_treatment_cost;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ATU_START_DATE';
      v_old_value := :OLD.atu_start_date;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DISTURBANCE_CODE';
      v_old_value := :OLD.disturbance_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_SYSTEM_CODE';
      v_old_value := :OLD.silv_system_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_SYSTEM_VARIANT_CODE';
      v_old_value := :OLD.silv_system_variant_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_CUT_PHASE_CODE';
      v_old_value := :OLD.silv_cut_phase_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DISTURBANCE_COMPLETED_IND';
      v_old_value := :OLD.disturbance_completed_ind;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'CUT_BLOCK_OPEN_ADMIN_ID';
      v_old_value := :OLD.cut_block_open_admin_id;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ACTUAL_TREATMENT_COST';
      v_old_value := :OLD.actual_treatment_cost;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'INTER_TREE_MIN_DISTANCE';
      v_old_value := :OLD.inter_tree_min_distance;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'INTER_TREE_TARGET_DISTANCE';
      v_old_value := :OLD.inter_tree_target_distance;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'INTER_TREE_VARIATION';
      v_old_value := :OLD.inter_tree_variation;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'MAX_TREES_PER_PLOT';
      v_old_value := :OLD.max_trees_per_plot;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'MAX_TREES_PER_HA';
      v_old_value := :OLD.max_trees_per_ha;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLANNED_TREATMENT_AMOUNT';
      v_old_value := :OLD.planned_treatment_amount;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLAN_SILV_FUND_SRCE_CODE';
      v_old_value := :OLD.plan_silv_fund_srce_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLAN_SILV_METHOD_CODE';
      v_old_value := :OLD.plan_silv_method_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLAN_SILV_OBJECTIVE_CODE_1';
      v_old_value := :OLD.plan_silv_objective_code_1;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLAN_SILV_OBJECTIVE_CODE_2';
      v_old_value := :OLD.plan_silv_objective_code_2;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLAN_SILV_OBJECTIVE_CODE_3';
      v_old_value := :OLD.plan_silv_objective_code_3;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLAN_SILV_TECHNIQUE_CODE';
      v_old_value := :OLD.plan_silv_technique_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PROJECT_UNIT_ID';
      v_old_value := :OLD.project_unit_id;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PRUNE_HEIGHT';
      v_old_value := :OLD.prune_height;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PRUNING_MIN_CROWN_PCT';
      v_old_value := :OLD.pruning_min_crown_pct;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILVICULTURE_PROJECT_ID';
      v_old_value := :OLD.silviculture_project_id;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_TREE_SPECIES_CODE';
      v_old_value := :OLD.silv_tree_species_code;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'STEMS_TO_PRUNE';
      v_old_value := :OLD.stems_to_prune;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SURVEY_ACTUAL_NUM_PLOTS';
      v_old_value := :OLD.survey_actual_num_plots;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SURVEY_PLANNED_NUM_PLOTS';
      v_old_value := :OLD.survey_planned_num_plots;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SURVEY_MIN_PLOTS_PER_STRATUM';
      v_old_value := :OLD.survey_min_plots_per_stratum;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'TARGET_PREPARED_SPOTS';
      v_old_value := :OLD.target_prepared_spots;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'TOTAL_STEMS_PER_HA';
      v_old_value := :OLD.total_stems_per_ha;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'UNIT_BID_PRICE';
      v_old_value := :OLD.unit_bid_price;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'MIN_ACCEPTABLE_DENSITY';
      v_old_value := :OLD.min_acceptable_density;
      v_new_value := NULL;

      IF v_old_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;
    -----------------------------------
    -- INSERTING
    -----------------------------------
    ELSIF INSERTING THEN
      v_column_name := 'ATU_COMPLETION_DATE';
      v_new_value :=
                 sil_date_conversion.convert_to_char_trunc_time(:NEW.atu_completion_date);
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'RESULTS_IND';
      v_new_value := :NEW.results_ind;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'FIA_PROJECT_ID';
      v_new_value := :NEW.fia_project_id;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'RESULTS_SUBMISSION_ID';
      v_new_value := :NEW.results_submission_id;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ACTIVITY_LICENSEE_ID';
      v_new_value := :NEW.activity_licensee_id;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ACTIVITY_TREATMENT_UNIT_ID';
      v_new_value := :NEW.activity_treatment_unit_id;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ORG_UNIT_NO';
      v_new_value := :NEW.org_unit_no;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'OPENING_ID';
      v_new_value := :NEW.opening_id;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_BASE_CODE';
      v_new_value := :NEW.silv_base_code;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_TECHNIQUE_CODE';
      v_new_value := :NEW.silv_technique_code;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_METHOD_CODE';
      v_new_value := :NEW.silv_method_code;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_OBJECTIVE_CODE_1';
      v_new_value := :NEW.silv_objective_code_1;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_OBJECTIVE_CODE_2';
      v_new_value := :NEW.silv_objective_code_2;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_OBJECTIVE_CODE_3';
      v_new_value := :NEW.silv_objective_code_3;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_FUND_SRCE_CODE';
      v_new_value := :NEW.silv_fund_srce_code;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_ACTIVITY_MEASUREMENT_CODE';
      v_new_value := :NEW.silv_activity_measurement_code;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'TREATMENT_AMOUNT';
      v_new_value := :NEW.treatment_amount;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ACT_PLANTED_NO';
      v_new_value := :NEW.act_planted_no;
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLANNED_DATE';
      v_new_value := sil_date_conversion.convert_to_char_trunc_time(:NEW.planned_date);
      v_old_value := NULL;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLANNED_TREATMENT_COST';
      v_old_value := NULL;
      v_new_value := :NEW.planned_treatment_cost;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ATU_START_DATE';
      v_old_value := NULL;
      v_new_value := :NEW.atu_start_date;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DISTURBANCE_CODE';
      v_old_value := NULL;
      v_new_value := :NEW.disturbance_code;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_SYSTEM_CODE';
      v_old_value := NULL;
      v_new_value := :NEW.silv_system_code;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_SYSTEM_VARIANT_CODE';
      v_old_value := NULL;
      v_new_value := :NEW.silv_system_variant_code;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_CUT_PHASE_CODE';
      v_old_value := NULL;
      v_new_value := :NEW.silv_cut_phase_code;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'DISTURBANCE_COMPLETED_IND';
      v_old_value := NULL;
      v_new_value := :NEW.disturbance_completed_ind;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'CUT_BLOCK_OPEN_ADMIN_ID';
      v_old_value := NULL;
      v_new_value := :NEW.cut_block_open_admin_id;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'ACTUAL_TREATMENT_COST';
      v_old_value := NULL;
      v_new_value := :NEW.actual_treatment_cost;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'INTER_TREE_MIN_DISTANCE';
      v_old_value := NULL;
      v_new_value := :NEW.inter_tree_min_distance;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'INTER_TREE_TARGET_DISTANCE';
      v_old_value := NULL;
      v_new_value := :NEW.inter_tree_target_distance;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'INTER_TREE_VARIATION';
      v_old_value := NULL;
      v_new_value := :NEW.inter_tree_variation;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'MAX_TREES_PER_PLOT';
      v_old_value := NULL;
      v_new_value := :NEW.max_trees_per_plot;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'MAX_TREES_PER_HA';
      v_old_value := NULL;
      v_new_value := :NEW.max_trees_per_ha;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLANNED_TREATMENT_AMOUNT';
      v_old_value := NULL;
      v_new_value := :NEW.planned_treatment_amount;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLAN_SILV_FUND_SRCE_CODE';
      v_old_value := NULL;
      v_new_value := :NEW.plan_silv_fund_srce_code;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLAN_SILV_METHOD_CODE';
      v_old_value := NULL;
      v_new_value := :NEW.plan_silv_method_code;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLAN_SILV_OBJECTIVE_CODE_1';
      v_old_value := NULL;
      v_new_value := :NEW.plan_silv_objective_code_1;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLAN_SILV_OBJECTIVE_CODE_2';
      v_old_value := NULL;
      v_new_value := :NEW.plan_silv_objective_code_2;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLAN_SILV_OBJECTIVE_CODE_3';
      v_old_value := NULL;
      v_new_value := :NEW.plan_silv_objective_code_3;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PLAN_SILV_TECHNIQUE_CODE';
      v_old_value := NULL;
      v_new_value := :NEW.plan_silv_technique_code;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PROJECT_UNIT_ID';
      v_old_value := NULL;
      v_new_value := :NEW.project_unit_id;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PRUNE_HEIGHT';
      v_old_value := NULL;
      v_new_value := :NEW.prune_height;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'PRUNING_MIN_CROWN_PCT';
      v_old_value := NULL;
      v_new_value := :NEW.pruning_min_crown_pct;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILVICULTURE_PROJECT_ID';
      v_old_value := NULL;
      v_new_value := :NEW.silviculture_project_id;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SILV_TREE_SPECIES_CODE';
      v_old_value := NULL;
      v_new_value := :NEW.silv_tree_species_code;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'STEMS_TO_PRUNE';
      v_old_value := NULL;
      v_new_value := :NEW.stems_to_prune;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SURVEY_ACTUAL_NUM_PLOTS';
      v_old_value := NULL;
      v_new_value := :NEW.survey_actual_num_plots;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SURVEY_PLANNED_NUM_PLOTS';
      v_old_value := NULL;
      v_new_value := :NEW.survey_planned_num_plots;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'SURVEY_MIN_PLOTS_PER_STRATUM';
      v_old_value := NULL;
      v_new_value := :NEW.survey_min_plots_per_stratum;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'TARGET_PREPARED_SPOTS';
      v_old_value := NULL;
      v_new_value := :NEW.target_prepared_spots;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'TOTAL_STEMS_PER_HA';
      v_old_value := NULL;
      v_new_value := :NEW.total_stems_per_ha;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'UNIT_BID_PRICE';
      v_old_value := NULL;
      v_new_value := :NEW.unit_bid_price;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;

      v_column_name := 'MIN_ACCEPTABLE_DENSITY';
      v_old_value := NULL;
      v_new_value := :NEW.min_acceptable_density;

      IF v_new_value IS NOT NULL THEN
        results_audit.insert_audit_detail
                                         (p_table_name => v_table_name
                                        , p_column_name => v_column_name
                                        , p_business_identifier => v_business_identifier
                                        , p_old_value => v_old_value
                                        , p_new_value => v_new_value);
      END IF;
    END IF;
  END IF;
END results_atu_ar_iud_trg;

/
ALTER TRIGGER "THE"."RESULTS_ATU_AR_IUD_TRG" ENABLE;


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