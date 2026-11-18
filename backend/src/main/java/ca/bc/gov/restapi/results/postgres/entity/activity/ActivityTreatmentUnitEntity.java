package ca.bc.gov.restapi.results.postgres.entity.activity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "activity_treatment_unit")
public class ActivityTreatmentUnitEntity implements Persistable<Long> {
  @Column(name = "activity_treatment_unit_id")
  @Id
  private Long id;

  @Column(name = "activity_tu_seq_no")
  private Integer activityTuSeqNo;

  @Column(name = "org_unit_no")
  private Long orgUnitNo;

  @Column(name = "opening_id")
  private Long openingId;

  @Column(name = "silv_base_code")
  private String silvBaseCode;

  @Column(name = "silv_technique_code")
  private String silvTechniqueCode;

  @Column(name = "silv_method_code")
  private String silvMethodCode;

  @Column(name = "silv_activity_measurement_code")
  private String silvActivityMeasurementCode;

  @Column(name = "activity_licensee_id")
  private String activityLicenseeId;

  @Column(name = "cut_block_open_admin_id")
  private Long cutBlockOpenAdminId;

  @Column(name = "treatment_amount", precision = 11, scale = 1)
  private BigDecimal treatmentAmount;

  @Column(name = "actual_treatment_cost")
  private Integer actualTreatmentCost;

  @Column(name = "plan_silv_technique_code")
  private String planSilvTechniqueCode;

  @Column(name = "plan_silv_method_code")
  private String planSilvMethodCode;

  @Column(name = "planned_date")
  private LocalDateTime plannedDate;

  @Column(name = "planned_treatment_amount", precision = 11, scale = 1)
  private BigDecimal plannedTreatmentAmount;

  @Column(name = "planned_treatment_cost")
  private Integer plannedTreatmentCost;

  @Column(name = "atu_start_date")
  private LocalDateTime atuStartDate;

  @Column(name = "atu_completion_date")
  private LocalDateTime atuCompletionDate;

  @Column(name = "results_ind", length = 1)
  private String resultsInd;

  @Column(name = "fia_project_id", length = 10)
  private String fiaProjectId;

  @Column(name = "entry_userid", length = 30)
  private String entryUserid;

  @Column(name = "entry_timestamp")
  private LocalDateTime entryTimestamp;

  @Column(name = "update_userid", length = 30)
  private String updateUserid;

  @Column(name = "update_timestamp")
  private LocalDateTime updateTimestamp;

  @Column(name = "revision_count")
  private Integer revisionCount;

  @Transient private boolean isNew;

  @Override
  public boolean isNew() {
    return isNew;
  }
}
