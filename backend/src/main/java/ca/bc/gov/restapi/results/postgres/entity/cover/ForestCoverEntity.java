package ca.bc.gov.restapi.results.postgres.entity.cover;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "forest_cover")
@EntityListeners(AuditingEntityListener.class)
public class ForestCoverEntity {

  @Id
  @Column(name = "forest_cover_id")
  private Long id;

  @Column(name = "entry_userid", length = 30)
  private String entryUserId;

  @Column(name = "update_userid", length = 30)
  private String updateUserId;

  @Column(name = "update_timestamp")
  private LocalDateTime updateTimestamp;

  @Column(name = "entry_timestamp")
  private LocalDateTime entryTimestamp;

  @Column(name = "revision_count")
  private Long revision;

}

