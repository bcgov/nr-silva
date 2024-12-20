package ca.bc.gov.restapi.results.postgres.entity;

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
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

/**
 * This class represents an Opening saved as favourite to the user.
 */
@RegisterReflectionForBinding
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "user_openings")
@IdClass(UserOpeningEntityId.class)
public class UserOpeningEntity {

  @Id
  @Column(name = "user_id")
  private String userId;

  @Id
  @Column(name = "opening_id")
  private Long openingId;
}
