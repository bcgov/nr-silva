package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "org_unit")
public class OrgUnitEntity {

  @Id
  @Column(name = "org_unit_no")
  private Long orgUnitNo;

  @Column(name = "org_unit_code", length = 6, nullable = false)
  private String orgUnitCode;

  @Column(name = "org_unit_name", length = 100, nullable = false)
  private String orgUnitName;

  @Column(name = "location_code", length = 3, nullable = false)
  private String locationCode;

  @Column(name = "area_code", length = 3, nullable = false)
  private String areaCode;

  @Column(name = "telephone_no", length = 7, nullable = false)
  private String telephoneNo;

  @Column(name = "org_level_code", length = 1, nullable = false)
  private Character orgLevelCode;

  @Column(name = "office_name_code", length = 2, nullable = false)
  private String officeNameCode;

  @Column(name = "rollup_region_no", nullable = false)
  private Long rollupRegionNo;

  @Column(name = "rollup_region_code", length = 6, nullable = false)
  private String rollupRegionCode;

  @Column(name = "rollup_dist_no", nullable = false)
  private Long rollupDistNo;

  @Column(name = "rollup_dist_code", length = 6, nullable = false)
  private String rollupDistCode;

  @Column(name = "effective_date", nullable = false)
  private LocalDate effectiveDate;

  @Column(name = "expiry_date", nullable = false)
  private LocalDate expiryDate;

  @Column(name = "update_timestamp", nullable = false)
  private LocalDate updateTimestamp;

}
