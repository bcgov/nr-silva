package ca.bc.gov.restapi.results.common.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.restapi.results.common.projection.CutBlockOpenAdminProjection;
import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;

@ExtendWith(MockitoExtension.class)
@DisplayName("Integrated Test | CutBlockOpenAdminService | Contract")
public abstract class AbstractCutBlockOpenAdminServiceIntegrationTest<T extends CutBlockOpenAdminService> extends AbstractTestContainerIntegrationTest{

  @Autowired
  protected CutBlockOpenAdminService cutBlockOpenAdminService;

  @Test
  @DisplayName("Find all openings by opening id list")
  void findAllByOpeningIdIn_simpleFetch_shouldSucceed() {

    List<CutBlockOpenAdminProjection> entityList =
        cutBlockOpenAdminService.findAllByOpeningIdIn(List.of(101017L));

    Assertions.assertNotNull(entityList);
    Assertions.assertEquals(1, entityList.size());
  }

  @Test
  @DisplayName("Find no openings by opening id list")
  void findAllByOpeningIdIn_simpleFetch_shouldEmpty() {

    List<CutBlockOpenAdminProjection> entityList =
        cutBlockOpenAdminService.findAllByOpeningIdIn(List.of(192L));

    Assertions.assertNotNull(entityList);
    Assertions.assertEquals(0, entityList.size());
  }
}
