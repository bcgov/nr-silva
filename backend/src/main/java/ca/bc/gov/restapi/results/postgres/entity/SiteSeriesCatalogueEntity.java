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

/** Entity for silva.site_series_catalogue — valid BEC site series definitions. */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "site_series_catalogue")
public class SiteSeriesCatalogueEntity {

  @Id
  @Column(name = "site_series_catalogue_id")
  private Long id;

  @Column(name = "bec_region_code", length = 3, nullable = false)
  private String becRegionCode;

  @Column(name = "biogeoclimatic_catalogue_id", nullable = false)
  private Long biogeoclimaticCatalogueId;

  @Column(name = "site_series", length = 4, nullable = false)
  private String siteSeries;

  @Column(name = "site_series_phase", length = 3)
  private String siteSeriesPhase;

  @Column(name = "site_series_variation")
  private Short siteSeriesVariation;

  @Column(name = "seral", length = 4)
  private String seral;

  @Column(name = "description", length = 80, nullable = false)
  private String description;

  @Column(name = "effective_date", nullable = false)
  private LocalDate effectiveDate;

  @Column(name = "expiry_date", nullable = false)
  private LocalDate expiryDate;

  @Column(name = "update_timestamp", nullable = false)
  private LocalDateTime updateTimestamp;

  @Column(name = "update_userid", length = 30, nullable = false)
  private String updateUserid;
}
