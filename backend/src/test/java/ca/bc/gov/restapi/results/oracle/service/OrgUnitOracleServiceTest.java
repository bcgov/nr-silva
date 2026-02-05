package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.common.service.AbstractOrgUnitServiceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Unit Test | Org Unit Service | Legacy(Oracle primary)")
class OrgUnitOracleServiceTest extends AbstractOrgUnitServiceTest<OrgUnitOracleService> {

  @Override
  protected OrgUnitOracleService createService(
      OrgUnitRepository orgUnitRepository, SilvaConfiguration silvaConfiguration) {
    return new OrgUnitOracleService(orgUnitRepository, silvaConfiguration);
  }
}
