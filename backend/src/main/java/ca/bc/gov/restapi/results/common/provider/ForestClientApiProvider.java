package ca.bc.gov.restapi.results.common.provider;

import ca.bc.gov.restapi.results.common.config.ProvidersConfig;
import ca.bc.gov.restapi.results.common.dto.ForestClientDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ForestClientApiProvider {

  private final ProvidersConfig providersConfig;

  private final RestTemplate restTemplate;

  private final String rootUri;

  private static final String PROVIDER = "ForestClient API";

  ForestClientApiProvider(ProvidersConfig providersConfig, RestTemplateBuilder templateBuilder) {
    this.providersConfig = providersConfig;
    this.restTemplate = templateBuilder.build();
    this.rootUri = providersConfig.getForestClientBaseUri();
  }

  /**
   * Fetch a ForestClient by its number.
   *
   * @param number the client number to search for
   * @return the ForestClient with client number, if one exists
   */
  public Optional<ForestClientDto> fetchClientByNumber(String number) {
    String apiUrl = String.format("%s/clients/findByClientNumber/{number}", rootUri);
    log.info("Starting {} request to {}", PROVIDER, apiUrl);

    try {
      ResponseEntity<ForestClientDto> response =
          restTemplate.exchange(
              apiUrl,
              HttpMethod.GET,
              new HttpEntity<>(addHttpHeaders()),
              ForestClientDto.class,
              createParamsMap("number", number));

      log.info("Finished {} request for function {} - 200 OK!", PROVIDER, "fetchClientByNumber");
      return Optional.of(response.getBody());
    } catch (HttpClientErrorException httpExc) {
      log.error("Finished {} request - Response code error: {}", PROVIDER, httpExc.getStatusCode());
    }

    return Optional.empty();
  }

  private String[] addAuthorizationHeader() {
    String apiKey = this.providersConfig.getForestClientApiKey();
    return new String[] {"X-API-KEY", apiKey};
  }

  private HttpHeaders addHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    String[] authorization = addAuthorizationHeader();
    headers.set(authorization[0], authorization[1]);

    return headers;
  }

  private Map<String, String> createParamsMap(String... values) {
    Map<String, String> uriVars = new HashMap<>();
    for (int i = 0; i < values.length; i += 2) {
      uriVars.put(values[i], values[i + 1]);
    }
    return uriVars;
  }
}
