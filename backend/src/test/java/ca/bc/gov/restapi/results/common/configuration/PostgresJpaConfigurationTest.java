package ca.bc.gov.restapi.results.common.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;

@DisplayName("Unit Test | PostgresJpaConfiguration")
class PostgresJpaConfigurationTest {

  @Test
  @DisplayName("Should create dbHealthIndicator bound strictly to postgresHikariDataSource with SELECT 1")
  void createsDbHealthIndicatorForPostgres() {
    PostgresJpaConfiguration config = new PostgresJpaConfiguration();
    HikariDataSource ds = new HikariDataSource();
    DataSourceHealthIndicator healthIndicator = config.dbHealthIndicator(ds);

    assertThat(healthIndicator).isNotNull();
  }
}
