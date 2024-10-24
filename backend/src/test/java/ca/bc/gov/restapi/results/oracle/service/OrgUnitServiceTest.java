package ca.bc.gov.restapi.results.oracle.service;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.oracle.entity.OrgUnitEntity;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@DisplayName("Integrated Test | Org Unit Service")
@TestPropertySource(properties = {
    "ca.bc.gov.nrs.results.opening-search.org-units="
})
class OrgUnitServiceTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private OrgUnitService orgUnitService;

  @Test
  @DisplayName("No org units if no params is set")
  void findAllOrgUnits_happyPath_shouldSucceed() {

    List<OrgUnitEntity> entities = orgUnitService.findAllOrgUnits();

    Assertions.assertNotNull(entities);
    Assertions.assertTrue(entities.isEmpty());
  }
}
