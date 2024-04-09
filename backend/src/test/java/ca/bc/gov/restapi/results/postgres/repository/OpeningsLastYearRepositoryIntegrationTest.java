package ca.bc.gov.restapi.results.postgres.repository;

import ca.bc.gov.restapi.results.postgres.entity.OpeningsLastYearEntity;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = {"classpath:sql_scripts/OpeningsLastYearRepositoryIntegrationTest.sql"})
class OpeningsLastYearRepositoryIntegrationTest {

  @Autowired private OpeningsLastYearRepository openingsLastYearRepository;

  @Test
  @DisplayName("find all by Opening ID in List")
  void findAllByOpeningIdInListTest() {
    List<Long> idList = List.of(7012511L, 7012512L, 7012513L);
    List<OpeningsLastYearEntity> openingList =
        openingsLastYearRepository.findAllByOpeningIdInList(idList);

    Assertions.assertFalse(openingList.isEmpty());
    Assertions.assertEquals(3, openingList.size());

    OpeningsLastYearEntity first = openingList.get(0);

    Assertions.assertEquals(7012511L, first.getOpeningId());
    Assertions.assertEquals("TEST", first.getUserId());
    Assertions.assertEquals("APP", first.getStatus());
    Assertions.assertEquals("DCR", first.getOrgUnitCode());
  }
}
