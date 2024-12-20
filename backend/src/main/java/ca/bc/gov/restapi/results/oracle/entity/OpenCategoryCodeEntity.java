package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

/**
 * This class represents an Opening Category in the database.
 */
@RegisterReflectionForBinding
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "THE", name = "OPEN_CATEGORY_CODE")
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

  @Transient
  public boolean isExpired() {
    return expiryDate != null && LocalDate.now().isAfter(expiryDate);
  }
}
