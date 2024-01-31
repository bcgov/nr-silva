package ca.bc.gov.restapi.results.entity;

import ca.bc.gov.restapi.results.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.enums.OpeningStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

  @Column(name = "OPENING_STATUS_CODE", length = 3)
  private OpeningStatusEnum status;

  @Column(name = "OPEN_CATEGORY_CODE", length = 7)
  private OpeningCategoryEnum category;

  @Column(name = "ENTRY_USERID", length = 30)
  private String entryUserId;
}
