
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
