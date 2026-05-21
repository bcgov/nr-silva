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

/** Minimal entity for STANDARDS_REGIME (anchor for JPA repository). */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "THE", name = "STANDARDS_REGIME")
public class StandardsRegimeEntity {

  @Id
  @Column(name = "STANDARDS_REGIME_ID")
  private Long id;

  @Column(name = "STANDARDS_REGIME_NAME", length = 50)
  private String standardsRegimeName;
}
