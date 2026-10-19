package ca.bc.gov.restapi.results.postgres.entity.activity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.domain.Persistable;

/** JPA entity for the silva.activity_treatment_geometry table (one row per activity). */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "activity_treatment_geometry")
public class ActivityTreatmentGeometryEntity implements Persistable<Long> {

  @Id
  @Column(name = "activity_treatment_unit_id")
  private Long activityTreatmentUnitId;

  @Column(name = "geometry", columnDefinition = "geometry(Geometry,3005)", nullable = false)
  private Geometry geometry;

  @Column(name = "feature_area", precision = 11, scale = 4, nullable = false)
  private BigDecimal featureArea;

  @Column(name = "feature_perimeter", precision = 11, scale = 4, nullable = false)
  private BigDecimal featurePerimeter;

  @Column(name = "capture_method_code", length = 30)
  private String captureMethodCode;

  @Column(name = "feature_class_skey", nullable = false)
  private Long featureClassSkey;

  @Column(name = "data_source_code", length = 10)
  private String dataSourceCode;

  @Column(name = "observation_date")
  private LocalDateTime observationDate;

  @Column(name = "data_quality_comment", length = 255)
  private String dataQualityComment;

  @Column(name = "entry_userid", length = 30, nullable = false)
  private String entryUserId;

  @Column(name = "entry_timestamp", nullable = false)
  private LocalDateTime entryTimestamp;

  @Column(name = "update_userid", length = 30, nullable = false)
  private String updateUserId;

  @Column(name = "update_timestamp", nullable = false)
  private LocalDateTime updateTimestamp;

  @Column(name = "revision_count", nullable = false)
  private Integer revisionCount;

  @Transient private boolean isNew;

  @Override
  public Long getId() {
    return activityTreatmentUnitId;
  }

  @Override
  public boolean isNew() {
    return isNew;
  }
}
