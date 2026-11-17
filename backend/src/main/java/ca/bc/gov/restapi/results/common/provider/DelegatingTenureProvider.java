package ca.bc.gov.restapi.results.common.provider;

import ca.bc.gov.restapi.results.common.dto.ExternalCutBlockDetails;
import ca.bc.gov.restapi.results.common.dto.ExternalTenureSummary;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Primary {@link TenureProvider} router that dynamically delegates calls to the configured concrete
 * adapter ({@code oracle}, {@code mock}, or future {@code api}) at runtime.
 *
 * <p>Reads {@code silva.integrations.tenure.provider} (configured in {@code application.yml} to
 * default to {@code mock}) via {@link DelegatingProviderRouter} so provider switching works via
 * environment variables in GraalVM native images without build-time bean pruning.
 */
@Component
@Primary
public class DelegatingTenureProvider
    extends AbstractDelegatingProvider<TenureProvider>
    implements TenureProvider {

  private static final String DOMAIN = "tenure";

  /**
   * Creates a {@link DelegatingTenureProvider} backed by all registered {@link TenureProvider}
   * adapter beans.
   *
   * @param adapters candidate {@link TenureProvider} beans indexed by Spring bean name
   * @param environment Spring environment used to resolve {@code
   *     silva.integrations.tenure.provider}
   */
  public DelegatingTenureProvider(
      Map<String, TenureProvider> adapters, Environment environment) {
    super(DOMAIN, adapters, environment);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Forwards the verification request to the active {@link TenureProvider} delegate.
   */
  @Override
  public ExternalCutBlockDetails verifyCutBlock(
      String forestFileId,
      String cuttingPermitId,
      String cutBlockId,
      String clientNumber) {
    return getDelegate().verifyCutBlock(forestFileId, cuttingPermitId, cutBlockId, clientNumber);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Forwards the tenure summary lookup to the active {@link TenureProvider} delegate.
   */
  @Override
  public Optional<ExternalTenureSummary> getTenureSummary(
      String forestFileId, String cuttingPermitId, String cutBlockId) {
    return getDelegate().getTenureSummary(forestFileId, cuttingPermitId, cutBlockId);
  }
}
