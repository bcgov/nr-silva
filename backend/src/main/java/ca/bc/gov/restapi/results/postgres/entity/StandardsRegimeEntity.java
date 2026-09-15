package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/** Entity for silva.standards_regime — the base Stocking Standard record. */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "standards_regime")
public class StandardsRegimeEntity {

  @Id
  @Column(name = "standards_regime_id")
  private Long id;

  @Column(name = "standards_regime_name", length = 50)
  private String standardsRegimeName;

  @Column(name = "standards_regime_status_code", length = 3, nullable = false)
  private String standardsRegimeStatusCode;

  @Column(name = "silv_statute_code", length = 3)
  private String silvStatuteCode;

  @Column(name = "standards_objective", length = 50)
  private String standardsObjective;

  @Column(name = "geographic_description", length = 50)
  private String geographicDescription;

  @Column(name = "mof_default_standard_ind", length = 1, nullable = false)
  private String mofDefaultStandardInd;

  @Column(name = "alternative_method_ind", length = 1, nullable = false)
  private String alternativeMethodInd;

  @Column(name = "regen_delay_offset_yrs")
  private Integer regenDelayOffsetYrs;

  @Column(name = "regen_obligation_ind", length = 1, nullable = false)
  private String regenObligationInd;

  @Column(name = "no_regen_early_offset_yrs")
  private Integer noRegenEarlyOffsetYrs;

  @Column(name = "no_regen_late_offset_yrs")
  private Integer noRegenLateOffsetYrs;

  @Column(name = "free_growing_early_offset_yrs")
  private Integer freeGrowingEarlyOffsetYrs;

  @Column(name = "free_growing_late_offset_yrs")
  private Integer freeGrowingLateOffsetYrs;

  @Column(name = "approved_by_userid", length = 30)
  private String approvedByUserid;

  @Column(name = "approved_date")
  private LocalDateTime approvedDate;

  @Column(name = "submitted_by_userid", length = 30)
  private String submittedByUserid;

  @Column(name = "submitted_date")
  private LocalDateTime submittedDate;

  @Column(name = "effective_date")
  private LocalDateTime effectiveDate;

  @Column(name = "expiry_date")
  private LocalDateTime expiryDate;

  @Column(name = "additional_standards", length = 4000)
  private String additionalStandards;

  @Column(name = "reject_note", length = 2000)
  private String rejectNote;

  @Column(name = "entry_userid", length = 30, nullable = false)
  private String entryUserid;

  @Column(name = "entry_timestamp", nullable = false)
  private LocalDateTime entryTimestamp;

  @Column(name = "update_userid", length = 30, nullable = false)
  private String updateUserid;

  @Column(name = "update_timestamp", nullable = false)
  private LocalDateTime updateTimestamp;

  @Column(name = "revision_count", nullable = false)
  private Integer revisionCount;
}
