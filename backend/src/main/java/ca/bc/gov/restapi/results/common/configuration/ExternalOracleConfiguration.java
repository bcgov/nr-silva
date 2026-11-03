package ca.bc.gov.restapi.results.common.configuration;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

  private static void checkRuntimeEnabled(Environment env) throws SQLException {
    String enabled =
        env.getProperty(
            "EXTERNAL_ORACLE_ENABLED",
            env.getProperty("silva.integrations.external-oracle.enabled", "true"));
    if ("false".equalsIgnoreCase(enabled)) {
      throw new SQLException(
          "External Oracle integration is disabled at runtime (EXTERNAL_ORACLE_ENABLED=false).");
    }
  }
}
