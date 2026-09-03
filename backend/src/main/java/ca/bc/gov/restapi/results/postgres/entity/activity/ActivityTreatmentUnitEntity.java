package ca.bc.gov.restapi.results.postgres.entity.activity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "activity_treatment_unit")
public class ActivityTreatmentUnitEntity {
  @Column(name = "activity_treatment_unit_id")
  @Id
  private Long id;

  @Column(name = "opening_id")
  private Long openingId;

  @Column(name = "silv_base_code")
  private String silvBaseCode;

  @Column(name = "cut_block_open_admin_id")
  private Long cutBlockOpenAdminId;

  @Column(name = "treatment_amount", precision = 11, scale = 1)
  private BigDecimal treatmentAmount;

  @Column(name = "atu_start_date")
  private LocalDateTime atuStartDate;

  @Column(name = "atu_completion_date")
  private LocalDateTime atuCompletionDate;
}
