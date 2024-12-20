package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

/**
 * This class represents an Opening in the database.
 */
@RegisterReflectionForBinding
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "THE", name = "OPENING")
public class OpeningEntity {

  @Id
  @Column(name = "OPENING_ID")
  private Long id;

  @Column(name = "OPENING_STATUS_CODE", length = 3)
  private String status;

  @Column(name = "OPEN_CATEGORY_CODE", length = 7)
  private String category;

  @Column(name = "ENTRY_USERID", length = 30)
  private String entryUserId;

  @Column(name = "UPDATE_TIMESTAMP")
  private LocalDateTime updateTimestamp;

  @Column(name = "ENTRY_TIMESTAMP")
  private LocalDateTime entryTimestamp;
}
