package ca.bc.gov.restapi.results.common.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.zaxxer.hikari.HikariDataSource;
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

@DisplayName("Unit Test | ExternalOracleConfiguration")
class ExternalOracleConfigurationTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withUserConfiguration(ExternalOracleConfiguration.class);

  @Test
  @DisplayName("Should register externalOracleDataSource and externalOracleJdbcTemplate in postgres mode")
  void registersInPostgresModeWithDefaultTimeout() {
    contextRunner
        .withPropertyValues(
            "server.primary-db=postgres",
            "silva.integrations.external-oracle.enabled=true",
            "silva.integrations.external-oracle.hikari.jdbc-url=jdbc:oracle:thin:@placeholder-host:1521/TESTPDB",
            "silva.integrations.external-oracle.hikari.username=testuser",
            "silva.integrations.external-oracle.hikari.password=testpassword")
        .run(
            context -> {
              assertThat(context).hasBean("externalOracleDataSource");
              assertThat(context).hasBean("externalOracleJdbcTemplate");

              HikariDataSource ds =
                  context.getBean("externalOracleDataSource", HikariDataSource.class);
              assertThat(ds.isReadOnly()).isTrue();

              NamedParameterJdbcTemplate template =
                  context.getBean("externalOracleJdbcTemplate", NamedParameterJdbcTemplate.class);
              assertThat(template.getJdbcTemplate().getQueryTimeout()).isEqualTo(5);
              assertThat(template.getJdbcTemplate().getDataSource()).isSameAs(ds);
            });
  }

  @Test
  @DisplayName("Should configure custom query timeout when property is provided")
  void registersInPostgresModeWithCustomTimeout() {
    contextRunner
        .withPropertyValues(
            "server.primary-db=postgres",
            "silva.integrations.external-oracle.enabled=true",
            "silva.integrations.external-oracle.query-timeout=15",
            "silva.integrations.external-oracle.hikari.jdbc-url=jdbc:oracle:thin:@placeholder-host:1521/TESTPDB",
            "silva.integrations.external-oracle.hikari.username=testuser",
            "silva.integrations.external-oracle.hikari.password=testpassword")
        .run(
            context -> {
              NamedParameterJdbcTemplate template =
                  context.getBean("externalOracleJdbcTemplate", NamedParameterJdbcTemplate.class);
              assertThat(template.getJdbcTemplate().getQueryTimeout()).isEqualTo(15);
            });
  }

  @Test
  @DisplayName("Should bind Hikari pool settings and enforce read-only pool")
  void configuresPoolPropertiesCorrectly() {
    contextRunner
        .withPropertyValues(
            "server.primary-db=postgres",
            "silva.integrations.external-oracle.enabled=true",
            "silva.integrations.external-oracle.hikari.pool-name=SilvaExternalOracleConnPool",
            "silva.integrations.external-oracle.hikari.maximum-pool-size=3",
            "silva.integrations.external-oracle.hikari.connection-timeout=15000",
            "silva.integrations.external-oracle.hikari.idle-timeout=60000",
            "silva.integrations.external-oracle.hikari.jdbc-url=jdbc:oracle:thin:@placeholder-host:1521/TESTPDB",
            "silva.integrations.external-oracle.hikari.username=testuser",
            "silva.integrations.external-oracle.hikari.password=testpassword")
        .run(
            context -> {
              HikariDataSource ds =
                  context.getBean("externalOracleDataSource", HikariDataSource.class);
              assertThat(ds.getPoolName()).isEqualTo("SilvaExternalOracleConnPool");
              assertThat(ds.getMaximumPoolSize()).isEqualTo(3);
              assertThat(ds.getConnectionTimeout()).isEqualTo(15000);
              assertThat(ds.getIdleTimeout()).isEqualTo(60000);
              assertThat(ds.isReadOnly()).isTrue();
            });
  }

  @Test
  @DisplayName("Should not register any beans when enabled=false, attempting zero connections")
  void doesNotRegisterWhenEnabledIsFalse() {
    contextRunner
        .withPropertyValues(
            "server.primary-db=postgres",
            "silva.integrations.external-oracle.enabled=false")
        .run(
            context -> {
              assertThat(context).doesNotHaveBean("externalOracleDataSource");
              assertThat(context).doesNotHaveBean("externalOracleJdbcTemplate");
              assertThat(context).doesNotHaveBean(ExternalOracleConfiguration.class);
            });
  }

  @Test
  @DisplayName("Should not register ExternalOracleConfiguration in oracle mode")
  void doesNotRegisterInOracleMode() {
    contextRunner
        .withPropertyValues(
            "server.primary-db=oracle",
            "silva.integrations.external-oracle.enabled=true")
        .run(
            context -> {
              assertThat(context).doesNotHaveBean("externalOracleDataSource");
              assertThat(context).doesNotHaveBean("externalOracleJdbcTemplate");
              assertThat(context).doesNotHaveBean(ExternalOracleConfiguration.class);
            });
  }

  @Test
  @DisplayName("Should not register ExternalOracleConfiguration when primary-db is missing")
  void doesNotRegisterWithoutPrimaryDb() {
    contextRunner
        .withPropertyValues("server.primary-db=", "silva.integrations.external-oracle.enabled=true")
        .run(
            context -> {
              assertThat(context).doesNotHaveBean("externalOracleDataSource");
              assertThat(context).doesNotHaveBean("externalOracleJdbcTemplate");
              assertThat(context).doesNotHaveBean(ExternalOracleConfiguration.class);
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

    ExternalOracleConfiguration config = new ExternalOracleConfiguration();
    NamedParameterJdbcTemplate template = config.externalOracleJdbcTemplate(dataSourceProxy, 5);

    template.queryForList("SELECT 1 FROM DUAL", Map.of());
    assertThat(timeoutCaptured.get()).isEqualTo(5);

    throwTimeout.set(true);
    assertThatThrownBy(() -> template.queryForList("SELECT 1 FROM DUAL", Map.of()))
        .isInstanceOf(QueryTimeoutException.class)
        .hasMessageContaining("Statement execution timed out");
  }
}
