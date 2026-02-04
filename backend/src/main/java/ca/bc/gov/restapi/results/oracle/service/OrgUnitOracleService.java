package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.common.service.impl.AbstractOrgUnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** This class contains methods to handle Org Units. */
@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
@Service
public class OrgUnitOracleService extends AbstractOrgUnitService {

  public OrgUnitOracleService(
      OrgUnitRepository orgUnitRepository, SilvaConfiguration silvaConfiguration) {
    super(orgUnitRepository, silvaConfiguration);
  }
}
