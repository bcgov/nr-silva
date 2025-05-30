package ca.bc.gov.restapi.results.oracle.dto.opening;

import java.time.LocalDate;
import java.util.List;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientLocationDto;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.comment.CommentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.With;

@With
public record OpeningDetailsActivitiesDisturbanceDto(
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    Long atuId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto disturbance,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto system,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto variant,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    CodeDescriptionDto cutPhase,

    @Schema(types = {"number", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    Float disturbanceArea,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate lastUpdate,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate startDate,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate endDate,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String licenseeActivityId,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    ForestClientDto forestClient,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    ForestClientLocationDto forestClientLocation,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String licenceNumber,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String cuttingPermitId,

    @Schema(types = {"string", "null"}, requiredMode = Schema.RequiredMode.REQUIRED)
    String cutBlock,

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    List<CommentDto> comments
) {

}
