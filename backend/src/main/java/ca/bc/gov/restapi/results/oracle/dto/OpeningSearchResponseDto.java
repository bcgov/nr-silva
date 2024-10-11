package ca.bc.gov.restapi.results.oracle.dto;

import ca.bc.gov.restapi.results.oracle.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.oracle.enums.OpeningStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

/**
 * This class represents a record of the opening search api response.
 */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class OpeningSearchResponseDto {

  @Schema(
      description = "System generated value uniquely identifying the opening.",
      example = "114207")
  private Integer openingId;

  @Schema(
      description =
          """
              An unique identifier up to four characters long that describes the opening on a
              specified mapsheet.""",
      example = "1234")
  private String openingNumber;

  @Schema(
      description =
          """
              A code used to describe the category for the opening. The opening categories
              reference the governing applicable legislation and are determined by responsibility,
              opening origin, tenure type and prescription type.
              """,
      example = "FTML")
  private OpeningCategoryEnum category;

  @Schema(
      description =
          """
              A code indicating the status of the prescription. Examples include but are not
              limited to DFT (draft) and APP (approved). A subset of the STATUS_CODE table.
              """,
      example = "APP")
  private OpeningStatusEnum status;

  @Schema(
      description =
          "Identifier for a cutting permit associated with a quota type harvesting tenure.",
      example = "12T")
  private String cuttingPermitId;

  @Schema(
      description =
          """
              Unique identifying set of characters to be stamped or marked on the end of each log
              to associate the log with the specific authority to harvest and move timber.
              """,
      example = "47/12S")
  private String timberMark;

  @Schema(
      description =
          """
              Identifier for a cut block of a harvesting tenure (within a cutting permit for
              tenures with cp's).
              """,
      example = "12-69")
  private String cutBlockId;

  @Schema(description = "Gross area of the opening, in hectares.", example = "12.9")
  private BigDecimal openingGrossAreaHa;

  @Schema(description = "Actual date that harvesting started on the cut block.")
  private LocalDateTime disturbanceStartDate;

  @Schema(
      description =
          """
              Identifies any office within the ministry. First character identifiesExec,
              HQ Branch, Region, or District; next two chars identify the office name; next two
              the section (HQ Branch) or program (Region or District); last char identifies the
              subsection.""",
      example = "DPG")
  private String orgUnitCode;

  @Schema(
      description =
          """
              The name or title of a ministry office or section; for example Kamloops Forest
              Region; Silviculture Branch; Kispiox Forest District Protection program.
              """,
      example = "Prince George Natural District")
  private String orgUnitName;

  @Schema(
      description = "Sequentially assigned number to identify a ministry client.",
      example = "00012797")
  private String clientNumber;

  @Schema(
      description = "Sequentially assigned number to identify a ministry client location.",
      example = "01")
  private String clientLocation;

  @Schema(
      description =
          """
              A familiar alphabetic acronym to be used as an alternative to the Ministry's Client
              Number for data entry and display.
              """,
      example = "MOF")
  private String clientAcronym;

  private String clientName;

  @Schema(description = "The final date based on the EARLY and LATE offset years.")
  private LocalDateTime regenDelayDate;

  @Schema(description = "The final date based on the EARLY offset years.")
  private LocalDateTime earlyFreeGrowingDate;

  @Schema(description = "The final date based on the LATE offset years.")
  private LocalDateTime lateFreeGrowingDate;

  @Schema(description = "The date and time of the last update.")
  private LocalDateTime updateTimestamp;

  @Schema(description = "The USERID of the individual who entered the information.")
  private String entryUserId;

  @Schema(description = "Describes if the opening got submitted to FRPA section 108")
  private Boolean submittedToFrpa;

  @Schema(description = "Uniquely identifies the attached file.", example = "407")
  private String forestFileId;

  @Schema(
      description = "Uniquely identifies an Application for Relief from obligations.",
      example = "56")
  private Long silvaReliefAppId;
}
