package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/** Minimal entity for stocking_standard_unit (anchor for JPA repository). */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "stocking_standard_unit")
public class StockingStandardUnitEntity {

  @Id
  @Column(name = "stocking_standard_unit_id")
  private Long id;

  @Column(name = "opening_id")
  private Long openingId;

  @Column(name = "standards_unit_id", length = 4)
  private String standardsUnitId;
}
