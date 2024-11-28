package ca.bc.gov.restapi.results.common.provider;

import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import ca.bc.gov.restapi.results.common.dto.ForestClientLocationDto;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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

    log.info("Starting {} request to /clients/findByClientNumber/{}", PROVIDER,number);

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
    } catch (HttpClientErrorException | HttpServerErrorException httpExc) {
      log.error("Finished {} request - Response code error: {}", PROVIDER, httpExc.getStatusCode());
    }

    return Optional.empty();
  }

  public List<ForestClientDto> searchClients(
      int page,
      int size,
      String value
  ) {
    log.info("Starting {} request to /clients/search/by?name={}&acronym={}&number={}", PROVIDER,value,value,value);

    try {
      return
            restClient
                .get()
                .uri(uriBuilder ->
                    uriBuilder
                        .path("/clients/search/by")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParam("name", value)
                        .queryParam("acronym", value)
                        .queryParam("number", value)
                        .build()
                    )
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    } catch (HttpClientErrorException | HttpServerErrorException httpExc) {
      log.error("{} requested on search by - Response code error: {}", PROVIDER, httpExc.getStatusCode());
    }

    return List.of();
  }

  public List<ForestClientLocationDto> fetchLocationsByClientNumber(String clientNumber) {
    log.info("Starting {} request to /clients/{}/locations", PROVIDER, clientNumber);

    try {
      return
          restClient
              .get()
              .uri(uriBuilder ->
                  uriBuilder
                      .path("/clients/{clientNumber}/locations")
                      .queryParam("page",0)
                      .queryParam("size",100)
                      .build(clientNumber)
                  )
              .retrieve()
              .body(new ParameterizedTypeReference<>() {});
    } catch (HttpClientErrorException | HttpServerErrorException httpExc) {
      log.error("Client location {} request - Response code error: {}", PROVIDER, httpExc.getStatusCode());
    }

    return List.of();
  }
}
