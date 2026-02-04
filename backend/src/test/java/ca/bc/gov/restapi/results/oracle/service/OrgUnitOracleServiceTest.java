package ca.bc.gov.restapi.results.oracle.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.common.service.AbstractOrgUnitServiceTest;

@DisplayName("Unit Test | Org Unit Service | Legacy(Oracle primary)")
class OrgUnitOracleServiceTest extends AbstractOrgUnitServiceTest<OrgUnitOracleService> {

  @Override
  protected OrgUnitOracleService createService(OrgUnitRepository orgUnitRepository, SilvaConfiguration silvaConfiguration) {
    return new OrgUnitOracleService(orgUnitRepository, silvaConfiguration);
  }

  @Test
  @DisplayName("Should execute testOracleService without throwing exception")
  void testOracleServiceExecutes() {
    // Act & Assert - Should complete without exception
    orgUnitService.testOracleService();
  }
}
