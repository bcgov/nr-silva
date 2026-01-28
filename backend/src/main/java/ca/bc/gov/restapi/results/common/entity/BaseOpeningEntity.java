package ca.bc.gov.restapi.results.common.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;


@Data
@MappedSuperclass
public abstract class BaseOpeningEntity {
  @Id
  private Long id;
  
  private String status;
  private String category;
  private String entryUserId;
  private LocalDateTime updateTimestamp;
  private LocalDateTime entryTimestamp;
}
