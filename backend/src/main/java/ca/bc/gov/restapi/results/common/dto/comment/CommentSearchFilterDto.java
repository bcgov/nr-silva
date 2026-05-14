package ca.bc.gov.restapi.results.common.dto.comment;

import ca.bc.gov.restapi.results.common.SilvaConstants;
import ca.bc.gov.restapi.results.common.enums.CommentLocationCode;
import ca.bc.gov.restapi.results.common.util.StringUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

/** Filters for the comment search endpoint. */
@Getter
@ToString
public class CommentSearchFilterDto {

  @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
  private final String searchTerm;

  @Schema(nullable = true)
  private final CommentLocationCode commentLocation;

  @Schema(type = "array", nullable = true)
  private final List<String> clientNumbers;

  @Schema(type = "array", nullable = true)
  private final List<String> orgUnits;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateStart;

  @Schema(type = "string", format = "date", nullable = true)
  private final String updateDateEnd;

  public CommentSearchFilterDto(
      String searchTerm,
      CommentLocationCode commentLocation,
      List<String> clientNumbers,
      List<String> orgUnits,
      String updateDateStart,
      String updateDateEnd) {
    this.searchTerm = searchTerm;
    this.commentLocation = commentLocation;
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
   * Returns the commentLocation as a string for use in SpEL expressions in native queries. Returns
   * "NOVALUE" when no location filter is specified.
   */
  public String getCommentLocationValue() {
    return commentLocation != null ? commentLocation.name() : SilvaConstants.NOVALUE;
  }
}
