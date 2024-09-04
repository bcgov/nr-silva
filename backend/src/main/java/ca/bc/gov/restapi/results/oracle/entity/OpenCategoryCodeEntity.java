package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/** This class represents an Opening Category in the database. */
@Getter
@Setter
@Entity
@Table(name = "OPEN_CATEGORY_CODE")
public class OpenCategoryCodeEntity {

  @Id
  @Column(name = "OPEN_CATEGORY_CODE")
  private String code;

  @Column(name = "DESCRIPTION", length = 120, nullable = false)
  private String description;

  @Column(name = "EFFECTIVE_DATE", nullable = false)
  private LocalDate effectiveDate;

  @Column(name = "EXPIRY_DATE", nullable = false)
  private LocalDate expiryDate;

  @Column(name = "UPDATE_TIMESTAMP", nullable = false)
  private LocalDate updateTimestamp;
}
