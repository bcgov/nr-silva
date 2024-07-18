package ca.bc.gov.restapi.results.common.service;

import ca.bc.gov.restapi.results.common.dto.ForestsClientDto;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ForestClientService {

  public Optional<ForestsClientDto> getClientByNumber(String clientNumber) {
    log.info("Getting Forests client number {}", clientNumber);
    return Optional.ofNullable(mockForestClient(clientNumber));
  }

  private ForestsClientDto mockForestClient(String clientNumber) {
    if (clientNumber.equals("00132184")) {
      return new ForestsClientDto(
          "00132184", "TIMBER SALES MANAGER BABINE", null, null, "ACT", 'F', "TBA");
    }
    if (clientNumber.equals("00012797")) {
      return new ForestsClientDto("00012797", "MINISTRY OF FORESTS", null, null, "ACT", 'F', "MOF");
    }
    if (clientNumber.equals("00001012")) {
      return new ForestsClientDto(
          "00001012", "BELL LUMBER & POLE CANADA, ULC", null, null, "ACT", 'C', "BELL");
    }
    if (clientNumber.equals("00149081")) {
      return new ForestsClientDto(
          "00149081", "WESTERN FOREST PRODUCTS INC.", null, null, "ACT", 'C', "WFP");
    }

    return null;
  }
}
