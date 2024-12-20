package ca.bc.gov.restapi.results.common.dto;

import ca.bc.gov.restapi.results.common.enums.ForestClientStatusEnum;
import ca.bc.gov.restapi.results.common.enums.ForestClientTypeEnum;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.With;
import org.springframework.data.annotation.Transient;

/**
 * This record represents a Forest Client object.
 */
@Builder
@With
public record ForestClientDto(
    String clientNumber,
    String clientName,
    String legalFirstName,
    String legalMiddleName,
    ForestClientStatusEnum clientStatusCode,
    ForestClientTypeEnum clientTypeCode,
    String acronym
) {

  @Transient
  public String name() {
    if (Objects.equals(this.clientTypeCode, "I")) {
      return Stream.of(this.legalFirstName, this.legalMiddleName, this.clientName)
          .filter(Objects::nonNull)
          .map(String::trim)
          .collect(Collectors.joining(" "));
    } else {
      return this.clientName;
    }
  }

}
