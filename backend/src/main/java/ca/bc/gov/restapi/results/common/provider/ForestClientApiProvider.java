package ca.bc.gov.restapi.results.common.provider;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

/**
 * This class contains methods to integrate SILVA REST API with ForestClient API.
 */
@Slf4j
@Component
public class ForestClientApiProvider {

  private final RestClient restClient;

  private static final String PROVIDER = "ForestClient API";

  ForestClientApiProvider(@Qualifier("forestClientApi") RestClient forestClientApi) {
    this.restClient = forestClientApi;
  }

  /**
   * Fetch a ForestClient by its number.
   *
   * @param number the client number to search for
   * @return the ForestClient with client number, if one exists
   */
  public Optional<ForestClientDto> fetchClientByNumber(String number) {

    log.info("Starting {} request to /clients/findByClientNumber/{number}", PROVIDER);

    try {

      return
          Optional
              .ofNullable(
                  restClient
                      .get()
                      .uri("/clients/findByClientNumber/{number}", number)
                      .retrieve()
                      .body(ForestClientDto.class)
              );
    } catch (HttpClientErrorException httpExc) {
      log.error("Finished {} request - Response code error: {}", PROVIDER, httpExc.getStatusCode());
    }

    return Optional.empty();
  }
}
