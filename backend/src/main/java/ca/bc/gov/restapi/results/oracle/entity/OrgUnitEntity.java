package ca.bc.gov.restapi.results.oracle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

/** This class represents an Organization Unity in the database. */
@Data
@Builder
@With
@Entity
@Table(name = "ORG_UNIT")
public class OrgUnitEntity {

  @Id
  @Column(name = "ORG_UNIT_NO")
  private Long orgUnitNo;

  @Column(name = "ORG_UNIT_CODE", length = 6, nullable = false)
  private String orgUnitCode;

  @Column(name = "ORG_UNIT_NAME", length = 100, nullable = false)
  private String orgUnitName;

  @Column(name = "LOCATION_CODE", length = 3, nullable = false)
  private String locationCode;

  @Column(name = "AREA_CODE", length = 3, nullable = false)
  private String areaCode;

  @Column(name = "TELEPHONE_NO", length = 7, nullable = false)
  private String telephoneNo;

  @Column(name = "ORG_LEVEL_CODE", length = 1, nullable = false)
  private Character orgLevelCode;

  @Column(name = "OFFICE_NAME_CODE", length = 2, nullable = false)
  private String officeNameCode;

  @Column(name = "ROLLUP_REGION_NO", nullable = false)
  private Long rollupRegionNo;

  @Column(name = "ROLLUP_REGION_CODE", length = 6, nullable = false)
  private String rollupRegionCode;

  @Column(name = "ROLLUP_DIST_NO", nullable = false)
  private Long rollupDistNo;

  @Column(name = "ROLLUP_DIST_CODE", length = 6, nullable = false)
  private String rollupDistCode;

  @Column(name = "EFFECTIVE_DATE", nullable = false)
  private LocalDate effectiveDate;

  @Column(name = "EXPIRY_DATE", nullable = false)
  private LocalDate expiryDate;

  @Column(name = "UPDATE_TIMESTAMP", nullable = false)
  private LocalDate updateTimestamp;
}
