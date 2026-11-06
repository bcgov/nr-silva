package ca.bc.gov.restapi.results.common.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

/**
 * Reusable unit test template for delegating providers implementing the runtime provider routing
 * pattern.
 *
 * <p>Any concrete delegating provider (e.g. {@code DelegatingTenureProvider}, {@code
 * DelegatingForestClientProvider}) can extend this test template to inherit full verification of
 * runtime environment switching, fallback defaults, relaxed property binding, and error handling.
 *
 * @param <T> the provider port interface
 * @param <D> the concrete delegating provider subclass under test
 */
public abstract class AbstractDelegatingProviderTestTemplate<
    T, D extends AbstractDelegatingProvider<T>> {

  protected abstract String getDomain();

  protected abstract T createOracleAdapter();

  protected abstract T createMockAdapter();

  protected abstract T createApiAdapter();

  protected abstract D createDelegatingProvider(
      Map<String, T> adapters, Environment environment);

  protected String getCapitalizedDomain() {
    String d = getDomain().replace("-", "_");
    StringBuilder sb = new StringBuilder();
    boolean upperNext = true;
    for (int i = 0; i < d.length(); i++) {
      char c = d.charAt(i);
      if (c == '_') {
        upperNext = true;
      } else if (upperNext) {
        sb.append(Character.toUpperCase(c));
        upperNext = false;
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  protected String getOracleBeanName() {
    return "oracle" + getCapitalizedDomain() + "JdbcAdapter";
  }

  protected String getMockBeanName() {
    return "mock" + getCapitalizedDomain() + "Adapter";
  }

  protected String getApiBeanName() {
    return "restApi" + getCapitalizedDomain() + "Adapter";
  }

  protected String getPropertyKey() {
    return DelegatingProviderRouter.PROPERTY_PREFIX
        + getDomain().toLowerCase(Locale.ROOT)
        + DelegatingProviderRouter.PROPERTY_SUFFIX;
  }

  protected String getEnvVarKey() {
    return "SILVA_INTEGRATION_"
        + getDomain().replace("-", "_").toUpperCase(Locale.ROOT)
        + "_PROVIDER";
  }

  protected Map<String, T> buildStandardAdapters(
      T oracleAdapter, T mockAdapter, T apiAdapter) {
    Map<String, T> adapters = new HashMap<>();
    adapters.put(getOracleBeanName(), oracleAdapter);
    adapters.put(getMockBeanName(), mockAdapter);
    adapters.put(getApiBeanName(), apiAdapter);
    return adapters;
  }

  @Test
  @DisplayName("Should route to Oracle adapter when property is configured as oracle")
  void shouldRouteToOracleAdapterWhenConfigured() {
    T oracleAdapter = createOracleAdapter();
    T mockAdapter = createMockAdapter();
    T apiAdapter = createApiAdapter();
    Map<String, T> adapters = buildStandardAdapters(oracleAdapter, mockAdapter, apiAdapter);

    MockEnvironment env = new MockEnvironment();
    env.setProperty(getPropertyKey(), "oracle");

    D delegating = createDelegatingProvider(adapters, env);

    assertThat(delegating.getCurrentProvider()).isEqualTo("oracle");
    assertThat(delegating.getDelegate()).isSameAs(oracleAdapter);
  }

  @Test
  @DisplayName("Should route to Mock adapter when property is configured as mock")
  void shouldRouteToMockAdapterWhenConfigured() {
    T oracleAdapter = createOracleAdapter();
    T mockAdapter = createMockAdapter();
    T apiAdapter = createApiAdapter();
    Map<String, T> adapters = buildStandardAdapters(oracleAdapter, mockAdapter, apiAdapter);

    MockEnvironment env = new MockEnvironment();
    env.setProperty(getPropertyKey(), "mock");

    D delegating = createDelegatingProvider(adapters, env);

    assertThat(delegating.getCurrentProvider()).isEqualTo("mock");
    assertThat(delegating.getDelegate()).isSameAs(mockAdapter);
  }

  @Test
  @DisplayName("Should route to API adapter when property is configured as api")
  void shouldRouteToApiAdapterWhenConfigured() {
    T oracleAdapter = createOracleAdapter();
    T mockAdapter = createMockAdapter();
    T apiAdapter = createApiAdapter();
    Map<String, T> adapters = buildStandardAdapters(oracleAdapter, mockAdapter, apiAdapter);

    MockEnvironment env = new MockEnvironment();
    env.setProperty(getPropertyKey(), "api");

    D delegating = createDelegatingProvider(adapters, env);

    assertThat(delegating.getCurrentProvider()).isEqualTo("api");
    assertThat(delegating.getDelegate()).isSameAs(apiAdapter);
  }

  @Test
  @DisplayName("Should fall back to default provider (oracle) when property is not set")
  void shouldFallbackToDefaultWhenUnconfigured() {
    T oracleAdapter = createOracleAdapter();
    T mockAdapter = createMockAdapter();
    T apiAdapter = createApiAdapter();
    Map<String, T> adapters = buildStandardAdapters(oracleAdapter, mockAdapter, apiAdapter);

    MockEnvironment env = new MockEnvironment();

    D delegating = createDelegatingProvider(adapters, env);

    assertThat(delegating.getCurrentProvider())
        .isEqualTo(DelegatingProviderRouter.DEFAULT_PROVIDER);
    assertThat(delegating.getDelegate()).isSameAs(oracleAdapter);
  }

  @Test
  @DisplayName("Should route via OpenShift environment variable relaxed binding")
  void shouldRouteViaEnvironmentVariable() {
    T oracleAdapter = createOracleAdapter();
    T mockAdapter = createMockAdapter();
    T apiAdapter = createApiAdapter();
    Map<String, T> adapters = buildStandardAdapters(oracleAdapter, mockAdapter, apiAdapter);

    MockEnvironment env = new MockEnvironment();
    env.setProperty(getEnvVarKey(), "mock");

    D delegating = createDelegatingProvider(adapters, env);

    assertThat(delegating.getCurrentProvider()).isEqualTo("mock");
    assertThat(delegating.getDelegate()).isSameAs(mockAdapter);
  }

  @Test
  @DisplayName("Should normalize whitespace and case in provider configuration")
  void shouldNormalizeWhitespaceAndCase() {
    T oracleAdapter = createOracleAdapter();
    T mockAdapter = createMockAdapter();
    T apiAdapter = createApiAdapter();
    Map<String, T> adapters = buildStandardAdapters(oracleAdapter, mockAdapter, apiAdapter);

    MockEnvironment env = new MockEnvironment();
    env.setProperty(getPropertyKey(), "  MoCk  ");

    D delegating = createDelegatingProvider(adapters, env);

    assertThat(delegating.getCurrentProvider()).isEqualTo("mock");
    assertThat(delegating.getDelegate()).isSameAs(mockAdapter);
  }

  @Test
  @DisplayName("Should throw when configured provider has no matching adapter")
  void shouldThrowWhenUnknownProvider() {
    T oracleAdapter = createOracleAdapter();
    T mockAdapter = createMockAdapter();
    T apiAdapter = createApiAdapter();
    Map<String, T> adapters = buildStandardAdapters(oracleAdapter, mockAdapter, apiAdapter);

    MockEnvironment env = new MockEnvironment();
    env.setProperty(getPropertyKey(), "unknown_provider");

    D delegating = createDelegatingProvider(adapters, env);

    assertThatThrownBy(delegating::getDelegate)
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("No adapter bean found for provider 'unknown_provider'");
  }

  @Test
  @DisplayName("Should prevent circular delegation when delegating bean is in adapters map")
  void shouldPreventCircularDelegation() {
    T oracleAdapter = createOracleAdapter();
    Map<String, T> adapters = new HashMap<>();
    adapters.put(getOracleBeanName(), oracleAdapter);

    MockEnvironment env = new MockEnvironment();
    env.setProperty(getPropertyKey(), "oracle");

    D delegating = createDelegatingProvider(adapters, env);

    // If Spring injected the delegating bean itself into the adapters map:
    String delegatingBeanName = "delegating" + getCapitalizedDomain() + "Provider";
    adapters.put(delegatingBeanName, (T) delegating);

    // Router should still cleanly resolve oracleAdapter without circular recursion
    assertThat(delegating.getDelegate()).isSameAs(oracleAdapter);
  }
}
