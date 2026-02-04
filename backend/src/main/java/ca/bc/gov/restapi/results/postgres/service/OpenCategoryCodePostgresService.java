package ca.bc.gov.restapi.results.postgres.service;

import ca.bc.gov.restapi.results.common.repository.OpenCategoryCodeRepository;
import ca.bc.gov.restapi.results.common.service.impl.AbstractOpenCategoryCodeService;
import ca.bc.gov.restapi.results.postgres.entity.code.OpenCategoryCodeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@ConditionalOnProperty(prefix = "server", name = "primary-db", havingValue = "postgres")
@Service
public class OpenCategoryCodePostgresService extends AbstractOpenCategoryCodeService {

  public OpenCategoryCodePostgresService(OpenCategoryCodeRepository<OpenCategoryCodeEntity> openCategoryCodeRepository) {
    super(openCategoryCodeRepository);
  }
}
