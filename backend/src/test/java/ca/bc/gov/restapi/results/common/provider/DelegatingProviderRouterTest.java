package ca.bc.gov.restapi.results.common.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.env.MockPropertySource;

@DisplayName("Unit Test | DelegatingProviderRouter")
class DelegatingProviderRouterTest {

  interface DummyPort {
    String call();
  }

  @Test
  @DisplayName("Should validate required constructor arguments")
  void shouldValidateConstructorArguments() {
    MockEnvironment env = new MockEnvironment();
    Map<String, DummyPort> map = Map.of();

    assertThatThrownBy(() -> new DelegatingProviderRouter<>(null, map, env, "oracle", null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("Domain must not be null");

    assertThatThrownBy(() -> new DelegatingProviderRouter<>("tenure", map, null, "oracle", null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("Environment must not be null");
  }

  @Test
  @DisplayName("Should populate properties and handle null or empty adapters map")
  void shouldHandleNullAdaptersMap() {
    MockEnvironment env = new MockEnvironment();
    DelegatingProviderRouter<DummyPort> router =
        new DelegatingProviderRouter<>("tenure", null, env, null, null);

    assertThat(router.getDomain()).isEqualTo("tenure");
    assertThat(router.getDefaultProvider()).isEqualTo(DelegatingProviderRouter.DEFAULT_PROVIDER);
    assertThat(router.getPropertyKey()).isEqualTo("silva.integrations.tenure.provider");
    assertThat(router.getAdapters()).isEmpty();
  }

  @Test
  @DisplayName("Should resolve secondary environment variable variant (SILVA_INTEGRATIONS_...)")
  void shouldResolveSecondaryEnvVarVariant() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("SILVA_INTEGRATIONS_TENURE_PROVIDER", "mock");

    DummyPort mockPort = () -> "mock";
    Map<String, DummyPort> adapters = Map.of("mockTenureAdapter", mockPort);

    DelegatingProviderRouter<DummyPort> router =
        new DelegatingProviderRouter<>("tenure", adapters, env, "oracle", null);

    assertThat(router.resolveProviderName()).isEqualTo("mock");
    assertThat(router.getDelegate()).isSameAs(mockPort);
  }

  @Test
  @DisplayName("Should support custom bean name resolver function")
  void shouldSupportCustomBeanNameResolver() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("silva.integrations.tenure.provider", "cloud");

    DummyPort cloudPort = () -> "cloud";
    Map<String, DummyPort> adapters = Map.of("mySpecialCloudAdapter", cloudPort);

    DelegatingProviderRouter<DummyPort> router =
        new DelegatingProviderRouter<>(
            "tenure",
            adapters,
            env,
            "oracle",
            null,
            provider -> "cloud".equals(provider) ? "mySpecialCloudAdapter" : null);

    assertThat(router.getDelegate()).isSameAs(cloudPort);
  }

  @Test
  @DisplayName("Should resolve alternate convention names for oracle and rest adapters")
  void shouldResolveAlternateConventionNames() {
    MockEnvironment env = new MockEnvironment();

    DummyPort oraclePort = () -> "oracle";
    // Using oracleTenureAdapter instead of oracleTenureJdbcAdapter
    Map<String, DummyPort> oracleMap = Map.of("oracleTenureAdapter", oraclePort);
    DelegatingProviderRouter<DummyPort> oracleRouter =
        new DelegatingProviderRouter<>("tenure", oracleMap, env, "oracle", null);
    assertThat(oracleRouter.getDelegate()).isSameAs(oraclePort);

    // Using apiTenureAdapter instead of restApiTenureAdapter
    env.setProperty("silva.integrations.tenure.provider", "restapi");
    DummyPort apiPort = () -> "api";
    Map<String, DummyPort> apiMap = Map.of("apiTenureAdapter", apiPort);
    DelegatingProviderRouter<DummyPort> apiRouter =
        new DelegatingProviderRouter<>("tenure", apiMap, env, "oracle", null);
    assertThat(apiRouter.getDelegate()).isSameAs(apiPort);
  }

  @Test
  @DisplayName("Should resolve via fallback match when convention name is slightly different")
  void shouldResolveViaFallbackMatch() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("silva.integrations.tenure.provider", "oracle");

    DummyPort port = () -> "oracle-fallback";
    Map<String, DummyPort> adapters = Map.of("customOracleTenureService", port);

    DelegatingProviderRouter<DummyPort> router =
        new DelegatingProviderRouter<>("tenure", adapters, env, "oracle", null);

    assertThat(router.getDelegate()).isSameAs(port);
  }

  @Test
  @DisplayName("Should detect circular delegation if resolved bean is identical to self")
  void shouldDetectCircularDelegationWhenResolvedBeanIsSelf() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("silva.integrations.tenure.provider", "oracle");

    class SelfPort implements DummyPort {
      @Override
      public String call() {
        return "self";
      }
    }

    SelfPort selfInstance = new SelfPort();
    Map<String, DummyPort> adapters = new HashMap<>();
    adapters.put("oracleTenureJdbcAdapter", selfInstance);

    DelegatingProviderRouter<DummyPort> router =
        new DelegatingProviderRouter<>("tenure", adapters, env, "oracle", selfInstance);

    assertThatThrownBy(router::getDelegate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("Circular delegation detected");
  }

  @Test
  @DisplayName("Should throw IllegalStateException if adapter value in map is null")
  void shouldThrowWhenAdapterInMapIsNull() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("silva.integrations.tenure.provider", "oracle");

    Map<String, DummyPort> adapters = new HashMap<>();
    adapters.put("oracleTenureJdbcAdapter", null);

    DelegatingProviderRouter<DummyPort> router =
        new DelegatingProviderRouter<>("tenure", adapters, env, "oracle", null);

    assertThatThrownBy(router::getDelegate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("was null in adapters map");
  }

  @Test
  @DisplayName("Should handle kebab-case domain in candidate name building")
  void shouldHandleKebabCaseDomain() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("silva.integrations.forest-client.provider", "mock");

    DummyPort mockPort = () -> "mock";
    Map<String, DummyPort> adapters = Map.of("mockForestClientAdapter", mockPort);

    DelegatingProviderRouter<DummyPort> router =
        new DelegatingProviderRouter<>("forest-client", adapters, env, "oracle", null);

    assertThat(router.getDelegate()).isSameAs(mockPort);
  }

  @Test
  @DisplayName("Should not match domain substring when configured provider adapter is absent")
  void shouldNotMatchDomainSubstringWhenProviderAdapterIsAbsent() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("silva.integrations.forest-client.provider", "rest");

    DummyPort oraclePort = () -> "oracle";
    DummyPort mockPort = () -> "mock";
    Map<String, DummyPort> adapters =
        Map.of(
            "oracleForestClientJdbcAdapter", oraclePort,
            "mockForestClientAdapter", mockPort);

    DelegatingProviderRouter<DummyPort> router =
        new DelegatingProviderRouter<>("forest-client", adapters, env, "oracle", null);

    assertThatThrownBy(router::getDelegate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No adapter bean found for provider 'rest'");
  }

  @Test
  @DisplayName("Should throw IllegalStateException when configured provider is unknown")
  void shouldThrowWhenConfiguredProviderIsUnknown() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("silva.integrations.forest-client.provider", "unsupported");

    DummyPort oraclePort = () -> "oracle";
    Map<String, DummyPort> adapters = Map.of("oracleForestClientJdbcAdapter", oraclePort);

    DelegatingProviderRouter<DummyPort> router =
        new DelegatingProviderRouter<>("forest-client", adapters, env, "oracle", null);

    assertThatThrownBy(router::getDelegate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No adapter bean found for provider 'unsupported'");
  }

  @Test
  @DisplayName("Should throw when partial provider name is configured (no partial substring match)")
  void shouldThrowWhenPartialProviderNameIsConfigured() {
    MockEnvironment env = new MockEnvironment();
    env.setProperty("silva.integrations.tenure.provider", "or");

    DummyPort oraclePort = () -> "oracle";
    Map<String, DummyPort> adapters = Map.of("oracleTenureJdbcAdapter", oraclePort);

    DelegatingProviderRouter<DummyPort> router =
        new DelegatingProviderRouter<>("tenure", adapters, env, "oracle", null);

    assertThatThrownBy(router::getDelegate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No adapter bean found for provider 'or'");

    // Also test 'moc' against mock adapter
    env.setProperty("silva.integrations.tenure.provider", "moc");
    DummyPort mockPort = () -> "mock";
    Map<String, DummyPort> mockAdapters = Map.of("mockTenureAdapter", mockPort);

    DelegatingProviderRouter<DummyPort> mockRouter =
        new DelegatingProviderRouter<>("tenure", mockAdapters, env, "oracle", null);

    assertThatThrownBy(mockRouter::getDelegate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No adapter bean found for provider 'moc'");
  }

  @Test
  @DisplayName("Should allow environment variable to override configuration property default")
  void shouldAllowEnvVarToOverrideConfigurationPropertyDefault() {
    MockEnvironment env = new MockEnvironment();
    // Simulate lower-precedence application.yml setting default to oracle
    env.getPropertySources()
        .addLast(
            new MockPropertySource("applicationConfig")
                .withProperty("silva.integrations.tenure.provider", "oracle"));

    // Simulate higher-precedence OpenShift environment variable setting to api
    env.getPropertySources()
        .addFirst(
            new MockPropertySource("systemEnvironment")
                .withProperty("SILVA_INTEGRATION_TENURE_PROVIDER", "api"));

    DummyPort apiPort = () -> "api";
    DummyPort oraclePort = () -> "oracle";
    Map<String, DummyPort> adapters =
        Map.of(
            "restApiTenureAdapter", apiPort,
            "oracleTenureJdbcAdapter", oraclePort);

    DelegatingProviderRouter<DummyPort> router =
        new DelegatingProviderRouter<>("tenure", adapters, env, "oracle", null);

    assertThat(router.resolveProviderName()).isEqualTo("api");
    assertThat(router.getDelegate()).isSameAs(apiPort);
  }

  @Test
  @DisplayName("Should resolve Spring placeholder defaults in property source values")
  void shouldResolvePlaceholderDefaultsInPropertySourceValues() {
    MockEnvironment env = new MockEnvironment();
    env.getPropertySources()
        .addLast(
            new MockPropertySource("applicationConfig")
                .withProperty(
                    "silva.integrations.tenure.provider",
                    "${SILVA_INTEGRATION_TENURE_PROVIDER:mock}"));

    DummyPort mockPort = () -> "mock";
    DummyPort oraclePort = () -> "oracle";
    Map<String, DummyPort> adapters =
        Map.of(
            "mockTenureAdapter", mockPort,
            "oracleTenureJdbcAdapter", oraclePort);

    DelegatingProviderRouter<DummyPort> router =
        new DelegatingProviderRouter<>("tenure", adapters, env, "oracle", null);

    assertThat(router.resolveProviderName()).isEqualTo("mock");
    assertThat(router.getDelegate()).isSameAs(mockPort);

    env.getPropertySources()
        .addFirst(
            new MockPropertySource("systemEnvironment")
                .withProperty("SILVA_INTEGRATION_TENURE_PROVIDER", "oracle"));

    assertThat(router.resolveProviderName()).isEqualTo("oracle");
    assertThat(router.getDelegate()).isSameAs(oraclePort);
  }
}

