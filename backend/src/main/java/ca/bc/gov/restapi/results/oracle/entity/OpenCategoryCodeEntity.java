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

/** This class represents an Opening Category in the database. */
@SuperBuilder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(schema = "THE", name = "OPEN_CATEGORY_CODE")
public class OpenCategoryCodeEntity extends AbstractCodeEntity {

  @Id
  @Column(name = "OPEN_CATEGORY_CODE")
  private String code;

  // description, effectiveDate, expiryDate, updateTimestamp are inherited
}
