package ca.bc.gov.restapi.results.common.provider;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

/**
 * Reusable routing utility that dynamically resolves concrete adapter beans from a collection of
 * registered candidates based on runtime environment configuration.
 *
 * <p>Designed for compatibility with GraalVM Native Image Ahead-of-Time (AOT) compilation in
 * Spring Boot 3. Instead of relying on {@code @ConditionalOnProperty} (which prunes inactive beans
 * at build time), all adapter variants are registered in the Spring application context and routed
 * dynamically at runtime via {@link Environment#getProperty}.
 *
 * @param <T> the provider interface type
 */
@Slf4j
public class DelegatingProviderRouter<T> {

  public static final String DEFAULT_PROVIDER = "oracle";
  public static final String PROPERTY_PREFIX = "silva.integrations.";
  public static final String PROPERTY_SUFFIX = ".provider";

  private static final String PROVIDER_API = "api";
  private static final String PROVIDER_REST = "rest";
  private static final String PROVIDER_REST_API = "restapi";
  private static final String PROVIDER_MOCK = "mock";
  private static final String PREFIX_ORACLE_JDBC = "oracleJdbc";
  private static final String PREFIX_REST_API = "restApi";

  private static final String ADAPTER_SUFFIX = "Adapter";
  private static final String JDBC_ADAPTER_SUFFIX = "JdbcAdapter";
  private static final Pattern TOKEN_SPLIT_PATTERN =
      Pattern.compile("(?<=[a-z0-9])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|[-_]");

  private final String domain;
  private final String propertyKey;
  private final List<String> candidatePropertyKeys;
  private final Map<String, T> adapters;
  private final Environment environment;
  private final String defaultProvider;
  private final Object self;
  private final UnaryOperator<String> customBeanNameResolver;

  /**
   * Constructs a DelegatingProviderRouter with standard convention routing.
   *
   * @param domain the domain identifier (e.g. "tenure", "org-unit", "forest-client")
   * @param adapters the map of candidate adapter beans injected by Spring
   * @param environment the Spring environment used to read runtime properties
   * @param defaultProvider the fallback provider name when property is unset (e.g. "oracle")
   * @param self optional reference to the delegating provider itself to guard against circular
   *     routing
   */
  public DelegatingProviderRouter(
      String domain,
      Map<String, T> adapters,
      Environment environment,
      String defaultProvider,
      Object self) {
    this(domain, adapters, environment, defaultProvider, self, null);
  }

  /**
   * Constructs a DelegatingProviderRouter with an optional custom bean name resolver.
   *
   * @param domain the domain identifier (e.g. "tenure", "org-unit", "forest-client")
   * @param adapters the map of candidate adapter beans injected by Spring
   * @param environment the Spring environment used to read runtime properties
   * @param defaultProvider the fallback provider name when property is unset (e.g. "oracle")
   * @param self optional reference to the delegating provider itself to guard against circular
   *     routing
   * @param customBeanNameResolver optional custom mapping function from provider name to bean name
   */
  public DelegatingProviderRouter(
      String domain,
      Map<String, T> adapters,
      Environment environment,
      String defaultProvider,
      Object self,
      UnaryOperator<String> customBeanNameResolver) {
    this.domain = Objects.requireNonNull(domain, "Domain must not be null").trim();
    this.adapters = adapters != null ? adapters : Collections.emptyMap();
    this.environment = Objects.requireNonNull(environment, "Environment must not be null");
    this.defaultProvider =
        (defaultProvider != null && !defaultProvider.isBlank())
            ? defaultProvider.trim().toLowerCase(Locale.ROOT)
            : DEFAULT_PROVIDER;
    this.self = self;
    this.customBeanNameResolver = customBeanNameResolver;

    String normalizedDomain = this.domain.toLowerCase(Locale.ROOT);
    this.propertyKey = PROPERTY_PREFIX + normalizedDomain + PROPERTY_SUFFIX;

    String envDomain = normalizedDomain.replace("-", "_").toUpperCase(Locale.ROOT);
    String singularPropertyKey = "silva.integration." + normalizedDomain + ".provider";
    String singularEnvKey = "SILVA_INTEGRATION_" + envDomain + "_PROVIDER";
    String pluralEnvKey = "SILVA_INTEGRATIONS_" + envDomain + "_PROVIDER";

    this.candidatePropertyKeys =
        List.of(propertyKey, singularPropertyKey, singularEnvKey, pluralEnvKey);
  }

  /**
   * Resolves the configured provider name dynamically from the environment.
   *
   * @return the normalized provider name (e.g. "oracle", "api", "mock")
   */
  public String resolveProviderName() {
    if (environment instanceof ConfigurableEnvironment configurableEnv) {
      for (PropertySource<?> propertySource : configurableEnv.getPropertySources()) {
        for (String key : candidatePropertyKeys) {
          if (propertySource.containsProperty(key)) {
            Object value = propertySource.getProperty(key);
            String raw = value != null ? value.toString() : null;
            if (raw != null && !raw.isBlank()) {
              String resolved = configurableEnv.resolvePlaceholders(raw);
              if (resolved != null && !resolved.isBlank() && !isUnresolvedPlaceholder(resolved)) {
                return resolved.trim().toLowerCase(Locale.ROOT);
              }
            }
          }
        }
      }
    } else {
      for (String key : candidatePropertyKeys) {
        String value = environment.getProperty(key);
        if (value != null && !value.isBlank()) {
          return value.trim().toLowerCase(Locale.ROOT);
        }
      }
    }

    return defaultProvider;
  }

  private static boolean isUnresolvedPlaceholder(String value) {
    String trimmed = value.trim();
    return trimmed.startsWith("${") && trimmed.endsWith("}");
  }

  /**
   * Dynamically resolves and returns the delegate adapter instance based on the runtime
   * environment configuration.
   *
   * @return the resolved delegate adapter instance
   * @throws IllegalStateException if no suitable adapter is found or if circular routing is
   *     detected
   */
  public T getDelegate() {
    String provider = resolveProviderName();

    // 1. Try custom resolver if provided
    if (customBeanNameResolver != null) {
      String customBeanName = customBeanNameResolver.apply(provider);
      if (customBeanName != null && adapters.containsKey(customBeanName)) {
        return validateAndReturn(customBeanName, adapters.get(customBeanName));
      }
    }

    // 2. Try convention-based candidate bean names
    List<String> candidateNames = buildCandidateBeanNames(provider);
    for (String candidate : candidateNames) {
      if (adapters.containsKey(candidate)) {
        return validateAndReturn(candidate, adapters.get(candidate));
      }
    }

    // 3. Fallback: Search for matching non-delegating adapter bean in the map
    for (Map.Entry<String, T> entry : adapters.entrySet()) {
      String beanName = entry.getKey();
      T bean = entry.getValue();

      if (isSelfOrDelegating(beanName, bean)) {
        continue;
      }

      Set<String> providerTokens = extractProviderTokens(beanName);
      if (matchesProviderTokens(providerTokens, provider)) {
        log.debug(
            "Resolved adapter '{}' for domain '{}' and provider '{}' via fallback match",
            beanName,
            domain,
            provider);
        return validateAndReturn(beanName, bean);
      }
    }

    throw new IllegalStateException(
        String.format(
            "No adapter bean found for provider '%s' (domain: '%s'). Configured property: '%s'."
                + " Available adapter beans: %s",
            provider, domain, propertyKey, adapters.keySet()));
  }

  private List<String> buildCandidateBeanNames(String provider) {
    String capitalizedDomain = capitalize(toCamelCase(domain));
    return switch (provider) {
      case DEFAULT_PROVIDER -> List.of(
          DEFAULT_PROVIDER + capitalizedDomain + JDBC_ADAPTER_SUFFIX,
          DEFAULT_PROVIDER + capitalizedDomain + ADAPTER_SUFFIX,
          PREFIX_ORACLE_JDBC + capitalizedDomain + ADAPTER_SUFFIX);
      case PROVIDER_API, PROVIDER_REST, PROVIDER_REST_API -> List.of(
          PREFIX_REST_API + capitalizedDomain + ADAPTER_SUFFIX,
          PROVIDER_API + capitalizedDomain + ADAPTER_SUFFIX,
          PROVIDER_REST + capitalizedDomain + ADAPTER_SUFFIX);
      case PROVIDER_MOCK -> List.of(PROVIDER_MOCK + capitalizedDomain + ADAPTER_SUFFIX);
      default -> List.of(
          provider + capitalizedDomain + ADAPTER_SUFFIX,
          provider + capitalizedDomain + JDBC_ADAPTER_SUFFIX,
          provider);
    };
  }

  private T validateAndReturn(String beanName, T delegate) {
    if (delegate == null) {
      throw new IllegalStateException(
          String.format(
              "Adapter bean '%s' for domain '%s' was null in adapters map.", beanName, domain));
    }
    if (isSelfOrDelegating(beanName, delegate)) {
      throw new IllegalStateException(
          String.format(
              "Circular delegation detected: resolved adapter bean '%s' is the delegating router"
                  + " itself.",
              beanName));
    }
    return delegate;
  }

  private boolean isSelfOrDelegating(String beanName, T bean) {
    if (self != null && bean == self) {
      return true;
    }
    String lower = beanName.toLowerCase(Locale.ROOT);
    return lower.startsWith("delegating")
        || lower.contains("delegating" + domain.toLowerCase(Locale.ROOT));
  }

  private static String toCamelCase(String text) {
    if (text == null || text.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    boolean upperNext = false;
    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (c == '-' || c == '_') {
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

  private Set<String> extractProviderTokens(String beanName) {
    Set<String> domainTokens = tokenize(domain);
    Set<String> beanTokens = tokenize(beanName);
    beanTokens.removeAll(domainTokens);
    beanTokens.remove(ADAPTER_SUFFIX.toLowerCase(Locale.ROOT));
    beanTokens.remove(JDBC_ADAPTER_SUFFIX.toLowerCase(Locale.ROOT));
    beanTokens.remove("jdbc");
    return beanTokens;
  }

  private static Set<String> tokenize(String text) {
    if (text == null || text.isBlank()) {
      return Collections.emptySet();
    }
    String[] parts = TOKEN_SPLIT_PATTERN.split(text);
    Set<String> result = new HashSet<>();
    for (String part : parts) {
      if (!part.isBlank()) {
        result.add(part.toLowerCase(Locale.ROOT));
      }
    }
    return result;
  }

  private boolean matchesProviderTokens(Set<String> tokens, String provider) {
    return switch (provider) {
      case PROVIDER_API, PROVIDER_REST, PROVIDER_REST_API ->
          tokens.contains(PROVIDER_REST)
              || tokens.contains(PROVIDER_API)
              || tokens.contains(PROVIDER_REST_API);
      case DEFAULT_PROVIDER -> tokens.contains(DEFAULT_PROVIDER);
      case PROVIDER_MOCK -> tokens.contains(PROVIDER_MOCK);
      default -> tokens.contains(provider);
    };
  }

  private static String capitalize(String text) {
    if (text == null || text.isEmpty()) {
      return "";
    }
    return Character.toUpperCase(text.charAt(0)) + text.substring(1);
  }

  public String getDomain() {
    return domain;
  }

  public String getPropertyKey() {
    return propertyKey;
  }

  public List<String> getCandidatePropertyKeys() {
    return Collections.unmodifiableList(candidatePropertyKeys);
  }

  public String getDefaultProvider() {
    return defaultProvider;
  }

  public Map<String, T> getAdapters() {
    return Collections.unmodifiableMap(adapters);
  }
}

