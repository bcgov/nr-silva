package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientLocationDto;
import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import java.time.LocalDate;

public record OpeningDetailsActivitiesDisturbanceDto(
    Long atuId,
    CodeDescriptionDto disturbance,
    CodeDescriptionDto system,
    CodeDescriptionDto variant,
    CodeDescriptionDto cutPhase,
    Float disturbanceArea,
    LocalDate startDate,
    LocalDate endDate,
    String licenseeActivityId,
    ForestClientDto forestClient,
    ForestClientLocationDto forestClientLocation,
    String licenceNumber,
    String cuttingPermitId,
    String cutBlock
) {

}
