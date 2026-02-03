package ca.bc.gov.restapi.results.postgres.entity.opening;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "opening_attachment")
public class OpeningAttachmentEntity {

  @Id
  @Column(name = "opening_attachment_file_id", nullable = false)
  private Long attachmentFileId;

  @Column(name = "opening_id", nullable = false)
  private Long openingId;

  @Column(name = "attachment_name", nullable = false, length = 50)
  private String attachmentName;

  @Column(name = "attachment_description", length = 120)
  private String attachmentDescription;

  @Column(name = "mime_type_code", length = 3)
  private String mimeTypeCode;

  @Lob
  @Column(name = "attachment_data", nullable = false)
  private byte[] attachmentData;

  @Column(name = "entry_userid", nullable = false, length = 30)
  private String entryUserId;

  @Column(name = "entry_timestamp", nullable = false)
  private LocalDateTime entryTimestamp;

  @Column(name = "update_userid", nullable = false, length = 30)
  private String updateUserId;

  @Column(name = "update_timestamp", nullable = false)
  private LocalDateTime updateTimestamp;

  @Column(name = "revision_count", nullable = false)
  private Integer revisionCount;

  @Column(name = "opening_attachment_guid", nullable = false, columnDefinition = "uuid")
  @Convert(converter = ca.bc.gov.restapi.results.oracle.converter.UuidToBytesConverter.class)
  private UUID attachmentGuid;

}
