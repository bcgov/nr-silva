package ca.bc.gov.restapi.results.postgres.entity.opening;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "opening")
public class OpeningEntity {
  @Id
  @Column(name = "opening_id")
  private Long id;

  @Column(name = "opening_status_code", length = 3)
  private String status;

  @Column(name = "open_category_code", length = 7)
  private String category;

  @Column(name = "entry_userid", length = 30)
  private String entryUserId;

  @Column(name = "update_timestamp")
  private LocalDateTime updateTimestamp;

  @Column(name = "entry_timestamp")
  private LocalDateTime entryTimestamp;
}
