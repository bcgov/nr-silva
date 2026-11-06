package ca.bc.gov.restapi.results.common.provider;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;

@DisplayName("Unit Test | AbstractDelegatingProvider")
class AbstractDelegatingProviderTest
    extends AbstractDelegatingProviderTestTemplate<
        AbstractDelegatingProviderTest.TestSampleProvider,
        AbstractDelegatingProviderTest.DelegatingTestSampleProvider> {

  interface TestSampleProvider {
    String getMessage();
  }

  static class OracleTestSampleJdbcAdapter implements TestSampleProvider {
    @Override
    public String getMessage() {
      return "oracle";
    }
  }

  static class MockTestSampleAdapter implements TestSampleProvider {
    @Override
    public String getMessage() {
      return "mock";
    }
  }

  static class RestApiTestSampleAdapter implements TestSampleProvider {
    @Override
    public String getMessage() {
      return "api";
    }
  }

  static class DelegatingTestSampleProvider
      extends AbstractDelegatingProvider<TestSampleProvider>
      implements TestSampleProvider {

    DelegatingTestSampleProvider(
        Map<String, TestSampleProvider> adapters, Environment environment) {
      super("test-sample", adapters, environment);
    }

    DelegatingTestSampleProvider(
        Map<String, TestSampleProvider> adapters,
        Environment environment,
        String defaultProvider) {
      super("test-sample", adapters, environment, defaultProvider);
    }

    @Override
    public String getMessage() {
      return getDelegate().getMessage();
    }
  }

  @Override
  protected String getDomain() {
    return "test-sample";
  }

  @Override
  protected TestSampleProvider createOracleAdapter() {
    return new OracleTestSampleJdbcAdapter();
  }

  @Override
  protected TestSampleProvider createMockAdapter() {
    return new MockTestSampleAdapter();
  }

  @Override
  protected TestSampleProvider createApiAdapter() {
    return new RestApiTestSampleAdapter();
  }

  @Override
  protected DelegatingTestSampleProvider createDelegatingProvider(
      Map<String, TestSampleProvider> adapters, Environment environment) {
    return new DelegatingTestSampleProvider(adapters, environment);
  }

  @Test
  @DisplayName("Should expose underlying router and delegate method execution")
  void shouldExposeRouterAndDelegateExecution() {
    Map<String, TestSampleProvider> adapters = new HashMap<>();
    adapters.put("oracleTestSampleJdbcAdapter", createOracleAdapter());
    MockEnvironment env = new MockEnvironment();

    DelegatingTestSampleProvider provider = new DelegatingTestSampleProvider(adapters, env);

    assertThat(provider.getRouter()).isNotNull();
    assertThat(provider.getMessage()).isEqualTo("oracle");
  }

  @Test
  @DisplayName("Should support custom default provider constructor")
  void shouldSupportCustomDefaultProvider() {
    Map<String, TestSampleProvider> adapters = new HashMap<>();
    adapters.put("mockTestSampleAdapter", createMockAdapter());
    MockEnvironment env = new MockEnvironment();

    DelegatingTestSampleProvider provider =
        new DelegatingTestSampleProvider(adapters, env, "mock");

    assertThat(provider.getCurrentProvider()).isEqualTo("mock");
    assertThat(provider.getMessage()).isEqualTo("mock");
  }
}

