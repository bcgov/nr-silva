package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.oracle.entity.CutBlockOpenAdminEntity;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("Integrated Test | CutBlockOpenAdminService")
class CutBlockOpenAdminServiceTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private CutBlockOpenAdminService cutBlockOpenAdminService;

  @Test
  @DisplayName("Find all openings by opening id list")
  void findAllByOpeningIdIn_simpleFetch_shouldSucceed() {

    List<CutBlockOpenAdminEntity> entityList =
        cutBlockOpenAdminService.findAllByOpeningIdIn(List.of(101L));

    Assertions.assertNotNull(entityList);
    Assertions.assertEquals(1, entityList.size());
  }

  @Test
  @DisplayName("Find no openings by opening id list")
  void findAllByOpeningIdIn_simpleFetch_shouldEmpty() {

    List<CutBlockOpenAdminEntity> entityList =
        cutBlockOpenAdminService.findAllByOpeningIdIn(List.of(192L));

    Assertions.assertNotNull(entityList);
    Assertions.assertEquals(0, entityList.size());
  }
}
