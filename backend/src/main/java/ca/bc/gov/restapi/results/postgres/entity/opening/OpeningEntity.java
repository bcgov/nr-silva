package ca.bc.gov.restapi.results.postgres.entity.opening;

import ca.bc.gov.restapi.results.common.entity.BaseOpeningEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Persistable;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "silva", name = "opening")
@AttributeOverrides({
  @AttributeOverride(name = "id", column = @Column(name = "opening_id")),
  @AttributeOverride(name = "status", column = @Column(name = "opening_status_code", length = 3)),
  @AttributeOverride(name = "category", column = @Column(name = "open_category_code", length = 7)),
  @AttributeOverride(name = "entryUserId", column = @Column(name = "entry_userid", length = 30)),
  @AttributeOverride(name = "updateTimestamp", column = @Column(name = "update_timestamp")),
  @AttributeOverride(name = "entryTimestamp", column = @Column(name = "entry_timestamp")),
  @AttributeOverride(name = "geoDistrictNo", column = @Column(name = "geo_district_no")),
  @AttributeOverride(name = "adminDistrictNo", column = @Column(name = "admin_district_no")),
  @AttributeOverride(name = "orgUnitNo", column = @Column(name = "org_unit_no")),
  @AttributeOverride(
      name = "maxAllowPermntAccessPct",
      column = @Column(name = "max_allow_permnt_access_pct", precision = 3, scale = 1)),
  @AttributeOverride(
      name = "licenseeOpeningId",
      column = @Column(name = "licensee_opening_id", length = 30)),
  @AttributeOverride(name = "amendmentInd", column = @Column(name = "amendment_ind", length = 1)),
  @AttributeOverride(name = "mapsheetGrid", column = @Column(name = "mapsheet_grid", length = 3)),
  @AttributeOverride(
      name = "mapsheetLetter",
      column = @Column(name = "mapsheet_letter", length = 1)),
  @AttributeOverride(
      name = "mapsheetSquare",
      column = @Column(name = "mapsheet_square", length = 3)),
  @AttributeOverride(name = "mapsheetQuad", column = @Column(name = "mapsheet_quad", length = 1)),
  @AttributeOverride(
      name = "mapsheetSubQuad",
      column = @Column(name = "mapsheet_sub_quad", length = 1)),
  @AttributeOverride(name = "openingNumber", column = @Column(name = "opening_number", length = 4)),
  @AttributeOverride(name = "revisionCount", column = @Column(name = "revision_count")),
  @AttributeOverride(name = "updateUserId", column = @Column(name = "update_userid", length = 30))
})
public class OpeningEntity extends BaseOpeningEntity implements Persistable<Long> {

  /** Silva-owned source of truth for the opening's gross area in hectares. */
  @Column(name = "opening_gross_area", precision = 11, scale = 4)
  private BigDecimal openingGrossArea;

  @Transient private boolean isNew;

  @Override
  public Long getId() {
    return super.getId();
  }

  @Override
  public boolean isNew() {
    return isNew;
  }
}
