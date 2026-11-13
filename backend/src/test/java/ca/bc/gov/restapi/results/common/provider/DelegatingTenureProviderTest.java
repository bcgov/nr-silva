package ca.bc.gov.restapi.results.common.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import ca.bc.gov.restapi.results.common.dto.ExternalCutBlockDetails;
import ca.bc.gov.restapi.results.common.dto.ExternalTenureSummary;
import ca.bc.gov.restapi.results.common.provider.mock.MockTenureAdapter;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.mock.env.MockEnvironment;

/**
 * Unit tests for {@link DelegatingTenureProvider}, covering template routing behavior, delegation
 * of {@link TenureProvider} operations, runtime environment switching, and Spring {@code @Primary}
 * wiring.
 */
@DisplayName("Unit Test | DelegatingTenureProvider")
class DelegatingTenureProviderTest
    extends AbstractDelegatingProviderTestTemplate<TenureProvider, DelegatingTenureProvider> {

  private static final String FOREST_FILE_ID = "TFL001";
  private static final String CUTTING_PERMIT_ID = "CP1";
  private static final String CUT_BLOCK_ID = "CB001";
  private static final String CLIENT_NUMBER = "00010003";
  private static final String TIMBER_MARK = "47/12K";
  private static final String STATUS_ACT = "ACT";
  private static final String PROPERTY_KEY = "silva.integrations.tenure.provider";

  @Override
  protected String getDomain() {
    return "tenure";
  }

  @Override
  protected TenureProvider createOracleAdapter() {
    return mock(TenureProvider.class);
  }

  @Override
  protected TenureProvider createMockAdapter() {
    return mock(TenureProvider.class);
  }

  @Override
  protected TenureProvider createApiAdapter() {
    return mock(TenureProvider.class);
  }

  @Override
  protected DelegatingTenureProvider createDelegatingProvider(
      Map<String, TenureProvider> adapters, Environment environment) {
    return new DelegatingTenureProvider(adapters, environment);
  }

  @Test
  @DisplayName("verifyCutBlock delegates to oracle adapter by default and switches at runtime")
  void verifyCutBlock_delegatesToConfiguredAdapterAndSwitchesAtRuntime() {
    TenureProvider oracleAdapter = createOracleAdapter();
    TenureProvider mockAdapter = createMockAdapter();
    ExternalCutBlockDetails oracleDetails =
        ExternalCutBlockDetails.of(100L, true, "ORACLE_TM", STATUS_ACT);
    ExternalCutBlockDetails mockDetails =
        ExternalCutBlockDetails.of(200L, false, "MOCK_TM", "S");
    when(oracleAdapter.verifyCutBlock(
            FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID, CLIENT_NUMBER))
        .thenReturn(oracleDetails);
    when(mockAdapter.verifyCutBlock(
            FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID, CLIENT_NUMBER))
        .thenReturn(mockDetails);

    MockEnvironment environment = new MockEnvironment();
    DelegatingTenureProvider provider =
        createDelegatingProvider(
            Map.of(
                getOracleBeanName(), oracleAdapter,
                getMockBeanName(), mockAdapter),
            environment);

    ExternalCutBlockDetails firstResult =
        provider.verifyCutBlock(FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID, CLIENT_NUMBER);
    assertThat(firstResult).isEqualTo(oracleDetails);
    verify(oracleAdapter)
        .verifyCutBlock(FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID, CLIENT_NUMBER);
    verifyNoInteractions(mockAdapter);

    environment.setProperty(PROPERTY_KEY, "mock");

    ExternalCutBlockDetails secondResult =
        provider.verifyCutBlock(FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID, CLIENT_NUMBER);
    assertThat(secondResult).isEqualTo(mockDetails);
    verify(mockAdapter)
        .verifyCutBlock(FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID, CLIENT_NUMBER);
  }

  @Test
  @DisplayName("getTenureSummary delegates to configured api or mock adapter")
  void getTenureSummary_delegatesToConfiguredAdapter() {
    TenureProvider oracleAdapter = createOracleAdapter();
    TenureProvider apiAdapter = createApiAdapter();
    ExternalTenureSummary apiSummary =
        new ExternalTenureSummary(
            FOREST_FILE_ID,
            CUTTING_PERMIT_ID,
            CUT_BLOCK_ID,
            TIMBER_MARK,
            CLIENT_NUMBER,
            STATUS_ACT,
            999L);
    when(apiAdapter.getTenureSummary(FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID))
        .thenReturn(Optional.of(apiSummary));

    MockEnvironment environment = new MockEnvironment().withProperty(PROPERTY_KEY, "api");
    DelegatingTenureProvider provider =
        createDelegatingProvider(
            Map.of(
                getOracleBeanName(), oracleAdapter,
                getApiBeanName(), apiAdapter),
            environment);

    Optional<ExternalTenureSummary> result =
        provider.getTenureSummary(FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID);

    assertThat(result).contains(apiSummary);
    verify(apiAdapter).getTenureSummary(FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID);
    verifyNoInteractions(oracleAdapter);
  }

  @Test
  @DisplayName("Spring wiring resolves @Primary DelegatingTenureProvider and switches dynamically")
  void springContext_resolvesPrimaryDelegatingProviderAndSwitchesAtRuntime() {
    TenureProvider mockOracleAdapter = mock(TenureProvider.class);
    ExternalCutBlockDetails oracleResult =
        ExternalCutBlockDetails.of(888L, false, "ORCL_888", "S");
    when(mockOracleAdapter.verifyCutBlock(
            FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID, CLIENT_NUMBER))
        .thenReturn(oracleResult);

    new ApplicationContextRunner()
        .withBean("oracleTenureJdbcAdapter", TenureProvider.class, () -> mockOracleAdapter)
        .withBean("mockTenureAdapter", MockTenureAdapter.class)
        .withBean("delegatingTenureProvider", DelegatingTenureProvider.class)
        .run(
            context -> {
              assertThat(context).hasSingleBean(DelegatingTenureProvider.class);
              TenureProvider primary = context.getBean(TenureProvider.class);
              assertThat(primary).isInstanceOf(DelegatingTenureProvider.class);

              assertThat(
                      primary.verifyCutBlock(
                          FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID, CLIENT_NUMBER))
                  .isEqualTo(oracleResult);

              ConfigurableEnvironment env = context.getEnvironment();
              env.getPropertySources()
                  .addFirst(new MapPropertySource("override", Map.of(PROPERTY_KEY, "mock")));

              ExternalCutBlockDetails mockResult =
                  primary.verifyCutBlock(
                      FOREST_FILE_ID, CUTTING_PERMIT_ID, CUT_BLOCK_ID, CLIENT_NUMBER);
              assertThat(mockResult.exists()).isTrue();
              assertThat(mockResult.cbSkey()).isEqualTo(1001L);
              assertThat(mockResult.clientIsLicensee()).isTrue();
              assertThat(mockResult.timberMark()).isEqualTo(TIMBER_MARK);
              assertThat(mockResult.status()).isEqualTo(STATUS_ACT);

              env.getPropertySources().remove("override");
              assertThat(((DelegatingTenureProvider) primary).getCurrentProvider())
                  .isEqualTo("oracle");
            });
  }
}

