package ca.bc.gov.restapi.results.oracle.dto;

import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Represents an Recent Opening in the Home screen. */
@Schema(description = "Represents an Recent Opening in the Home screen.")
public record RecentOpeningDto(
    @Schema(
            description = "System generated value uniquely identifying the opening.",
            example = "114207")
        Long openingId,
    @Schema(
            description =
                """
                File identification assigned to Provincial Forest Use files. Assigned file number.
                Usually the Licence, Tenure or Private Mark number.
                """,
            example = "TFL47")
        String fileId,
    @Schema(
            description =
                "Identifier for a cutting permit associated with a quota type harvesting tenure.",
            example = "12T")
        String cuttingPermit,
    @Schema(
            description =
                """
                Unique identifying set of characters to be stamped or marked on the end of each log
                to associate the log with the specific authority to harvest and move timber.
                """,
            example = "47/12S")
        String timberMark,
    @Schema(
            description =
                """
                Identifier for a cut block of a harvesting tenure (within a cutting permit for
                tenures with cp's).
                """,
            example = "12-69")
        String cutBlock,
    @Schema(description = "Gross area of the opening, in hectares.", example = "12.9")
        BigDecimal grossAreaHa,
    @Schema(
            description =
                """
                A code indicating the status of the prescription. Examples include but are not
                limited to DFT (draft) and APP (approved). A subset of the STATUS_CODE table.
                """,
            example = "APP")
        OpeningStatusEnum status,
    @Schema(
            description =
                """
                A code used to describe the category for the opening. The opening categories
                reference the governing applicable legislation and are determined by responsibility,
                opening origin, tenure type and prescription type.
                """,
            example = "FTML")
        OpeningCategoryEnum category,
    @Schema(description = "Actual date that harvesting started on the cut block.")
        LocalDate disturbanceStart,
    @Schema(description = "The date and time the information was entered.")
        LocalDateTime entryTimestamp,
    @Schema(description = "The date and time of the last update.") LocalDateTime updateTimestamp) {}
