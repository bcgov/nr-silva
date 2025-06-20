package ca.bc.gov.restapi.results.oracle.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Represents the status of a notification in the opening details.",
        type = "string",
        allowableValues = {"ERROR", "WARNING", "INFO", "SUCCESS"}
)
public enum OpeningDetailsNotificationStatusEnum {
    ERROR,
    WARNING,
    INFO,
    SUCCESS;
}
