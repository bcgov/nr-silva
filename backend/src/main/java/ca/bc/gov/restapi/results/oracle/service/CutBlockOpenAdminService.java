package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.repository.CutBlockOpenAdminRepository;
import ca.bc.gov.restapi.results.common.service.impl.AbstractCutBlockOpenAdminService;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import org.springframework.stereotype.Service;

/** This class holds methods for handling {@link CutBlockOpenAdminEntity}. */
@Service
public class CutBlockOpenAdminService extends AbstractCutBlockOpenAdminService {

  public CutBlockOpenAdminService(CutBlockOpenAdminRepository cutBlockOpenAdminRepository) {
    super(cutBlockOpenAdminRepository);
  }
}
