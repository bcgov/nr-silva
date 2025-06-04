package ca.bc.gov.restapi.results.oracle.entity.cover;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "THE", name = "FOREST_COVER")
@EntityListeners(AuditingEntityListener.class)
public class ForestCoverEntity {

  @Id
  @Column(name = "FOREST_COVER_ID")
  private Long id;

  @Column(name = "ENTRY_USERID", length = 30)
  private String entryUserId;

  @Column(name = "UPDATE_USERID", length = 30)
  private String updateUserId;

  @Column(name = "UPDATE_TIMESTAMP")
  private LocalDateTime updateTimestamp;

  @Column(name = "ENTRY_TIMESTAMP")
  private LocalDateTime entryTimestamp;

  @Column(name = "REVISION_COUNT")
  private Long revision;
}
