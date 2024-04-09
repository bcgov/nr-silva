package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** This class represents an Opening saved as favourite to the user. */
@Getter
@Setter
@Entity
@Table(name = "user_openings")
@IdClass(UserOpeningEntityId.class)
@ToString
@EqualsAndHashCode
public class UserOpeningEntity {

  @Id
  @Column(name = "user_id")
  private String userId;

  @Id
  @Column(name = "opening_id")
  private Long openingId;
}
