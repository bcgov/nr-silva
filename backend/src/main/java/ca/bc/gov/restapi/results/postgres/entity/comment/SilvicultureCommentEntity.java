package ca.bc.gov.restapi.results.postgres.entity.comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "silviculture_comment")
@EntityListeners(AuditingEntityListener.class)
public class SilvicultureCommentEntity {

  @Id
  @Column(name = "silviculture_comment_id")
  private Long id;

  @Column(name = "comment_date")
  private LocalDateTime commentDate;

  @Column(name = "silv_comment_source_code")
  private String commentSourceCode;

  @Column(name = "silv_comment_type_code")
  private String commentTypeCode;

  @Column(name = "comment_text", length = 2000)
  @Max(2000)
  private String comment;

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
