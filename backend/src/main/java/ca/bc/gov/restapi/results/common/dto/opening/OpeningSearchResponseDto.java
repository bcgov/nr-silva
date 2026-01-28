package ca.bc.gov.restapi.results.common.dto.opening;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import ca.bc.gov.restapi.results.common.enums.OpeningCategoryEnum;
import ca.bc.gov.restapi.results.common.enums.OpeningStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/** This class represents a record of the opening search api response. */
@Data
@Builder
@With
@NoArgsConstructor
@AllArgsConstructor
public class OpeningSearchResponseDto {

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private Long openingId;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String openingNumber;

  @Schema(
      types = {"object", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private OpeningCategoryEnum category;

  @Schema(
      types = {"object", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private OpeningStatusEnum status;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String cuttingPermitId;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String timberMark;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String cutBlockId;

  @Schema(
      types = {"number", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private BigDecimal openingGrossAreaHa;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate disturbanceStartDate;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private String orgUnitCode;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private String orgUnitName;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String clientNumber;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String clientLocation;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String clientAcronym;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String clientName;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate regenDelayDate;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate earlyFreeGrowingDate;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate lateFreeGrowingDate;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDateTime updateTimestamp;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private String entryUserId;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDateTime entryTimestamp;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private Boolean submittedToFrpa;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private String forestFileId;

  @Schema(
      types = {"integer", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private Long silvaReliefAppId;

  @Schema(
      types = {"string", "null"},
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDateTime lastViewDate;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  private boolean favourite;

  public boolean isValid() {
    return Objects.nonNull(openingId);
  }
}
