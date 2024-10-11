package ca.bc.gov.restapi.results.oracle.repository;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("Integration Test | CutBlockOpenAdminRepository")
class CutBlockOpenAdminRepositoryIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private CutBlockOpenAdminRepository cutBlockOpenAdminRepository;

  @Test
  @DisplayName("find all openings given a list of opening ids")
  void findAllByOpeningIdIn() {
    List<Long> openingIdList = List.of(101L, 102L);

    List<CutBlockOpenAdminEntity> cutBlocks =
        cutBlockOpenAdminRepository.findAllByOpeningIdIn(openingIdList);

    Assertions.assertNotNull(cutBlocks);
    Assertions.assertEquals(2, cutBlocks.size());
  }
}
