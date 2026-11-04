package ca.bc.gov.restapi.results.common.configuration;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DelegatingDataSource;

/**
 * Configures the {@code externalOracleJdbcTemplate} bean in Oracle mode.
 *
 * <p>Rather than opening a secondary connection pool back to the same Oracle database,
 * this configuration reuses the existing primary {@code oracleDataSource} with the configured
 * query timeout, preventing duplicate connection pools in Oracle mode.
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
public class PrimaryOracleTemplateConfiguration {

  /**
   * NamedParameterJdbcTemplate bean wrapping the primary Oracle DataSource with strict execution
   * timeout and runtime enablement gating.
   *
   * @param dataSource the primary Oracle data source
   * @param env the Spring environment to check runtime enablement
   * @param queryTimeout execution timeout in seconds
   * @return configured NamedParameterJdbcTemplate
   */
  @Bean(name = "externalOracleJdbcTemplate")
  public NamedParameterJdbcTemplate externalOracleJdbcTemplate(
      @Qualifier("oracleDataSource") DataSource dataSource,
      Environment env,
      @Value("${silva.integrations.external-oracle.query-timeout:5}") int queryTimeout) {
    DataSource gatedDataSource =
        new DelegatingDataSource(dataSource) {
          @Override
          public Connection getConnection() throws SQLException {
            checkRuntimeEnabled(env);
            return super.getConnection();
          }

          @Override
          public Connection getConnection(String username, String password) throws SQLException {
            checkRuntimeEnabled(env);
            return super.getConnection(username, password);
          }
        };
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(gatedDataSource);
    template.getJdbcTemplate().setQueryTimeout(queryTimeout);
    return template;
  }

  /**
   * Non-blocking startup logger that logs the external Oracle template initialization state
   * in primary Oracle mode.
   *
   * @param env the Spring environment to check runtime enablement
   * @param queryTimeout execution timeout in seconds
   * @return ApplicationRunner executing on application startup
   */
  @Bean
  public ApplicationRunner primaryOracleTemplateStartupLogger(
      Environment env,
      @Value("${silva.integrations.external-oracle.query-timeout:5}") int queryTimeout) {
    return args -> {
      if (isRuntimeEnabled(env)) {
        log.info(
            "External Oracle template initialized (reusing primary Oracle DataSource with {}s"
                + " query timeout).",
            queryTimeout);
      } else {
        log.info(
            "External Oracle integration is disabled at runtime (EXTERNAL_ORACLE_ENABLED=false).");
      }
    };
  }

  private static boolean isRuntimeEnabled(Environment env) {
    String enabled =
        env.getProperty(
            "EXTERNAL_ORACLE_ENABLED",
            env.getProperty("silva.integrations.external-oracle.enabled", "true"));
    return !"false".equalsIgnoreCase(enabled);
  }

  private static void checkRuntimeEnabled(Environment env) throws SQLException {
    if (!isRuntimeEnabled(env)) {
      throw new SQLException(
          "External Oracle integration is disabled at runtime (EXTERNAL_ORACLE_ENABLED=false).");
    }
  }
}

