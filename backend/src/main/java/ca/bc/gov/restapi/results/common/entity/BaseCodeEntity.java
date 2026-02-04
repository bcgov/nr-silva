package ca.bc.gov.restapi.results.common.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
public abstract class BaseCodeEntity {
  @Id
  private String code;

  private String description;
  private LocalDate effectiveDate;
  private LocalDate expiryDate;
  private LocalDateTime updateTimestamp;

  @Transient
  public boolean isExpired() {
    return expiryDate != null && LocalDate.now().isAfter(expiryDate);
  }
}
