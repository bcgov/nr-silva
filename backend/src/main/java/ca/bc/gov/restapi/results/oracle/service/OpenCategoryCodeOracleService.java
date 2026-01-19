package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.common.repository.OpenCategoryCodeRepository;
import ca.bc.gov.restapi.results.common.service.impl.AbstractOpenCategoryCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * This class contains methods to handle Opening Categories.
 */
@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "oracle")
@Service
public class OpenCategoryCodeOracleService extends AbstractOpenCategoryCodeService {
  public OpenCategoryCodeOracleService(OpenCategoryCodeRepository openCategoryCodeRepository) {
    super(openCategoryCodeRepository);
  }
}
