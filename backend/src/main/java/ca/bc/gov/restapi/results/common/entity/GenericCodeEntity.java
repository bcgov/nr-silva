package ca.bc.gov.restapi.results.common.entity;

import ca.bc.gov.restapi.results.common.util.DateUtil;
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
    return isExpired(DateUtil.todayInVancouver());
  }

  @Transient
  public boolean isExpired(LocalDate date) {
    return expiryDate != null && date != null && date.isAfter(expiryDate);
  }

  @Transient
  public boolean isEffective() {
    return isEffective(DateUtil.todayInVancouver());
  }

  @Transient
  public boolean isEffective(LocalDate date) {
    if (date == null) {
      return false;
    }
    return (effectiveDate == null || !date.isBefore(effectiveDate)) && !isExpired(date);
  }
}

