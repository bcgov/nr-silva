package ca.bc.gov.restapi.results.oracle.dto.opening;

import ca.bc.gov.restapi.results.oracle.dto.CodeDescriptionDto;
import java.time.LocalDate;

public record OpeningDetailsActivitiesActivitiesDto(
    Long atuId,
    CodeDescriptionDto status,
    CodeDescriptionDto base,
    CodeDescriptionDto tech,
    CodeDescriptionDto method,
    CodeDescriptionDto objective1,
    CodeDescriptionDto objective2,
    CodeDescriptionDto objective3,
    Float area,
    CodeDescriptionDto funding,
    String projectId,
    LocalDate lastUpdate,
    LocalDate plannedDate,
    LocalDate endDate
) {

}
