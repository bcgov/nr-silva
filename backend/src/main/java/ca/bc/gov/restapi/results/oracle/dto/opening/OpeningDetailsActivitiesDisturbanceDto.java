package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientLocationDto;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import ca.bc.gov.restapi.results.oracle.dto.comment.CommentDto;
import java.time.LocalDate;
import java.util.List;
import lombok.With;

@With
public record OpeningDetailsActivitiesDisturbanceDto(
    Long atuId,
    CodeDescriptionDto disturbance,
    CodeDescriptionDto system,
    CodeDescriptionDto variant,
    CodeDescriptionDto cutPhase,
    Float disturbanceArea,
    LocalDate lastUpdate,
    LocalDate startDate,
    LocalDate endDate,
    String licenseeActivityId,
    ForestClientDto forestClient,
    ForestClientLocationDto forestClientLocation,
    String licenceNumber,
    String cuttingPermitId,
    String cutBlock,
    List<CommentDto> comments
) {

}
