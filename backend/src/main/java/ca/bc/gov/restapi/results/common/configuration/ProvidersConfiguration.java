package ca.bc.gov.restapi.results.common.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/** This class contains configurations for all external APIs like address and keys. */
@Getter
@Configuration
public class ProvidersConfiguration {

  @Value("${forest-client-api.address}")
  private String forestClientBaseUri;

  @Value("${forest-client-api.key}")
  private String forestClientApiKey;
}
