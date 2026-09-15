package ca.bc.gov.restapi.results.postgres.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/** Entity for silva.standards_regime_site_series — BEC data for a Stocking Standard. */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "standards_regime_site_series")
public class StandardsRegimeSiteSeriesEntity {

  @Id
  @Column(name = "standard_regime_site_series_id")
  private Long id;

  @Column(name = "standards_regime_id", nullable = false)
  private Long standardsRegimeId;

  @Column(name = "bec_region_code", length = 3)
  private String becRegionCode;

  @Column(name = "bgc_zone_code", length = 4)
  private String bgcZoneCode;

  @Column(name = "bgc_subzone_code", length = 3)
  private String bgcSubzoneCode;

  @Column(name = "bgc_variant", length = 1)
  private String bgcVariant;

  @Column(name = "bgc_phase", length = 1)
  private String bgcPhase;

  @Column(name = "bec_site_series", length = 4)
  private String becSiteSeries;

  @Column(name = "bec_site_type", length = 3)
  private String becSiteType;

  @Column(name = "bec_seral", length = 4)
  private String becSeral;

  @Column(name = "entry_userid", length = 30, nullable = false)
  private String entryUserid;

  @Column(name = "entry_timestamp", nullable = false)
  private LocalDateTime entryTimestamp;

  @Column(name = "update_userid", length = 30, nullable = false)
  private String updateUserid;

  @Column(name = "update_timestamp", nullable = false)
  private LocalDateTime updateTimestamp;

  @Column(name = "revision_count", nullable = false)
  private Integer revisionCount;
}
