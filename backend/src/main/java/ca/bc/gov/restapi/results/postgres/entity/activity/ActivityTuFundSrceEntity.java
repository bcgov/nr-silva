package ca.bc.gov.restapi.results.postgres.entity.activity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * Entity for silva.activity_tu_fund_srce — funding sources of an activity treatment unit beyond the
 * single legacy column. {@code resultsInd} is 'N' for the planned set and 'Y' for the actual set.
 */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "activity_tu_fund_srce")
@IdClass(ActivityTuFundSrceEntityId.class)
public class ActivityTuFundSrceEntity {

  @Id
  @Column(name = "activity_treatment_unit_id")
  private Long activityTreatmentUnitId;

  @Id
  @Column(name = "results_ind", length = 1)
  private String resultsInd;

  @Id
  @Column(name = "silv_fund_srce_code", length = 3)
  private String silvFundSrceCode;

  @Column(name = "fund_srce_order", nullable = false)
  private Short fundSrceOrder;

  @Column(name = "entry_userid", length = 30, nullable = false)
  private String entryUserid;

  @Column(name = "entry_timestamp", nullable = false)
  private LocalDateTime entryTimestamp;
}
