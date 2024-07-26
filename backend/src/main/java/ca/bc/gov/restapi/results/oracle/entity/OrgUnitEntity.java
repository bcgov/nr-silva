package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** This class represents an Organization Unity in the database. */
@Getter
@Setter
@Entity
@ToString
@Table(name = "ORG_UNIT")
@EqualsAndHashCode
public class OrgUnitEntity {

  @Id
  @Column(name = "ORG_UNIT_NO")
  private Long orgUnitNo;

  @Column(name = "ORG_UNIT_CODE", length = 6, nullable = false)
  private String orgUnitCode;

  @Column(name = "ORG_UNIT_NAME", length = 100, nullable = false)
  private String orgUnitName;

  // An Org Unit belongs to one Opening
  // Foreign key referencing the opening table
  // @OneToOne(mappedBy = "adminDistrict")
  // private OpeningEntity openingEntity;
}
