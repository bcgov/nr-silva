package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
@Entity
@EqualsAndHashCode(exclude = {"lastViewed"})
@Table(schema = "silva", name = "user_recent_openings")
@IdClass(UserOpeningEntityId.class)
public class UserRecentOpeningEntity {

  @Id
  @Column(name = "user_id", nullable = false)
  private String userId;

  @Id
  @Column(name = "opening_id", nullable = false)
  private Long openingId;

  @Column(name = "last_viewed", nullable = false)
  private LocalDateTime lastViewed;
}
