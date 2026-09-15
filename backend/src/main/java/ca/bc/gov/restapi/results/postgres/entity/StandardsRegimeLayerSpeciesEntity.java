package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/** Entity for silva.standards_regime_layer_species — a species entry within a layer. */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "standards_regime_layer_species")
@IdClass(StandardsRegimeLayerSpeciesEntityId.class)
public class StandardsRegimeLayerSpeciesEntity {

  @Id
  @Column(name = "standards_regime_layer_id")
  private Long standardsRegimeLayerId;

  @Id
  @Column(name = "silv_tree_species_code", length = 8)
  private String silvTreeSpeciesCode;

  @Column(name = "species_order", nullable = false)
  private Integer speciesOrder;

  @Column(name = "species_type_code", length = 3, nullable = false)
  private String speciesTypeCode;

  @Column(name = "min_height")
  private BigDecimal minHeight;

  @Column(name = "regen_milestone_ind", length = 1, nullable = false)
  private String regenMilestoneInd;

  @Column(name = "free_growing_milestone_ind", length = 1, nullable = false)
  private String freeGrowingMilestoneInd;

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
