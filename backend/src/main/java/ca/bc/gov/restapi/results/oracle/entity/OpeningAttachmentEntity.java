package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * This class represents an Opening Attachment in the database.
 */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "THE", name = "OPENING_ATTACHMENT")
public class OpeningAttachmentEntity {

  @Id
  @Column(name = "OPENING_ATTACHMENT_FILE_ID")
  private Long id;

  @Column(name = "ATTACHMENT_NAME", length = 50, nullable = false)
  private String attachmentName;

  @Column(name = "ATTACHMENT_DESCRIPTION", length = 120)
  private String attachmentDescription;

  @Column(name = "MIME_TYPE_CODE", length = 3)
  private String mimeTypeCode;

}
