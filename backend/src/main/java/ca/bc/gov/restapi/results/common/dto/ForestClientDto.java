package ca.bc.gov.restapi.results.common.dto;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.annotation.Transient;

import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.With;

/**
 * This record represents a Forest Client object.
 */
@Builder
@With
public record ForestClientDto(
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String clientNumber,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String clientName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String legalFirstName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String legalMiddleName,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ForestClientStatusEnum clientStatusCode,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  ForestClientTypeEnum clientTypeCode,

  @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
  String acronym,

  String name
) {

  /**
   * Returns the name of the client.
   * It resolves it based on client type code, so it can be either individual with first, middle and
   * last name, or a company with a single name.
   *
   * @return the name of the client
   */
  @Transient
  public String name() {
    if (Objects.equals(this.clientTypeCode, ForestClientTypeEnum.of('I'))) {
      return Stream.of(this.legalFirstName, this.legalMiddleName, this.clientName)
          .filter(Objects::nonNull)
          .map(String::trim)
          .collect(Collectors.joining(" "));
    } else {
      return this.clientName;
    }
  }

}
