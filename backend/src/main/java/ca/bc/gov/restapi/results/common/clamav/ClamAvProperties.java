package ca.bc.gov.restapi.results.common.clamav;

import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/** ClamAV connection and policy settings, bound from {@code ca.bc.gov.nrs.clamav.*}. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@With
@Builder
@Configuration
@Component
@ConfigurationProperties("ca.bc.gov.nrs.clamav")
public class ClamAvProperties {

  /** clamd host (TCP). */
  private String host;

  /** clamd port. Default 3310. */
  private int port;

  /** Socket connect timeout. */
  private Duration connectTimeout;

  /** Socket read timeout. */
  private Duration readTimeout;

  /**
   * When {@code true}, a scanner connection failure allows the upload through (warn-only). When
   * {@code false} (fail-closed), a connection failure rejects the upload. An infected verdict
   * always rejects regardless of this flag.
   */
  private boolean failOpen;
}
