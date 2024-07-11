package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** This class represents an Opening Attachment in the database. */
@Getter
@Setter
@Entity
@ToString
@Table(name = "OPENING_ATTACHMENT")
@EqualsAndHashCode
public class OpeningAttachmentEntity {

  @Id
  @Column(name = "OPENING_ATTACHMENT_FILE_ID")
  private Long id;

  //@Column(name = "OPENING_ID", nullable = false)
  //private Long openingId;

  @Column(name = "ATTACHMENT_NAME", length = 50, nullable = false)
  private String attachmentName;

  @Column(name = "ATTACHMENT_DESCRIPTION", length = 120)
  private String attachmentDescription;

  @Column(name = "MIME_TYPE_CODE", length = 3)
  private String mimeTypeCode;

  // An Opening Attachment belongs to one Opening
  // Foreign key referencing the opening table
  @ManyToOne
  @JoinColumn(name = "OPENING_ID")
  private OpeningEntity openingEntity;
}
