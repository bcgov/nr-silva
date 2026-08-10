package ca.bc.gov.restapi.results.postgres.entity;

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

/** JPA entity for the silva.opening_geometry table. */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "opening_geometry")
public class OpeningGeometryEntity implements Persistable<Long> {

  @Id
  @Column(name = "opening_id")
  private Long openingId;

  @Column(name = "geometry", columnDefinition = "geometry(Geometry,3005)")
  private Geometry geometry;

  @Column(name = "feature_area", precision = 11, scale = 4)
  private BigDecimal featureArea;

  @Column(name = "feature_perimeter", precision = 11, scale = 4)
  private BigDecimal featurePerimeter;

  @Column(name = "feature_class_skey")
  private Long featureClassSkey;

  @Column(name = "entry_userid", length = 30)
  private String entryUserId;

  @Column(name = "entry_timestamp")
  private LocalDateTime entryTimestamp;

  @Column(name = "update_userid", length = 30)
  private String updateUserId;

  @Column(name = "update_timestamp")
  private LocalDateTime updateTimestamp;

  @Column(name = "revision_count")
  private Integer revisionCount;

  @Transient
  private boolean isNew;

  @Override
  public Long getId() {
    return openingId;
  }

  @Override
  public boolean isNew() {
    return isNew;
  }
}
