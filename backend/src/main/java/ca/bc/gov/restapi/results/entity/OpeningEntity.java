package ca.bc.gov.restapi.results.entity;

import ca.bc.gov.restapi.results.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.enums.OpeningStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "OPENING")
public class OpeningEntity {

  @Id
  @Column(name = "OPENING_ID", length = 10)
  private String id;

  @Column(name = "OPENING_STATUS_CODE", length = 3)
  private OpeningStatusEnum status;

  @Column(name = "OPEN_CATEGORY_CODE", length = 7)
  private OpeningCategoryEnum category;
}
