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
  private final List<String> category;
  private final List<String> statusList;
  private final String licenseNumber;
  private final String licenseeOpeningId;
  private final String entryDateStart;
  private final String entryDateEnd;
  private final String cutBlockId;
  private final String cuttingPermitId;
  private final String timberMark;
  private final List<String> orgUnit;
  private final List<String> clientNumbers;
  private final Boolean isCreatedByUser;
  private final String submittedToFrpa;

  @Setter private String requestUserId;

  /** Creates an instance of the exact search opening filter dto. */
  public OpeningSearchExactFiltersDto(
      Long openingId,
      List<String> category,
      List<String> statusList,
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
      Boolean submittedToFrpa) {
    this.openingId = openingId;
    this.category =
        !CollectionUtils.isEmpty(category) ? category : List.of(SilvaOracleConstants.NOVALUE);
    this.statusList =
        !CollectionUtils.isEmpty(statusList) ? statusList : List.of(SilvaOracleConstants.NOVALUE);
    this.licenseNumber = Objects.isNull(licenseNumber) ? null : licenseNumber.toUpperCase().trim();
    this.licenseeOpeningId =
        Objects.isNull(licenseeOpeningId) ? null : licenseeOpeningId.toUpperCase().trim();
    this.entryDateStart = Objects.isNull(entryDateStart) ? null : entryDateStart.trim();
    this.entryDateEnd = Objects.isNull(entryDateEnd) ? null : entryDateEnd.trim();
    this.cutBlockId = Objects.isNull(cutBlockId) ? null : cutBlockId.toUpperCase().trim();
    this.cuttingPermitId =
        Objects.isNull(cuttingPermitId) ? null : cuttingPermitId.toUpperCase().trim();
    this.timberMark = Objects.isNull(timberMark) ? null : timberMark.toUpperCase().trim();
    this.orgUnit =
        !CollectionUtils.isEmpty(orgUnit) ? orgUnit : List.of(SilvaOracleConstants.NOVALUE);
    this.clientNumbers =
        !CollectionUtils.isEmpty(clientNumbers)
            ? clientNumbers
            : List.of(SilvaOracleConstants.NOVALUE);
    this.isCreatedByUser = isCreatedByUser;
    this.submittedToFrpa = BooleanUtils.toString(submittedToFrpa, "YES", "NO", "NO");
  }

  /**
   * Define if a property has value.
   *
   * @param prop The property to be checked.
   * @return True if it has, false otherwise.
   */
  public boolean hasValue(String prop) {
    return switch (prop) {
      case "openingId" -> !Objects.isNull(this.openingId);
      case SilvaOracleConstants.CATEGORY -> !Objects.isNull(this.category)
          && !this.category.isEmpty();
      case SilvaOracleConstants.STATUS_LIST -> !Objects.isNull(this.statusList)
          && !this.statusList.isEmpty();
      case "licenseNumber" -> !Objects.isNull(this.licenseNumber);
      case "licenseeOpeningId" -> !Objects.isNull(this.licenseeOpeningId);
      case "entryDateStart" -> !Objects.isNull(this.entryDateStart);
      case "entryDateEnd" -> !Objects.isNull(this.entryDateEnd);
      case SilvaOracleConstants.CUT_BLOCK_ID -> !Objects.isNull(this.cutBlockId);
      case SilvaOracleConstants.CUTTING_PERMIT_ID -> !Objects.isNull(this.cuttingPermitId);
      case SilvaOracleConstants.TIMBER_MARK -> !Objects.isNull(this.timberMark);
      case SilvaOracleConstants.ORG_UNIT -> !Objects.isNull(this.orgUnit)
          && !this.orgUnit.isEmpty();
      case "clientNumbers" -> !Objects.isNull(this.clientNumbers) && !this.clientNumbers.isEmpty();
      case "isCreatedByUser" -> !Objects.isNull(this.isCreatedByUser);
      case SilvaOracleConstants.SUBMITTED_TO_FRPA -> !Objects.isNull(this.submittedToFrpa);
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
    return hasValue("openingId")
        || hasValue(SilvaOracleConstants.CATEGORY)
        || hasValue(SilvaOracleConstants.STATUS_LIST)
        || hasValue("licenseNumber")
        || hasValue("licenseeOpeningId")
        || hasValue("entryDateStart")
        || hasValue("entryDateEnd")
        || hasValue(SilvaOracleConstants.CUT_BLOCK_ID)
        || hasValue(SilvaOracleConstants.CUTTING_PERMIT_ID)
        || hasValue(SilvaOracleConstants.TIMBER_MARK)
        || hasValue(SilvaOracleConstants.ORG_UNIT)
        || hasValue("clientNumbers")
        || hasValue("isCreatedByUser")
        || hasValue(SilvaOracleConstants.SUBMITTED_TO_FRPA);
  }
}
