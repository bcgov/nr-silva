package ca.bc.gov.restapi.results.common.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseOpeningEntity {
  @Id private Long id;

  private String status;
  private String category;
  private String entryUserId;
  private LocalDateTime updateTimestamp;
  private LocalDateTime entryTimestamp;
}
