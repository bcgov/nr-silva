package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;

/**
 * This class represents a record in the database for the openings_activity table.
 */
@RegisterReflectionForBinding
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@IdClass(OpeningsActivityEntityId.class)
@Entity
@Table(schema = "silva", name = "openings_activity")
public class OpeningsActivityEntity {

  @Id
  @Column(name = "activity_id")
  private Integer activityId;

  @Id
  @Column(name = "opening_id")
  private Long openingId;

  @Column(name = "activity_type_code", length = 3)
  private String activityTypeCode;

  @Column(name = "activity_type_desc", length = 120)
  private String activityTypeDesc;

  @Column(name = "status_code", length = 3)
  private String statusCode;

  @Column(name = "status_desc", length = 120)
  private String statusDesc;

  @Column(name = "last_updated", nullable = false)
  private LocalDateTime lastUpdated;

  @Column(name = "entry_userid", length = 30, nullable = false)
  private String entryUserid;
}
