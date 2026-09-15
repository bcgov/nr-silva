package ca.bc.gov.restapi.results.postgres.entity;

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

/** Entity for silva.standards_regime_org_unit — org units a Stocking Standard is assigned to. */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "standards_regime_org_unit")
@IdClass(StandardsRegimeOrgUnitEntityId.class)
public class StandardsRegimeOrgUnitEntity {

  @Id
  @Column(name = "standards_regime_id")
  private Long standardsRegimeId;

  @Id
  @Column(name = "org_unit_no")
  private Long orgUnitNo;

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
