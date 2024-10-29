package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.extensions.AbstractTestContainerIntegrationTest;
import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OpeningsLastYearRepositoryIntegrationTest extends AbstractTestContainerIntegrationTest {

  @Autowired
  private OpeningsLastYearRepository openingsLastYearRepository;

  @Test
  @DisplayName("find all by Opening ID in List")
  void findAllByOpeningIdIsInTest() {
    List<Long> idList = List.of(7012511L, 7012512L, 7012513L);
    List<OpeningsLastYearEntity> openingList =
        openingsLastYearRepository.findAllByOpeningIdIn(idList);

    Assertions.assertFalse(openingList.isEmpty());
    Assertions.assertEquals(3, openingList.size());

    OpeningsLastYearEntity first = openingList.get(0);

    Assertions.assertEquals(7012511L, first.getOpeningId());
    Assertions.assertEquals("TEST", first.getUserId());
    Assertions.assertEquals("APP", first.getStatus());
    Assertions.assertEquals("DCR", first.getOrgUnitCode());
  }
}
