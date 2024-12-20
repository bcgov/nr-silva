package ca.bc.gov.restapi.results.postgres.entity;

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
 * This class represents a record in the database for the openings_last_year table.
 */
@RegisterReflectionForBinding
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "openings_last_year")
public class OpeningsLastYearEntity {

  @Id
  @Column(name = "opening_id")
  private Long openingId;

  @Column(name = "opening_entry_userid", nullable = false, length = 30)
  private String userId;

  @Column(name = "entry_timestamp", nullable = false)
  private LocalDateTime entryTimestamp;

  @Column(name = "update_timestamp", nullable = false)
  private LocalDateTime updateTimestamp;

  @Column(name = "status_code", length = 3)
  private String status;

  @Column(name = "org_unit_code", length = 6)
  private String orgUnitCode;

  @Column(name = "org_unit_name", length = 120)
  private String orgUnitName;

  @Column(name = "client_number", length = 8)
  private String clientNumber;
}
