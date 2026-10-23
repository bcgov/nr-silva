package ca.bc.gov.restapi.results.postgres.entity.activity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/** Entity for silva.activity_tu_comment_link — links an activity to its comments. */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "activity_tu_comment_link")
@IdClass(ActivityTuCommentLinkEntityId.class)
public class ActivityTuCommentLinkEntity {

  @Id
  @Column(name = "activity_treatment_unit_id")
  private Long activityTreatmentUnitId;

  @Id
  @Column(name = "silviculture_comment_id")
  private Long silvicultureCommentId;
}
