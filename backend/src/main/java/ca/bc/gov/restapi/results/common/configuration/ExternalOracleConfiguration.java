package ca.bc.gov.restapi.results.common.configuration;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Secondary Oracle connection pool and JdbcTemplate configuration for external integrations
 * active strictly in PostgreSQL mode.
 */
@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
@ConditionalOnProperty(
    prefix = "silva.integrations.external-oracle",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
public class ExternalOracleConfiguration {

  /**
   * Dedicated read-only Oracle DataSource for querying external schemas.
   *
   * @return read-only HikariDataSource configured with secondary pool parameters
   */
  @Bean(name = "externalOracleDataSource")
  @ConfigurationProperties(prefix = "silva.integrations.external-oracle.hikari")
  public HikariDataSource externalOracleDataSource() {
    HikariDataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
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
}
