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
