package ca.bc.gov.restapi.results.oracle.entity.opening;

import ca.bc.gov.restapi.results.common.entity.BaseOpeningEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/** This class represents an Opening in the database. */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@With
@NoArgsConstructor
@Entity
@Table(schema = "THE", name = "OPENING")
@AttributeOverrides({
  @AttributeOverride(name = "id", column = @Column(name = "OPENING_ID")),
  @AttributeOverride(name = "status", column = @Column(name = "OPENING_STATUS_CODE", length = 3)),
  @AttributeOverride(name = "category", column = @Column(name = "OPEN_CATEGORY_CODE", length = 7)),
  @AttributeOverride(name = "entryUserId", column = @Column(name = "ENTRY_USERID", length = 30)),
  @AttributeOverride(name = "updateTimestamp", column = @Column(name = "UPDATE_TIMESTAMP")),
  @AttributeOverride(name = "entryTimestamp", column = @Column(name = "ENTRY_TIMESTAMP")),
  @AttributeOverride(name = "geoDistrictNo", column = @Column(name = "GEO_DISTRICT_NO")),
  @AttributeOverride(name = "adminDistrictNo", column = @Column(name = "ADMIN_DISTRICT_NO")),
  @AttributeOverride(name = "orgUnitNo", column = @Column(name = "ORG_UNIT_NO")),
  @AttributeOverride(name = "maxAllowPermntAccessPct", column = @Column(name = "MAX_ALLOW_PERMNT_ACCESS_PCT", precision = 3, scale = 1)),
  @AttributeOverride(name = "licenseeOpeningId", column = @Column(name = "LICENSEE_OPENING_ID", length = 30)),
  @AttributeOverride(name = "amendmentInd", column = @Column(name = "AMENDMENT_IND", length = 1)),
  @AttributeOverride(name = "mapsheetGrid", column = @Column(name = "MAPSHEET_GRID", length = 3)),
  @AttributeOverride(name = "mapsheetLetter", column = @Column(name = "MAPSHEET_LETTER", length = 1)),
  @AttributeOverride(name = "mapsheetSquare", column = @Column(name = "MAPSHEET_SQUARE", length = 3)),
  @AttributeOverride(name = "mapsheetQuad", column = @Column(name = "MAPSHEET_QUAD", length = 1)),
  @AttributeOverride(name = "mapsheetSubQuad", column = @Column(name = "MAPSHEET_SUB_QUAD", length = 1)),
  @AttributeOverride(name = "openingNumber", column = @Column(name = "OPENING_NUMBER", length = 4)),
  @AttributeOverride(name = "revisionCount", column = @Column(name = "REVISION_COUNT")),
  @AttributeOverride(name = "updateUserId", column = @Column(name = "UPDATE_USERID", length = 30))
})
public class OpeningEntity extends BaseOpeningEntity {}
