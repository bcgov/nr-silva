package ca.bc.gov.restapi.results.oracle.entity.comments;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
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
  @CreatedBy
  private String entryUserId;

  @Column(name = "UPDATE_USERID", length = 30)
  @LastModifiedBy
  private String updateUserId;

  @Column(name = "UPDATE_TIMESTAMP")
  @LastModifiedDate
  private LocalDateTime updateTimestamp;

  @Column(name = "ENTRY_TIMESTAMP")
  @CreatedDate
  private LocalDateTime entryTimestamp;

  @Column(name = "REVISION_COUNT")
  @Version
  private Long revision;

}
