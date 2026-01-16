package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.configuration.SilvaConfiguration;
import ca.bc.gov.restapi.results.common.repository.OrgUnitRepository;
import ca.bc.gov.restapi.results.common.service.impl.AbstractOrgUnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * This class contains methods to handle Org Units.
 */
@Slf4j
@Service
public class OrgUnitService extends AbstractOrgUnitService {

  public OrgUnitService(OrgUnitRepository orgUnitRepository,
      SilvaConfiguration silvaConfiguration) {
    super(orgUnitRepository, silvaConfiguration);
  }

}
