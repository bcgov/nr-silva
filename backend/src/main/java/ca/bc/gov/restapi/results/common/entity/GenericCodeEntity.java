package ca.bc.gov.restapi.results.common.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public abstract class GenericCodeEntity {
  @Id private String code;

  private String description;
  private LocalDate effectiveDate;
  private LocalDate expiryDate;
  private LocalDateTime updateTimestamp;

  @Transient
  public boolean isExpired() {
    return expiryDate != null && LocalDate.now().isAfter(expiryDate);
  }
}
