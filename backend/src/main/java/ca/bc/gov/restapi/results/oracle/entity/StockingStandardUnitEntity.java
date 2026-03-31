package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/** Minimal entity for STOCKING_STANDARD_UNIT (anchor for JPA repository). */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "THE", name = "STOCKING_STANDARD_UNIT")
public class StockingStandardUnitEntity {

  @Id
  @Column(name = "STOCKING_STANDARD_UNIT_ID")
  private Long id;

  @Column(name = "OPENING_ID")
  private Long openingId;

  @Column(name = "STANDARDS_UNIT_ID", length = 4)
  private String standardsUnitId;
}
