package ca.bc.gov.restapi.results.oracle.dto;

import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SearchOpeningDto(
    @Schema(
            description = "System generated value uniquely identifying the opening.",
            example = "114207")
        Long openingId,
    @Schema(
            description =
                """
                An unique identifier up to four characters long that describes the opening on a
                specified mapsheet.""",
            example = "1234")
        String openingNumber,
    @Schema(
            description =
                """
                A code used to describe the category for the opening. The opening categories
                reference the governing applicable legislation and are determined by responsibility,
                opening origin, tenure type and prescription type.
                """,
            example = "FTML")
        OpeningCategoryEnum category,
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
                "Identifier for a cutting permit associated with a quota type harvesting tenure.",
            example = "12T")
        String cuttingPermitId,
    @Schema(
            description =
                """
                Unique identifying set of characters to be stamped or marked on the end of each log
                to associate the log with the specific authority to harvest and move timber.
                """,
            example = "47/12S")
        String timberMarkId,
    @Schema(
            description =
                """
                Identifier for a cut block of a harvesting tenure (within a cutting permit for
                tenures with cp's).
                """,
            example = "12-69")
        String cutBlockId,
    @Schema(description = "Gross area of the opening, in hectares.", example = "12.9")
        BigDecimal grossAreaHa,
    @Schema(description = "Actual date that harvesting started on the cut block.")
        LocalDate disturbanceDate,
    @Schema(
            description =
                """
                Unique physical identifier for the storage of organizational unit codes for the
                Ministry of Forests' offices. Values stored here are for the computer's use only,
                and are not to be used by people as "ministry codes.
                """,
            example = "?")
        Long orgUnitNo,
    @Schema(
            description =
                """
                Identifies any office within the ministry. First character identifiesExec,
                HQ Branch, Region, or District; next two chars identify the office name; next two
                the section (HQ Branch) or program (Region or District); last char identifies the
                subsection.""",
            example = "?")
        String orgUnitCode,
    @Schema(
            description =
                """
                The name or title of a ministry office or section; for example Kamloops Forest
                Region; Silviculture Branch; Kispiox Forest District Protection program.
                """,
            example = "Silviculture Branch")
        String orgUnitName,
    @Schema(
            description = "Sequentially assigned number to identify a ministry client.",
            example = "00012797")
        String clientNumber,
    @Schema(
            description =
                """
                A familiar alphabetic acronym to be used as an alternative to the Ministry's Client
                Number for data entry and display.
                """,
            example = "MOF")
        String clientAcronym,
    // https://www.for.gov.bc.ca/pscripts/isb/idd/Column.asp?Name=STOCKING_MILESTONE&PlatformName=ORACLE
    String regenDelayDate,
    // https://www.for.gov.bc.ca/pscripts/isb/idd/Column.asp?Name=STOCKING_MILESTONE&PlatformName=ORACLE
    String freeGrowingDate,
    @Schema(description = "The date and time of the last update.") LocalDateTime updateTimestamp,
    @Schema(description = "The USERID of the individual who entered the information.")
        String entryUserId,
    Boolean submittedToFrpa) {}
