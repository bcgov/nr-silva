package ca.bc.gov.restapi.results.oracle.endpoint;

import ca.bc.gov.restapi.results.common.endpoint.AbstractUserActionEndpointIntegrationTest;
import ca.bc.gov.restapi.results.oracle.repository.OpeningOracleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;

@EnabledIfSystemProperty(named = "primary-db", matches = "oracle")
@DisplayName("Integrated Test | User Actions Endpoint | Legacy (Oracle primary)")
class UserActionsEndpointOracleIntegrationTest extends
    AbstractUserActionEndpointIntegrationTest<OpeningOracleRepository> {

  @Autowired
  private OpeningOracleRepository openingOracleRepository;

  @Override
  protected void setupTestData() {
    openingOracleRepository.findById(1009974L).ifPresent(openingEntity -> {
            openingEntity.setUpdateTimestamp(LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIDNIGHT));
            openingEntity.setEntryTimestamp(LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIDNIGHT));
      openingOracleRepository.save(openingEntity);
    });
  }
}
