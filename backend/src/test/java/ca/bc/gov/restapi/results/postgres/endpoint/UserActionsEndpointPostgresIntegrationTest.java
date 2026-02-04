package ca.bc.gov.restapi.results.postgres.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractUserActionEndpointIntegrationTest;
import ca.bc.gov.restapi.results.oracle.repository.OpeningOracleRepository;
import ca.bc.gov.restapi.results.postgres.repository.OpeningPostgresRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@EnabledIfSystemProperty(named = "server.primary-db", matches = "postgres")
@DisplayName("Integrated Test | User Actions Endpoint | Postgres-only")
public class UserActionsEndpointPostgresIntegrationTest extends
    AbstractUserActionEndpointIntegrationTest<OpeningOracleRepository> {

  @Autowired
  protected OpeningPostgresRepository openingPostgresRepository;

  @Override
  protected void setupTestData() {
    openingPostgresRepository.findById(1009974L).ifPresent(openingEntity -> {
      openingEntity.setUpdateTimestamp(
          LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIDNIGHT));
      openingEntity.setEntryTimestamp(LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIDNIGHT));
      openingPostgresRepository.save(openingEntity);
    });
  }
}
