package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.common.service.AbstractOrgUnitServiceTest;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Unit Test | Org Unit Service | Postgres-only")
public class OrgUnitServicePostgresTest  extends AbstractOrgUnitServiceTest<OrgUnitPostgresService> {

  @Override
  protected OrgUnitPostgresService createService(OrgUnitRepository orgUnitRepository,
      SilvaConfiguration silvaConfiguration) {
    return new OrgUnitPostgresService(orgUnitRepository, silvaConfiguration);
  }
}
