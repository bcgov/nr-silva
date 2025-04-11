package ca.bc.gov.restapi.results.oracle.entity.comments;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
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
@Table(schema = "THE", name = "SILVICULTURE_COMMENT")
@EntityListeners(AuditingEntityListener.class)
public class SilvicultureCommentEntity {

  @Id
  @Column(name = "SILVICULTURE_COMMENT_ID")
  private Long id;

  @Column(name = "COMMENT_DATE")
  private LocalDateTime commentDate;

  @Column(name = "SILV_COMMENT_SOURCE_CODE")
  private String commentSourceCode;

  @Column(name = "SILV_COMMENT_TYPE_CODE")
  private String commentTypeCode;

  @Column(name = "COMMENT_TEXT", length = 2000)
  @Max(2000)
  private String comment;

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
