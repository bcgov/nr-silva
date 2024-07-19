package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

  // An opening can have many attachments
  @OneToMany(mappedBy = "openingEntity", fetch = FetchType.LAZY)
  private List<OpeningAttachmentEntity> attachments;

  // An opening can have many cut blocks
  @OneToMany(mappedBy = "openingEntity", fetch = FetchType.LAZY)
  private List<CutBlockOpenAdminEntity> cutBlocksOpenAdmins;

  // An opening can have one org unit (ADMIN_DISTRICT)NO
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "ADMIN_DISTRICT_NO", referencedColumnName = "ORG_UNIT_NO")
  private OrgUnitEntity adminDistrict;
}
