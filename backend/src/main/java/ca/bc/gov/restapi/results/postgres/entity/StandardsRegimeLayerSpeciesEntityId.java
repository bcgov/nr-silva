package ca.bc.gov.restapi.results.postgres.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/** Composite key for {@link StandardsRegimeLayerSpeciesEntity}. */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Data
@Builder
@With
public class StandardsRegimeLayerSpeciesEntityId {

  private Long standardsRegimeLayerId;
  private String silvTreeSpeciesCode;
}
