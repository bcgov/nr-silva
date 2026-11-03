package ca.bc.gov.restapi.results.common.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLTimeoutException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@DisplayName("Unit Test | PrimaryOracleTemplateConfiguration")
class PrimaryOracleTemplateConfigurationTest {

  private final DataSource primaryOracleDataSource = new DriverManagerDataSource();

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withUserConfiguration(PrimaryOracleTemplateConfiguration.class)
          .withBean("oracleDataSource", DataSource.class, () -> primaryOracleDataSource);

  @Test
  @DisplayName("Should register externalOracleJdbcTemplate in oracle mode reusing oracleDataSource")
  void registersInOracleModeWithDefaultTimeout() {
    contextRunner
        .withPropertyValues("server.primary-db=oracle")
        .run(
            context -> {
              assertThat(context).hasBean("externalOracleJdbcTemplate");

              NamedParameterJdbcTemplate template =
                  context.getBean("externalOracleJdbcTemplate", NamedParameterJdbcTemplate.class);
              assertThat(template.getJdbcTemplate().getQueryTimeout()).isEqualTo(5);
              assertThat(template.getJdbcTemplate().getDataSource())
                  .isSameAs(primaryOracleDataSource);
            });
  }

  @Test
  @DisplayName("Should configure custom query timeout in oracle mode when property provided")
  void registersInOracleModeWithCustomTimeout() {
    contextRunner
        .withPropertyValues(
            "server.primary-db=oracle",
            "silva.integrations.external-oracle.query-timeout=12")
        .run(
            context -> {
              NamedParameterJdbcTemplate template =
                  context.getBean("externalOracleJdbcTemplate", NamedParameterJdbcTemplate.class);
              assertThat(template.getJdbcTemplate().getQueryTimeout()).isEqualTo(12);
            });
  }

  @Test
  @DisplayName("Should not register PrimaryOracleTemplateConfiguration in postgres mode")
  void doesNotRegisterInPostgresMode() {
    contextRunner
        .withPropertyValues("server.primary-db=postgres")
        .run(
            context -> {
              assertThat(context).doesNotHaveBean("externalOracleJdbcTemplate");
              assertThat(context).doesNotHaveBean(PrimaryOracleTemplateConfiguration.class);
            });
  }

  @Test
  @DisplayName("Should enforce query timeout on statement and throw QueryTimeoutException")
  void enforcesQueryExecutionTimeout() {
    AtomicInteger timeoutCaptured = new AtomicInteger();
    AtomicBoolean throwTimeout = new AtomicBoolean(false);

    PreparedStatement statementProxy =
        (PreparedStatement)
            Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[] {PreparedStatement.class},
                (proxy, method, args) -> {
                  if ("setQueryTimeout".equals(method.getName())) {
                    timeoutCaptured.set((int) args[0]);
                    return null;
                  }
                  if ("executeQuery".equals(method.getName())
                      || "execute".equals(method.getName())) {
                    if (throwTimeout.get()) {
                      throw new SQLTimeoutException("Statement execution timed out");
                    }
                    return Proxy.newProxyInstance(
                        getClass().getClassLoader(),
                        new Class<?>[] {ResultSet.class},
                        (rsProxy, rsMethod, rsArgs) -> {
                          if ("next".equals(rsMethod.getName())) {
                            return false;
                          }
                          return null;
                        });
                  }
                  if ("close".equals(method.getName()) || "isClosed".equals(method.getName())) {
                    return null;
                  }
                  return null;
                });

    Connection connectionProxy =
        (Connection)
            Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[] {Connection.class},
                (proxy, method, args) -> {
                  if ("prepareStatement".equals(method.getName())) {
                    return statementProxy;
                  }
                  if ("close".equals(method.getName()) || "isClosed".equals(method.getName())) {
                    return null;
                  }
                  return null;
                });

    DataSource dataSourceProxy =
        (DataSource)
            Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[] {DataSource.class},
                (proxy, method, args) -> {
                  if ("getConnection".equals(method.getName())) {
                    return connectionProxy;
                  }
                  return null;
                });

    PrimaryOracleTemplateConfiguration config = new PrimaryOracleTemplateConfiguration();
    NamedParameterJdbcTemplate template = config.externalOracleJdbcTemplate(dataSourceProxy, 5);

    template.queryForList("SELECT 1 FROM DUAL", Map.of());
    assertThat(timeoutCaptured.get()).isEqualTo(5);

    throwTimeout.set(true);
    assertThatThrownBy(() -> template.queryForList("SELECT 1 FROM DUAL", Map.of()))
        .isInstanceOf(QueryTimeoutException.class)
        .hasMessageContaining("Statement execution timed out");
  }
}
