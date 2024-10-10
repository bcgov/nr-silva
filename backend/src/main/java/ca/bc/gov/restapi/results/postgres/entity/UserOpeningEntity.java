package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

/** This class represents an Opening saved as favourite to the user. */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_openings")
@IdClass(UserOpeningEntityId.class)
public class UserOpeningEntity {

  @Id
  @Column(name = "user_id")
  private String userId;

  @Id
  @Column(name = "opening_id")
  private Long openingId;
}
