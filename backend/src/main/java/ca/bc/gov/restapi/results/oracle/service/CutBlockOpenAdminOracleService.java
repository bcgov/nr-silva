package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.repository.CutBlockOpenAdminRepository;
import ca.bc.gov.restapi.results.common.service.impl.AbstractCutBlockOpenAdminService;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** This class holds methods for handling {@link CutBlockOpenAdminEntity}. */
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
@Service
public class CutBlockOpenAdminOracleService extends AbstractCutBlockOpenAdminService {

  public CutBlockOpenAdminOracleService(CutBlockOpenAdminRepository cutBlockOpenAdminRepository) {
    super(cutBlockOpenAdminRepository);
  }
}
