package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.SuperBuilder;

/** Entity for opening status codes. */
@SuperBuilder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(schema = "THE", name = "OPENING_STATUS_CODE")
public class OpeningStatusCodeEntity extends AbstractCodeEntity {

  @Id
  @Column(name = "OPENING_STATUS_CODE")
  private String code;
}
