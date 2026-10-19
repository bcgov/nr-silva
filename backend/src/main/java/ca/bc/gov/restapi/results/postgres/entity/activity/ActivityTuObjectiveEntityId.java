package ca.bc.gov.restapi.results.postgres.entity.activity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/** Composite key for {@link ActivityTuObjectiveEntity}. */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Data
@Builder
@With
public class ActivityTuObjectiveEntityId {

  private Long activityTreatmentUnitId;
  private String resultsInd;
  private String silvObjectiveCode;
}
