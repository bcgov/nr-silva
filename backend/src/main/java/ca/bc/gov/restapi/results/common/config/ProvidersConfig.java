package ca.bc.gov.restapi.results.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** This class contains configurations for all external APIs like address and keys. */
@Getter
@Setter
@Configuration
@ConfigurationProperties("ca.bc.gov.nrs.forest-client-api")
public class ProvidersConfig {

  private String address;
  private String key;
}
