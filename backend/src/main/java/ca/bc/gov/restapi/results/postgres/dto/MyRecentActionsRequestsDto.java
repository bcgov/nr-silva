package ca.bc.gov.restapi.results.postgres.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.With;

/**
 * This record represents an opening activity in the requests tab.
 */
@Builder
@With
public record MyRecentActionsRequestsDto(
    @Schema(description = "Full description of the action made by the user.", example = "Update")
    String activityType,
    @Schema(
        description = "System generated value uniquely identifying the opening.",
        example = "1541297")
    Long openingId,
    @Schema(
        description = """
            A code indicating the status of the prescription. Examples include but are not
            limited to DFT (draft) and APP (approved). A subset of the STATUS_CODE table.
            """,
        example = "APP")
    String statusCode,
    @Schema(
        description = """
            The code description indicating the status of the prescription. Examples include but
            are not limited to Draft (DFT) and Approved (APP). A subset of the STATUS_CODE table.
            """,
        example = "Approved")
    String statusDescription,
    @Schema(
        description = "The date and time of the activity action in for 'time ago' format",
        example = "1 minute ago")
    String lastUpdatedLabel,
    @Schema(description = "The date and time of the activity action") LocalDateTime lastUpdated) {

}
