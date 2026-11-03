package ca.bc.gov.restapi.results.common.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Configures the {@code externalOracleJdbcTemplate} bean in Oracle mode.
 *
 * <p>Rather than opening a secondary connection pool back to the same Oracle database,
 * this configuration reuses the existing primary {@code oracleDataSource} with the configured
 * query timeout, preventing duplicate connection pools in Oracle mode.
 */
@Configuration
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public class PrimaryOracleTemplateConfiguration {

  /**
   * NamedParameterJdbcTemplate bean wrapping the primary Oracle DataSource with strict execution
   * timeout.
   *
   * @param dataSource the primary Oracle data source
   * @param queryTimeout execution timeout in seconds
   * @return configured NamedParameterJdbcTemplate
   */
  @Bean(name = "externalOracleJdbcTemplate")
  public NamedParameterJdbcTemplate externalOracleJdbcTemplate(
      @Qualifier("oracleDataSource") DataSource dataSource,
      @Value("${silva.integrations.external-oracle.query-timeout:5}") int queryTimeout) {
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
    template.getJdbcTemplate().setQueryTimeout(queryTimeout);
    return template;
  }
}

