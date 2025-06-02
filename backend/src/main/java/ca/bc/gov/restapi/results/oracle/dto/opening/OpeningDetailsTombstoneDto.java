package ca.bc.gov.restapi.results.oracle.dto.opening;

import java.time.LocalDate;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningDetailsTombstoneDto(
  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String openingNumber,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  CodeDescriptionDto openingStatus,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String orgUnitCode,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String orgUnitName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  CodeDescriptionDto openCategory,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ForestClientDto client,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String fileId,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String cutBlockID,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String cuttingPermitId,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String timberMark,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String maxAllowedAccess,

  @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  Float openingGrossArea,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String createdBy,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  LocalDate createdOn,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  LocalDate lastUpdatedOn,

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  LocalDate disturbanceStartDate
) {

}
