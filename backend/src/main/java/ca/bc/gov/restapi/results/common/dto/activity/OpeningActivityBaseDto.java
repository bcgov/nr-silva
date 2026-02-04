package ca.bc.gov.restapi.results.common.dto.activity;

import java.util.List;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientLocationDto;
import ca.bc.gov.restapi.results.common.dto.comment.CommentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.With;

@Data
@With
public class OpeningActivityBaseDto {

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String licenseeActivityId;

  @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  String intraAgencyNumber;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ForestClientDto activityClient;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ForestClientLocationDto activityLocation;

  @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  Float plannedAmount;

  @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  Float treatedAmount;

  @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  Float plannedCost;

  @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  Float actualCost;

  @Schema(types = {"integer", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
  Long totalPlanting;

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  List<CommentDto> comments;

  public OpeningActivityBaseDto() {
  }

  public OpeningActivityBaseDto(
      String licenseeActivityId,
      String intraAgencyNumber,
      ForestClientDto activityClient,
      ForestClientLocationDto activityLocation,
      Float plannedAmount,
      Float treatedAmount,
      Float plannedCost,
      Float actualCost,
      Long totalPlanting,
      List<CommentDto> comments
  ) {
    this.licenseeActivityId = licenseeActivityId;
    this.intraAgencyNumber = intraAgencyNumber;
    this.activityClient = activityClient;
    this.activityLocation = activityLocation;
    this.plannedAmount = plannedAmount;
    this.treatedAmount = treatedAmount;
    this.plannedCost = plannedCost;
    this.actualCost = actualCost;
    this.totalPlanting = totalPlanting;
    this.comments = comments;
  }

  public OpeningActivityBaseDto(OpeningActivityBaseDto base) {
    this.licenseeActivityId = base.licenseeActivityId;
    this.intraAgencyNumber = base.intraAgencyNumber;
    this.activityClient = base.activityClient;
    this.activityLocation = base.activityLocation;
    this.plannedAmount = base.plannedAmount;
    this.treatedAmount = base.treatedAmount;
    this.plannedCost = base.plannedCost;
    this.actualCost = base.actualCost;
    this.totalPlanting = base.totalPlanting;
    this.comments = base.comments;
  }
}
