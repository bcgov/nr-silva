package ca.bc.gov.restapi.results.common.provider;

import java.util.Map;
import java.util.function.UnaryOperator;
import org.springframework.core.env.Environment;

/**
 * Abstract base class for delegating providers implementing the runtime provider routing pattern.
 *
 * <p>Subclasses can extend this class and invoke {@link #getDelegate()} to forward functional port
 * calls to the dynamically resolved adapter.
 *
 * @param <T> the provider interface type
 */
public abstract class AbstractDelegatingProvider<T> {

  private final DelegatingProviderRouter<T> router;

  /**
   * Initializes the delegating provider with standard default provider ("oracle").
   *
   * @param domain the domain identifier (e.g. "tenure", "org-unit", "forest-client")
   * @param adapters all beans of type T injected by Spring
   * @param environment the Spring environment
   */
  protected AbstractDelegatingProvider(
      String domain, Map<String, T> adapters, Environment environment) {
    this(domain, adapters, environment, DelegatingProviderRouter.DEFAULT_PROVIDER, null);
  }

  /**
   * Initializes the delegating provider with an explicit default provider.
   *
   * @param domain the domain identifier (e.g. "tenure", "org-unit", "forest-client")
   * @param adapters all beans of type T injected by Spring
   * @param environment the Spring environment
   * @param defaultProvider the default provider when unconfigured (e.g. "oracle")
   */
  protected AbstractDelegatingProvider(
      String domain, Map<String, T> adapters, Environment environment, String defaultProvider) {
    this(domain, adapters, environment, defaultProvider, null);
  }

  /**
   * Initializes the delegating provider with an explicit default provider and custom resolver.
   *
   * @param domain the domain identifier (e.g. "tenure", "org-unit", "forest-client")
   * @param adapters all beans of type T injected by Spring
   * @param environment the Spring environment
   * @param defaultProvider the default provider when unconfigured (e.g. "oracle")
   * @param customBeanNameResolver optional custom mapping function from provider name to bean name
   */
  protected AbstractDelegatingProvider(
      String domain,
      Map<String, T> adapters,
      Environment environment,
      String defaultProvider,
      UnaryOperator<String> customBeanNameResolver) {
    this.router =
        new DelegatingProviderRouter<>(
            domain, adapters, environment, defaultProvider, this, customBeanNameResolver);
  }

  /**
   * Returns the dynamically resolved delegate adapter for the current request.
   *
   * @return the resolved delegate
   */
  protected T getDelegate() {
    return router.getDelegate();
  }

  /**
   * Returns the current provider name resolved from the environment.
   *
   * @return current provider name (e.g. "oracle", "api", "mock")
   */
  public String getCurrentProvider() {
    return router.resolveProviderName();
  }

  /**
   * Returns the underlying router utility.
   *
   * @return the DelegatingProviderRouter instance
   */
  public DelegatingProviderRouter<T> getRouter() {
    return router;
  }
}

