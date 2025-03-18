package ca.bc.gov.restapi.results.common.dto;

import ca.bc.gov.restapi.results.common.enums.YesNoEnum;
import java.time.LocalDate;
import lombok.With;

/**
 * This record represents a Forest Client Location object.
 */
@With
public record ForestClientLocationDto(
    String clientNumber,
    String locationCode,
    String locationName,
    String companyCode,
    String address1,
    String address2,
    String address3,
    String city,
    String province,
    String postalCode,
    String country,
    String businessPhone,
    String homePhone,
    String cellPhone,
    String faxNumber,
    String email,
    YesNoEnum expired,
    YesNoEnum trusted,
    LocalDate returnedMailDate,
    String comment
) {

}
