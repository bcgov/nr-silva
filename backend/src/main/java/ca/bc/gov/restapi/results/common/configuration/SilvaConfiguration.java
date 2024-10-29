package ca.bc.gov.restapi.results.common.configuration;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * This class contains configurations for all external APIs like address and keys.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties("ca.bc.gov.nrs")
public class SilvaConfiguration {

  private List<String> dashboardJobUsers;
  private List<String> wmsWhitelist;
  private List<String> orgUnits;

  @NestedConfigurationProperty
  private ExternalApiAddress forestClientApi;
  @NestedConfigurationProperty
  private ExternalApiAddress openMaps;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ExternalApiAddress {

    private String address;
    private String key;
  }

}
