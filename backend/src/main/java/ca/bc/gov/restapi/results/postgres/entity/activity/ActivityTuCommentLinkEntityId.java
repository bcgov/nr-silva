package ca.bc.gov.restapi.results.postgres.entity.activity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/** Composite key for {@link ActivityTuCommentLinkEntity}. */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Data
@Builder
@With
public class ActivityTuCommentLinkEntityId {

  private Long activityTreatmentUnitId;
  private Long silvicultureCommentId;
}
