package ca.bc.gov.restapi.results.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

/**
 * Configuration class for external API clients. This class is responsible for creating beans for
 * the RestClient instances used to interact with external APIs.
 */
@Configuration
public class ExternalApiConfiguration {

  /**
   * Creates a RestClient bean for the Forest Client API.
   *
   * @param silvaConfiguration the configuration properties for providers
   * @return the configured RestClient instance for the Forest Client API
   */
  @Bean
  public RestClient forestClientApi(SilvaConfiguration silvaConfiguration) {
    return RestClient
        .builder()
        .baseUrl(silvaConfiguration.getForestClientApi().getAddress())
        .defaultHeader("X-API-KEY", silvaConfiguration.getForestClientApi().getKey())
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

  /**
   * Creates a RestClient bean for the Open Maps API.
   *
   * @return the configured RestClient instance for the Open Maps API
   */
  @Bean
  public RestClient openMapsApi(SilvaConfiguration silvaConfiguration) {
    return RestClient
        .builder()
        .baseUrl(silvaConfiguration.getOpenMaps().getAddress())
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .build();
  }

}
