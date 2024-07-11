package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** This class represents an Opening in the database. */
@Getter
@Setter
@Entity
@Table(name = "OPENING")
public class OpeningEntity {

  @Id
  @Column(name = "OPENING_ID")
  private Long id;

  @Column(name = "OPENING_NUMBER")
  private String openingNumber;

  @Column(name = "OPENING_STATUS_CODE", length = 3)
  private String status;

  @Column(name = "OPEN_CATEGORY_CODE", length = 7)
  private String category;

  @Column(name = "ENTRY_USERID", length = 30)
  private String entryUserId;

  @Column(name = "UPDATE_TIMESTAMP")
  private LocalDateTime updateTimestamp;

  @Column(name = "ENTRY_TIMESTAMP")
  private LocalDateTime entryTimestamp;

  @OneToMany(mappedBy = "opening")
  private List<OpeningAttachmentEntity> attachments;
}
