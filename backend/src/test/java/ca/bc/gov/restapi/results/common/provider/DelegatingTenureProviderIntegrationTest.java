package ca.bc.gov.restapi.results.common.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ca.bc.gov.restapi.results.common.dto.ExternalCutBlockDetails;
import ca.bc.gov.restapi.results.common.dto.ExternalTenureSummary;
import ca.bc.gov.restapi.results.common.exception.ExternalServiceErrorCode;
import ca.bc.gov.restapi.results.common.exception.ExternalServiceException;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.HttpStatus;

/**
 * Integration tests verifying Spring wiring of {@link DelegatingTenureProvider} and dynamic
 * runtime switching between {@code mock} and {@code oracle} providers without application restart.
 */
@DisplayName("Integration Test | DelegatingTenureProvider")
class DelegatingTenureProviderIntegrationTest extends AbstractTestContainerIntegrationTest {

  private static final String OVERRIDE_PROPERTY_SOURCE = "tenure-provider-test-override";
  private static final String TENURE_PROVIDER_PROP = "silva.integrations.tenure.provider";
  private static final String EXTERNAL_ORACLE_ENABLED_ENV = "EXTERNAL_ORACLE_ENABLED";

  @Autowired private TenureProvider tenureProvider;

  @Autowired private ConfigurableEnvironment environment;

  @Value("${server.primary-db:oracle}")
  private String primaryDb;

  @AfterEach
  void tearDown() {
    environment.getPropertySources().remove(OVERRIDE_PROPERTY_SOURCE);
  }

  @Test
  @DisplayName("Injects @Primary DelegatingTenureProvider defaulting to mock")
  void shouldInjectPrimaryDelegatingTenureProviderAndDefaultToMock() {
    assertThat(tenureProvider).isInstanceOf(DelegatingTenureProvider.class);
    DelegatingTenureProvider delegating = (DelegatingTenureProvider) tenureProvider;
    assertThat(delegating.getCurrentProvider()).isEqualTo("mock");
  }

  @Test
  @DisplayName("Switches dynamically between mock and oracle providers at runtime")
  void shouldSwitchDynamicallyBetweenMockAndOracleAtRuntime() {
    DelegatingTenureProvider delegating = (DelegatingTenureProvider) tenureProvider;

    // 1. Default mock provider serves MockTenureAdapter fixtures out of the box.
    assertThat(delegating.getCurrentProvider()).isEqualTo("mock");

    ExternalCutBlockDetails mockDetails =
        tenureProvider.verifyCutBlock("TFL001", "CP1", "CB001", "00010003");
    assertThat(mockDetails.exists()).isTrue();
    assertThat(mockDetails.cbSkey()).isEqualTo(1001L);
    assertThat(mockDetails.clientIsLicensee()).isTrue();
    assertThat(mockDetails.timberMark()).isEqualTo("47/12K");
    assertThat(mockDetails.status()).isEqualTo("ACT");

    Optional<ExternalTenureSummary> mockSummary =
        tenureProvider.getTenureSummary("TFL001", "CP1", "CB001");
    assertThat(mockSummary)
        .isPresent()
        .get()
        .satisfies(
            summary -> {
              assertThat(summary.cbSkey()).isEqualTo(1001L);
              assertThat(summary.licenseeClientNumber()).isEqualTo("00010003");
            });

    // 2. Switch runtime provider to oracle with external-oracle disabled in application-default.yml
    //    -> must fail fast with ExternalServiceException (503).
    applyOverride(Map.of(TENURE_PROVIDER_PROP, "oracle"));
    assertThat(delegating.getCurrentProvider()).isEqualTo("oracle");
    assertThatThrownBy(() -> tenureProvider.verifyCutBlock("A72929", "001", "028", "00010006"))
        .isInstanceOf(ExternalServiceException.class)
        .satisfies(
            ex -> {
              ExternalServiceException ese = (ExternalServiceException) ex;
              assertThat(ese.getErrorCode())
                  .isEqualTo(ExternalServiceErrorCode.EXTERNAL_SERVICE_UNAVAILABLE);
              assertThat(ese.getServiceName()).isEqualTo("FTA");
              assertThat(ese.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            });

    // 3. When running against Oracle TestContainer with EXTERNAL_ORACLE_ENABLED=true, verify live
    //    query.
    if ("oracle".equalsIgnoreCase(primaryDb)) {
      applyOverride(Map.of(TENURE_PROVIDER_PROP, "oracle", EXTERNAL_ORACLE_ENABLED_ENV, "true"));
      assertThat(delegating.getCurrentProvider()).isEqualTo("oracle");

      ExternalCutBlockDetails oracleDetails =
          tenureProvider.verifyCutBlock("A72929", "001", "028", "00010006");
      assertThat(oracleDetails.exists()).isTrue();
      assertThat(oracleDetails.cbSkey()).isEqualTo(43589L);
      assertThat(oracleDetails.clientIsLicensee()).isTrue();
      assertThat(oracleDetails.timberMark()).isEqualTo("AY5001");
      assertThat(oracleDetails.status()).isEqualTo("S");
    }

    // 4. Switch back to mock again to verify repeated runtime toggles remain responsive.
    applyOverride(Map.of(TENURE_PROVIDER_PROP, "mock"));
    assertThat(delegating.getCurrentProvider()).isEqualTo("mock");
    assertThat(tenureProvider.verifyCutBlock("TFL999", null, "CB999", "00010003").exists())
        .isFalse();
  }

  private void applyOverride(Map<String, Object> properties) {
    environment.getPropertySources().remove(OVERRIDE_PROPERTY_SOURCE);
    environment
        .getPropertySources()
        .addFirst(new MapPropertySource(OVERRIDE_PROPERTY_SOURCE, properties));
  }
}

