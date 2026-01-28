package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.common.service.impl.AbstractOrgUnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
@Service
public class OrgUnitPostgresService extends AbstractOrgUnitService {

  public OrgUnitPostgresService(OrgUnitRepository orgUnitRepository,
      SilvaConfiguration silvaConfiguration) {
    super(orgUnitRepository, silvaConfiguration);
  }
}
