package ca.bc.gov.restapi.results.common.dto.stockingstandards;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.enums.StockingStandardsCommentLocationCode;
import ca.bc.gov.restapi.results.common.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

@Getter
@ToString
public class StockingStandardsCommentSearchFilterDto {

  @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
  private final String searchTerm;

  @Schema(nullable = true)
  private final List<StockingStandardsCommentLocationCode> commentLocations;

  @Schema(type = "array", nullable = true)
  private final List<String> clientNumbers;

  @Schema(type = "array", nullable = true)
  private final List<String> orgUnits;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateStart;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateEnd;

  public StockingStandardsCommentSearchFilterDto(
      String searchTerm,
      List<StockingStandardsCommentLocationCode> commentLocations,
      List<String> clientNumbers,
      List<String> orgUnits,
      String updateDateStart,
      String updateDateEnd) {
    this.searchTerm = searchTerm;
    this.commentLocations = commentLocations;
    this.clientNumbers =
        !CollectionUtils.isEmpty(clientNumbers)
            ? StringUtil.toUpperCase(clientNumbers)
            : List.of(SilvaConstants.NOVALUE);
    this.orgUnits =
        !CollectionUtils.isEmpty(orgUnits)
            ? StringUtil.toUpperCase(orgUnits)
            : List.of(SilvaConstants.NOVALUE);
    this.updateDateStart = updateDateStart;
    this.updateDateEnd = updateDateEnd;
  }

  /**
   * Returns the commentLocations as a list of strings for use in SpEL expressions in native
   * queries. Returns ["NOVALUE"] when no location filter is specified.
   */
  public List<String> getCommentLocationValues() {
    if (commentLocations == null || commentLocations.isEmpty()) {
      return List.of(SilvaConstants.NOVALUE);
    }
    return commentLocations.stream().map(StockingStandardsCommentLocationCode::name).toList();
  }
}
