package ca.bc.gov.restapi.results.common.configuration;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Secondary Oracle connection pool and JdbcTemplate configuration for external integrations
 * active strictly in PostgreSQL mode.
 */
@Slf4j
@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
public class ExternalOracleConfiguration {

  /**
   * Dedicated read-only Oracle DataSource for querying external schemas.
   *
   * <p>Gates connection acquisition at runtime against {@code EXTERNAL_ORACLE_ENABLED} and
   * {@code silva.integrations.external-oracle.enabled} to support runtime toggling in
   * GraalVM native images where build-time bean definitions cannot be dynamically pruned.
   *
   * @param env the Spring environment to check runtime enablement
   * @return read-only HikariDataSource configured with secondary pool parameters and runtime gating
   */
  @Bean(name = "externalOracleDataSource")
  @ConfigurationProperties(prefix = "silva.integrations.external-oracle.hikari")
  public HikariDataSource externalOracleDataSource(Environment env) {
    HikariDataSource dataSource =
        new HikariDataSource() {
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
    dataSource.setReadOnly(true);
    return dataSource;
  }

  /**
   * NamedParameterJdbcTemplate bean with strict execution timeout for external validations.
   *
   * @param dataSource the secondary Oracle data source
   * @param queryTimeout execution timeout in seconds
   * @return configured NamedParameterJdbcTemplate
   */
  @Bean(name = "externalOracleJdbcTemplate")
  public NamedParameterJdbcTemplate externalOracleJdbcTemplate(
      @Qualifier("externalOracleDataSource") DataSource dataSource,
      @Value("${silva.integrations.external-oracle.query-timeout:5}") int queryTimeout) {
    NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
    template.getJdbcTemplate().setQueryTimeout(queryTimeout);
    return template;
  }

  /**
   * Non-blocking startup logger that logs the external Oracle integration configuration state
   * without eagerly opening any physical database connections.
   *
   * @param env the Spring environment to check runtime enablement
   * @return ApplicationRunner executing on application startup
   */
  @Bean
  public ApplicationRunner externalOracleStartupLogger(Environment env) {
    return args -> {
      if (isRuntimeEnabled(env)) {
        log.info(
            "External Oracle integration is enabled (read-only, lazy pool ready; 0 active"
                + " connections).");
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
