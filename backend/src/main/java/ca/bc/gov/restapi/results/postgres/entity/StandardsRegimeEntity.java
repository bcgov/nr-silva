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

/** Minimal entity for standards_regime (anchor for JPA repository). */
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
}
