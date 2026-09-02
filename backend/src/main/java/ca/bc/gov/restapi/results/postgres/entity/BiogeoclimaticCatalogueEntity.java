package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/** Entity for silva.biogeoclimatic_catalogue — valid BEC Zone/Subzone/Variant/Phase combinations. */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "biogeoclimatic_catalogue")
public class BiogeoclimaticCatalogueEntity {

  @Id
  @Column(name = "biogeoclimatic_catalogue_id")
  private Long id;

  @Column(name = "bec_zone_code", length = 4, nullable = false)
  private String becZoneCode;

  @Column(name = "subzone", length = 3, nullable = false)
  private String subzone;

  @Column(name = "variant", length = 1)
  private String variant;

  @Column(name = "phase", length = 1)
  private String phase;

  @Column(name = "bec_natural_disturbance_code", length = 4, nullable = false)
  private String becNaturalDisturbanceCode;

  @Column(name = "zone_name", length = 35, nullable = false)
  private String zoneName;

  @Column(name = "subzone_name", length = 35, nullable = false)
  private String subzoneName;

  @Column(name = "variant_name", length = 20)
  private String variantName;

  @Column(name = "phase_name", length = 15)
  private String phaseName;

  @Column(name = "notes", length = 72)
  private String notes;

  @Column(name = "effective_date", nullable = false)
  private LocalDate effectiveDate;

  @Column(name = "expiry_date", nullable = false)
  private LocalDate expiryDate;

  @Column(name = "update_timestamp", nullable = false)
  private LocalDateTime updateTimestamp;

  @Column(name = "update_userid", length = 30, nullable = false)
  private String updateUserid;
}
