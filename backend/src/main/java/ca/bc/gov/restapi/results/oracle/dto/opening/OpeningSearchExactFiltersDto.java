package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.SilvaOracleConstants;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.CollectionUtils;

/** This record contains all possible filters when using the Opening Search Exact API. */
@Slf4j
@Getter
@ToString
public class OpeningSearchExactFiltersDto {

  private final Long openingId;
  private final List<String> categories;
  private final List<String> openingStatuses;
  private final String licenseNumber;
  private final String licenseeOpeningId;
  private final String entryDateStart;
  private final String entryDateEnd;
  private final String cutBlockId;
  private final String cuttingPermitId;
  private final String timberMark;
  private final List<String> orgUnits;
  private final List<String> clientNumbers;
  private final Boolean isCreatedByUser;
  private final String submittedToFrpa;
  private final String mapsheetGrid;
  private final String mapsheetLetter;
  private final String mapsheetSquare;
  private final String mapsheetQuad;
  private final String mapsheetSubQuad;
  private final String subOpeningNumber;

  @Setter private String requestUserId;

  /** Creates an instance of the exact search opening filter dto. */
  public OpeningSearchExactFiltersDto(
      Long openingId,
      List<String> categories,
      List<String> openingStatuses,
      String licenseNumber,
      String licenseeOpeningId,
      String entryDateStart,
      String entryDateEnd,
      String cutBlockId,
      String cuttingPermitId,
      String timberMark,
      List<String> orgUnit,
      List<String> clientNumbers,
      Boolean isCreatedByUser,
      Boolean submittedToFrpa,
      String mapsheetGrid,
      String mapsheetLetter,
      String mapsheetSquare,
      String mapsheetQuad,
      String mapsheetSubQuad,
      String subOpeningNumber) {
    this.openingId = openingId;
    this.categories =
        !CollectionUtils.isEmpty(categories) ? categories : List.of(SilvaOracleConstants.NOVALUE);
    this.openingStatuses =
        !CollectionUtils.isEmpty(openingStatuses)
            ? openingStatuses
            : List.of(SilvaOracleConstants.NOVALUE);
    this.licenseNumber = Objects.isNull(licenseNumber) ? null : licenseNumber.toUpperCase().trim();
    this.licenseeOpeningId =
        Objects.isNull(licenseeOpeningId) ? null : licenseeOpeningId.toUpperCase().trim();
    this.entryDateStart = Objects.isNull(entryDateStart) ? null : entryDateStart.trim();
    this.entryDateEnd = Objects.isNull(entryDateEnd) ? null : entryDateEnd.trim();
    this.cutBlockId = Objects.isNull(cutBlockId) ? null : cutBlockId.toUpperCase().trim();
    this.cuttingPermitId =
        Objects.isNull(cuttingPermitId) ? null : cuttingPermitId.toUpperCase().trim();
    this.timberMark = Objects.isNull(timberMark) ? null : timberMark.toUpperCase().trim();
    this.orgUnits =
        !CollectionUtils.isEmpty(orgUnit) ? orgUnit : List.of(SilvaOracleConstants.NOVALUE);
    this.clientNumbers =
        !CollectionUtils.isEmpty(clientNumbers)
            ? clientNumbers
            : List.of(SilvaOracleConstants.NOVALUE);
    this.isCreatedByUser = isCreatedByUser;
    this.submittedToFrpa = BooleanUtils.toString(submittedToFrpa, "YES", "NO", "NO");
    this.mapsheetGrid = Objects.isNull(mapsheetGrid) ? null : mapsheetGrid.trim();
    this.mapsheetLetter = Objects.isNull(mapsheetLetter) ? null : mapsheetLetter.trim();
    this.mapsheetSquare = Objects.isNull(mapsheetSquare) ? null : mapsheetSquare.trim();
    this.mapsheetQuad = Objects.isNull(mapsheetQuad) ? null : mapsheetQuad.trim();
    this.mapsheetSubQuad = Objects.isNull(mapsheetSubQuad) ? null : mapsheetSubQuad.trim();
    this.subOpeningNumber = Objects.isNull(subOpeningNumber) ? null : subOpeningNumber.trim();
  }

  /**
   * Define if a property has value.
   *
   * @param prop The property to be checked.
   * @return True if it has, false otherwise.
   */
  public boolean hasValue(String prop) {
    return switch (prop) {
      case SilvaOracleConstants.OPENING_ID -> !Objects.isNull(this.openingId);
      case SilvaOracleConstants.CATEGORIES -> !Objects.isNull(this.categories)
          && !this.categories.isEmpty();
      case SilvaOracleConstants.OPENING_STATUSES -> !Objects.isNull(this.openingStatuses)
          && !this.openingStatuses.isEmpty();
      case SilvaOracleConstants.LICENSE_NUMBER -> !Objects.isNull(this.licenseNumber);
      case SilvaOracleConstants.LICENSEE_OPENING_ID -> !Objects.isNull(this.licenseeOpeningId);
      case SilvaOracleConstants.ENTRY_DATE_START -> !Objects.isNull(this.entryDateStart);
      case SilvaOracleConstants.ENTRY_DATE_END -> !Objects.isNull(this.entryDateEnd);
      case SilvaOracleConstants.CUT_BLOCK_ID -> !Objects.isNull(this.cutBlockId);
      case SilvaOracleConstants.CUTTING_PERMIT_ID -> !Objects.isNull(this.cuttingPermitId);
      case SilvaOracleConstants.TIMBER_MARK -> !Objects.isNull(this.timberMark);
      case SilvaOracleConstants.ORG_UNITS -> !Objects.isNull(this.orgUnits)
          && !this.orgUnits.isEmpty();
      case SilvaOracleConstants.CLIENT_NUMBERS -> !Objects.isNull(this.clientNumbers)
          && !this.clientNumbers.isEmpty();
      case SilvaOracleConstants.IS_CREATED_BY_USER -> !Objects.isNull(this.isCreatedByUser);
      case SilvaOracleConstants.SUBMITTED_TO_FRPA -> !Objects.isNull(this.submittedToFrpa);
      case SilvaOracleConstants.MAPSHEET_GRID -> !Objects.isNull(this.mapsheetGrid);
      case SilvaOracleConstants.MAPSHEET_LETTER -> !Objects.isNull(this.mapsheetLetter);
      case SilvaOracleConstants.MAPSHEET_SQUARE -> !Objects.isNull(this.mapsheetSquare);
      case SilvaOracleConstants.MAPSHEET_QUAD -> !Objects.isNull(this.mapsheetQuad);
      case SilvaOracleConstants.MAPSHEET_SUB_QUAD -> !Objects.isNull(this.mapsheetSubQuad);
      case SilvaOracleConstants.SUB_OPENING_NUMBER -> !Objects.isNull(this.subOpeningNumber);
      default -> {
        log.warn("Prop not found {}", prop);
        yield false;
      }
    };
  }

  /**
   * Check if at least one search filter is provided.
   *
   * @return True if at least one filter has a value, false otherwise.
   */
  public boolean hasAnyFilter() {
    return hasValue(SilvaOracleConstants.OPENING_ID)
        || hasValue(SilvaOracleConstants.CATEGORIES)
        || hasValue(SilvaOracleConstants.OPENING_STATUSES)
        || hasValue(SilvaOracleConstants.LICENSE_NUMBER)
        || hasValue(SilvaOracleConstants.LICENSEE_OPENING_ID)
        || hasValue(SilvaOracleConstants.ENTRY_DATE_START)
        || hasValue(SilvaOracleConstants.ENTRY_DATE_END)
        || hasValue(SilvaOracleConstants.CUT_BLOCK_ID)
        || hasValue(SilvaOracleConstants.CUTTING_PERMIT_ID)
        || hasValue(SilvaOracleConstants.TIMBER_MARK)
        || hasValue(SilvaOracleConstants.ORG_UNITS)
        || hasValue(SilvaOracleConstants.CLIENT_NUMBERS)
        || hasValue(SilvaOracleConstants.IS_CREATED_BY_USER)
        || hasValue(SilvaOracleConstants.SUBMITTED_TO_FRPA)
        || hasValue(SilvaOracleConstants.MAPSHEET_GRID)
        || hasValue(SilvaOracleConstants.MAPSHEET_LETTER)
        || hasValue(SilvaOracleConstants.MAPSHEET_SQUARE)
        || hasValue(SilvaOracleConstants.MAPSHEET_QUAD)
        || hasValue(SilvaOracleConstants.MAPSHEET_SUB_QUAD)
        || hasValue(SilvaOracleConstants.SUB_OPENING_NUMBER);
  }
}
