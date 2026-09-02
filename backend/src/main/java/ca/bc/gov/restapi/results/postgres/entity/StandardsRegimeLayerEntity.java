package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/** Entity for silva.standards_regime_layer — a Tree Stocking Criteria layer definition. */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "standards_regime_layer")
public class StandardsRegimeLayerEntity {

  @Id
  @Column(name = "standards_regime_layer_id")
  private Long id;

  @Column(name = "standards_regime_id", nullable = false)
  private Long standardsRegimeId;

  @Column(name = "stocking_layer_code", length = 2, nullable = false)
  private String stockingLayerCode;

  @Column(name = "tree_size_unit_code", length = 3)
  private String treeSizeUnitCode;

  @Column(name = "residual_basal_area")
  private Integer residualBasalArea;

  @Column(name = "min_horizontal_distance")
  private BigDecimal minHorizontalDistance;

  @Column(name = "min_pref_stocking_standard")
  private Integer minPrefStockingStandard;

  @Column(name = "min_stocking_standard")
  private Integer minStockingStandard;

  @Column(name = "target_stocking")
  private Integer targetStocking;

  @Column(name = "min_post_spacing")
  private Integer minPostSpacing;

  @Column(name = "max_post_spacing")
  private Integer maxPostSpacing;

  @Column(name = "max_conifer")
  private Integer maxConifer;

  @Column(name = "hght_relative_to_comp")
  private Integer hghtRelativeToComp;

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
