package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "open_category_code")
public class OpenCategoryCodeEntity {
  @Id
  @Column(name = "open_category_code")
  private String code;

  @Column(name = "description", length = 120, nullable = false)
  private String description;

  @Column(name = "effective_date", nullable = false)
  private LocalDate effectiveDate;

  @Column(name = "expiry_date", nullable = false)
  private LocalDate expiryDate;

  @Column(name = "update_timestamp", nullable = false)
  private LocalDate updateTimestamp;

  @Transient
  public boolean isExpired() {
    return expiryDate != null && LocalDate.now().isAfter(expiryDate);
  }
}
